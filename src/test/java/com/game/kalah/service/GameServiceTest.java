package com.game.kalah.service;

import com.game.kalah.entity.Game;
import com.game.kalah.exception.GameNotFoundException;
import com.game.kalah.exception.InvalidMoveException;
import com.game.kalah.executors.GameExecutor;
import com.game.kalah.executors.validators.GameRequestValidator;
import com.game.kalah.executors.validators.GameStatusValidator;
import com.game.kalah.mapper.GameMapper;
import com.game.kalah.model.GameDetailedResponse;
import com.game.kalah.model.GameResponse;
import com.game.kalah.repository.GameRepository;
import com.game.kalah.service.impl.GameServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.when;
/**
 * @author shailesh trivedi
 */
@ExtendWith(MockitoExtension.class)
public class GameServiceTest {

    private GameMapper gameMapper = new GameMapper();
    private GameExecutor gameExecutor = gameExecutor();
    @Mock private GameRepository gameRepository;
    private GameService gameService;

    private static final Integer gameID = 101;
    private static final Integer invalidGameID = 500;
    private static final String gameURL = "/games/" + gameID;

    @BeforeEach
    public void init(){
        gameService = new GameServiceImpl(gameRepository, gameExecutor, gameMapper);
    }

    @Test
    public void testCreateNewGame(){
        when(gameRepository.save(any(Game.class)))
                .thenReturn(gameEntity().get());

        GameResponse gameResponse = gameService.createNewGame();

        assertEquals(gameID.toString(), gameResponse.getId());
    }

    @Test
    public void testMakeMove_WhenGameNotExists_ThenGameNotFoundError(){
        when(gameRepository.findById(invalidGameID))
                .thenReturn(Optional.empty());
        assertThrows(GameNotFoundException.class, () -> gameService.makeMove(invalidGameID, 2));
    }

    @Test
    public void testMakeMove_WhenGivenHousePitToMove_ThenInvalidMoveError(){
        when(gameRepository.findById(gameID))
                .thenReturn(gameEntity());
        assertThrows(InvalidMoveException.class, () -> gameService.makeMove(gameID, 7));
    }

    @Test
    public void testMakeMove_WhenGivenFirstPitToMove_ThenMove(){
        when(gameRepository.findById(gameID))
                .thenReturn(gameEntity());
        when(gameRepository.save(any(Game.class)))
                .thenAnswer(invocationOnMock -> invocationOnMock.getArguments()[0]);

        GameDetailedResponse gameResponse = (GameDetailedResponse) gameService.makeMove(gameID, 3);
        assertEquals(gameID.toString(), gameResponse.getId());
        assertEquals(gameURL, gameResponse.getUri());
        assertEquals("0", gameResponse.getStatus().get("3"));
        assertEquals("7", gameResponse.getStatus().get("4"));
        assertEquals("7", gameResponse.getStatus().get("5"));
        assertEquals("1", gameResponse.getStatus().get("7"));
    }

    @Test
    public void testMakeMove_WhenGivenNinthPitToMove_ThenMove(){
        when(gameRepository.findById(gameID))
                .thenReturn(gameEntity());
        when(gameRepository.save(any(Game.class)))
                .thenAnswer(invocationOnMock -> invocationOnMock.getArguments()[0]);

        GameDetailedResponse gameResponse = (GameDetailedResponse) gameService.makeMove(gameID, 9);
        assertEquals(gameID.toString(), gameResponse.getId());
        assertEquals(gameURL, gameResponse.getUri());
        assertEquals("0", gameResponse.getStatus().get("9"));
        assertEquals("7", gameResponse.getStatus().get("10"));
        assertEquals("7", gameResponse.getStatus().get("11"));
        assertEquals("1", gameResponse.getStatus().get("14"));
    }

    @Test
    public void testMakeMove_WhenLastStoneInOwnEmptyPit_ThenTakeOppPitStonesToHome(){
        when(gameRepository.findById(gameID))
                .thenReturn(gameEntity(boardForMoveToSelfEmptyPit));
        when(gameRepository.save(any(Game.class)))
                .thenAnswer(invocationOnMock -> invocationOnMock.getArguments()[0]);

        GameDetailedResponse gameResponse = (GameDetailedResponse) gameService.makeMove(gameID, 1);
        assertEquals(gameID.toString(), gameResponse.getId());
        assertEquals(gameURL, gameResponse.getUri());
        assertEquals("0", gameResponse.getStatus().get("1"));
        assertEquals("5", gameResponse.getStatus().get("2"));
        assertEquals("7", gameResponse.getStatus().get("3"));
        assertEquals("0", gameResponse.getStatus().get("4"));//self pit stones moved to house
        assertEquals("0", gameResponse.getStatus().get("10"));//opposite pit stones also moved to house
        assertEquals("4", gameResponse.getStatus().get("7"));// 4th & 10th pit stones added to house
    }

    @Test
    public void testMakeMove_WhenAllPitsEmptyAfterMove_ThenGameFinished(){
        when(gameRepository.findById(gameID))
                .thenReturn(gameEntity(boardForGameFinishedMove));
        when(gameRepository.save(any(Game.class)))
                .thenAnswer(invocationOnMock -> invocationOnMock.getArguments()[0]);

        GameDetailedResponse gameResponse = (GameDetailedResponse) gameService.makeMove(gameID, 6);

        assertEquals(gameID.toString(), gameResponse.getId());
        assertEquals(gameURL, gameResponse.getUri());

        assertEquals("0", gameResponse.getStatus().get("1"));
        assertEquals("0", gameResponse.getStatus().get("6"));
        assertEquals("30", gameResponse.getStatus().get("7"));
        assertEquals("0", gameResponse.getStatus().get("10"));
        assertEquals("0", gameResponse.getStatus().get("11"));//pit emptied
        assertEquals("0", gameResponse.getStatus().get("12"));//pit emptied
        assertEquals("42", gameResponse.getStatus().get("14"));
    }

    Map<Integer, Integer> boardForMoveToSelfEmptyPit = new HashMap() {{
            put(1, 3);
            put(2, 4);
            put(3, 6);
            put(4, 0);
            put(5, 8);
            put(6, 8);
            put(7, 0);
            put(8, 2);
            put(9, 4);
            put(10, 3);
            put(11, 5);
            put(12, 8);
            put(13, 3);
            put(14, 0);
        }};

    Map<Integer, Integer> boardForGameFinishedMove = new HashMap() {{
        put(1, 0);
        put(2, 0);
        put(3, 0);
        put(4, 0);
        put(5, 0);
        put(6, 1);
        put(7, 29);
        put(8, 10);
        put(9, 0);
        put(10, 5);
        put(11, 5);
        put(12, 10);
        put(13, 0);
        put(14, 12);
    }};

    private Optional<Game> gameEntity(Map<Integer, Integer> board){
        Game game = new Game();
        game.initialize();
        game.setId(Integer.valueOf(gameID));
        if(board != null){
            game.setBoard(board);
        }
        return Optional.of(game);
    }

    private Optional<Game> gameEntity(){
        return gameEntity(null);
    }

    GameExecutor gameExecutor(){
        return new GameExecutor(new GameRequestValidator(), new GameStatusValidator());
    }
}