package com.codingame.game.gameEntities;

import java.util.Set;

/**
 * 
 */
public class forcefield extends InGameEntity {

    /**
     * Default constructor
     */
    public forcefield() {
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