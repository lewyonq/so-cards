package com.lewyonq.so_cards_app.game.dto;

import lombok.Data;

@Data
public class TestQuestionDto {
    private String question;
    private TestAnswerDto a;
    private TestAnswerDto b;
    private TestAnswerDto c;
    private TestAnswerDto d;
}
