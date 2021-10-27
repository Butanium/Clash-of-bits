package com.codingame.game;


import com.codingame.game.gameElements.Point;

import java.util.ArrayList;

public class Spawner {
    private final int teamCount;
    private final int botCount;
    private final long seed;

    public Spawner(long seed, int botCount, int teamCount) {
        this.teamCount = teamCount;
        this.botCount = botCount;
        this.seed = seed;
    }

    public ArrayList<Point>[] getSpawnsPosition() {
        ArrayList<Point>[] result = new ArrayList[teamCount];

        return result;
    }
}
