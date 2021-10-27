package view.entitiesSprites;

import com.codingame.game.gameElements.Bullet;
import com.codingame.game.gameElements.Point;
import com.codingame.gameengine.module.entities.Circle;
import com.codingame.gameengine.module.entities.Curve;
import com.codingame.gameengine.module.entities.Entity;
import com.codingame.gameengine.module.entities.Group;
import view.ViewManager;

import static com.codingame.game.Constants.BULLET_SIZE;
import static com.codingame.game.Constants.getPlayerColor;
import static com.codingame.gameengine.module.entities.Curve.EASE_IN;
import static com.codingame.gameengine.module.entities.Curve.LINEAR;


public class BulletSprite extends ViewPart {
    private final Bullet model;
    private final Group bulletGroup;
    private final ViewManager viewManager;
    private boolean active = true;
    private Entity<Circle> bulletSprite;
    private final int spriteSize;

    public BulletSprite(Bullet bullet, Group playerField, ViewManager viewManager) {
        this.viewManager = viewManager;
        spriteSize = (int) (BULLET_SIZE*viewManager.sizeRatio);
        model = bullet;
        bulletSprite = viewManager.graphicEntityModule.createCircle().
                setRadius(spriteSize).
                setFillColor(bullet.getOwner().getColorToken());
        bulletGroup = viewManager.graphicEntityModule.createGroup(bulletSprite).setZIndex(6);
        playerField.add(bulletGroup);
        bulletGroup.setX(coordToScreen(model.getX()), Curve.IMMEDIATE)
                .setY(coordToScreen(model.getY()), Curve.IMMEDIATE);
    }

    public BulletSprite(Bullet bullet, Group playerField, ViewManager viewManager, Point deviation) {
        this.viewManager = viewManager;
        spriteSize = (int) (BULLET_SIZE*viewManager.sizeRatio);
        model = bullet;
        bulletSprite = viewManager.graphicEntityModule.createCircle().
                setRadius(spriteSize).
                setFillColor(bullet.getOwner().getColorToken());
        bulletGroup = viewManager.graphicEntityModule.createGroup(bulletSprite).setZIndex(6);
        playerField.add(bulletGroup);
        bulletGroup
                .setX(coordToScreen(model.getX() + deviation.getX()), Curve.IMMEDIATE)
                .setY(coordToScreen(model.getY() + deviation.getY()), Curve.IMMEDIATE)
                .setAlpha(0.2);
    }


    @Override
    public void update() {
        bulletGroup.setX(coordToScreen(model.getX()), LINEAR)
                .setY(coordToScreen(model.getY()), LINEAR);
        if (bulletGroup.getAlpha() != 1.) {
            bulletGroup.setAlpha(1., Curve.EASE_OUT);
        }
    }

    @Override
    public boolean isActive() {
        if (!model.isActive() && active) {
            active = false;
            bulletSprite.setAlpha(0., EASE_IN);
            if (model.willHit()) {
                int playerColor = getPlayerColor(model.getOwner().getIndex());
                model.getTarget().getSprite().takeDamage(model.getDamage(), playerColor);
            }
            return true;
        }
        return active;
    }

    @Override
    public void onRemove() {

    }

    @Override
    public Group getSprite() {
        return bulletGroup;
    }

    @Override
    public boolean isShown() {
        return true;
    }

    private int coordToScreen(double pos) {
        return viewManager.coordToScreen(pos);
    }
}
