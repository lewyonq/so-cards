package com.lewyonq.so_cards_app.deck;

import com.lewyonq.so_cards_app.deck.dto.DeckDetailDto;
import com.lewyonq.so_cards_app.deck.dto.DeckRequestDto;
import com.lewyonq.so_cards_app.deck.dto.DeckResponseDto;
import com.lewyonq.so_cards_app.exception.ResourceNotFoundException;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("api/v1/deck")
@Slf4j
public class DeckController {
    private final DeckService deckService;

    @PostMapping
    public ResponseEntity<DeckDetailDto> createDeck(@Valid @RequestBody DeckRequestDto requestDto) {
        log.debug("Entering createDeck method");
        DeckDetailDto deckDetailDto = deckService.createDeck(requestDto);

        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(deckDetailDto.getId())
                .toUri();
        log.info("Successfully created deck with ID: {}. Returning HTTP 201 Created, location: {}",
                deckDetailDto.getId(), location);

        return ResponseEntity
                .created(location)
                .body(deckDetailDto);
    }

    @GetMapping
    public ResponseEntity<List<DeckResponseDto>> getAllDecks() {
        log.debug("Entering getAllDecks method");
        List<DeckResponseDto> decks = deckService.getAllDecks();
        log.info("Successfully retrieved {} decks. Returning HTTP {}", decks.size(), HttpStatus.OK);
        return ResponseEntity.ok(decks);
    }

    @GetMapping("/{id}")
    public ResponseEntity<DeckDetailDto> getDeckDetails(@PathVariable Long id) {
        log.debug("Entering getDeckDetails method");
        try {
            DeckDetailDto deckDetails = deckService.getDeckDetails(id);
            log.info("Successfully retrieved details for deck ID: {}. Returning HTTP 200", id);
            return ResponseEntity.ok(deckDetails);
        } catch (ResourceNotFoundException e) {
            log.warn("{}. Returning HTTP 404", e.getMessage());
            return ResponseEntity.notFound().build();
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<DeckDetailDto> updateDeck(@PathVariable Long id,
                                                    @Valid @RequestBody DeckRequestDto requestDto) {
        log.debug("Entering updateDeck method");
        try {
            DeckDetailDto deckDetail = deckService.updateDeck(id, requestDto);
            log.info("Successfully updated details for deck ID: {}. Returning HTTP 200", id);
            return ResponseEntity.ok(deckDetail);
        } catch (ResourceNotFoundException e) {
            log.warn("{}. Returning HTTP 404", e.getMessage());
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteDeck(@PathVariable Long id) {
        log.debug("Entering deleteDeck method");
        try {
            deckService.deleteDeck(id);
            log.info("Successfully deleted deck ID: {}. Returning HTTP 204", id);
            return ResponseEntity.noContent().build();
        } catch (ResourceNotFoundException e) {
            log.warn("{}. Returning HTTP 404", e.getMessage());
            return ResponseEntity.notFound().build();
        }
    }
}
