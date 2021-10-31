package view.managers;
/* Zindex :
    - 0 : map
    - 1 : robot base
    - 2 : robot surface
    - 3 : robot move
    - 4 : robot canon
    - 5 : robot hit marker
    - 6 : bullets
    - 7 : UI 0
    - 8 : UI 1


*/

import com.codingame.game.gameElements.Bullet;
import com.codingame.game.gameElements.Point;
import com.codingame.game.gameEntities.Robot;
import com.codingame.gameengine.module.entities.Curve;
import com.codingame.gameengine.module.entities.Entity;
import com.codingame.gameengine.module.entities.GraphicEntityModule;
import com.codingame.gameengine.module.entities.Group;
import view.entitiesSprites.RobotSprite;
import view.entitiesSprites.ViewPart;
import view.modules.CameraModule;
import view.modules.TooltipModule;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import static com.codingame.game.Constants.*;

public class ViewManager {
    public final TooltipModule tooltips;
    public final GraphicEntityModule graphicEntityModule;
    public final CameraModule camera;
    private final int X0;
    private final int Y0;
    private final Set<ViewPart> priorityViewParts = new HashSet<>();
    private final Set<ViewPart> viewParts = new HashSet<>();
    private final Group gameGroup;
    private final Point arenaSize;
    private final BulletManager bulletManager;
    private final AnimationManager animManager;

    private boolean newObjectToCommit = false;
    public double sizeRatio;
    private Group arena;


    public ViewManager(GraphicEntityModule graphicEntityModule, TooltipModule tooltipModule, CameraModule cameraModule) {
        this.graphicEntityModule = graphicEntityModule;
        double xRatio = 1920 / WALL_SIZE.getX();
        double yRatio = 1080 / WALL_SIZE.getY();
        if (xRatio > yRatio) {
            sizeRatio = yRatio;
            X0 = (int) ((1920 - WALL_SIZE.getX() * sizeRatio) * .5);
            Y0 = 0;
        } else {
            sizeRatio = xRatio;
            X0 = 0;
            Y0 = (int) ((1080 - WALL_SIZE.getY() * sizeRatio) * .5);
        }
        arenaSize = new Point((int) (sizeRatio * WALL_SIZE.getX()),
                (int) (sizeRatio * WALL_SIZE.getY()));
        gameGroup = graphicEntityModule.createGroup();
        tooltips = tooltipModule;
        camera = cameraModule;
        bulletManager = new BulletManager(this);
        animManager = new AnimationManager(this);
    }

    private Group createArena() {
        Group arena = graphicEntityModule.createGroup()
                .setX(X0)
                .setY(Y0);

        arena.add(graphicEntityModule.createRectangle()
                .setHeight((int) arenaSize.getX(), Curve.IMMEDIATE)
                .setWidth((int) arenaSize.getY(), Curve.IMMEDIATE)
                .setX(0).setY(0).setLineWidth(0.5 * sizeRatio)
                .setLineColor(WALL_COLOR).setFillAlpha(1).setFillColor(BACKGROUND_COLOR));
        return arena;
    }

    public void init(Set<Robot> robots) {
        arena = createArena();
        gameGroup.add(arena);
        //camera.setContainer(autoCameraArena, (int) (0.5 + fieldSize.getX()), (int) (0.5 + fieldSize.getY()));
        camera.setContainer(arena, 1920, 1080);
        for (Robot robot : robots) {
            ViewPart robotSprite = new RobotSprite(robot, arena, this);
            viewParts.add(robotSprite);
            camera.addTrackedEntity(robotSprite.getSprite());

        }
        camera.setCameraOffset(CAMERA_OFFSET * sizeRatio);
    }

    public void instantiateBullet(Bullet bullet, Point deviation) {
        commitEntity();
        bulletManager.instantiateBullet(bullet, deviation, arena);
    }

    private void applyCommits() {
        if (newObjectToCommit) {
            graphicEntityModule.commitWorldState(0.);
            newObjectToCommit = false;
        }
    }


    private void updateViewParts(Set<ViewPart> viewParts) {
        Iterator<ViewPart> it = viewParts.iterator();
        while (it.hasNext()) {
            ViewPart viewPart = it.next();
            if (!viewPart.isActive()) {
                it.remove();
                viewPart.onRemove();
                viewPart.getSprite().setVisible(false);
            }
        }
        // useless for now may be useful if remove animation needs a world commit at t=0
        applyCommits();
        for (ViewPart viewPart : viewParts) {
            viewPart.updateVisibility();
            viewPart.update();
        }

    }

    public void update() {
        applyCommits();
        bulletManager.updateBullets();
        updateViewParts(viewParts);
        animManager.updateAnimations();

    }

    public int coordToScreen(double pos) {
        double padding = PADDING * sizeRatio;
        return (int) (pos / MAP_SIZE.max() * (WALL_SIZE.max() * sizeRatio - 2 * padding) + padding);
    }
    
    public void addToArena (Entity entity) {
        arena.add(entity);
    }

    public void commitEntity () {
        newObjectToCommit = true;
    }

    public AnimationManager getAnimManager() {
        return animManager;
    }

}
