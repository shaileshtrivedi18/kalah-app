package com.game.kalah.executors.validators;

import com.game.kalah.entity.Game;
import com.game.kalah.model.PlayRequest;
import com.game.kalah.util.GameStatus;
import org.springframework.stereotype.Component;

import static com.game.kalah.model.Player.PLAYERA;
import static com.game.kalah.model.Player.PLAYERB;

/**
 * @author shailesh trivedi
 */
@Component
public class GameStatusValidator {

    /**
     * This method check if any of the player has all pits empty.
     * If so, then other player's pits are put in its house and
     * depending on the stones count of both the players, game status is updated
     * with the winning player
     * @param playRequest
     */
    public void validate(PlayRequest playRequest){
        Game game = playRequest.getGame();
        int playerASumPits = PLAYERA.pits()
                .map(pit -> game.getPit(pit))
                .sum();

        int playerBSumPits = PLAYERB.pits()
                .map(pit -> game.getPit(pit))
                .sum();

        if(playerASumPits == 0 || playerBSumPits == 0){
            GameStatus gameStatus;

            game.addStonesToPit(PLAYERA.getHouse(), playerASumPits);
            game.addStonesToPit(PLAYERB.getHouse(), playerBSumPits);

            game.resetPits();

            if(game.getPit(PLAYERA.getHouse()) > game.getPit(PLAYERB.getHouse())){
                gameStatus = GameStatus.PLAYER_A_WIN;
                game.setWinner(PLAYERA);
            }
            else if(game.getPit(PLAYERA.getHouse()) < game.getPit(PLAYERB.getHouse())){
                gameStatus = GameStatus.PLAYER_B_WIN;
                game.setWinner(PLAYERB);
            }
            else{
                gameStatus = GameStatus.DRAW;
            }

            game.setStatus(gameStatus);
            game.setPlayerTurn(null);
        }
    }
}