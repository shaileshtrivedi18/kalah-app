package com.game.kalah.mapper;

import com.game.kalah.entity.Game;
import com.game.kalah.model.GameDetailedResponse;
import com.game.kalah.model.GameResponse;
import com.game.kalah.rest.api.GameController;
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder;
import org.springframework.stereotype.Component;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * This mapper maps the game entity object to model response that will be returned to the consumer
 * @author shailesh trivedi
 */
@Component
public class GameMapper {

    public GameResponse map(Game game){
        return GameResponse.builder()
                .id(String.valueOf(game.getId()))
                .uri(gameLink(game)).build();
    }

    public GameResponse mapDetailedResponse(Game game){
        return GameDetailedResponse.builder()
                .id(String.valueOf(game.getId()))
                .uri(gameLink(game))
                .status(transformStatus(game))
                .build();
    }

    private String gameLink(Game game){
        return WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(GameController.class).gameInfo(game.getId())).toUri().toString();
    }

    private Map<String, String> transformStatus(Game game){
        return game.getBoard().entrySet().stream()
                .collect(Collectors.toMap(
                            entry -> entry.getKey().toString(),
                            entry -> entry.getValue().toString(),
                            (e1,e2) -> e1,
                            LinkedHashMap::new)
                );
    }
}