package com.game.kalah.model;

import com.game.kalah.entity.Game;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

/**
 * @author shailesh trivedi
 */
@Getter
@Builder
public class PlayRequest {
    private Game game;
    private Integer pit;
    private Integer lastDropPit;
    @Setter private Player currentPlayer;
}