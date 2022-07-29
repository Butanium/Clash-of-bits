package view.entitiesSprites;

import com.codingame.game.gameElements.Bullet;
import com.codingame.game.gameElements.Point;
import com.codingame.gameengine.module.entities.*;
import view.managers.ViewManager;

import static com.codingame.game.Constants.*;
import static com.codingame.gameengine.module.entities.Curve.EASE_IN;
import static com.codingame.gameengine.module.entities.Curve.LINEAR;


public class BulletSprite extends ViewPart {
    private final ViewManager viewManager;
    private final Group bulletGroup;
    private final int spriteSize;
    private final Sprite bulletSprite;
    private final Circle debugCircle;
    private final SpriteAnimation trail;
    private Bullet model;
    private boolean active = true;

    public BulletSprite(Bullet bullet, ViewManager viewManager, Point deviation) {
        this.viewManager = viewManager;
        spriteSize = viewManager.sizeToScreen(BULLET_SIZE);
        model = bullet;
        int color = bullet.getOwner().getColorToken();
        bulletSprite = viewManager.graphicEntityModule.createSprite().setImage(BULLET_SPRITE)
                .setTint(color).setAnchor(0.5).setScale(BULLET_SCALE);
        trail = viewManager.graphicEntityModule.createSpriteAnimation().setLoop(true).
                setImages("b1.png", "b2.png", "b3.png").setDuration(BULLET_ANIMATION_DURATION)
                .setTint(color).setAnchor(0.5).setScale(BULLET_SCALE);
        debugCircle = viewManager.graphicEntityModule.createCircle()
                .setRadius(spriteSize)
                .setFillColor(color);
        viewManager.addDebug(debugCircle);
        viewManager.removeForDebug(bulletSprite);
        viewManager.removeForDebug(trail);
        bulletGroup = viewManager.graphicEntityModule.createGroup(trail, bulletSprite, debugCircle)
                .setX(coordToScreen(model.getX() + deviation.getX()), Curve.IMMEDIATE)
                .setY(coordToScreen(model.getY() + deviation.getY()), Curve.IMMEDIATE)
                .setZIndex(Z_INDEX_BULLETS).setRotation(model.getDirection().getRotation() + Math.PI / 2);
        viewManager.addToArena(bulletGroup);
        if (!model.willHit()) {
            viewManager.removeForDebug(bulletGroup);
        } else {
            viewManager.addDebug(bulletGroup);
        }
    }

    public void reset(Bullet bullet, Point deviation) {
        active = true;
        this.setVisible(true);
        this.model = bullet;
        int color = bullet.getOwner().getColorToken();
        bulletSprite.setTint(color);
        trail.setTint(color);
        debugCircle.setFillColor(color);
        bulletGroup.setX(coordToScreen(model.getX() + deviation.getX()), Curve.IMMEDIATE)
                .setY(coordToScreen(model.getY() + deviation.getY()), Curve.IMMEDIATE).setAlpha(1)
                .setRotation(model.getDirection().getRotation() + Math.PI / 2);

        ;
        //.setAlpha(0.2);
        updateVisibility();
        if (!model.willHit()) {
            viewManager.removeForDebug(bulletGroup);
        } else {
            viewManager.addDebug(bulletGroup);
        }
    }


    @Override
    public void update() {
        bulletGroup.setX(coordToScreen(model.getX()), LINEAR)
                .setY(coordToScreen(model.getY()), LINEAR);
        if (bulletGroup.getAlpha() != 1. && active) {
            bulletGroup.setAlpha(1., Curve.EASE_OUT);
        }
    }

    @Override
    public boolean isActive() {
        if (!model.isActive() && active) {
            active = false;
            bulletGroup.setAlpha(0., EASE_IN);
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
    public Group getSprite() {
        return bulletGroup;
    }

    @Override
    public Group getDebugSprite() {
        return getSprite();
    }


    private int coordToScreen(double pos) {
        return viewManager.coordToScreen(pos);
    }
}
