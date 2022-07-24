package view.managers;
/* Zindex :
    - -4 : background
    - -3 : floor
    - -2 : crater
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
import com.codingame.gameengine.module.entities.Entity;
import com.codingame.gameengine.module.entities.GraphicEntityModule;
import com.codingame.gameengine.module.entities.Group;
import com.codingame.gameengine.module.entities.Line;
import com.codingame.gameengine.module.entities.TilingSprite;
import com.codingame.gameengine.module.toggle.ToggleModule;
import view.entitiesSprites.RobotSprite;
import view.entitiesSprites.ViewPart;
import view.fx.AnimationType;
import view.fx.GraphicModuleAnimation;
import view.modules.CameraModule;
import view.modules.DebugOnHoverModule;
import view.modules.FollowEntityModule;
import view.modules.TooltipModule;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;

import static com.codingame.game.Constants.*;

public class ViewManager {
    public final TooltipModule tooltips;
    public final GraphicEntityModule graphicEntityModule;
    public final ToggleModule toggleModule;
    public final FollowEntityModule followEntityModule;
    public final CameraModule camera;
    public final DebugOnHoverModule debugOnHoverModule;
    private final int X0;
    private final int Y0;
    //    private final Set<ViewPart> priorityViewParts = new HashSet<>();
    private final Set<ViewPart> viewParts = new HashSet<>();
    private final Group gameGroup;
    private final BulletManager bulletManager;
    private final AnimationManager animManager;
    private final CraterManager craterManager;
    private final Random random;
    private final double sizeRatio;
    private boolean newObjectToCommit = false;
    private Group arena;
    private GraphicModuleAnimation tesla2;

    private Map<String, Line> vectorMap;


    public ViewManager(GraphicEntityModule graphicEntityModule, TooltipModule tooltipModule, CameraModule cameraModule
            , long seed, ToggleModule toggleModule, FollowEntityModule followEntityModule, DebugOnHoverModule debugOnHoverModule) {
        this.toggleModule = toggleModule;
        this.debugOnHoverModule = debugOnHoverModule;
        this.followEntityModule = followEntityModule;
        random = new Random(seed);
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
        gameGroup = graphicEntityModule.createGroup();
        tooltips = tooltipModule;
        camera = cameraModule;
        bulletManager = new BulletManager(this);
        animManager = new AnimationManager(this);
        craterManager = new CraterManager(this, random);
    }

    public void init(Set<Robot> robots) {
        createArena();
        gameGroup.add(arena);
        //camera.setContainer(autoCameraArena, (int) (0.5 + fieldSize.getX()), (int) (0.5 + fieldSize.getY()));
        camera.setContainer(arena, 1920, 1080);
        for (Robot robot : robots) {
            ViewPart robotSprite = new RobotSprite(robot, arena, this);
            viewParts.add(robotSprite);
            camera.addTrackedEntity(robotSprite.getSprite());

        }
        
        camera.setCameraOffset(CAMERA_OFFSET * sizeRatio);   
        initVectors(viewParts);     
    }

    private void createArena() {
        this.arena = graphicEntityModule.createGroup()
                .setX(X0)
                .setY(Y0);

        double scaleX = 3 * 1920 / 100., scaleY = 3 * 1150 / 100.;
        arena.add(graphicEntityModule.createTilingSprite().setImage(BACKGROUND_TILE_IMAGE)
                .setScaleX(scaleX)
                .setTileScaleX(1 / scaleX * BACKGROUND_TILE_SCALE)
                .setScaleY(scaleY)
                .setTileScaleY(1 / scaleY * BACKGROUND_TILE_SCALE)
                .setZIndex(Z_INDEX_BACKGROUND)
                .setAnchor(.5)
                .setX(24));
        new GraphicModuleAnimation(this, AnimationType.Tesla, -200, 400, Z_INDEX_ROBOTS, 1, 1);
        tesla2 = new GraphicModuleAnimation(this, AnimationType.Tesla, 1300, 730, Z_INDEX_CRATER, 1, 1);
        tesla2.getSprite().setPlaying(false);
        arena.add(
                graphicEntityModule.createSprite().setImage("prop_1.png")
                        .setX(-250).setY(320).setAnchor(.5).setZIndex(Z_INDEX_CRATER),
                graphicEntityModule.createSprite().setImage("prop_1.png")
                        .setX(1230).setY(800).setAnchor(.5).setZIndex(Z_INDEX_ROBOTS),
                graphicEntityModule.createSprite().setImage("prop_2.png")
                        .setAnchor(.5).setX(-200).setY(900).setZIndex(Z_INDEX_ROBOTS),
                graphicEntityModule.createSprite().setImage("prop_2.png")
                        .setAnchor(.5).setX(1350).setY(180).setZIndex(Z_INDEX_ROBOTS),
                graphicEntityModule.createSprite().setImage("prop_3.png")
                        .setAnchor(.5).setX(1190).setY(500).setZIndex(Z_INDEX_ROBOTS)


        );
        TilingSprite[] walls = new TilingSprite[4];
        scaleX = sizeRatio * WALL_SIZE.getX() / 100.;
        scaleY = sizeRatio * WALL_THICKNESS / 100.;
        for (int i = 0; i < 4; i++) {
            walls[i] = graphicEntityModule.createTilingSprite().setImage("w.png").setY(0)
                    .setScaleX(scaleX)
                    .setTileScaleX(1. / scaleX * WALL_TILE_SCALE)
                    .setScaleY(scaleY)
                    .setTileScaleY(1. / scaleY * WALL_TILE_SCALE)
                    .setX(sizeToScreen(ARENA_PADDING))
                    .setY(sizeToScreen(ARENA_PADDING))
                    .setZIndex(Z_INDEX_WALLS);
            arena.add(walls[i]);

        }
        walls[0].setZIndex(Z_INDEX_WALL0).setTileX(31);
        walls[1].setRotation(Math.PI / 2).setX(walls[1].getX() + sizeToScreen(WALL_THICKNESS));
        walls[2].setY(walls[2].getY() + sizeToScreen(WALL_SIZE.getY() - WALL_THICKNESS)).setTileX(-3);
        walls[3].setRotation(Math.PI / 2).setX(walls[3].getX() + sizeToScreen(WALL_SIZE.getX()));
        scaleX = sizeRatio * MAP_SIZE.add(WALL_THICKNESS * .5).getX() / 100.;
        scaleY = sizeRatio * MAP_SIZE.add(WALL_THICKNESS * .5).getY() / 100.;
        TilingSprite background = graphicEntityModule.createTilingSprite().setImage(ARENA_TILE_IMAGE)
                .setScaleX(scaleX) // .add(WALL_THICKNESS)
                .setTileScaleX(1 / scaleX * ARENA_TILE_SCALE)
                // because of round issue (1 pixel left black)
                .setScaleY(scaleY)
                .setTileScaleY(1 / scaleY * ARENA_TILE_SCALE)
                .setZIndex(Z_INDEX_ARENA_FLOOR);
        arena.add(background);
        background.setX(coordToScreen(0))
                .setY(coordToScreen(0));
    }

    public void addCrater(Point position, double size) {
        craterManager.addCrater(position, size);
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

    private void updateBackground(int turn) {
        if (turn == 2) {
            tesla2.getSprite().setPlaying(true).setDuration(FRAME_DURATION * 8);
        }
    }

    public void update(int turn) {
        updateBackground(turn);
        applyCommits();
        bulletManager.updateBullets();
        updateViewParts(viewParts);
        animManager.updateAnimations();
        updateVectors();
    }

    public void updateVectors(){

        // create a hashMap with all Robots

        Map<Integer, Robot> robots = new HashMap<Integer, Robot>();

        for(ViewPart viewPart : viewParts){

            if(viewPart.isRobot() && viewPart.isActive()){

                Robot robot = ((RobotSprite) viewPart).getRobot();
                
                robots.put(robot.getId(), robot);
            }
        }

        // update all existing vectors

        for(String vectorKey : this.vectorMap.keySet()){

            Line vector = vectorMap.get(vectorKey);

            Robot robot_Team_0 = robots.get(Integer.parseInt(vectorKey.split(":")[0]));
            Robot robot_Team_1 = robots.get(Integer.parseInt(vectorKey.split(":")[1]));

            if(robot_Team_0 == null || robot_Team_1 == null){ // it means than one of the two / or the robots have been deactivated, so we hide the existing vector
                vector.setVisible(false);
            }
            else{ // the two robots are still active, so we update the vector coords

                vector

                .setX(coordToScreen(robot_Team_0.getX()))
                .setY(coordToScreen(robot_Team_0.getY()))
                .setX2(coordToScreen(robot_Team_1.getX()))
                .setY2(coordToScreen(robot_Team_1.getY()));

                //addDebug(vector);
            }
        }        
    }

    public void initVectors(Set<ViewPart> viewParts){

        this.vectorMap = new HashMap<String, Line>();

        // create a hashMap with all robots by team

        Map<Integer, List<Robot>> robotMap = new HashMap<Integer, List<Robot>>();

        robotMap.put(0, new ArrayList<Robot>());
        robotMap.put(1, new ArrayList<Robot>());
        
        for(ViewPart viewPart : viewParts){

            if(viewPart.isRobot() && viewPart.isActive()){

                Robot robot = ((RobotSprite) viewPart).getRobot();
                robotMap.get(robot.getTeam()).add(robot);
            }
        }

        // create lines bewtween all robots 

        for(Robot robot_Team_0 : robotMap.get(0)){

            double x0 = robot_Team_0.getX();
            double y0 = robot_Team_0.getY();

            for(Robot robot_Team_1 : robotMap.get(1)){

                String vectorKey = String.format("%s:%s", robot_Team_0.getId(), robot_Team_1.getId());

                double x1 = robot_Team_1.getX();
                double y1 = robot_Team_1.getY();

                Line vector = graphicEntityModule.createLine();

                vector
                .setVisible(true)

                .setX(coordToScreen(x0))
                .setY(coordToScreen(y0))
                .setX2(coordToScreen(x1))
                .setY2(coordToScreen(y1))

                .setZIndex(Z_INDEX_ARENA_FLOOR + 1)

                .setLineWidth(1)
                .setLineColor(0xf70000);

                addToArena(vector);
                addDebug(vector);

                this.vectorMap.put(vectorKey, vector);
            }
        }        
    }

    public void removeForDebug(Entity<?> entity) {
        toggleModule.displayOnToggleState(entity, "debug", false);
    }

    public void addDebug(Entity<?> entity) {
        toggleModule.displayOnToggleState(entity, "debug", true);
    }

    public int coordToScreen(double pos) {
        return (int) ((pos + WALL_THICKNESS + ARENA_PADDING) * sizeRatio);
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
