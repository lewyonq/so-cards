package com.lewyonq.so_cards_app.deck;

import com.lewyonq.so_cards_app.deck.dto.DeckDetailDto;
import com.lewyonq.so_cards_app.deck.dto.DeckRequestDto;
import com.lewyonq.so_cards_app.deck.dto.DeckResponseDto;
import com.lewyonq.so_cards_app.exception.ResourceNotFoundException;
import com.lewyonq.so_cards_app.model.entity.Deck;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class DeckService {
    private final DeckRepository deckRepository;
    private final DeckMapper deckMapper;

    @Transactional
    public DeckDetailDto createDeck(@NotNull DeckRequestDto requestDto) {
        log.debug("Attempting to create deck with name: {}", requestDto.getName());
        Deck deck = deckMapper.toEntity(requestDto);
        Deck savedDeck = deckRepository.save(deck);
        log.info("Successfully created deck with ID: {}", savedDeck.getId());

        return deckMapper.toDetailDto(savedDeck);
    }

    @Transactional(readOnly = true)
    public List<DeckResponseDto> getAllDecks() {
        log.debug("Attempting to get all decks");
        List<Deck> decks = deckRepository.findAll();
        log.info("Successfully found all decks");

        return decks.stream()
                .map(deckMapper::toResponseDto)
                .toList();
    }

    @Transactional(readOnly = true)
    public DeckDetailDto getDeckDetails(Long id) {
        log.debug("Attempting to get deck details with id: {}", id);
        Deck deck = findDeckById(id);
        log.info("Successfully get deck details with ID: {}", id);

        return deckMapper.toDetailDto(deck);
    }

    @Transactional
    public DeckDetailDto updateDeck(Long id, @NotNull DeckRequestDto requestDto) {
        Deck deck = findDeckById(id);
        log.debug("Attempting to edit deck with id: {}", id);
        deckMapper.updateDeckFromDto(requestDto, deck);
        log.info("Successfully updated deck with ID: {}", id);

        return deckMapper.toDetailDto(deck);
    }

    @Transactional
    public void deleteDeck(Long id) {
        Deck deck = findDeckById(id);
        log.debug("Attempting to delete deck with id: {}", id);
        deckRepository.delete(deck);
        log.info("Successfully deleted deck with ID: {}", id);
    }

    private Deck findDeckById(Long id) {
        log.trace("Attempting to find deck with id: {}", id);

        return deckRepository.findById(id)
                .orElseThrow(() -> {
                    log.warn("Deck not found with ID: {}", id);
                    return new ResourceNotFoundException("Deck", id);
                });
    }
}
