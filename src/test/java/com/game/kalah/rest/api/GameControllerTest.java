package com.game.kalah.rest.api;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.game.kalah.exception.GameNotFoundException;
import com.game.kalah.exception.InvalidMoveException;
import com.game.kalah.model.GameDetailedResponse;
import com.game.kalah.model.GameResponse;
import com.game.kalah.service.GameService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

import java.util.HashMap;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
/**
 * @author shailesh trivedi
 */

@ExtendWith(SpringExtension.class)
@WebMvcTest(controllers = GameController.class)
public class GameControllerTest {
    private static final String URL = "http://localhost:8080/games/1";
    private static final String gameId = "1";
    private static final int pitId = 3;

    @Autowired private MockMvc mockMvc;
    @MockBean private GameService gameService;
    @Autowired private ObjectMapper objectMapper;

    @Test
    public void testCreateNewGame() throws Exception{
        GameResponse gameResponse = GameResponse.builder().id(gameId).uri(URL).build();
        given(gameService.createNewGame()).willReturn(gameResponse);

        mockMvc.perform(post("/games")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.uri").value(gameResponse.getUri()));
    }

    @Test
    public void testMakeMove() throws Exception{
        GameDetailedResponse gameResponse = moveResponse();
        given(gameService.makeMove(Integer.valueOf(gameId), pitId)).willReturn(gameResponse);

        mockMvc.perform(put("/games/" + Integer.valueOf(gameId) + "/pits/"+ pitId )
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value("1"))
                .andExpect(jsonPath("$.uri").value(gameResponse.getUri()))
                .andExpect(jsonPath("$.status['1']").value(6));

        ArgumentCaptor<Integer> gameIdCaptor = ArgumentCaptor.forClass(Integer.class);
        ArgumentCaptor<Integer> pitIdCaptor = ArgumentCaptor.forClass(Integer.class);
        verify(gameService, times(1)).makeMove(gameIdCaptor.capture(), pitIdCaptor.capture());
        assertThat(gameIdCaptor.getValue()).isEqualTo(Integer.valueOf(gameId));
        assertThat(pitIdCaptor.getValue()).isEqualTo(pitId);
    }

    @Test
    public void testMakeMove_GameNotExists() throws Exception {
        given(gameService.makeMove(Integer.valueOf(gameId), pitId)).willThrow(new GameNotFoundException("Game with Id: " + gameId + " not found."));

        mockMvc.perform(put("/games/" + gameId + "/pits/"+ pitId )
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.code").value(102))
                .andExpect(jsonPath("$.description").value("Game with Id: " + gameId + " not found."));
    }

    @Test
    public void testMakeMove_NotYourTurnError() throws Exception {
        given(gameService.makeMove(Integer.valueOf(gameId), pitId)).willThrow(new InvalidMoveException("Invalid move for pit " + pitId +" : It's not your turn"));

        mockMvc.perform(put("/games/" + gameId + "/pits/"+ pitId )
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.code").value(101))
                .andExpect(jsonPath("$.description").value("Invalid move for pit " + pitId +" : It's not your turn"));
    }

    @Test
    public void testMakeMove_NotPossible_GameIsAlreadyOver() throws Exception{
        given(gameService.makeMove(Integer.valueOf(gameId), pitId)).willThrow(new InvalidMoveException("No move possible : Game is already over"));

        mockMvc.perform(put("/games/" + gameId + "/pits/"+ pitId )
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.code").value(101))
                .andExpect(jsonPath("$.description").value("No move possible : Game is already over"));
    }

    private GameDetailedResponse moveResponse() throws JsonProcessingException {
        Map status = objectMapper.readValue("{\"1\":\"6\",\"2\":\"6\",\"3\":\"6\",\"4\":\"6\",\"5\":\"6\",\"6\":\"6\",\"7\":\"0\",\"8\":\"6\",\"9\":\"6\",\"10\":\"6\",\"11\":\"6\",\"12\":\"6\",\"13\":\"6\",\"14\":\"0\"}", HashMap.class);
        return GameDetailedResponse.builder().id("1")
                .uri(URL)
                .status(status)
                .build();
    }
}