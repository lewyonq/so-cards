package com.lewyonq.so_cards_app.game;

import com.lewyonq.so_cards_app.exception.ResourceNotFoundException;
import com.lewyonq.so_cards_app.game.dto.GameRequestDto;
import com.lewyonq.so_cards_app.game.dto.GameResponseDto;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;

@RestController
@RequiredArgsConstructor
@RequestMapping("api/v1/game")
public class GameController {
    private final GameService gameService;

    @PostMapping
    public ResponseEntity<?> createGame(@Valid @RequestBody GameRequestDto gameRequestDto) {
        try {
            GameResponseDto gameResponseDto;
            switch (gameRequestDto.getGameType()) {
                case VIEW -> gameResponseDto = gameService.createViewModeGame(gameRequestDto);
                case TEST -> gameResponseDto = gameService.createTestModeGame(gameRequestDto);
                case ANSWER -> gameResponseDto = gameService.createAnswerModeGame(gameRequestDto);
                default -> {
                    return ResponseEntity.badRequest().body("Unsupported game type: " + gameRequestDto.getGameType());
                }
            }

            URI location = ServletUriComponentsBuilder
                    .fromCurrentRequest()
                    .path("/{id}")
                    .buildAndExpand(gameResponseDto.getGameId())
                    .toUri();

            return ResponseEntity.created(location).body(gameResponseDto);
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body("An unexpected error occurred.");
        }
    }

    @PutMapping("/{id}/finish")
    public ResponseEntity<Void> finishGame(@PathVariable Long id) {
        gameService.makeGameAsFinished(id);
        return ResponseEntity.noContent().build();
    }
}
