package com.game.kalah.exception;

/**
 * @author shailesh trivedi
 */
public class InvalidMoveException extends RuntimeException{

    public InvalidMoveException(String message){
        super(message);
    }
}