package com.lewyonq.so_cards_app.deck;

import com.lewyonq.so_cards_app.deck.dto.DeckRequestDto;
import com.lewyonq.so_cards_app.deck.dto.DeckResponseDto;
import com.lewyonq.so_cards_app.model.entity.Deck;
import org.mapstruct.Mapper;

@Mapper
public interface DeckMapper {

    DeckResponseDto toResponseDto(Deck deck);

    Deck toEntity(DeckRequestDto dto);
}
