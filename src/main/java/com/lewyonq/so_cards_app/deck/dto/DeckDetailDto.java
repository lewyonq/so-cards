package com.lewyonq.so_cards_app.deck.dto;

import com.lewyonq.so_cards_app.card.dto.CardBasicDto;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
public class DeckDetailDto {
    private Long id;
    private String name;
    private String description;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private List<CardBasicDto> cardDtos;
}
