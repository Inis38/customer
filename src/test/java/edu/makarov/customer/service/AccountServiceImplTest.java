package edu.makarov.customer.service;

import edu.makarov.customer.models.Account;
import edu.makarov.customer.models.Customer;
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
        account = new Account();
        account.setAccountNumber("404000000000000000");
        account.setBalance(new BigDecimal(15000));

        customer = new Customer();
        customer.setFullName("Иванов");
        customer.setDocumentNumber("12345");
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

        List<Account> accounts = accountService.findAllByCustomerId(3);

        Mockito.verify(accountRepository, Mockito.times(1)).findAccountByCustomerId(3L);
        Mockito.verify(customerRepository, Mockito.times(1)).findById(3L);
        Assert.assertNotNull(accounts);
    }

    @Test
    public void findAllByCustomerIdFailTest() {
        Mockito.doReturn(Optional.empty()).when(customerRepository).findById(10L);

        List<Account> accounts = accountService.findAllByCustomerId(10);

        Mockito.verify(accountRepository, Mockito.times(0)).findAccountByCustomerId(10L);
        Mockito.verify(customerRepository, Mockito.times(1)).findById(10L);
        Assert.assertNull(accounts);
    }

    @Test
    public void findByIdTest() {
        Mockito.doReturn(Optional.of(account)).when(accountRepository).findById(3L);

        Account accountFromDb = accountService.findById(3);

        Mockito.verify(accountRepository, Mockito.times(1)).findById(3L);
        Assert.assertNotNull(accountFromDb);
        Assert.assertEquals(account.getAccountNumber(), accountFromDb.getAccountNumber());
    }

    @Test
    public void findByIdFailTest() {
        Mockito.doReturn(Optional.empty()).when(accountRepository).findById(10L);

        Account accountFromDb = accountService.findById(10);

        Mockito.verify(accountRepository, Mockito.times(1)).findById(10L);
        Assert.assertNull(accountFromDb);
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

        Account accountFromDb = accountService.create(account, 10);

        Mockito.verify(accountRepository, Mockito.times(1)).save(account);
        Mockito.verify(customerRepository, Mockito.times(1)).findById(10L);
        Assert.assertNotNull(accountFromDb);
        Assert.assertEquals(account.getAccountNumber(), accountFromDb.getAccountNumber());
        Assert.assertNotNull(accountFromDb.getCustomer().getFullName());
    }

    @Test
    public void createAccountByCustomerIdFailTest() {
        Mockito.doReturn(Optional.empty()).when(customerRepository).findById(10L);
        Mockito.doReturn(account).when(accountRepository).save(account);

        Account accountFromDb = accountService.create(account, 10);

        Mockito.verify(accountRepository, Mockito.times(0)).save(account);
        Mockito.verify(customerRepository, Mockito.times(1)).findById(10L);
        Assert.assertNull(accountFromDb);
    }

    @Test
    public void updateTest() {
        account.setId(3);
        Mockito.doReturn(Optional.of(account)).when(accountRepository).findById(10L);
        Mockito.doReturn(account).when(accountRepository).save(account);

        Account accountFromDb = accountService.update(10, account);

        Mockito.verify(accountRepository, Mockito.times(1)).findById(Mockito.anyLong());
        Mockito.verify(accountRepository, Mockito.times(1)).save(Mockito.any());
        Assert.assertEquals(account.getId(), accountFromDb.getId());
    }

    @Test
    public void updateFailTest() {
        Mockito.doReturn(Optional.empty()).when(accountRepository).findById(10L);
        Mockito.doReturn(account).when(accountRepository).save(account);

        Account accountFromDb = accountService.update(10, account);

        Mockito.verify(accountRepository, Mockito.times(1)).findById(Mockito.anyLong());
        Mockito.verify(accountRepository, Mockito.times(0)).save(Mockito.any());
        Assert.assertNull(accountFromDb);
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
}