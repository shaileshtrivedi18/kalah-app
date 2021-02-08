package com.game.kalah.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.game.kalah.model.Player;
import com.game.kalah.util.GameStatus;
import lombok.Data;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.IntStream;
import java.util.stream.Stream;

import static com.game.kalah.model.Player.isHouse;
import static com.game.kalah.util.Constants.PIT_END_INDEX;
import static com.game.kalah.util.Constants.PIT_START_INDEX;
import static com.game.kalah.util.Constants.STONES_PER_PIT;

/**
 * @author shailesh trivedi
 */
@Entity
@Table(name = "games")
@Data
public class Game {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;

    @Enumerated private GameStatus status;
    @Enumerated private Player playerTurn;
    private Player winner;

    @ElementCollection(fetch = FetchType.EAGER)
    @MapKeyColumn(name = "pit")
    @Column(name = "stones")
    @CollectionTable(name="board", joinColumns = @JoinColumn(name = "id"))
    private Map<Integer, Integer> board;

    @UpdateTimestamp private LocalDateTime updatedTimestamp;

    @JsonIgnore
    public void initialize(){
        this.status = GameStatus.IN_PROGRESS;
        this.board = new HashMap<>();
        IntStream.rangeClosed(PIT_START_INDEX, PIT_END_INDEX).forEach(
                index -> {
                    if(isHouse(index)){
                        this.board.put(index, 0);
                    }
                    else{
                        this.board.put(index, STONES_PER_PIT);
                    }
                }
        );
    }

    @JsonIgnore
    public static Player getNextPlayer(Player player){
        return Stream.of(Player.values())
                .filter(nextPlayer -> !nextPlayer.equals(player))
                .findFirst()
                .get();
    }

    @JsonIgnore
    public Integer getPit(Integer pit){
        return this.board.get(pit);
    }

    @JsonIgnore
    public void addStonesToPit(int pit, int addToPit){
        this.board.put(pit, this.board.get(pit) + addToPit);
    }

    @JsonIgnore
    public void resetPits(){
        this.board.entrySet().stream()
                .filter(pit -> !isHouse(pit.getKey()))
                .forEach(pit -> pit.setValue(0));
    }

    @JsonIgnore
    public void resetPit(int pit){
        this.board.put(pit, 0);
    }

    @JsonIgnore
    public static Player findPlayerByPit(Integer playPit){
        return Stream.of(Player.values()).filter(
                player -> player.isMyPit(playPit)
        ).findFirst().get();
    }
}