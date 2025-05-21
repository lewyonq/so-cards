package com.lewyonq.so_cards_app.card;

import com.lewyonq.so_cards_app.card.dto.CardDetailDto;
import com.lewyonq.so_cards_app.card.dto.CardRequestDto;
import com.lewyonq.so_cards_app.exception.ResourceNotFoundException;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("api/v1/card")
@Slf4j
@CrossOrigin("http://localhost:4200")
public class CardController {
    private final CardService cardService;

    @PostMapping("/add-to-deck/{deckId}")
    public ResponseEntity<CardDetailDto> createCard(
            @PathVariable Long deckId,
            @Valid @RequestBody CardRequestDto requestDto
    ) {
        log.debug("Entering createCard method");

        try {
            CardDetailDto cardDetailDto = cardService.createCard(deckId, requestDto);
            URI location = ServletUriComponentsBuilder
                    .fromCurrentRequest()
                    .path("/{id}")
                    .buildAndExpand(cardDetailDto.getId())
                    .toUri();
            log.info("Successfully created card with ID: {}. Returning HTTP 201 Created, location: {}",
                    cardDetailDto.getId(), location);
            return ResponseEntity.created(location).body(cardDetailDto);
        } catch (ResourceNotFoundException e) {
            log.warn("{}. Returning HTTP 404", e.getMessage());
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping("/multiple-add-to-deck/{deckId}")
    public ResponseEntity<List<CardDetailDto>> createCards(
            @PathVariable Long deckId,
            @Valid @RequestBody List<CardRequestDto> requestDtos
    ) {
        log.debug("Entering createCards method");

        try {
            List<CardDetailDto> cardDetailDtos = cardService.createCards(deckId, requestDtos);
            log.info("Successfully created cards. Returning HTTP 201 Created");
            return ResponseEntity.ok(cardDetailDtos);
        } catch (ResourceNotFoundException e) {
            log.warn("{}. Returning HTTP 404", e.getMessage());
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<CardDetailDto> getCardDetails(@PathVariable Long id) {
        log.debug("Entering getCardDetails method");
        try {
            CardDetailDto cardDetailDto = cardService.getCardDetails(id);
            log.info("Successfully retrieved details for card ID: {}. Returning HTTP 200", id);
            return ResponseEntity.ok(cardDetailDto);
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<CardDetailDto> updateCard(
            @PathVariable Long id,
            @Valid @RequestBody CardRequestDto requestDto
    ) {
        log.debug("Entering updateCard method");
        try {
            CardDetailDto cardDetail = cardService.updateCard(id, requestDto);
            log.info("Successfully updated details for card ID: {}. Returning HTTP 200", id);
            return ResponseEntity.ok(cardDetail);
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCard(@PathVariable Long id) {
        log.debug("Entering deleteCard method");
        try {
            cardService.deleteCard(id);
            log.info("Successfully deleted card ID: {}. Returning HTTP 204", id);
            return ResponseEntity.noContent().build();
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }
}
