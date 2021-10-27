package view.entitiesSprites;

import com.codingame.gameengine.module.entities.Group;

public abstract class ViewPart {
    public abstract void update();

    public abstract boolean isActive();

    public abstract void onRemove();

    public abstract Group getSprite();

    public abstract boolean isShown();
}