package com.game.kalah.executors.validators;

import com.game.kalah.entity.Game;
import com.game.kalah.exception.InvalidMoveException;
import com.game.kalah.model.PlayRequest;
import com.game.kalah.util.GameStatus;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import static com.game.kalah.model.Player.isHouse;

/**
 * This class validates the play request before the actual game execution can start
 * @author shailesh trivedi
 */
@Component
public class GameRequestValidator {

    public void validate(PlayRequest request){
        if(!GameStatus.IN_PROGRESS.equals(request.getGame().getStatus())){
            throw new InvalidMoveException("No move possible : Game is already over");
        }

        if(isHouse(request.getPit())){
            throw new InvalidMoveException("Invalid move. pit " + request.getPit() + " is a house");
        }

        if(!StringUtils.isEmpty(request.getGame().getPlayerTurn()) && !request.getGame().getPlayerTurn().equals(Game.findPlayerByPit(request.getPit()))){
            throw new InvalidMoveException("Invalid move for pit " + request.getPit() +" : It's not your turn");
        }

        if(request.getGame().getPit(request.getPit()) == 0){
            throw new InvalidMoveException("Invalid move. pit " + request.getPit() + " has no stones left. Select any other pit.");
        }
    }
}