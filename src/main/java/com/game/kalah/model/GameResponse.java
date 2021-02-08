package com.game.kalah.model;

import lombok.Getter;
import lombok.experimental.SuperBuilder;

@SuperBuilder
@Getter
public class GameResponse {

    private String id;
    private String uri;
}