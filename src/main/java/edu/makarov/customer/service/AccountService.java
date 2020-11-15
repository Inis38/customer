package edu.makarov.customer.service;

import edu.makarov.customer.models.Account;
import edu.makarov.customer.models.dto.BalanceChangeDTO;
import edu.makarov.customer.models.dto.MoneyTransactionDTO;

import java.util.List;
import java.util.Optional;

public interface AccountService extends BaseService<Account> {

    Optional<Account> create(Account account, long customerId);

    Optional<List<Account>> findAllByCustomerId(long id);

    Optional<Account> increaseBalance(BalanceChangeDTO balanceChangeDTO);

    Optional<Account> reduceBalance(BalanceChangeDTO balanceChangeDTO);

    boolean transferMoney(MoneyTransactionDTO transaction);
}
