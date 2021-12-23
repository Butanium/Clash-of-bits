package com.codingame.game.gameEntities;

import java.util.ArrayList;

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
    public String giveInfo(int league, Robot asker, int distRank) {
        return null;
    }

    @Override
    public String getSelfInfo(int league, ArrayList<Robot> enemies, int playerId) {
        return null;
    }

}