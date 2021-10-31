package view.managers;

import com.codingame.game.gameElements.Bullet;
import com.codingame.game.gameElements.Point;
import com.codingame.gameengine.module.entities.Group;
import view.entitiesSprites.BulletSprite;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

public class BulletManager {
    private final Set<BulletSprite> availableBullets = new HashSet<>();
    private final Set<BulletSprite> usedBullets = new HashSet<>();
    private final ViewManager viewManager;

    public BulletManager(ViewManager viewManager) {
        this.viewManager = viewManager;
    }

    public void instantiateBullet(Bullet bullet, Point deviation, Group arena) {
        if (availableBullets.isEmpty()) {
            usedBullets.add(new BulletSprite(bullet,  viewManager, deviation));
        } else {
            BulletSprite b = availableBullets.stream().findAny().get();
            availableBullets.remove(b);
            b.reset(bullet, deviation);
            usedBullets.add(b);
        }
    }

    public void updateBullets() {
        Iterator<BulletSprite> it = usedBullets.iterator();
        while (it.hasNext()) {
            BulletSprite bullet = it.next();
            bullet.update();
            if (!bullet.isActive()) {
                it.remove();
                bullet.onRemove();
                availableBullets.add(bullet);
            }
        }
    }
}
