package com.lewyonq.so_cards_app.game.dto;

import lombok.Data;

import java.util.List;

@Data
public class TestQuestionDto {
    private String question;
    private List<TestAnswerDto> answers;
}
