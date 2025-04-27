package com.lewyonq.so_cards_app.game;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.lewyonq.so_cards_app.ai.AIService;
import com.lewyonq.so_cards_app.card.CardMapper;
import com.lewyonq.so_cards_app.card.dto.CardResponseDto;
import com.lewyonq.so_cards_app.deck.DeckRepository;
import com.lewyonq.so_cards_app.exception.ResourceNotFoundException;
import com.lewyonq.so_cards_app.game.dto.GameRequestDto;
import com.lewyonq.so_cards_app.game.dto.ViewGameResponseDto;
import com.lewyonq.so_cards_app.game.dto.TestGameResponseDto;
import com.lewyonq.so_cards_app.game.dto.TestQuestionDto;
import com.lewyonq.so_cards_app.model.entity.Card;
import com.lewyonq.so_cards_app.model.entity.Deck;
import com.lewyonq.so_cards_app.model.entity.Game;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class GameService {
    private final GameRepository gameRepository;
    private final DeckRepository deckRepository;
    private final CardMapper cardMapper;
    private final AIService aiService;
    private final ObjectMapper objectMapper;

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
    public void submitViewModeGame(Long id) {
        Game game = gameRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Game", id));
        if (game.getFinishedAt() != null) {
            throw new IllegalStateException("Game is already finished");
        }
        game.setFinishedAt(LocalDateTime.now());
    }

    public ViewGameResponseDto createViewModeGame(GameRequestDto gameRequestDto) {
        Game game = createGame(gameRequestDto);
        List<CardResponseDto> cardResponseDtos = game.getCards().stream()
                .map(cardMapper::toResponseDto)
                .toList();

        ViewGameResponseDto viewGameResponseDto = new ViewGameResponseDto();
        viewGameResponseDto.setGameId(game.getId());
        viewGameResponseDto.setCardResponseDtos(cardResponseDtos);

        return viewGameResponseDto;
    }

    public TestGameResponseDto createTestModeGame(GameRequestDto gameRequestDto) {
        Game game = createGame(gameRequestDto);
        List<CardResponseDto> cardResponseDtos = game.getCards().stream()
                .map(cardMapper::toResponseDto)
                .toList();
        List<TestQuestionDto> testQuestions = prepareTestQuestions(cardResponseDtos);

        TestGameResponseDto testGameResponseDto = new TestGameResponseDto();
        testGameResponseDto.setGameId(game.getId());
        testGameResponseDto.setTestQuestions(testQuestions);

        return testGameResponseDto;
    }

    public ViewGameResponseDto createAnswerModeGame(GameRequestDto gameRequestDto) {
        return new ViewGameResponseDto();
    }

    private List<TestQuestionDto> prepareTestQuestions(List<CardResponseDto> cardResponseDtos) {
        try {
            String jsonString = objectMapper.writeValueAsString(cardResponseDtos);
            String prompt = aiService.getMultipleChoiceJsonPrompt(jsonString);
            String response = aiService.chat(prompt);
            return objectMapper.readValue(response, new TypeReference<List<TestQuestionDto>>(){});
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
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
