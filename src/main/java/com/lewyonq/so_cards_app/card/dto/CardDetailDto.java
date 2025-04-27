package com.lewyonq.so_cards_app.card.dto;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class CardDetailDto {
    private Long id;
    private String question;
    private String answer;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
