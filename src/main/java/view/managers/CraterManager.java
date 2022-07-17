package view.managers;

import com.codingame.game.gameElements.Point;
import com.codingame.gameengine.module.entities.Curve;
import com.codingame.gameengine.module.entities.Sprite;
import view.fx.Crater;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.Optional;
import java.util.Random;

import static com.codingame.game.Constants.*;

public class CraterManager {
    private final Random random;
    private final ViewManager vM;
    private final ArrayList<Crater> craters = new ArrayList<>();

    public CraterManager(ViewManager viewManager, Random random) {
        this.vM = viewManager;
        this.random = random;
    }


    public void addCrater(Point position, double size) {
        double hitBoxSize = size * CRATER_HITBOX_SIZE;
        Crater newCrater = new Crater(position, hitBoxSize);

        Optional<Crater> closest = craters.stream().min(Comparator.comparingDouble(c -> c.getDist(newCrater)));
        String img = CRATER_IMAGE;
        if (closest.isPresent() && AVOID_CRATER_OVERLAP){
            Crater conflict = closest.get();
            double dist = conflict.getDist(newCrater);
            if (dist < conflict.getSize() + size * CRATER_SMALL_HITBOX_SIZE) {
                return;
            } else if (dist < conflict.getSize() + size * CRATER_HITBOX_SIZE) {
                img = SMALL_CRATER_IMAGE;
            }
        }
        int screenSize = (int) (vM.sizeToScreen(size) * CRATER_SIZE);
        int x = vM.coordToScreen(position.getX());
        int y = vM.coordToScreen(position.getY());
        Sprite sprite = vM.graphicEntityModule.createSprite().setImage(img).setBaseHeight(screenSize)
                .setBaseWidth(screenSize)
                .setX(x, Curve.IMMEDIATE).setY(y, Curve.IMMEDIATE).setZIndex(Z_INDEX_CRATER).setAnchor(.5)
                .setRotation(Math.PI * 2 * random.nextDouble())
                .setAlpha(CRATER_ALPHA);
        vM.addToArena(sprite);
        newCrater.setSprite(sprite);
        vM.removeForDebug(sprite);
        craters.add(newCrater);
    }

}
