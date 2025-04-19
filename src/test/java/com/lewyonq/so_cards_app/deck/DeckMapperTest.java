package com.lewyonq.so_cards_app.deck;

import com.lewyonq.so_cards_app.card.dto.CardBasicDto;
import com.lewyonq.so_cards_app.deck.dto.DeckDetailDto;
import com.lewyonq.so_cards_app.deck.dto.DeckRequestDto;
import com.lewyonq.so_cards_app.deck.dto.DeckResponseDto;
import com.lewyonq.so_cards_app.model.entity.Card;
import com.lewyonq.so_cards_app.model.entity.Deck;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class DeckMapperTest {
    private DeckMapper deckMapper;

    @BeforeEach
    void setUp() {
        deckMapper = Mappers.getMapper(DeckMapper.class);
    }

    @Test
    void toEntity_Success() {
        DeckRequestDto dto = new DeckRequestDto();
        dto.setName("test name");
        dto.setDescription("test description");

        Deck deck = deckMapper.toEntity(dto);

        assertNotNull(deck);
        assertEquals(dto.getName(), deck.getName());
        assertEquals(dto.getDescription(), deck.getDescription());
    }

    @Test
    void toResponseDto_Success() {
        Deck deck = Deck.builder()
                .id(1L)
                .name("test name")
                .description("test description")
                .build();

        DeckResponseDto dto = deckMapper.toResponseDto(deck);

        assertNotNull(dto);
        assertEquals(deck.getId(), dto.getId());
        assertEquals(deck.getName(), dto.getName());
        assertEquals(deck.getDescription(), dto.getDescription());
    }

    @Test
    void toResponseDto_Success_WhenDescriptionNull() {
        Deck deck = Deck.builder()
                .id(1L)
                .name("test name")
                .description(null)
                .build();

        DeckResponseDto dto = deckMapper.toResponseDto(deck);

        assertNotNull(dto);
        assertEquals(deck.getId(), dto.getId());
        assertEquals(deck.getName(), dto.getName());
        assertEquals(deck.getDescription(), dto.getDescription());
    }

    @Test
    void toDetailDto_Success_WithNoCards() {
        Deck deck = Deck.builder()
                .id(1L)
                .name("test name")
                .description(null)
                .cards(new ArrayList<>())
                .build();

        DeckDetailDto dto = deckMapper.toDetailDto(deck);

        assertNotNull(dto);
        assertEquals(deck.getId(), dto.getId());
        assertEquals(deck.getName(), dto.getName());
        assertEquals(deck.getDescription(), dto.getDescription());
        assertEquals(deck.getCreatedAt(), dto.getCreatedAt());
        assertEquals(deck.getUpdatedAt(), dto.getUpdatedAt());
        assertEquals(new ArrayList<>(), dto.getCardDtos());
    }

    @Test
    void toDetailDto_Success_WithCards() {
        Card testCard1 = Card.builder()
                .id(1L)
                .question("Test question 1")
                .answer("Test answer 1")
                .build();
        Card testCard2 = Card.builder()
                .id(2L)
                .question("Test question 2")
                .answer("Test answer 2")
                .build();
        List<Card> cards = List.of(testCard1, testCard2);

        Deck deck = Deck.builder()
                .id(1L)
                .name("test name")
                .description(null)
                .cards(cards)
                .build();

        DeckDetailDto dto = deckMapper.toDetailDto(deck);

        assertNotNull(dto);
        assertEquals(deck.getId(), dto.getId());
        assertEquals(deck.getName(), dto.getName());
        assertEquals(deck.getDescription(), dto.getDescription());
        assertEquals(deck.getCreatedAt(), dto.getCreatedAt());
        assertEquals(deck.getUpdatedAt(), dto.getUpdatedAt());
        assertNotNull(dto.getCardDtos());
        assertEquals(2, dto.getCardDtos().size());

        CardBasicDto actualCardDto1 = dto.getCardDtos().getFirst();
        assertEquals(testCard1.getId(), actualCardDto1.getId());
        assertEquals(testCard1.getQuestion(), actualCardDto1.getQuestion());
        assertEquals(testCard1.getAnswer(), actualCardDto1.getAnswer());

        CardBasicDto actualCardDto2 = dto.getCardDtos().get(1);
        assertEquals(testCard2.getId(), actualCardDto2.getId());
        assertEquals(testCard2.getQuestion(), actualCardDto2.getQuestion());
        assertEquals(testCard2.getAnswer(), actualCardDto2.getAnswer());
    }

    @Test
    void updateDeckFromDto_Success() {
        Deck deck = Deck.builder()
                .id(1L)
                .name("test name")
                .description("test description")
                .build();

        DeckRequestDto dto = new DeckRequestDto();
        dto.setName("updated test name");
        dto.setDescription("updated test description");

        deckMapper.updateDeckFromDto(dto, deck);

        assertNotNull(deck);
        assertEquals(dto.getName(), deck.getName());
        assertEquals(dto.getDescription(), deck.getDescription());
    }

    @Test
    void updateDeckFromDto_Success_WithNullDescription() {
        Deck deck = Deck.builder()
                .id(1L)
                .name("test name")
                .description("test description")
                .build();

        DeckRequestDto dto = new DeckRequestDto();
        dto.setName("updated test name");
        dto.setDescription(null);

        deckMapper.updateDeckFromDto(dto, deck);

        assertNotNull(deck);
        assertEquals(dto.getName(), deck.getName());
        assertEquals("test description", deck.getDescription());
    }

    @Test
    void updateDeckFromDto_Success_WithNullDescriptionAndName() {
        Deck deck = Deck.builder()
                .id(1L)
                .name("test name")
                .description("test description")
                .build();

        DeckRequestDto dto = new DeckRequestDto();
        dto.setName(null);
        dto.setDescription(null);

        deckMapper.updateDeckFromDto(dto, deck);

        assertNotNull(deck);
        assertEquals("test name", deck.getName());
        assertEquals("test description", deck.getDescription());
    }
}