package com.lewyonq.so_cards_app.game.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class TestGameResponseDto {
    private Long gameId;
    private List<TestQuestionDto> testQuestions;
}
