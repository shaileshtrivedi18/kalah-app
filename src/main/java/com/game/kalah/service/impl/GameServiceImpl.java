package com.game.kalah.service.impl;

import com.game.kalah.entity.Game;
import com.game.kalah.exception.GameNotFoundException;
import com.game.kalah.executors.GameExecutor;
import com.game.kalah.mapper.GameMapper;
import com.game.kalah.model.GameResponse;
import com.game.kalah.model.PlayRequest;
import com.game.kalah.repository.GameRepository;
import com.game.kalah.service.GameService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

/**
 * This is a service class for handling games queries like creating a game / making a move etc.
 */
@Service
@RequiredArgsConstructor
public class GameServiceImpl implements GameService {

    private final GameRepository gameRepository;
    private final GameExecutor gameExecutor;
    private final GameMapper gameMapper;

    @Override
    public GameResponse createNewGame() {
        com.game.kalah.entity.Game game = new com.game.kalah.entity.Game();
        game.initialize();
        saveGame(game);

        return gameMapper.map(game);
    }

    @Override
    public GameResponse makeMove(Integer gameId, Integer pit) {
        Game game = findGame(gameId);
        PlayRequest request = PlayRequest.builder().game(game).pit(pit).build();

        gameExecutor.play(request);
        saveGame(request.getGame());

        return gameMapper.mapDetailedResponse(request.getGame());
    }

    @Override
    public GameResponse gameInfo(Integer gameId) {
        return gameMapper.map(findGame(gameId));
    }

    private Game findGame(Integer gameId){
        return gameRepository.findById(gameId)
                .orElseThrow(() -> new GameNotFoundException("Game with Id: " + gameId + " not found.")
        );
    }

    private void saveGame(com.game.kalah.entity.Game game){
        gameRepository.save(game);
    }
}