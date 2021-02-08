package com.game.kalah.model;

import lombok.Builder;
import lombok.Getter;

/**
 * @author shailesh trivedi
 */
@Getter
@Builder
public class ErrorResponse {
    private final String code;
    private final String description;
}