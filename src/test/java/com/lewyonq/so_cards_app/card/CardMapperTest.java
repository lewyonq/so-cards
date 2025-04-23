package com.lewyonq.so_cards_app.card;

import com.lewyonq.so_cards_app.card.dto.CardDetailDto;
import com.lewyonq.so_cards_app.card.dto.CardRequestDto;
import com.lewyonq.so_cards_app.card.dto.CardResponseDto;
import com.lewyonq.so_cards_app.model.entity.Card;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;

class CardMapperTest {
    private CardMapper cardMapper;

    @BeforeEach
    void setUp() {
        cardMapper = Mappers.getMapper(CardMapper.class);
    }

    @Test
    void toEntity_Success() {
        CardRequestDto dto = new CardRequestDto();
        dto.setQuestion("test question");
        dto.setAnswer("test answer");

        Card card = cardMapper.toEntity(dto);

        assertEquals(dto.getQuestion(), card.getQuestion());
        assertEquals(dto.getAnswer(), card.getAnswer());
    }

    @Test
    void toResponseDto_Success() {
        Card card = Card.builder()
                .question("test question")
                .answer("test answer")
                .build();

        CardResponseDto responseDto = cardMapper.toResponseDto(card);

        assertEquals(card.getQuestion(), responseDto.getQuestion());
        assertEquals(card.getAnswer(), responseDto.getAnswer());
    }

    @Test
    void toDetailDto_Success() {
        Card card = Card.builder()
                .question("test question")
                .answer("test answer")
                .createdAt(LocalDateTime.of(2025,4,22,9,58))
                .build();

        CardDetailDto detailDto = cardMapper.toDetailDto(card);

        assertEquals(card.getQuestion(), detailDto.getQuestion());
        assertEquals(card.getAnswer(), detailDto.getAnswer());
        assertEquals(card.getCreatedAt(), detailDto.getCreatedAt());
    }

    @Test
    void updateCardFromDto_Success() {
        Card card = Card.builder()
                .question("test question")
                .answer("test answer")
                .createdAt(LocalDateTime.of(2025,4,22,9,58))
                .build();

        CardRequestDto requestDto = new CardRequestDto();
        requestDto.setQuestion("updated question");
        requestDto.setAnswer("updated answer");

        cardMapper.updateCardFromDto(requestDto, card);

        assertEquals(requestDto.getQuestion(), card.getQuestion());
        assertEquals(requestDto.getAnswer(), card.getAnswer());
    }

    @Test
    void updateCardFromDto_Success_WithNullQuestionAndAnswer() {
        Card card = Card.builder()
                .question("test question")
                .answer("test answer")
                .createdAt(LocalDateTime.of(2025,4,22,9,58))
                .build();

        CardRequestDto requestDto = new CardRequestDto();
        requestDto.setQuestion(null);
        requestDto.setAnswer(null);

        cardMapper.updateCardFromDto(requestDto, card);

        assertEquals("test question", card.getQuestion());
        assertEquals("test answer", card.getAnswer());
    }
}