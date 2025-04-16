package com.lewyonq.so_cards_app.deck;

import com.lewyonq.so_cards_app.deck.dto.DeckRequestDto;
import com.lewyonq.so_cards_app.deck.dto.DeckResponseDto;
import com.lewyonq.so_cards_app.model.entity.Deck;
import jakarta.transaction.Transactional;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class DeckService {
    private final DeckRepository deckRepository;
    private final DeckMapper deckMapper;

    @Transactional
    public DeckResponseDto createDeck(@NonNull DeckRequestDto requestDto) {
        Deck deck = deckMapper.toEntity(requestDto);
        Deck savedDeck = deckRepository.save(deck);

        return deckMapper.toResponseDto(savedDeck);
    }
}
