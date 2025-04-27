package com.lewyonq.so_cards_app.game.dto;

import com.lewyonq.so_cards_app.model.enums.GameType;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class GameRequestDto {
    @NotNull(message = "Deck id cannot be null")
    private Long deckId;

    @NotNull(message = "Game type cannot be null")
    private GameType gameType;

    @NotNull(message = "Cards limit cannot be null. It must be between 1 and 30")
    @Min(1)
    @Max(30)
    private Integer cardsLimit;
}
