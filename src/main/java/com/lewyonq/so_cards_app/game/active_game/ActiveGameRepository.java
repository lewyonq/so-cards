package com.lewyonq.so_cards_app.game.active_game;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ActiveGameRepository extends JpaRepository<ActiveGame, Long> {
    Optional<ActiveGame> findByGameId(Long gameId);
}
