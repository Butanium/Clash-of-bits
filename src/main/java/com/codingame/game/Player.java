package com.codingame.game;
import com.codingame.gameengine.core.AbstractMultiplayerPlayer;

import java.util.*;

public class Player extends AbstractMultiplayerPlayer {
    @Override
    public int getExpectedOutputLines() {
        // Returns the number of expected lines of outputs for a player

        // TODO: Replace the returned value with a valid number. Most of the time the value is 1. 
        return 1;
    }
    public Player(long seed){
        rng = new Random(seed);
    }

    /**
     *
     */
    public Referee referee;
    private final Random rng;
    /**
     *
     */
    public void getId() {
        // TODO implement here
    }

    public Random getRNG() {
        return rng;
    }


}
