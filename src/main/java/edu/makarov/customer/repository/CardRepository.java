package edu.makarov.customer.repository;

import edu.makarov.customer.models.Card;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CardRepository extends JpaRepository<Card, Long> {

    List<Card> findCardByAccountId(long id);
}
