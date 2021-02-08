package com.game.kalah.service;

import com.game.kalah.exception.GameNotFoundException;
import com.game.kalah.exception.InvalidMoveException;
import com.game.kalah.model.GameDetailedResponse;
import com.game.kalah.model.GameResponse;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

/**
 * @author shailesh trivedi
 */

@ExtendWith(SpringExtension.class)
@SpringBootTest
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class GameServiceTest {

    @Autowired
    private GameService gameService;

    @Test
    @Order(1)
    public void testCreateNewGame_Success(){
        GameResponse gameResponse = gameService.createNewGame();
        assertNotNull(gameResponse.getId());
        assertEquals("1", gameResponse.getId());
        assertEquals("http://localhost/games/1", gameResponse.getUri());
    }

    @Test
    public void testMakeMove_GameNotFound(){
        assertThrows(GameNotFoundException.class, () -> gameService.makeMove(100, 1));
    }

    @Test
    @Order(2)
    public void testMakeMove_InvalidMove_PitIsAHouse(){
        Exception exception = assertThrows(InvalidMoveException.class, () -> gameService.makeMove(1, 7));
        assertEquals("Invalid move. pit " + 7 + " is a house", exception.getMessage());
    }

    @Test
    @Order(3)
    public void testMakeMove_Valid_PitOneEmpty(){
        Integer pit = 1;

        GameDetailedResponse response =  (GameDetailedResponse) gameService.makeMove(1, pit);

        assertEquals("http://localhost/games/1", response.getUri());
        assertEquals("1", response.getId());
        assertEquals("0", response.getStatus().get(pit.toString()));
        assertEquals("7", response.getStatus().get(String.valueOf(5)));
        assertEquals("1", response.getStatus().get(String.valueOf(7)));
    }

    @Test
    @Order(4)
    public void testMakeMove_Invalid_SamePlayerShouldPlay_LastDropPitWasHouse(){
        Integer pit = 9;
        Exception exception = assertThrows(InvalidMoveException.class, () -> gameService.makeMove(1, pit));
        assertEquals("Invalid move for pit " + pit +" : It's not your turn", exception.getMessage());
    }

    @Test
    @Order(5)
    public void testMakeMove_Valid_PlayerOneAgain(){
        Integer pit = 4;

        GameDetailedResponse response =  (GameDetailedResponse) gameService.makeMove(1, pit);

        assertEquals("http://localhost/games/1", response.getUri());
        assertEquals("1", response.getId());
        assertEquals("0", response.getStatus().get(pit.toString()));
        assertEquals("8", response.getStatus().get(String.valueOf(5)));
        assertEquals("2", response.getStatus().get(String.valueOf(7)));
    }

    @Test
    @Order(6)
    public void testMakeMove_Valid_NextPlayerTurn(){
        Integer pit = 13;

        GameDetailedResponse response =  (GameDetailedResponse) gameService.makeMove(1, pit);

        assertEquals("http://localhost/games/1", response.getUri());
        assertEquals("1", response.getId());
        assertEquals("0", response.getStatus().get(pit.toString()));
        assertEquals("9", response.getStatus().get(String.valueOf(5)));
        assertEquals("2", response.getStatus().get(String.valueOf(7)));
        assertEquals("1", response.getStatus().get(String.valueOf(14)));
    }
}