package com.lewyonq.so_cards_app.deck;

import com.lewyonq.so_cards_app.deck.dto.DeckDetailDto;
import com.lewyonq.so_cards_app.deck.dto.DeckRequestDto;
import com.lewyonq.so_cards_app.deck.dto.DeckResponseDto;
import com.lewyonq.so_cards_app.exception.ResourceNotFoundException;
import com.lewyonq.so_cards_app.model.entity.Deck;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

class DeckServiceTest {
    DeckRequestDto requestDto;
    Deck deck;
    Deck savedDeck;
    DeckDetailDto deckDetailDto;
    DeckResponseDto responseDto;

    @Mock
    private DeckRepository deckRepository;

    @Mock
    private DeckMapper deckMapper;

    @InjectMocks
    private DeckService deckService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        requestDto = new DeckRequestDto();
        requestDto.setName("test name");
        requestDto.setDescription("test description");

        deck = Deck.builder()
                .id(1L)
                .name("test name")
                .description("test description")
                .build();

        savedDeck = Deck.builder()
                .id(1L)
                .name("test name")
                .description("test description")
                .build();

        responseDto = new DeckResponseDto();
        responseDto.setId(1L);
        responseDto.setName("test name");
        responseDto.setDescription("test description");

        deckDetailDto = new DeckDetailDto();
        deckDetailDto.setId(1L);
        deckDetailDto.setName("test name");
        deckDetailDto.setDescription("test description");
    }

    @Test
    void createDeck_Success() {
        when(deckMapper.toEntity(requestDto)).thenReturn(deck);
        when(deckRepository.save(deck)).thenReturn(savedDeck);
        when(deckMapper.toDetailDto(savedDeck)).thenReturn(deckDetailDto);

        DeckDetailDto result = deckService.createDeck(requestDto);

        verify(deckMapper).toEntity(requestDto);
        verify(deckRepository).save(deck);
        verify(deckMapper).toDetailDto(savedDeck);
        assertEquals(deckDetailDto.getId(), result.getId());
        assertEquals(deckDetailDto.getName(), result.getName());
        assertEquals(deckDetailDto.getDescription(), result.getDescription());
    }

    @Test
    void createDeck_Failure_DeckRequestIsNull() {
        requestDto = null;
        assertThrows(
                RuntimeException.class,
                () -> deckService.createDeck(requestDto)
        );
        verifyNoInteractions(deckMapper, deckRepository);
    }

    @Test
    void getAllDecks_Success() {
        List<Deck> decks = List.of(deck);
        when(deckRepository.findAll()).thenReturn(decks);
        when(deckMapper.toResponseDto(decks.getFirst())).thenReturn(responseDto);

        List<DeckResponseDto> result = deckService.getAllDecks();

        verify(deckRepository).findAll();
        verify(deckMapper).toResponseDto(decks.getFirst());
        assertEquals(decks.getFirst().getName(), result.getFirst().getName());
        assertEquals(decks.getFirst().getDescription(), result.getFirst().getDescription());
    }

    @Test
    void getAllDecks_Success_EmptyList() {
        List<Deck> decks = Collections.emptyList();
        List<DeckResponseDto> deckResponseDtos = Collections.emptyList();
        when(deckRepository.findAll()).thenReturn(decks);

        List<DeckResponseDto> result = deckService.getAllDecks();

        verify(deckRepository).findAll();
        verifyNoInteractions(deckMapper);
        assertEquals(deckResponseDtos, result);
    }

    @Test
    void getDeckDetails_Success() {
        Long id = 1L;
        when(deckRepository.findById(id)).thenReturn(Optional.of(deck));
        when(deckMapper.toDetailDto(deck)).thenReturn(deckDetailDto);

        DeckDetailDto result = deckService.getDeckDetails(id);

        assertEquals(deckDetailDto.getId(), result.getId());
        assertEquals(deckDetailDto.getName(), result.getName());
        assertEquals(deckDetailDto.getDescription(), result.getDescription());
    }

    @Test
    void getDeckDetails_Failure() {
        Long id = 2L;
        when(deckRepository.findById(id)).thenReturn(Optional.empty());

        ResourceNotFoundException exception = assertThrows(
                ResourceNotFoundException.class,
                () -> deckService.getDeckDetails(id)
        );
        verifyNoInteractions(deckMapper);
        assertEquals("Deck with id 2 not found", exception.getMessage());
    }

    @Test
    void updateDeck_Success() {
        Long id = 1L;
        when(deckRepository.findById(id)).thenReturn(Optional.of(deck));
        deckService.updateDeck(id, requestDto);
        verify(deckMapper).updateDeckFromDto(requestDto, deck);
        verify(deckMapper).toDetailDto(deck);
        verify(deckRepository).findById(id);
    }

    @Test
    void updateDeck_Failure_ResourceNotFound() {
        Long id = 2L;
        when(deckRepository.findById(id)).thenReturn(Optional.empty());

        ResourceNotFoundException exception = assertThrows(
                ResourceNotFoundException.class,
                () -> deckService.updateDeck(id, requestDto)
        );
        verifyNoInteractions(deckMapper);
        assertEquals("Deck with id 2 not found", exception.getMessage());
    }

    @Test
    void updateDeck_Failure_RequestIsNull() {
        Long id = 1L;
        requestDto = null;
        assertThrows(
                RuntimeException.class,
                () -> deckService.updateDeck(id, requestDto)
        );
        verifyNoInteractions(deckMapper);
    }

    @Test
    void deleteDeck_Success() {
        Long id = 1L;
        doNothing().when(deckRepository).deleteById(id);
        deckService.deleteDeck(id);

        verify(deckRepository).deleteById(id);
    }
}