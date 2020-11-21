package edu.makarov.customer.controller;

import edu.makarov.customer.exception.ErrorResponse;
import edu.makarov.customer.models.Account;
import edu.makarov.customer.models.dto.BalanceChangeDTO;
import edu.makarov.customer.models.dto.MoneyTransactionDTO;
import org.checkerframework.checker.units.qual.A;
import org.checkerframework.checker.units.qual.C;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;

import java.math.BigDecimal;
import java.util.List;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class AccountControllerTest {

    @Autowired
    private TestRestTemplate restTemplate;

    @Test
    public void findAllTest() {
        ResponseEntity<List<Account>> response = restTemplate.exchange("/customers/1/accounts",
                HttpMethod.GET, null, new ParameterizedTypeReference<List<Account>>() {});
        List<Account> accounts = response.getBody();
        Assert.assertEquals(response.getStatusCode(), HttpStatus.OK);
        Assert.assertNotNull(accounts);
        Assert.assertFalse(accounts.isEmpty());
        Assert.assertEquals(accounts.size(), 2);
    }

    @Test
    public void findAllFailTest() {
        ResponseEntity<ErrorResponse> response = restTemplate.getForEntity("/customers/{id}/accounts", ErrorResponse.class, 10);
        Assert.assertNotNull(response);
        Assert.assertEquals(response.getStatusCode(), HttpStatus.NOT_FOUND);
    }

    @Test
    public void findByIdTest() {
        ResponseEntity<Account> response = restTemplate.getForEntity("/customers/{id}/accounts/{id}", Account.class, 1, 1);
        Assert.assertEquals(response.getStatusCode(), HttpStatus.OK);
        Assert.assertEquals(response.getBody().getAccountNumber(), "456333999929992000");
    }

    @Test
    public void findByIdFailTest() {
        ResponseEntity<ErrorResponse> response = restTemplate.getForEntity("/customers/{id}/accounts{id}", ErrorResponse.class, 1, 10);
        Assert.assertNotNull(response);
        Assert.assertEquals(response.getStatusCode(), HttpStatus.NOT_FOUND);
    }

    @Test
    public void createTest() {
        Account account = new Account();
        account.setAccountNumber("123456789");
        account.setBalance(new BigDecimal("22000"));
        HttpEntity<Account> entity = new HttpEntity<>(account);

        ResponseEntity<Account> response = restTemplate.exchange("/customers/{id}/accounts", HttpMethod.POST, entity, Account.class, 1);

        Assert.assertNotNull(response);
        Assert.assertEquals(response.getStatusCode(), HttpStatus.OK);
        Assert.assertEquals(response.getBody().getAccountNumber(), account.getAccountNumber());
    }

    @Test
    public void createFailTest() {
        Account account = new Account();
        account.setAccountNumber("123456789");
        account.setBalance(new BigDecimal("22000"));
        HttpEntity<Account> entity = new HttpEntity<>(account);

        ResponseEntity<ErrorResponse> response = restTemplate.exchange("/customers/{id}/accounts", HttpMethod.POST, entity, ErrorResponse.class, 10);

        Assert.assertNotNull(response);
        Assert.assertEquals(response.getStatusCode(), HttpStatus.NOT_FOUND);
    }

    @Test
    public void updateTest() {
        Account account = new Account();
        account.setAccountNumber("123456789");
        account.setBalance(new BigDecimal("22000"));
        HttpEntity<Account> entity = new HttpEntity<>(account);

        ResponseEntity<Account> response = restTemplate.exchange("/customers/{id}/accounts/{id}", HttpMethod.PUT, entity, Account.class, 1, 2);

        Assert.assertNotNull(response);
        Assert.assertEquals(response.getStatusCode(), HttpStatus.OK);
        Assert.assertEquals(response.getBody().getId(), 2);
        Assert.assertEquals(response.getBody().getAccountNumber(), account.getAccountNumber());
    }

    @Test
    public void updateFailTest() {
        Account account = new Account();
        account.setAccountNumber("123456789");
        account.setBalance(new BigDecimal("22000"));
        HttpEntity<Account> entity = new HttpEntity<>(account);

        ResponseEntity<ErrorResponse> response = restTemplate.exchange("/customers/{id}/accounts/{id}", HttpMethod.PUT, entity, ErrorResponse.class, 1, 20);

        Assert.assertNotNull(response);
        Assert.assertEquals(response.getStatusCode(), HttpStatus.BAD_REQUEST);
    }

    @Test
    public void deleteTest() {
        ResponseEntity<Account> response = restTemplate.exchange("/customers/{id}/accounts/{id}", HttpMethod.DELETE, null, Account.class, 1, 2);
        Assert.assertNotNull(response);
        Assert.assertEquals(response.getStatusCode(), HttpStatus.OK);
    }

    @Test
    public void deleteFailTest() {
        ResponseEntity<Account> response = restTemplate.exchange("/customers/{id}/accounts/{id}", HttpMethod.DELETE, null, Account.class, 1, 10);
        Assert.assertNotNull(response);
        Assert.assertEquals(response.getStatusCode(), HttpStatus.BAD_REQUEST);
    }

    @Test
    public void addBalanceTest() {
        BalanceChangeDTO balanceChangeDTO = new BalanceChangeDTO();
        balanceChangeDTO.setSum(new BigDecimal("1000"));
        HttpEntity<BalanceChangeDTO> entity = new HttpEntity<>(balanceChangeDTO);

        ResponseEntity<Account> response = restTemplate.exchange("/customers/{id}/accounts/{id}/addbalance", HttpMethod.PUT, entity,
                                                                        Account.class, 1, 1);
        Assert.assertNotNull(response);
        Assert.assertEquals(response.getStatusCode(), HttpStatus.OK);
        Assert.assertNotNull(response.getBody());
        Assert.assertEquals(response.getBody().getBalance(), new BigDecimal("6000.00"));
    }

    @Test
    public void addBalanceFailTest() {
        BalanceChangeDTO balanceChangeDTO = new BalanceChangeDTO();
        balanceChangeDTO.setSum(new BigDecimal("-1000"));
        HttpEntity<BalanceChangeDTO> entity = new HttpEntity<>(balanceChangeDTO);

        ResponseEntity<Account> response = restTemplate.exchange("/customers/{id}/accounts/{id}/addbalance", HttpMethod.PUT, entity,
                Account.class, 1, 1);
        Assert.assertNotNull(response);
        Assert.assertEquals(response.getStatusCode(), HttpStatus.BAD_REQUEST);
    }

    @Test
    public void payTest() {
        BalanceChangeDTO balanceChangeDTO = new BalanceChangeDTO();
        balanceChangeDTO.setSum(new BigDecimal("1000"));
        HttpEntity<BalanceChangeDTO> entity = new HttpEntity<>(balanceChangeDTO);

        ResponseEntity<Account> response = restTemplate.exchange("/customers/{id}/accounts/{id}/pay", HttpMethod.PUT, entity,
                Account.class, 1, 1);

        Assert.assertNotNull(response);
        Assert.assertEquals(response.getStatusCode(), HttpStatus.OK);
        Assert.assertNotNull(response.getBody());
        Assert.assertEquals(response.getBody().getBalance(), new BigDecimal("4000.00"));
    }

    @Test
    public void payFailTest() {
        BalanceChangeDTO balanceChangeDTO = new BalanceChangeDTO();
        balanceChangeDTO.setSum(new BigDecimal("10000"));
        HttpEntity<BalanceChangeDTO> entity = new HttpEntity<>(balanceChangeDTO);

        ResponseEntity<Account> response = restTemplate.exchange("/customers/{id}/accounts/{id}/pay", HttpMethod.PUT, entity,
                Account.class, 1, 1);

        Assert.assertNotNull(response);
        Assert.assertEquals(response.getStatusCode(), HttpStatus.BAD_REQUEST);
    }

    @Test
    public void transferTest() {
        MoneyTransactionDTO transaction = new MoneyTransactionDTO();
        transaction.setIncreaseId(2);
        transaction.setSum(new BigDecimal("1000"));
        HttpEntity<MoneyTransactionDTO> entity = new HttpEntity<>(transaction);

        ResponseEntity<Account> response = restTemplate.exchange("/customers/{id}/accounts/{id}/transfer", HttpMethod.PUT, entity,
                Account.class, 1, 1);

        Assert.assertNotNull(response);
        Assert.assertEquals(response.getStatusCode(), HttpStatus.OK);

        ResponseEntity<Account> response2 = restTemplate.getForEntity("/customers/{id}/accounts/{id}", Account.class, 1, 1);
        Assert.assertNotNull(response2.getBody());
        Assert.assertEquals(response2.getBody().getBalance(), new BigDecimal("4000.00"));

        ResponseEntity<Account> response3 = restTemplate.getForEntity("/customers/{id}/accounts/{id}", Account.class, 1, 2);
        Assert.assertNotNull(response3.getBody());
        Assert.assertEquals(response3.getBody().getBalance(), new BigDecimal("11000.00"));
    }

    @Test
    public void transferFailTest() {
        MoneyTransactionDTO transaction = new MoneyTransactionDTO();
        transaction.setIncreaseId(2);
        transaction.setSum(new BigDecimal("100000"));
        HttpEntity<MoneyTransactionDTO> entity = new HttpEntity<>(transaction);

        ResponseEntity<Account> response = restTemplate.exchange("/customers/{id}/accounts/{id}/transfer", HttpMethod.PUT, entity,
                Account.class, 1, 1);

        Assert.assertNotNull(response);
        Assert.assertEquals(response.getStatusCode(), HttpStatus.BAD_REQUEST);
    }
}
