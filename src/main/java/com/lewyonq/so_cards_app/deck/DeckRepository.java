package com.lewyonq.so_cards_app.deck;

import com.lewyonq.so_cards_app.model.entity.Deck;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DeckRepository extends JpaRepository<Deck, Long> {
}
