package view;
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
import com.codingame.gameengine.module.entities.GraphicEntityModule;
import com.codingame.gameengine.module.entities.Group;
import view.entitiesSprites.BulletSprite;
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
    private final Point fieldSize;
    public double sizeRatio;
    private Group playerField;

    public ViewManager(GraphicEntityModule graphicEntityModule, TooltipModule tooltipModule, CameraModule cameraModule) {
        this.graphicEntityModule = graphicEntityModule;
        double xRatio = 1920 / MAP_SIZE.getX();
        double yRatio = 1080 / MAP_SIZE.getY();
        if (xRatio > yRatio) {
            sizeRatio = yRatio;
            X0 = (int) ((1920 - MAP_SIZE.getX() * sizeRatio) * .5);
            Y0 = 0;
        } else {
            sizeRatio = xRatio;
            X0 = 0;
            Y0 = (int) ((1080 - MAP_SIZE.getY() * sizeRatio) * .5);
        }
        sizeRatio = Math.min(xRatio, yRatio);
        fieldSize = new Point(coordToScreen(MAP_SIZE.getX()), coordToScreen(MAP_SIZE.getY()));
        gameGroup = graphicEntityModule.createGroup();
        tooltips = tooltipModule;
        camera = cameraModule;
    }

    private Group createPlayerField() {
        Group playerField = graphicEntityModule.createGroup()
                .setX(X0)
                .setY(Y0);

        playerField.add(graphicEntityModule.createRectangle()
                .setHeight(coordToScreen(MAP_SIZE.getX()), Curve.IMMEDIATE)
                .setWidth(coordToScreen(MAP_SIZE.getY()), Curve.IMMEDIATE)
                .setX(0).setY(0).setLineWidth(coordToScreen(0.5))
                .setLineColor(WALL_COLOR).setFillAlpha(1).setFillColor(BACKGROUND_COLOR));
        return playerField;
    }

    public void init(Set<Robot> robots) {
        playerField = createPlayerField();
        gameGroup.add(playerField);
        //camera.setContainer(autoCameraPlayerField, (int) (0.5 + fieldSize.getX()), (int) (0.5 + fieldSize.getY()));
        camera.setContainer(playerField, 1920, 1080);
        for (Robot robot : robots) {
            ViewPart robotSprite = new RobotSprite(robot, playerField, this);
            viewParts.add(robotSprite);
            camera.addTrackedEntity(robotSprite.getSprite());

        }
        camera.setCameraOffset(CAMERA_OFFSET * sizeRatio);
    }

    public void instantiateBullet(Bullet bullet, Point deviation) {

        priorityViewParts.add(new BulletSprite(bullet, playerField, this, deviation));

    }


    private void updatePlayerField(Group playerField, Set<ViewPart> viewParts) {
        Iterator<ViewPart> it = viewParts.iterator();
        while (it.hasNext()) {
            ViewPart viewPart = it.next();
            if (!viewPart.isActive()) {
                it.remove();
                viewPart.onRemove();
                playerField.remove(viewPart.getSprite());
                viewPart.getSprite().setVisible(false);
            } else {
                viewPart.getSprite().setVisible(viewPart.isShown());
                viewPart.update();
            }
        }
    }

    public void update() {
        graphicEntityModule.commitWorldState(0.);
        updatePlayerField(playerField, priorityViewParts);
        updatePlayerField(playerField, viewParts);

    }

    public int coordToScreen(double pos) {
        return (int) (pos * sizeRatio);
    }

}
