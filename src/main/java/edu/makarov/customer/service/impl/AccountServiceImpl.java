package edu.makarov.customer.service.impl;

import edu.makarov.customer.models.Account;
import edu.makarov.customer.models.dto.BalanceChangeDTO;
import edu.makarov.customer.models.dto.MoneyTransactionDTO;
import edu.makarov.customer.repository.AccountRepository;
import edu.makarov.customer.service.AccountService;
import edu.makarov.customer.service.CustomerService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

/**
 * Class for performing operations with accounts
 */
@Service
public class AccountServiceImpl implements AccountService {

    private static final Logger logger = LogManager.getLogger(AccountServiceImpl.class);

    @Value("${account.exchange}")
    private String exchange;

    @Value("${account.routing.key}")
    private String routingKey;

    private final AccountRepository accountRepository;
    private final CustomerService customerService;
    private final RabbitTemplate template;

    @Autowired
    public AccountServiceImpl(AccountRepository accountRepository,
                              CustomerService customerService,
                              RabbitTemplate template) {
        this.accountRepository = accountRepository;
        this.customerService = customerService;
        this.template = template;
    }

    /**
     * Returns a list of all accounts from the database
     *
     * @return List of accounts
     */
    @Override
    public List<Account> findAll() {
        return accountRepository.findAll();
    }

    /**
     * Returns a list of all client accounts with the specified id
     *
     * @param id client id from database
     * @return List of customer accounts
     */
    @Override
    public Optional<List<Account>> findAllByCustomerId(long id) {

        logger.info("Запрос всех счетов для клиента с id {}", id);
        return customerService.findById(id)
                .map(accountRepository::findAccountsByCustomer)
                .orElse(Optional.empty());
    }

    /**
     * Search for an Account by its id
     *
     * @param id account id from database
     * @return customer Account
     */
    @Override
    public Optional<Account> findById(long id) {
        logger.info("Запрос инфмормации о счете с id {}", id);
        return accountRepository.findById(id);
    }

    /**
     * Save Account to database
     *
     * @param account Account to be kept
     * @return Saved Account
     */
    @Override
    public Account create(Account account) {

        logger.info("Сохраняем счет для клиента с id {}, счет - {}", account.getCustomer().getId(), account);
        return accountRepository.save(account);
    }

    /**
     * Save an Account for a specific customer
     *
     * @param account Account to be kept
     * @param customerId The id of the Customer who will own the account
     * @return Saved Account
     */
    @Override
    public Optional<Account> create(Account account, long customerId) {

        return customerService.findById(customerId)
                .map(customer -> {
                    account.setCustomer(customer);
                    return Optional.of(create(account));
                })
                .orElse(Optional.empty());
    }

    /**
     * Update account information
     *
     * @param id Account id
     * @param account the Account to be updated
     * @return Saved Account
     */
    @Override
    public Optional<Account> update(long id, Account account) {
        logger.info("Обновляем информауию о счете с id {}, счет - {}", id, account);
        return findById(id)
                .map(accountFromDb -> {
                    BeanUtils.copyProperties(account, accountFromDb, "id", "customer");
                    return Optional.of(create(accountFromDb));
                })
                .orElse(Optional.empty());
    }

    /**
     * Delete account
     *
     * @param id Account id
     * @return False if no such account exists, otherwise true
     */
    @Override
    public boolean delete(long id) {
        if (!findById(id).isPresent()) {
            logger.warn("Не удалось удалить счет с id {}", id);
            return false;
        }
        logger.info("Счет с id {} удален", id);
        accountRepository.deleteById(id);
        return true;
    }

    /**
     * Increases the Account balance.
     * Checks that the id and the sum are correct, the sum is greater than zero.
     *
     * @param balanceChangeDTO A class containing a request to change the balance
     * @return Account with new balance
     */
    @Override
    public Optional<Account> increaseBalance(BalanceChangeDTO balanceChangeDTO) {

        long id = balanceChangeDTO.getId();
        BigDecimal sum = balanceChangeDTO.getSum().setScale(2, BigDecimal.ROUND_DOWN);
        if (id == 0 || sum == null || sum.floatValue() < 0) {
            logger.warn("Попытка увеличить баланс счета. Некорректные данные. Операция отменена.");
            return Optional.empty();
        }
        return increaseBalance(id, sum);
    }

    private Optional<Account> increaseBalance(long id, BigDecimal sum) {
        logger.info("Увеличиваем баланс для счета с id {} на сумму {}", id, sum);
        return findById(id)
                .map(account -> {
                    account.setBalance(account.getBalance().add(sum));
                    return Optional.of(create(account));
                })
                .orElse(Optional.empty());
    }

    /**
     * Decrease in account balance.
     * Used for operations with funds withdrawal, payment and money transfer.
     * Checks that the account contains the amount required to be debited.
     *
     * @param balanceChangeDTO A class containing a request to change the balance
     * @return Account with new balance
     */
    @Override
    public Optional<Account> reduceBalance(BalanceChangeDTO balanceChangeDTO) {
        long id = balanceChangeDTO.getId();
        BigDecimal sum = balanceChangeDTO.getSum().setScale(2, BigDecimal.ROUND_DOWN);
        if (id == 0 || sum == null || sum.floatValue() < 0) {
            logger.warn("Попытка уменьшить баланс счета. Некорректные данные. Операция отменена. id - {}, sum = {}", id, sum);
            return Optional.empty();
        }
        return reduceBalance(id, sum);
    }

    private Optional<Account> reduceBalance(long id, BigDecimal sum) {
        logger.info("Уменьшаем баланс для счета с id {} на сумму {}", id, sum);
        return (Optional<Account>) findById(id)
                .map(account -> {
                    if (account.getBalance().floatValue() < sum.floatValue()) {
                        logger.warn("На балансе счета с id {} недостаточно средств, операция отменена.", id);
                        return Optional.empty();
                    }
                    account.setBalance(account.getBalance().subtract(sum));
                    return Optional.of(create(account));
                })
                .orElse(Optional.empty());
    }

    /**
     * Performs the operation of transferring funds from account to account.
     *
     * @param transaction Class containing data for transferring funds
     * @return true, if successful
     */
    @Override
    public boolean transferMoney(MoneyTransactionDTO transaction) {
        BigDecimal sum = transaction.getSum().setScale(2, BigDecimal.ROUND_DOWN);
        return transferMoney(transaction.getReduceId(), transaction.getIncreaseId(), sum);
    }

    private boolean transferMoney(long from, long to, BigDecimal sum) {
        Optional<Account> accountFrom = findById(from);
        Optional<Account> accountTo = findById(to);
        if (!accountFrom.isPresent() || !accountTo.isPresent()) {
            logger.warn("Попытка перевода денежных средств со счета #{} на счет #{}. Некорректные данные. Операция отменена.", from, to);
            return false;
        }
        if (!reduceBalance(accountFrom.get().getId(), sum).isPresent()) {
            return false;
        }
        increaseBalance(accountTo.get().getId(), sum);
        logger.warn("Денежные средства переведены со счета #{} на счет #{}", from, to);
        return true;
    }

    /**
     * Method for sending an account to mq
     *
     * @param accountId Account id
     * @return Account
     */
    @Override
    public Optional<Account> sendAccountToQueue(long accountId) {
        return findById(accountId)
                .map(account -> {
                    template.convertAndSend(exchange, routingKey, account);
                    return Optional.of(account);
                })
                .orElse(Optional.empty());
    }
}
