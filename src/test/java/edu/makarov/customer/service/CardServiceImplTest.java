package edu.makarov.customer.service;

import edu.makarov.customer.models.Account;
import edu.makarov.customer.models.Card;
import edu.makarov.customer.repository.AccountRepository;
import edu.makarov.customer.repository.CardRepository;
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
public class CardServiceImplTest {

    @Autowired
    private CardService cardService;

    @MockBean
    private AccountRepository accountRepository;

    @MockBean
    private CardRepository cardRepository;

    private Account account;

    private Card card;

    @Before
    public void initModels() {
        account = new Account();
        account.setAccountNumber("404000000000000000");
        account.setBalance(new BigDecimal(15000));

        card = new Card();
        card.setCardNumber("1000 2000 3000 4000");
        card.setAccount(account);
    }

    @Test
    public void findAllTest() {
        List<Card> cards = cardService.findAll();

        Mockito.verify(cardRepository, Mockito.times(1)).findAll();
        Assert.assertNotNull(cards);
    }

    @Test
    public void findAllByAccountIdTest() {
        Mockito.doReturn(Optional.of(account)).when(accountRepository).findById(3L);

        List<Card> cards = cardService.findAllByAccountId(3);

        Mockito.verify(cardRepository, Mockito.times(1)).findCardByAccountId(Mockito.anyLong());
        Mockito.verify(accountRepository, Mockito.times(1)).findById(Mockito.anyLong());
        Assert.assertNotNull(cards);
    }

    @Test
    public void findAllByAccountIdFailTest() {
        Mockito.doReturn(Optional.empty()).when(accountRepository).findById(10L);

        List<Card> cards = cardService.findAllByAccountId(10);

        Mockito.verify(cardRepository, Mockito.times(0)).findCardByAccountId(Mockito.anyLong());
        Mockito.verify(accountRepository, Mockito.times(1)).findById(Mockito.anyLong());
        Assert.assertNull(cards);
    }

    @Test
    public void findByIdTest() {
        Mockito.doReturn(Optional.of(card)).when(cardRepository).findById(3L);

        Card cardFromDb = cardService.findById(3);

        Mockito.verify(cardRepository, Mockito.times(1)).findById(Mockito.anyLong());
        Assert.assertNotNull(cardFromDb);
        Assert.assertEquals(card.getCardNumber(), cardFromDb.getCardNumber());
    }

    @Test
    public void findByIdFailTest() {
        Mockito.doReturn(Optional.empty()).when(cardRepository).findById(10L);

        Card cardFromDb = cardService.findById(10);

        Mockito.verify(cardRepository, Mockito.times(1)).findById(Mockito.anyLong());
        Assert.assertNull(cardFromDb);
    }

    @Test
    public void createCardTest() {
        Mockito.doReturn(card).when(cardRepository).save(card);

        Card cardFromDb = cardService.create(card);

        Mockito.verify(cardRepository, Mockito.times(1)).save(Mockito.any());
        Assert.assertNotNull(cardFromDb);
        Assert.assertEquals(card.getCardNumber(), cardFromDb.getCardNumber());
    }

    @Test
    public void createCardByAccountIdTest() {

        Mockito.doReturn(Optional.of(account)).when(accountRepository).findById(10L);
        Mockito.doReturn(card).when(cardRepository).save(card);

        Card cardFromDb = cardService.create(card, 10);

        Mockito.verify(cardRepository, Mockito.times(1)).save(Mockito.any());
        Mockito.verify(accountRepository, Mockito.times(1)).findById(Mockito.anyLong());
        Assert.assertNotNull(cardFromDb);
        Assert.assertEquals(card.getCardNumber(), cardFromDb.getCardNumber());
        Assert.assertNotNull(cardFromDb.getAccount().getBalance());
    }

    @Test
    public void createCardByAccountIdFailTest() {
        Mockito.doReturn(Optional.empty()).when(accountRepository).findById(10L);
        Mockito.doReturn(card).when(cardRepository).save(card);

        Card cardFromDb = cardService.create(card, 10);

        Mockito.verify(cardRepository, Mockito.times(0)).save(Mockito.any());
        Mockito.verify(accountRepository, Mockito.times(1)).findById(Mockito.anyLong());
        Assert.assertNull(cardFromDb);
    }

    @Test
    public void updateTest() {
        card.setId(3);
        Mockito.doReturn(Optional.of(card)).when(cardRepository).findById(10L);
        Mockito.doReturn(card).when(cardRepository).save(card);

        Card cardFromDb = cardService.update(10, card);

        Mockito.verify(cardRepository, Mockito.times(1)).findById(Mockito.anyLong());
        Mockito.verify(cardRepository, Mockito.times(1)).save(Mockito.any());
        Assert.assertEquals(card.getId(), cardFromDb.getId());
    }

    @Test
    public void updateFailTest() {
        Mockito.doReturn(Optional.empty()).when(cardRepository).findById(10L);
        Mockito.doReturn(card).when(cardRepository).save(card);

        Card cardFromDb = cardService.update(10, card);

        Mockito.verify(cardRepository, Mockito.times(1)).findById(Mockito.anyLong());
        Mockito.verify(cardRepository, Mockito.times(0)).save(Mockito.any());
        Assert.assertNull(cardFromDb);
    }

    @Test
    public void deleteTest() {
        Mockito.doReturn(Optional.of(card)).when(cardRepository).findById(10L);

        boolean result = cardService.delete(10);

        Mockito.verify(cardRepository, Mockito.times(1)).findById(Mockito.anyLong());
        Mockito.verify(cardRepository, Mockito.times(1)).deleteById(Mockito.anyLong());
        Assert.assertTrue(result);
    }

    @Test
    public void deleteFailTest() {
        Mockito.doReturn(Optional.empty()).when(cardRepository).findById(10L);

        boolean result = cardService.delete(10);

        Mockito.verify(cardRepository, Mockito.times(1)).findById(Mockito.anyLong());
        Mockito.verify(cardRepository, Mockito.times(0)).deleteById(Mockito.anyLong());
        Assert.assertFalse(result);
    }
}