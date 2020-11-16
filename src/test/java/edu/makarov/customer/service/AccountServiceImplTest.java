package edu.makarov.customer.service;

import edu.makarov.customer.models.Account;
import edu.makarov.customer.models.Customer;
import edu.makarov.customer.models.dto.BalanceChangeDTO;
import edu.makarov.customer.models.dto.MoneyTransactionDTO;
import edu.makarov.customer.repository.AccountRepository;
import edu.makarov.customer.repository.CustomerRepository;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

@RunWith(SpringRunner.class)
@SpringBootTest
public class AccountServiceImplTest {

    @Autowired
    private AccountService accountService;

    @MockBean
    private AccountRepository accountRepository;

    @MockBean
    private CustomerRepository customerRepository;

    private Account account;
    private Customer customer;

    @Before
    public void initModels() {
        customer = new Customer();
        customer.setFullName("Иванов");
        customer.setDocumentNumber("12345");

        account = new Account();
        account.setAccountNumber("404000000000000000");
        account.setBalance(new BigDecimal(15000));
        account.setCustomer(customer);
    }

    @Test
    public void findAllTest() {
        List<Account> accounts = accountService.findAll();

        Mockito.verify(accountRepository, Mockito.times(1)).findAll();
        Assert.assertNotNull(accounts);
    }

    @Test
    public void findAllByCustomerIdTest() {
        Mockito.doReturn(Optional.of(customer)).when(customerRepository).findById(3L);
        Mockito.doReturn(Optional.of(new ArrayList())).when(accountRepository).findAccountsByCustomer(customer);

        Optional<List<Account>> accounts = accountService.findAllByCustomerId(3);

        Mockito.verify(accountRepository, Mockito.times(1)).findAccountsByCustomer(customer);
        Mockito.verify(customerRepository, Mockito.times(1)).findById(3L);
        Assert.assertTrue(accounts.isPresent());
    }

    @Test
    public void findAllByCustomerIdFailTest() {
        Mockito.doReturn(Optional.empty()).when(customerRepository).findById(10L);

        Optional<List<Account>> accounts = accountService.findAllByCustomerId(10);

        Mockito.verify(accountRepository, Mockito.times(0)).findAccountsByCustomer(customer);
        Mockito.verify(customerRepository, Mockito.times(1)).findById(10L);
        Assert.assertFalse(accounts.isPresent());
    }

    @Test
    public void findByIdTest() {
        Mockito.doReturn(Optional.of(account)).when(accountRepository).findById(3L);

        Optional<Account> accountFromDb = accountService.findById(3);

        Mockito.verify(accountRepository, Mockito.times(1)).findById(3L);
        Assert.assertNotNull(accountFromDb);
        Assert.assertEquals(account.getAccountNumber(), accountFromDb.get().getAccountNumber());
    }

    @Test
    public void findByIdFailTest() {
        Mockito.doReturn(Optional.empty()).when(accountRepository).findById(10L);

        Optional<Account> accountFromDb = accountService.findById(10);

        Mockito.verify(accountRepository, Mockito.times(1)).findById(10L);
        Assert.assertFalse(accountFromDb.isPresent());
    }

    @Test
    public void createAccountTest() {
        Mockito.doReturn(account).when(accountRepository).save(account);

        Account accountFromDb = accountService.create(account);

        Mockito.verify(accountRepository, Mockito.times(1)).save(account);
        Assert.assertNotNull(accountFromDb);
        Assert.assertEquals(account.getAccountNumber(), accountFromDb.getAccountNumber());
    }

    @Test
    public void createAccountByCustomerIdTest() {

        Mockito.doReturn(Optional.of(customer)).when(customerRepository).findById(10L);
        Mockito.doReturn(account).when(accountRepository).save(account);

        Optional<Account> accountFromDb = accountService.create(account, 10);

        Mockito.verify(accountRepository, Mockito.times(1)).save(account);
        Mockito.verify(customerRepository, Mockito.times(1)).findById(10L);
        Assert.assertTrue(accountFromDb.isPresent());
        Assert.assertEquals(account.getAccountNumber(), accountFromDb.get().getAccountNumber());
        Assert.assertNotNull(accountFromDb.get().getCustomer().getFullName());
    }

