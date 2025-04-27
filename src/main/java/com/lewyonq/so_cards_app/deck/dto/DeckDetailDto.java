package com.lewyonq.so_cards_app.deck.dto;

import com.lewyonq.so_cards_app.card.dto.CardResponseDto;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
public class DeckDetailDto {
    private Long id;
    private String name;
    private String description;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private List<CardResponseDto> cardDtos;
}
