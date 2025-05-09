package com.lewyonq.so_cards_app.game;

import com.lewyonq.so_cards_app.exception.ResourceNotFoundException;
import com.lewyonq.so_cards_app.game.dto.GameRequestDto;
import com.lewyonq.so_cards_app.game.dto.GameResultDto;
import com.lewyonq.so_cards_app.game.dto.TestGameResponseDto;
import com.lewyonq.so_cards_app.game.dto.ViewGameResponseDto;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("api/v1/game")
public class GameController {
    private final GameService gameService;

    @PostMapping("/create/view")
    public ResponseEntity<?> createViewModeGame(@Valid @RequestBody GameRequestDto gameRequestDto) {
        try {
            ViewGameResponseDto viewGameResponseDto = gameService.createViewModeGame(gameRequestDto);
            URI location = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(viewGameResponseDto.getGameId())
                .toUri();
            return ResponseEntity.created(location).body(viewGameResponseDto);
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body("An unexpected error occurred.");
        }
    }

    @PostMapping("/create/test")
    public ResponseEntity<?> createTestModeGame(@Valid @RequestBody GameRequestDto gameRequestDto) {
        try {
            TestGameResponseDto testGameResponseDto = gameService.createTestModeGame(gameRequestDto);
            URI location = ServletUriComponentsBuilder
                    .fromCurrentRequest()
                    .path("/{id}")
                    .buildAndExpand(testGameResponseDto.getGameId())
                    .toUri();
            return ResponseEntity.created(location).body(testGameResponseDto);
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body("An unexpected error occurred.");
        }
    }

    @PostMapping("/{id}/submit/view")
    public ResponseEntity<Void> submitViewModeGame(@PathVariable Long id) {
        try {
            gameService.submitViewModeGame(id);
            return ResponseEntity.noContent().build();
        } catch (ResourceNotFoundException | IllegalStateException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping("/{id}/submit/test")
    public ResponseEntity<GameResultDto> submitTestModeGame(@PathVariable Long id, @RequestBody List<Integer> answerIndexes) {
        try {
            return ResponseEntity.ok(gameService.submitTestModeGame(id, answerIndexes));
        } catch (ResourceNotFoundException | IllegalStateException e) {
            return ResponseEntity.notFound().build();
        }
    }
}
