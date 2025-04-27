package com.lewyonq.so_cards_app.card;

import com.lewyonq.so_cards_app.card.dto.CardDetailDto;
import com.lewyonq.so_cards_app.card.dto.CardRequestDto;
import com.lewyonq.so_cards_app.deck.DeckRepository;
import com.lewyonq.so_cards_app.exception.ResourceNotFoundException;
import com.lewyonq.so_cards_app.model.entity.Card;
import com.lewyonq.so_cards_app.model.entity.Deck;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class CardService {
    private final CardMapper cardMapper;
    private final CardRepository cardRepository;
    private final DeckRepository deckRepository;

    @Transactional
    public CardDetailDto createCard(Long deckId, CardRequestDto requestDto) {
        log.debug("Attempting to create card with question: {}", requestDto.getQuestion());
        Deck deck = findDeckById(deckId);
        Card card = cardMapper.toEntity(requestDto);
        card.setDeck(deck);

        Card savedCard = cardRepository.save(card);
        log.info("Successfully created card with ID: {} to deck with ID: {}", savedCard.getId(), deck.getId());

        return cardMapper.toDetailDto(savedCard);
    }

    @Transactional
    public List<CardDetailDto> createCards(Long deckId, List<CardRequestDto> requestDtos) {
        log.debug("Attempting to create {} cards for deck ID: {}", requestDtos.size(), deckId);
        Deck deck = findDeckById(deckId);
        List<Card> cards = requestDtos.stream()
                .map(cardMapper::toEntity)
                .peek(card -> card.setDeck(deck))
                .toList();

        List<Card> savedCards = cardRepository.saveAll(cards);
        log.info("Successfully created {} cards to deck with ID: {}", requestDtos.size(), deck.getId());

        return savedCards.stream()
                .map(cardMapper::toDetailDto)
                .toList();
    }

    @Transactional
    public CardDetailDto getCardDetails(Long id) {
        Card card = findCardById(id);
        log.info("Successfully get card details with ID: {}", id);

        return cardMapper.toDetailDto(card);
    }

    @Transactional
    public CardDetailDto updateCard(Long id, CardRequestDto requestDto) {
        log.debug("Attempting to update card with id: {}", id);
        Card card = findCardById(id);
        cardMapper.updateCardFromDto(requestDto, card);
        log.info("Successfully updated card with ID: {}", id);

        return cardMapper.toDetailDto(card);
    }

    @Transactional
    public void deleteCard(Long id) {
        log.debug("Attempting to delete card with id: {}", id);
        cardRepository.deleteById(id);
        log.info("Successfully deleted card with ID: {}", id);
    }

    private Card findCardById(Long id) {
        return cardRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Card", id));
    }

    private Deck findDeckById(Long id) {
        return deckRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Deck", id));
    }
}
