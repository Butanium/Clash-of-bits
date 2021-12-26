package view.entitiesSprites;

import com.codingame.game.gameElements.Bullet;
import com.codingame.game.gameElements.Point;
import com.codingame.gameengine.module.entities.Circle;
import com.codingame.gameengine.module.entities.Curve;
import view.managers.ViewManager;

import static com.codingame.game.Constants.*;
import static com.codingame.gameengine.module.entities.Curve.EASE_IN;
import static com.codingame.gameengine.module.entities.Curve.LINEAR;


public class BulletSprite extends ViewPart {
    private final ViewManager viewManager;
    private final Circle bulletSprite;
    private final int spriteSize;
    private Bullet model;
    private boolean active = true;

    public BulletSprite(Bullet bullet, ViewManager viewManager, Point deviation) {
        this.viewManager = viewManager;
        spriteSize = viewManager.sizeToScreen(BULLET_SIZE);
        model = bullet;
        bulletSprite = viewManager.graphicEntityModule.createCircle()
                .setRadius(spriteSize)
                .setFillColor(bullet.getOwner().getColorToken())
                .setX(coordToScreen(model.getX() + deviation.getX()), Curve.IMMEDIATE)
                .setY(coordToScreen(model.getY() + deviation.getY()), Curve.IMMEDIATE)
               // .setAlpha(0.2)
                .setZIndex(Z_INDEX_BULLETS);
        viewManager.addToArena(bulletSprite);
    }

    public void reset(Bullet bullet, Point deviation) {
        active = true;
        this.setVisible(true);
        this.model = bullet;
        bulletSprite.setFillColor(bullet.getOwner().getColorToken())
                .setX(coordToScreen(model.getX() + deviation.getX()), Curve.IMMEDIATE)
                .setY(coordToScreen(model.getY() + deviation.getY()), Curve.IMMEDIATE)
                .setAlpha(1.)
                ;
    //.setAlpha(0.2);
        updateVisibility();
    }


    @Override
    public void update() {
        bulletSprite.setX(coordToScreen(model.getX()), LINEAR)
                .setY(coordToScreen(model.getY()), LINEAR);
        if (bulletSprite.getAlpha() != 1. && active) {
            bulletSprite.setAlpha(1., Curve.EASE_OUT);
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
        model = null;
        this.setVisible(false);
        updateVisibility();
    }

    @Override
    public Circle getSprite() {
        return bulletSprite;
    }


    private int coordToScreen(double pos) {
        return viewManager.coordToScreen(pos);
    }
}
