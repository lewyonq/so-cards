package com.lewyonq.so_cards_app.game.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class GameResultDto {
    private Long id;
    private Integer totalScore;
    private Integer amountOfQuestions;
    private Double scoreInPercent;
    private Long durationInSeconds;
}
