package com.lewyonq.so_cards_app.game;

import com.lewyonq.so_cards_app.card.CardMapper;
import com.lewyonq.so_cards_app.card.dto.CardResponseDto;
import com.lewyonq.so_cards_app.deck.DeckRepository;
import com.lewyonq.so_cards_app.exception.ResourceNotFoundException;
import com.lewyonq.so_cards_app.game.dto.GameRequestDto;
import com.lewyonq.so_cards_app.game.dto.GameResponseDto;
import com.lewyonq.so_cards_app.model.entity.Card;
import com.lewyonq.so_cards_app.model.entity.Deck;
import com.lewyonq.so_cards_app.model.entity.Game;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Service
@RequiredArgsConstructor
public class GameService {
    private final GameRepository gameRepository;
    private final DeckRepository deckRepository;
    private final CardMapper cardMapper;

    @Transactional
    public Game createGame(GameRequestDto gameRequestDto) {
        Deck deck = findDeckById(gameRequestDto.getDeckId());
        List<Card> gameCards = prepareShuffledLimitedCards(gameRequestDto.getCardsLimit(), deck.getCards());
        Game game = Game.builder()
                .gameType(gameRequestDto.getGameType())
                .deck(deck)
                .cards(gameCards)
                .build();

        return gameRepository.save(game);
    }

    @Transactional
    public void makeGameAsFinished(Long id) {
        Game game = gameRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Game", id));
        if (game.getFinishedAt() != null) {
            throw new IllegalStateException("Game is already finished");
        }
        game.setFinishedAt(LocalDateTime.now());
    }

    public GameResponseDto createViewModeGame(GameRequestDto gameRequestDto) {
        Game game = createGame(gameRequestDto);
        List<CardResponseDto> cardResponseDtos = game.getCards().stream()
                .map(cardMapper::toResponseDto)
                .toList();

        GameResponseDto gameResponseDto = new GameResponseDto();
        gameResponseDto.setGameId(game.getId());
        gameResponseDto.setCardResponseDtos(cardResponseDtos);

        return gameResponseDto;
    }

    public GameResponseDto createTestModeGame(GameRequestDto gameRequestDto) {
        return new GameResponseDto();
    }

    public GameResponseDto createAnswerModeGame(GameRequestDto gameRequestDto) {
        return new GameResponseDto();
    }

    private Deck findDeckById(Long id) {
        return deckRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Deck", id));
    }

    private List<Card> prepareShuffledLimitedCards(int limit, List<Card> deckCards) {
        if (deckCards == null || deckCards.isEmpty()) {
            return Collections.emptyList();
        }

        int checkedLimit = Math.min(limit, deckCards.size());

        List<Card> cardsCopy = new ArrayList<>(deckCards);
        Collections.shuffle(cardsCopy);
        return cardsCopy.subList(0, checkedLimit);
    }
}
