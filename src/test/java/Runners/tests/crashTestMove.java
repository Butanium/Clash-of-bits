package Runners.tests;

import agents.moveDumb;
import com.codingame.gameengine.runner.MultiplayerGameRunner;

public class crashTestMove {
    public static void main(String[] args) {
        /* Multiplayer Game */
        MultiplayerGameRunner gameRunner = new MultiplayerGameRunner();

        // Adds as many player as you need to test your game
        //gameRunner.addAgent(Agent1.class);
        gameRunner.addAgent(moveDumb.class);
        gameRunner.addAgent(moveDumb.class);


        //gameRunner.addAgent("js C:/Users/Clement/Documents/coding/codinGame/game-skeleton/src/test/java/testMaxime.js");

        //gameRunner.addAgent(league2outsider.class);

      //  gameRunner.addAgent(outsider1.class);
        gameRunner.setLeagueLevel(3);
        gameRunner.setSeed(3794553746263553451L);
        gameRunner.start();
        // Another way to add a player
        //gameRunner.addAgent("python3 C:/Users/Clement/Documents/coding/codinGame/game-skeleton/src/test/java/basicStub.py3");

    }
}
