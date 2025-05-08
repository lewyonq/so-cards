package com.lewyonq.so_cards_app.game;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.lewyonq.so_cards_app.ai.AIService;
import com.lewyonq.so_cards_app.card.CardMapper;
import com.lewyonq.so_cards_app.card.dto.CardResponseDto;
import com.lewyonq.so_cards_app.deck.DeckRepository;
import com.lewyonq.so_cards_app.exception.ResourceNotFoundException;
import com.lewyonq.so_cards_app.game.active_game.ActiveGame;
import com.lewyonq.so_cards_app.game.active_game.ActiveGameRepository;
import com.lewyonq.so_cards_app.game.dto.*;
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
import java.util.Objects;
import java.util.stream.IntStream;

@Slf4j
@Service
@RequiredArgsConstructor
public class GameService {
    private final GameRepository gameRepository;
    private final DeckRepository deckRepository;
    private final ActiveGameRepository activeGameRepository;
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

    @Transactional
    public void submitViewModeGame(Long id) {
        Game game = gameRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Game", id));
        if (game.getFinishedAt() != null) {
            throw new IllegalStateException("Game is already finished");
        }
        game.setFinishedAt(LocalDateTime.now());
    }

    @Transactional
    public TestGameResponseDto createTestModeGame(GameRequestDto gameRequestDto) {
        Game game = createGame(gameRequestDto);
        List<CardResponseDto> cardDtos = game.getCards().stream()
                .map(cardMapper::toResponseDto)
                .toList();
        List<TestQuestionDto> testQuestions = prepareTestQuestions(cardDtos);

        List<Integer> correctAnswerIds = testQuestions.stream()
                .flatMapToInt(testQuestionDto -> IntStream.range(0, testQuestionDto.getAnswers().size())
                        .filter(i -> testQuestionDto.getAnswers().get(i).getCorrect()))
                .boxed()
                .toList();

        ActiveGame activeGame = ActiveGame.builder()
                .gameId(game.getId())
                .correctAnswerIndexes(correctAnswerIds)
                .build();

        activeGameRepository.save(activeGame);

        TestGameResponseDto testGameResponseDto = new TestGameResponseDto();
        testGameResponseDto.setGameId(game.getId());
        testGameResponseDto.setTestQuestions(testQuestions);

        return testGameResponseDto;
    }

    @Transactional
    public Integer submitTestModeGame(Long gameId, List<Integer> answerIndexes) {
        Game game = gameRepository.findById(gameId)
                .orElseThrow(() -> new ResourceNotFoundException("Game", gameId));
        int sum = 0;

        if (game.getFinishedAt() != null) {
            throw new IllegalStateException("Game is already finished");
        }

        ActiveGame activeGame = activeGameRepository.findByGameId(gameId)
                .orElseThrow(() -> new ResourceNotFoundException("Game with id: " + gameId + " is not active."));

        for (int i = 0; i < activeGame.getCorrectAnswerIndexes().size(); i++) {
            if (Objects.equals(activeGame.getCorrectAnswerIndexes().get(i), answerIndexes.get(i))) {
                sum++;
            }
        }

        activeGameRepository.delete(activeGame);
        game.setFinishedAt(LocalDateTime.now());

        return sum;
    }

    public ViewGameResponseDto createAnswerModeGame(GameRequestDto gameRequestDto) {
        return new ViewGameResponseDto();
    }

    private List<TestQuestionDto> prepareTestQuestions(List<CardResponseDto> cardResponseDtos) {
        try {
            String jsonString = objectMapper.writeValueAsString(cardResponseDtos);
            String prompt = aiService.getMultipleChoiceJsonPrompt(jsonString);
            String response = aiService.chat(prompt);
            List<TestQuestionDto> testQuestions =
                    objectMapper.readValue(response, new TypeReference<List<TestQuestionDto>>(){});
            for (TestQuestionDto testQuestion : testQuestions) {
                Collections.shuffle(testQuestion.getAnswers());
            }
            return testQuestions;
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
