package com.lewyonq.so_cards_app.game.active_game;

import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ActiveGame {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long gameId;

    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "active_game_correct_answers", joinColumns = @JoinColumn(name = "active_game_id"))
    private List<Integer> correctAnswerIndexes = new ArrayList<>();
}
