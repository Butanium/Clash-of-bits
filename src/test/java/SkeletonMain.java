import com.codingame.gameengine.runner.MultiplayerGameRunner;

public class SkeletonMain {
    public static void main(String[] args) {
        /* Multiplayer Game */
        MultiplayerGameRunner gameRunner = new MultiplayerGameRunner();

        // Adds as many player as you need to test your game
        //gameRunner.addAgent(Agent1.class);

        gameRunner.addAgent(Agent3.class);
        gameRunner.addAgent(boss2.class);
        //gameRunner.addAgent("js C:/Users/Clement/Documents/coding/codinGame/game-skeleton/src/test/java/testMaxime.js");

        //gameRunner.addAgent(league2outsider.class);

      //  gameRunner.addAgent(outsider1.class);
        gameRunner.setLeagueLevel(2);
        gameRunner.setSeed(-2925940299915773277L);
        gameRunner.start();
        // Another way to add a player
        //gameRunner.addAgent("python3 C:/Users/Clement/Documents/coding/codinGame/game-skeleton/src/test/java/basicStub.py3");

    }
}
