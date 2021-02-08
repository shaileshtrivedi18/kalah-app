package com.game.kalah.model;

import java.util.Arrays;
import java.util.List;
import java.util.stream.IntStream;

import static com.game.kalah.util.Constants.PIT_START_INDEX;
import static com.game.kalah.util.Constants.PLAYER_A_HOUSE;
import static com.game.kalah.util.Constants.PLAYER_B_HOUSE;

/**
 * @author shailesh trivedi
 */
public enum Player {

    PLAYERA(PLAYER_A_HOUSE, PIT_START_INDEX, PLAYER_A_HOUSE - 1),
    PLAYERB(PLAYER_B_HOUSE, PLAYER_A_HOUSE + 1, PLAYER_B_HOUSE - 1);

    private final Integer house;
    private final Integer pitStart;
    private final Integer pitEnd;

    static List<Integer> allPlayerHouses = Arrays.asList(PLAYERA.getHouse(), PLAYERB.getHouse());

    Player(Integer house, Integer pitStart, Integer pitEnd){
        this.house = house;
        this.pitStart = pitStart;
        this.pitEnd = pitEnd;
    }

    public Integer getHouse(){
        return this.house;
    }

    public boolean isMyPit(Integer pit){
        return (pit >= pitStart) && (pit <= pitEnd);
    }

    public boolean isMyHouse(Integer house){
        return this.house == house;
    }

    public IntStream pits(){
        return IntStream.rangeClosed(this.pitStart, this.pitEnd);
    }

    public static boolean isHouse(Integer index){
        return allPlayerHouses.contains(index);
    }
}