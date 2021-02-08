package com.game.kalah.executors;

import com.game.kalah.entity.Game;
import com.game.kalah.executors.validators.GameRequestValidator;
import com.game.kalah.executors.validators.GameStatusValidator;
import com.game.kalah.model.PlayRequest;
import com.game.kalah.util.Constants;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import static com.game.kalah.model.Player.isHouse;

/**
 * This class is responding for actually making a move in the game.
 * After the request is validated, move action requested by the user is executed,
 * followed by a validation check on the game status
 *
 * @author shailesh trivedi
 */
@Component
@RequiredArgsConstructor
public class GameExecutor {

    private final GameRequestValidator requestValidator;
    private final GameStatusValidator gameStatusValidator;

    public void play(PlayRequest request){
        requestValidator.validate(request);
        request.setCurrentPlayer(Game.findPlayerByPit(request.getPit()));
        makeMove(request);
        gameStatusValidator.validate(request);
    }

    private void makeMove(PlayRequest playRequest){
        int lastDropPit = playRequest.getPit();
        int stones = collectStonesAndResetPit(playRequest.getGame(), playRequest.getPit());

        while(stones > 0){
            lastDropPit = nextPit(lastDropPit);

            if(isHouse(lastDropPit) && !playRequest.getCurrentPlayer().isMyHouse(lastDropPit)){
                continue;
            }

            playRequest.getGame().addStonesToPit(lastDropPit, 1);
            stones -= 1;
        }

        if(playRequest.getCurrentPlayer().isMyPit(lastDropPit) && playRequest.getGame().getPit(lastDropPit) == 1){
            int lastDropPitOppositePit = Constants.TOTAL_PITS - lastDropPit;
            int oppositeLastDropPitStones = collectStonesAndResetPit(playRequest.getGame(), lastDropPitOppositePit);

            playRequest.getGame().resetPit(lastDropPit);
            playRequest.getGame().addStonesToPit(playRequest.getCurrentPlayer().getHouse(), (oppositeLastDropPitStones + 1));
        }

        if(playRequest.getCurrentPlayer().isMyHouse(lastDropPit)){
            playRequest.getGame().setPlayerTurn(playRequest.getCurrentPlayer());
        } else {
            playRequest.getGame().setPlayerTurn(Game.getNextPlayer(playRequest.getCurrentPlayer()));
        }
    }

    private int collectStonesAndResetPit(Game game, int pit){
        int stones = game.getPit(pit);
        game.resetPit(pit);
        return stones;
    }

    private int nextPit(Integer currentPit){
        int nextPit = currentPit + 1;

        if(nextPit > Constants.TOTAL_PITS){
            nextPit = 1;
        }

        return nextPit;
    }
}