package com.game.kalah.exception.handler;

import com.game.kalah.exception.GameNotFoundException;
import com.game.kalah.exception.InvalidMoveException;
import com.game.kalah.model.ErrorResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * This class is responsible for handling the application errors and returning the error in a meaningful way to the consumer
 * @author shailesh trivedi
 */

@ControllerAdvice
@Slf4j
@ResponseBody
public class ErrorHandler {

    public static final String INVALID_REQUEST = "101";
    public static final String GAME_NOT_FOUND = "102";
    public static final String SYSTEM_ERROR = "103";

    @ExceptionHandler(GameNotFoundException.class)
    @ResponseStatus(value = HttpStatus.NOT_FOUND)
    public ErrorResponse handleGameNotFoundError(GameNotFoundException exception){
        return error(GAME_NOT_FOUND, exception);
    }

    @ExceptionHandler(InvalidMoveException.class)
    @ResponseStatus(value = HttpStatus.BAD_REQUEST)
    public ErrorResponse handleInvalidMoveError(InvalidMoveException exception){
        return error(INVALID_REQUEST, exception);
    }

    @ExceptionHandler(Exception.class)
    @ResponseStatus(value = HttpStatus.INTERNAL_SERVER_ERROR)
    public ErrorResponse handleGenericErrors(Exception exception){
        return error(SYSTEM_ERROR, exception);
    }

    private ErrorResponse error(String errorCode, Exception exception){
        log.error("Error occurred : code: " + errorCode + ", description: " + exception.getMessage(), exception);
        return ErrorResponse.builder().code(errorCode).description(exception.getMessage()).build();
    }
}