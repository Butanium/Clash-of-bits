package view.managers;
/* Zindex :
    - -3 : background
    - -2 : floor
    - -1 : wall 0
    - 0 : walls
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
import com.codingame.gameengine.module.entities.*;
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

    private final double sizeRatio;
    private boolean newObjectToCommit = false;
    private Group arena;


    public ViewManager(GraphicEntityModule graphicEntityModule, TooltipModule tooltipModule, CameraModule cameraModule) {
        this.graphicEntityModule = graphicEntityModule;
        double xRatio = 1920 / ARENA_SIZE.getX();
        double yRatio = 1080 / ARENA_SIZE.getY();
        if (xRatio > yRatio) {
            sizeRatio = yRatio;
            X0 = (int) ((1920 - ARENA_SIZE.getX() * sizeRatio) * .5);
            Y0 = 0;
        } else {
            sizeRatio = xRatio;
            X0 = 0;
            Y0 = (int) ((1080 - ARENA_SIZE.getY() * sizeRatio) * .5);
        }
        arenaSize = new Point((int) (sizeRatio * ARENA_SIZE.getX()),
                (int) (sizeRatio * ARENA_SIZE.getY()));
        gameGroup = graphicEntityModule.createGroup();
        tooltips = tooltipModule;
        camera = cameraModule;
        bulletManager = new BulletManager(this);
        animManager = new AnimationManager(this);
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
        //graphicEntityModule.createSprite().setImage("bb.jpg").setZIndex(-3);
        double scaleX = 1920 / 100., scaleY = 1080 / 100.;
        graphicEntityModule.createTilingSprite().setImage(BACKGROUND_TILE_IMAGE)
                .setScaleX(scaleX) // .add(WALL_THICKNESS)
                .setTileScaleX(1 / scaleX * BACKGROUND_TILE_SCALE)
                // because of round issue (1 pixel left black)
                .setScaleY(scaleY)
                .setTileScaleY(1 / scaleY * BACKGROUND_TILE_SCALE)
                .setZIndex(-3)
                .setX(-15);
//                .setRotation(Math.PI / 6.)
//                .setAnchor(.5)
//                .setX(1920 / 2)
//                .setY(1080 / 2);
    }

    private Group createArena() {
        Group arena = graphicEntityModule.createGroup()
                .setX(X0)
                .setY(Y0);
        TilingSprite[] walls = new TilingSprite[4];
        for (int i = 0; i < 4; i++) {
            double scaleX = sizeRatio * ARENA_SIZE.getX() / 100.;
            double scaleY = sizeRatio * WALL_THICKNESS / 100.;
            walls[i] = graphicEntityModule.createTilingSprite().setImage("w.png").setY(0)
                    .setScaleX(scaleX)
                    .setTileScaleX(1. / scaleX * WALL_TILE_SCALE)
                    .setScaleY(scaleY)
                    .setTileScaleY(1. / scaleY * WALL_TILE_SCALE);
            //.setAlpha(.5);
            arena.add(walls[i]);

        }
        walls[0].setZIndex(-1).setTileX(31);
        walls[1].setRotation(Math.PI / 2).setX(walls[1].getX() + sizeToScreen(WALL_THICKNESS));
        walls[2].setY(sizeToScreen(ARENA_SIZE.getY() - WALL_THICKNESS)).setTileX(-3);
        walls[3].setRotation(Math.PI / 2).setX(sizeToScreen(ARENA_SIZE.getX()));
        double scaleX = sizeRatio * MAP_SIZE.add(WALL_THICKNESS * .5).getX() / 100.;
        double scaleY = sizeRatio * MAP_SIZE.add(WALL_THICKNESS * .5).getY() / 100.;
        TilingSprite background = graphicEntityModule.createTilingSprite().setImage(ARENA_TILE_IMAGE)
                .setScaleX(scaleX) // .add(WALL_THICKNESS)
                .setTileScaleX(1 / scaleX * ARENA_TILE_SCALE)
                // because of round issue (1 pixel left black)
                .setScaleY(scaleY)
                .setTileScaleY(1 / scaleY * ARENA_TILE_SCALE)
                .setZIndex(-2);
        arena.add(background);
        background.setX(coordToScreen(0))
                .setY(coordToScreen(0));
        return arena;
    }

    public void instantiateBullet(Bullet bullet, Point deviation) {
        commitEntity();
        bulletManager.instantiateBullet(bullet, deviation);
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
        return (int) ((pos + WALL_THICKNESS) * sizeRatio);
    }

    public int sizeToScreen(double size) {
        return (int) (size * sizeRatio);
    }


    public void addToArena(Entity<?> entity) {
        arena.add(entity);
    }

    public void commitEntity() {
        newObjectToCommit = true;
    }

    public AnimationManager getAnimManager() {
        return animManager;
    }

}
