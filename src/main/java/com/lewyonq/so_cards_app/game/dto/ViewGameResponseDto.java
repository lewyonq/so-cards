package com.lewyonq.so_cards_app.game.dto;

import com.lewyonq.so_cards_app.card.dto.CardResponseDto;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class ViewGameResponseDto {
    private Long gameId;
    private List<CardResponseDto> cardResponseDtos;
}
