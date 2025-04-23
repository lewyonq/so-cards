package com.lewyonq.so_cards_app.card;

import com.lewyonq.so_cards_app.card.dto.CardDetailDto;
import com.lewyonq.so_cards_app.card.dto.CardRequestDto;
import com.lewyonq.so_cards_app.card.dto.CardResponseDto;
import com.lewyonq.so_cards_app.deck.DeckRepository;
import com.lewyonq.so_cards_app.exception.ResourceNotFoundException;
import com.lewyonq.so_cards_app.model.entity.Card;
import com.lewyonq.so_cards_app.model.entity.Deck;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

class CardServiceTest {
    CardRequestDto requestDto;
    Card card;
    Card savedCard;
    CardDetailDto cardDetailDto;
    CardResponseDto responseDto;

    @Mock
    private CardRepository cardRepository;

    @Mock
    private DeckRepository deckRepository;

    @Mock
    private CardMapper cardMapper;

    @InjectMocks
    private CardService cardService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        requestDto = new CardRequestDto();
        requestDto.setQuestion("test question");
        requestDto.setAnswer("test answer");

        card = Card.builder()
                .id(1L)
                .question("test question")
                .answer("test answer")
                .build();

        savedCard = Card.builder()
                .id(1L)
                .question("test question")
                .answer("test answer")
                .build();

        responseDto = new CardResponseDto();
        responseDto.setId(1L);
        responseDto.setQuestion("test question");
        responseDto.setAnswer("test answer");

        cardDetailDto = new CardDetailDto();
        cardDetailDto.setId(1L);
        cardDetailDto.setQuestion("test question");
        cardDetailDto.setAnswer("test answer");
    }

    @Test
    void createCard_Success() {
        when(deckRepository.findById(1L)).thenReturn(Optional.of(new Deck()));
        when(cardMapper.toEntity(requestDto)).thenReturn(card);
        when(cardRepository.save(card)).thenReturn(savedCard);
        when(cardMapper.toDetailDto(savedCard)).thenReturn(cardDetailDto);

        CardDetailDto result = cardService.createCard(1L, requestDto);

        verify(cardMapper).toEntity(requestDto);
        verify(cardRepository).save(card);
        verify(cardMapper).toDetailDto(savedCard);
        assertEquals(cardDetailDto.getId(), result.getId());
        assertEquals(cardDetailDto.getQuestion(), result.getQuestion());
        assertEquals(cardDetailDto.getAnswer(), result.getAnswer());
    }

    @Test
    void createCard_Failure_CardRequestIsNull() {
        requestDto = null;
        assertThrows(
                RuntimeException.class,
                () -> cardService.createCard(1L, requestDto)
        );
        verifyNoInteractions(cardMapper, cardRepository);
    }

    @Test
    void getCardDetails_Success() {
        Long id = 1L;
        when(cardRepository.findById(id)).thenReturn(Optional.of(card));
        when(cardMapper.toDetailDto(card)).thenReturn(cardDetailDto);

        CardDetailDto result = cardService.getCardDetails(id);

        assertEquals(cardDetailDto.getId(), result.getId());
        assertEquals(cardDetailDto.getQuestion(), result.getQuestion());
        assertEquals(cardDetailDto.getAnswer(), result.getAnswer());
    }

    @Test
    void getCardDetails_Failure() {
        Long id = 2L;
        when(cardRepository.findById(id)).thenReturn(Optional.empty());

        ResourceNotFoundException exception = assertThrows(
                ResourceNotFoundException.class,
                () -> cardService.getCardDetails(id)
        );
        verifyNoInteractions(cardMapper);
        assertEquals("Card with id 2 not found", exception.getMessage());
    }

    @Test
    void updateCard_Success() {
        Long id = 1L;
        when(cardRepository.findById(id)).thenReturn(Optional.of(card));
        cardService.updateCard(id, requestDto);
        verify(cardMapper).updateCardFromDto(requestDto, card);
        verify(cardMapper).toDetailDto(card);
        verify(cardRepository).findById(id);
    }

    @Test
    void updateCard_Failure_ResourceNotFound() {
        Long id = 2L;
        when(cardRepository.findById(id)).thenReturn(Optional.empty());

        ResourceNotFoundException exception = assertThrows(
                ResourceNotFoundException.class,
                () -> cardService.updateCard(id, requestDto)
        );
        verifyNoInteractions(cardMapper);
        assertEquals("Card with id 2 not found", exception.getMessage());
    }

    @Test
    void updateCard_Failure_RequestIsNull() {
        Long id = 1L;
        requestDto = null;
        assertThrows(
                RuntimeException.class,
                () -> cardService.updateCard(id, requestDto)
        );
        verifyNoInteractions(cardMapper);
    }

    @Test
    void deleteCard_Success() {
        Long id = 1L;
        doNothing().when(cardRepository).deleteById(id);
        cardService.deleteCard(id);

        verify(cardRepository).deleteById(id);
    }
}