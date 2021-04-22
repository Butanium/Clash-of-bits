package com.codingame.game;
import com.codingame.game.entities.Robot;
import com.codingame.gameengine.core.AbstractMultiplayerPlayer;

import java.util.*;

public class Player extends AbstractMultiplayerPlayer {
    @Override
    public int getExpectedOutputLines() {
        // Returns the number of expected lines of outputs for a player
        return 1;
    }
    public void initialize(long seed){
        rng = new Random(seed);
    }

    /**
     *
     */
    public Referee referee;
    private Random rng;

    /**
     *
     */

    public Random getRNG() {
        return rng;
    }


}
