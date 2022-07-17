package view.modules;

import com.codingame.gameengine.core.AbstractPlayer;
import com.codingame.gameengine.core.GameManager;
import com.codingame.gameengine.core.Module;
import com.codingame.gameengine.module.entities.Entity;
import com.google.inject.Inject;
import com.google.inject.Singleton;

import java.util.HashMap;
import java.util.Map;

/**
 * The FollowEntityModule allows you to make entities following other entities
 */
@Singleton
public class FollowEntityModule implements Module {

    GameManager<AbstractPlayer> gameManager;
    Map<Integer, Object[]> newRegistration;
//    Set<Entity<?>> trackedEntities; useful only if you want to allow unfollowing


    @Inject
    FollowEntityModule(GameManager<AbstractPlayer> gameManager) {
        this.gameManager = gameManager;
        gameManager.registerModule(this);
        newRegistration = new HashMap<>();
    }

    @Override
    public void onGameInit() {
        sendFrameData();
    }

    @Override
    public void onAfterGameTurn() {
        sendFrameData();
    }

    @Override
    public void onAfterOnEnd() {
    }


    private void sendFrameData() {
        if (!newRegistration.isEmpty()) {
            Object data = new HashMap[]{new HashMap<>(newRegistration)};
            newRegistration.clear();
            gameManager.setViewData("f", data);
        }
    }

    /**
     * Make <code>follower</code> follows <code>target</code> with an offset of <code>offsetx,offsety</code>
     *
     * @param follower the following entity
     * @param target   the entity to follow
     * @param offsetx  the x offset
     * @param offsety  the y offset
     */
    public void followEntity(Entity<?> follower, Entity<?> target, double offsetx, double offsety) {
        newRegistration.put(follower.getId(), new Object[]{target.getId(), offsetx, offsety});
    }

    public void followEntity(Entity<?> follower, Entity<?> target) {
        followEntity(follower, target, 0, 0);
    }

    public void followEntity(Entity<?>[] follower, Entity<?> target, double offsetx, double offsety) {
        for (Entity<?> f : follower) {
            followEntity(f, target, offsetx, offsety);
        }
    }

    public void followEntity(Entity<?>[] follower, Entity<?> target) {
        followEntity(follower, target, 0, 0);
    }


}