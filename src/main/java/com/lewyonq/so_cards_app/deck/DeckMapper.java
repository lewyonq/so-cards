package com.lewyonq.so_cards_app.deck;

import com.lewyonq.so_cards_app.card.CardMapper;
import com.lewyonq.so_cards_app.deck.dto.DeckDetailDto;
import com.lewyonq.so_cards_app.deck.dto.DeckRequestDto;
import com.lewyonq.so_cards_app.deck.dto.DeckResponseDto;
import com.lewyonq.so_cards_app.model.entity.Deck;
import org.mapstruct.*;

@Mapper(componentModel = "spring", uses = CardMapper.class)
public interface DeckMapper {
    Deck toEntity(DeckRequestDto dto);
    DeckResponseDto toResponseDto(Deck deck);

    @Mapping(source = "cards", target = "cardDtos")
    DeckDetailDto toDetailDto(Deck deck);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateDeckFromDto(DeckRequestDto dto, @MappingTarget Deck entity);
}
