package com.game.kalah.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import lombok.experimental.SuperBuilder;

import java.util.Map;

/**
 * @author shailesh trivedi
 */

@Getter
@SuperBuilder
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class GameDetailedResponse extends GameResponse {
    private Map<String, String> status;
}