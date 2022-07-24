package view.entitiesSprites;

import com.codingame.gameengine.module.entities.Entity;

public abstract class ViewPart {
    private boolean visible = true;
    public abstract void update();

    public abstract boolean isActive();

    public abstract void onRemove();

    public abstract Entity getSprite();

    public abstract Entity getDebugSprite();

    public boolean isVisible() {
        return visible;
    }

    public boolean isRobot(){
        return false;
    }

    public boolean isBullet(){
        return false;
    }

    public void setVisible(boolean visible) {
        this.visible = visible;
    }

    public void updateVisibility() {
        if (getSprite().isVisible() != this.isVisible()) {
            getSprite().setVisible(this.isVisible());
        }
    }
}