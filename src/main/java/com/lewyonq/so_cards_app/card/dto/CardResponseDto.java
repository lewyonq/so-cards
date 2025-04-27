package com.lewyonq.so_cards_app.card.dto;

import lombok.Data;

@Data
public class CardResponseDto {
    private Long id;
    private String question;
    private String answer;
}
