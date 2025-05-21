package com.lewyonq.so_cards_app.deck.dto;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class DeckResponseDto {
    private Long id;
    private String name;
    private String description;
    private LocalDateTime lastStudied;
    private Integer cardCount;
}
