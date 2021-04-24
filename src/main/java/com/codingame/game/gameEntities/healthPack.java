package com.codingame.game.gameEntities;

import java.util.Set;

/**
 * 
 */
public class healthPack extends InGameEntity {

    /**
     * Default constructor
     */
    public healthPack() {
    }

    @Override
    public String giveInfo(int league, Robot asker, int distRank, Set<Robot> enemies) {
        return null;
    }

    @Override
    public String getSelfInfo(int league, Set<Robot> enemies, int playerId) {
        return null;
    }

}