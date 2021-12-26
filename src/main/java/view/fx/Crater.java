package view.fx;

import com.codingame.game.gameElements.CircularHitBox;
import com.codingame.game.gameElements.Point;
import com.codingame.gameengine.module.entities.Sprite;

public class Crater extends CircularHitBox {
    private Sprite sprite;
    public Crater(Point position, double size) {
        super(position, size);
    }

    public void setSprite(Sprite sprite) {
        this.sprite = sprite;
    }

    public Sprite getSprite() {
        return sprite;
    }
}
