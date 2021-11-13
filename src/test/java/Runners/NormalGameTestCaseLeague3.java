package Runners;

import agents.Agent1;
import agents.boss1;
import com.codingame.gameengine.runner.MultiplayerGameRunner;

public class NormalGameTestCaseLeague3 {
    public static void main(String[] args) {
        /* Multiplayer Game */
        MultiplayerGameRunner gameRunner = new MultiplayerGameRunner();

        // Adds as many player as you need to test your game
        //gameRunner.addAgent(Agent1.class);
        gameRunner.addAgent(boss1.class);
        gameRunner.addAgent(Agent1.class);


        //gameRunner.addAgent("js C:/Users/Clement/Documents/coding/codinGame/game-skeleton/src/test/java/testMaxime.js");

        //gameRunner.addAgent(league2outsider.class);

      //  gameRunner.addAgent(outsider1.class);
        gameRunner.setLeagueLevel(3);
        //gameRunner.setSeed(6751687691868870743L);
        gameRunner.start();
        // Another way to add a player
        //gameRunner.addAgent("python3 C:/Users/Clement/Documents/coding/codinGame/game-skeleton/src/test/java/basicStub.py3");

    }
}
