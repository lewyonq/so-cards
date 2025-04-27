package com.lewyonq.so_cards_app.game.dto;

import com.lewyonq.so_cards_app.card.dto.CardResponseDto;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class ViewGameResponseDto {
    private Long gameId;
    private List<CardResponseDto> cardResponseDtos;
}