    @Test
    public void createAccountByCustomerIdFailTest() {
        Mockito.doReturn(Optional.empty()).when(customerRepository).findById(10L);
        Mockito.doReturn(account).when(accountRepository).save(account);

        Optional<Account> accountFromDb = accountService.create(account, 10);

        Mockito.verify(accountRepository, Mockito.times(0)).save(account);
        Mockito.verify(customerRepository, Mockito.times(1)).findById(10L);
        Assert.assertFalse(accountFromDb.isPresent());
    }

    @Test
    public void updateTest() {
        account.setId(10);
        Mockito.doReturn(Optional.of(account)).when(accountRepository).findById(10L);
        Mockito.doReturn(account).when(accountRepository).save(account);

        Optional<Account> accountFromDb = accountService.update(10, account);

        Mockito.verify(accountRepository, Mockito.times(1)).findById(Mockito.anyLong());
        Mockito.verify(accountRepository, Mockito.times(1)).save(Mockito.any());
        Assert.assertEquals(account.getId(), accountFromDb.get().getId());
    }

    @Test
    public void updateFailTest() {
        Mockito.doReturn(Optional.empty()).when(accountRepository).findById(10L);
        Mockito.doReturn(account).when(accountRepository).save(account);

        Optional<Account> accountFromDb = accountService.update(10, account);

        Mockito.verify(accountRepository, Mockito.times(1)).findById(Mockito.anyLong());
        Mockito.verify(accountRepository, Mockito.times(0)).save(Mockito.any());
        Assert.assertFalse(accountFromDb.isPresent());
    }

    @Test
    public void deleteTest() {
        Mockito.doReturn(Optional.of(account)).when(accountRepository).findById(10L);

        boolean result = accountService.delete(10);

        Mockito.verify(accountRepository, Mockito.times(1)).findById(Mockito.anyLong());
        Mockito.verify(accountRepository, Mockito.times(1)).deleteById(Mockito.anyLong());
        Assert.assertTrue(result);
    }

    @Test
    public void deleteFailTest() {
        Mockito.doReturn(Optional.empty()).when(accountRepository).findById(10L);

        boolean result = accountService.delete(10);

        Mockito.verify(accountRepository, Mockito.times(1)).findById(Mockito.anyLong());
        Mockito.verify(accountRepository, Mockito.times(0)).deleteById(Mockito.anyLong());
        Assert.assertFalse(result);
    }

    @Test
    public void increaseBalanceTest() {
        Mockito.doReturn(Optional.of(account)).when(accountRepository).findById(Mockito.anyLong());
        Mockito.doReturn(account).when(accountRepository).save(account);

        BalanceChangeDTO balanceChangeDTO = new BalanceChangeDTO(1, new BigDecimal("100.51687"));
        Optional<Account> accountFromDb = accountService.increaseBalance(balanceChangeDTO);

        Mockito.verify(accountRepository, Mockito.times(1)).findById(Mockito.anyLong());
        Mockito.verify(accountRepository, Mockito.times(1)).save(Mockito.any());
        Assert.assertEquals(accountFromDb.get().getBalance(), new BigDecimal("15100.51"));
    }

    @Test
    public void increaseBalanceFailTest() {
        Mockito.doReturn(Optional.of(account)).when(accountRepository).findById(Mockito.anyLong());
        Mockito.doReturn(account).when(accountRepository).save(account);

        BalanceChangeDTO balanceChangeDTO = new BalanceChangeDTO(1, new BigDecimal("-200"));
        Optional<Account> accountFromDb = accountService.increaseBalance(balanceChangeDTO);

        Mockito.verify(accountRepository, Mockito.times(0)).findById(Mockito.anyLong());
        Mockito.verify(accountRepository, Mockito.times(0)).save(Mockito.any());
        Assert.assertFalse(accountFromDb.isPresent());
    }

