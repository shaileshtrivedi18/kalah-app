package com.game.kalah.service;

import com.game.kalah.model.GameResponse;

public interface GameService {

    GameResponse createNewGame();

    GameResponse makeMove(Integer gameId, Integer pitId);

    GameResponse gameInfo(Integer gameId);

}