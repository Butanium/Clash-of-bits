package view.managers;

import com.codingame.game.gameElements.Bullet;
import com.codingame.game.gameElements.Point;
import view.entitiesSprites.BulletSprite;

import java.util.*;

public class BulletManager {
    private final Map<Boolean,Set<BulletSprite>> availableBullets = new HashMap<>();
    private final Set<BulletSprite> usedBullets = new HashSet<>();
    private final ViewManager viewManager;

    public BulletManager(ViewManager viewManager) {
        this.viewManager = viewManager;
        availableBullets.put(true, new HashSet<>());
        availableBullets.put(false, new HashSet<>());
    }

    public void instantiateBullet(Bullet bullet, Point deviation) {
        Set<BulletSprite> availableBullets = this.availableBullets.get(bullet.willHit());
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
                availableBullets.get(bullet.isHit()).add(bullet);
            }
        }
    }
}
