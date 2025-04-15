package com.lewyonq.so_cards_app.card.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CardBasicDto {
    private Long id;
    private String question;
    private String answer;
}
