package edu.makarov.customer.repository;

import edu.makarov.customer.models.Account;
import edu.makarov.customer.models.Card;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface CardRepository extends JpaRepository<Card, Long> {

    //List<Card> findCardByAccountId(long id);

    Optional<List<Card>> findCardsByAccount(Account account);
}
