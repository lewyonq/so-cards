package com.lewyonq.so_cards_app.deck;

import com.lewyonq.so_cards_app.deck.dto.DeckDetailDto;
import com.lewyonq.so_cards_app.deck.dto.DeckRequestDto;
import com.lewyonq.so_cards_app.deck.dto.DeckResponseDto;
import com.lewyonq.so_cards_app.model.entity.Deck;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;

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
        assertEquals(dto.getId(), deck.getId());
        assertEquals(dto.getName(), deck.getName());
        assertEquals(dto.getDescription(), deck.getDescription());
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
        assertEquals(dto.getId(), deck.getId());
        assertEquals(dto.getName(), deck.getName());
        assertEquals(dto.getDescription(), deck.getDescription());
    }

    @Test
    void toDetailDto_Success() {
        Deck deck = Deck.builder()
                .id(1L)
                .name("test name")
                .description(null)
                .build();

        DeckDetailDto dto = deckMapper.toDetailDto(deck);

        assertNotNull(dto);
        assertEquals(dto.getId(), deck.getId());
        assertEquals(dto.getName(), deck.getName());
        assertEquals(dto.getDescription(), deck.getDescription());
        assertEquals(dto.getCreatedAt(), deck.getCreatedAt());
        assertEquals(dto.getUpdatedAt(), deck.getUpdatedAt());
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