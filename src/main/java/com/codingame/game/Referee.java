package com.codingame.game;
import java.util.List;
import java.util.Set;

import com.codingame.gameengine.core.AbstractPlayer.TimeoutException;
import com.codingame.gameengine.core.AbstractReferee;
import com.codingame.gameengine.core.MultiplayerGameManager;
import com.codingame.gameengine.module.entities.GraphicEntityModule;
import com.google.inject.Inject;

public class Referee extends AbstractReferee {
    @Inject private MultiplayerGameManager<Player> gameManager;
    @Inject private GraphicEntityModule graphicEntityModule;

    @Override
    public void init() {
        long seed = gameManager.getSeed();
        // Initialize your game here.
    }

    @Override
    public void gameTurn(int turn) {
        for (Player player : gameManager.getActivePlayers()) {
            player.sendInputLine("input");
            player.execute();
        }

        for (Player player : gameManager.getActivePlayers()) {
            try {
                List<String> outputs = player.getOutputs();
                // Check validity of the player output and compute the new game state
            } catch (TimeoutException e) {
                player.deactivate(String.format("$%d timeout!", player.getIndex()));
            }
        }        
    }

    public Robot getRobot(int id) {
        // TODO implement here
        return null;
    }

    /**
     * @param id
     * @return
     */
    public Entity getEntity(int id) {
        // TODO implement here
        return null;
    }
    public Set<Robot> robots;

    /**
     *
     */
    public Set<Bullet> bullets;

    /**
     *
     */
    public Set<forcefield> forceFields;

    /**
     *
     */
    public Set<healthPack> healthPacks;
}
