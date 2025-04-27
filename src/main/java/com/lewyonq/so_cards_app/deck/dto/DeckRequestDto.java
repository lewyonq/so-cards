package com.lewyonq.so_cards_app.deck.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class DeckRequestDto {
    @NotBlank(message = "Deck name cannot be blank")
    @Size(max = 200, message = "Deck name cannot exceed 200 characters")
    private String name;
    private String description;
}
