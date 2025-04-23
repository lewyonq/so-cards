package com.lewyonq.so_cards_app.card;

import com.lewyonq.so_cards_app.card.dto.CardDetailDto;
import com.lewyonq.so_cards_app.card.dto.CardRequestDto;
import com.lewyonq.so_cards_app.card.dto.CardResponseDto;
import com.lewyonq.so_cards_app.model.entity.Card;
import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

@Mapper(componentModel = "spring")
public interface CardMapper {
    Card toEntity(CardRequestDto dto);
    CardResponseDto toResponseDto(Card entity);
    CardDetailDto toDetailDto(Card entity);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateCardFromDto(CardRequestDto dto, @MappingTarget Card entity);
}
