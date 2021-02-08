package com.game.kalah.rest.api;


import com.game.kalah.model.GameResponse;
import com.game.kalah.service.GameService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import lombok.RequiredArgsConstructor;
import org.hibernate.validator.constraints.Range;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.constraints.NotNull;

/**
 * This is controller class that handles all the games requests
 * @Author shailesh trivedi
 */
@RestController
@RequestMapping("/games")
@Validated
@RequiredArgsConstructor
public class GameController {

    private final GameService gameService;

    @Operation(summary = "creates a new game")
    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.CREATED)
    public GameResponse createNewGame(){
        return gameService.createNewGame();
    }

    @Operation(summary = "make a move in the game by the pitId")
    @PutMapping(value = "/{gameId}/pits/{pitId}", consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.OK)
    public GameResponse makeMove(@Parameter(description = "id of the game to be played")
                                     @PathVariable @NotNull final Integer gameId,
                         @Parameter(description = "id of the pit to be moved")
                         @PathVariable @NotNull @Range(min = 1, max= 14) final Integer pitId){
        return gameService.makeMove(gameId, pitId);
    }

    @GetMapping("/{gameId}")
    @ResponseStatus(HttpStatus.OK)
    public GameResponse gameInfo(@PathVariable @NotNull final Integer gameId){
        return gameService.gameInfo(gameId);
    }
}