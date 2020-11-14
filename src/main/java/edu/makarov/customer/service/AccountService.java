package edu.makarov.customer.service;

import edu.makarov.customer.models.Account;
import edu.makarov.customer.models.dto.BalanceChangeDTO;
import edu.makarov.customer.models.dto.MoneyTransactionDTO;

import java.util.List;
import java.util.Map;

public interface AccountService extends BaseService<Account> {

    Account create(Account account, long customerId);

    List<Account> findAllByCustomerId(long id);

    Account increaseBalance(BalanceChangeDTO balanceChangeDTO);

    Account reduceBalance(BalanceChangeDTO balanceChangeDTO);

    boolean transferMoney(MoneyTransactionDTO transaction);
}
