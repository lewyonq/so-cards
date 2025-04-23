package com.lewyonq.so_cards_app.card.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CardRequestDto {
    @NotBlank(message = "Card question cannot be blank")
    @Size(max = 250, message = "Card question cannot exceed 250 characters")
    private String question;

    @NotBlank(message = "Card answer cannot be blank")
    private String answer;
}
