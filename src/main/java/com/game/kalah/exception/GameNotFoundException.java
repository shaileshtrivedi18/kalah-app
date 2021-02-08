package com.game.kalah.exception;

/**
 * @author shailesh trivedi
 */
public class GameNotFoundException extends RuntimeException {

    public GameNotFoundException(String message){
        super(message);
    }
}