    @Test
    public void reduceBalanceTest() {
        Mockito.doReturn(Optional.of(account)).when(accountRepository).findById(Mockito.anyLong());
        Mockito.doReturn(account).when(accountRepository).save(account);

        BalanceChangeDTO balanceChangeDTO = new BalanceChangeDTO(1, new BigDecimal("100.504444"));
        Optional<Account> accountFromDb = accountService.reduceBalance(balanceChangeDTO);

        Mockito.verify(accountRepository, Mockito.times(1)).findById(Mockito.anyLong());
        Mockito.verify(accountRepository, Mockito.times(1)).save(Mockito.any());
        Assert.assertEquals(accountFromDb.get().getBalance(), new BigDecimal("14899.50"));
    }

    @Test
    public void reduceBalanceFailTest() {
        Mockito.doReturn(Optional.of(account)).when(accountRepository).findById(Mockito.anyLong());
        Mockito.doReturn(account).when(accountRepository).save(account);

        BalanceChangeDTO balanceChangeDTO = new BalanceChangeDTO(1, new BigDecimal("20000"));
        Optional<Account> accountFromDb = accountService.reduceBalance(balanceChangeDTO);

        Mockito.verify(accountRepository, Mockito.times(1)).findById(Mockito.anyLong());
        Mockito.verify(accountRepository, Mockito.times(0)).save(Mockito.any());
        Assert.assertFalse(accountFromDb.isPresent());
    }

    @Test
    public void transferMoneyTest() {
        Account to = new Account();
        to.setId(1);
        to.setBalance(new BigDecimal("1000"));
        to.setCustomer(customer);

        Account from = new Account();
        from.setId(2);
        from.setBalance(new BigDecimal("1000"));
        from.setCustomer(customer);

        Mockito.doReturn(Optional.of(to)).when(accountRepository).findById(1L);
        Mockito.doReturn(Optional.of(from)).when(accountRepository).findById(2L);
        Mockito.doReturn(to).when(accountRepository).save(to);
        Mockito.doReturn(from).when(accountRepository).save(from);

        MoneyTransactionDTO moneyTransactionDTO = new MoneyTransactionDTO(1, 2, new BigDecimal("500.55555"));

        accountService.transferMoney(moneyTransactionDTO);

        Mockito.verify(accountRepository, Mockito.times(4)).findById(Mockito.anyLong());
        Mockito.verify(accountRepository, Mockito.times(2)).save(Mockito.any());
        Assert.assertEquals(new BigDecimal("1500.55"), to.getBalance());
        Assert.assertEquals(new BigDecimal("499.45"), from.getBalance());
    }

    @Test
    public void transferMoneyFailTest() {
        Account to = new Account();
        to.setId(1);
        to.setBalance(new BigDecimal("1000"));

        Account from = new Account();
        from.setId(2);
        from.setBalance(new BigDecimal("1000"));

        Mockito.doReturn(Optional.of(to)).when(accountRepository).findById(1L);
        Mockito.doReturn(Optional.of(from)).when(accountRepository).findById(2L);
        Mockito.doReturn(to).when(accountRepository).save(to);
        Mockito.doReturn(from).when(accountRepository).save(from);

        MoneyTransactionDTO moneyTransactionDTO = new MoneyTransactionDTO(1, 2, new BigDecimal("2000"));

        accountService.transferMoney(moneyTransactionDTO);

        Mockito.verify(accountRepository, Mockito.times(3)).findById(Mockito.anyLong());
        Mockito.verify(accountRepository, Mockito.times(0)).save(Mockito.any());
        Assert.assertEquals(new BigDecimal("1000"), to.getBalance());
        Assert.assertEquals(new BigDecimal("1000"), from.getBalance());
    }
}