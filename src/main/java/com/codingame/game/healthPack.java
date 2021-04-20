package com.codingame.game;

import java.util.Set;

/**
 * 
 */
public class healthPack extends Entity {

    /**
     * Default constructor
     */
    public healthPack() {
    }

    @Override
    String giveInfo(int league, Robot asker, int distRank, Set<Robot> enemies) {
        return null;
    }

    @Override
    String getSelfInfo(int league, Set<Robot> enemies, int playerId) {
        return null;
    }

}