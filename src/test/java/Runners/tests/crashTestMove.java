package Runners.tests;

import agents.moveDumb;
import com.codingame.gameengine.runner.MultiplayerGameRunner;

public class crashTestMove {
    public static void main(String[] args) {
        /* Multiplayer Game */
        MultiplayerGameRunner gameRunner = new MultiplayerGameRunner();
        gameRunner.addAgent(moveDumb.class);
        gameRunner.addAgent(moveDumb.class);
        gameRunner.setLeagueLevel(3);
        gameRunner.setSeed(3794553746263553451L);
        gameRunner.start();


    }
}

