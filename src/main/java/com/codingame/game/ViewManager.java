package com.codingame.game;

import com.codingame.game.entities.Robot;
import com.codingame.gameengine.module.entities.*;

import java.util.HashSet;
import java.util.Set;

public class ViewManager {
    public static double sizeRatio;
    public static int X0;
    public static int Y0;
    private final GraphicEntityModule graphicEntityModule;
    private Group playerField;
    private final Set<ViewPart> viewParts = new HashSet<>();

    public ViewManager(GraphicEntityModule graphicEntityModule) {
        this.graphicEntityModule = graphicEntityModule;
        double xRatio = 1920 / Constants.MAP_SIZE.getX();
        double yRatio = 1080 / Constants.MAP_SIZE.getY();
        if (xRatio > yRatio) {
            sizeRatio = yRatio;
            X0 = (int) ((1920 - Constants.MAP_SIZE.getX() * sizeRatio) * .5);
            Y0 = 0;
        } else {
            sizeRatio = xRatio;
            X0 = 0;
            Y0 = (int) ((1080 - Constants.MAP_SIZE.getY() * sizeRatio) * .5);
        }
        sizeRatio = Math.min(xRatio, yRatio);


    }

    public void init(Set<Robot> robots) {
        playerField = graphicEntityModule.createGroup()
                .setX(X0)
                .setY(Y0);
        playerField.add(graphicEntityModule.createRectangle()
                .setHeight(coordToScreen(Constants.MAP_SIZE.getX()), Curve.IMMEDIATE)
                .setWidth(coordToScreen(Constants.MAP_SIZE.getY()), Curve.IMMEDIATE)
                .setX(0).setY(0).setLineWidth(coordToScreen(0.5))
                .setLineColor(0x00FF00).setFillAlpha(1).setFillColor(0x9B9B9B));
        for (Robot robot : robots) {
            viewParts.add(new RobotSprite(robot));
        }
    }

    public void instantiateBullet(Bullet bullet) {
        viewParts.add(new BulletSprite(bullet));
    }

    public void update() {
        for (ViewPart viewPart : viewParts) {
            if (!viewPart.isActive()) {
                viewParts.remove(viewPart);
                playerField.remove(viewPart.getSprite());
            } else {
                viewPart.getSprite().setVisible(viewPart.isShown());
                viewPart.update();
            }

        }
    }

    private int coordToScreen(double pos) {
        return (int) (pos * sizeRatio);
    }

    private int invertColor(int color) {
        return color ^ 0x00ffffff;
    }

    public abstract class ViewPart {
        public abstract void update();

        public abstract boolean isActive();

        public abstract void onRemove();

        public abstract Group getSprite();

        public abstract boolean isShown();
    }

    public class RobotSprite extends ViewPart {
        private final Robot model;
        private final Group robotGroup;

        public RobotSprite(Robot robot) {
            this.model = robot;
            int size = (int) (robot.getSpriteSize() * sizeRatio);
            int color = model.getOwner().getColorToken();
            robotGroup = graphicEntityModule.createGroup(
                    graphicEntityModule.createSprite().setImage(model.getRobotType() + "BODY.png")
                            .setAnchor(0.5)
                            .setBaseWidth(size)
                            .setBaseHeight(size)
                            .setAlpha(1.0)
                            .setTint(color)
                            .setRotation(Math.PI * (1 - robot.getTeam())));
            robotGroup.add(graphicEntityModule.createSprite().setImage(model.getRobotType() + "WIREFRAME.png")
                    .setAnchor(0.5)
                    .setBaseWidth(size)
                    .setBaseHeight(size)
                    .setAlpha(1.0)
                    .setTint(0)
                    .setRotation(Math.PI * (1-robot.getTeam())));
            playerField.add(robotGroup);
            robotGroup.setX(coordToScreen(robot.getX()));
            robotGroup.setY(coordToScreen(robot.getY()));

        }

        @Override
        public void update() {
            robotGroup.setX(coordToScreen(model.getX()), Curve.LINEAR);
            robotGroup.setY(coordToScreen(model.getY()), Curve.LINEAR);
        }

        @Override
        public boolean isActive() {
            return model.checkActive();
        }

        @Override
        public void onRemove() {

        }

        @Override
        public Group getSprite() {
            return robotGroup;
        }

        @Override
        public boolean isShown() {
            return true;
        }


    }

    public class BulletSprite extends ViewPart {
        private final Bullet model;
        private final Group bulletGroup;

        public BulletSprite(Bullet bullet) {
            model = bullet;
            bulletGroup = graphicEntityModule.createGroup(graphicEntityModule.createCircle().
                    setRadius(coordToScreen(Constants.BULLET_SIZE)).setAlpha(.8).
                    setFillColor(bullet.getOwner().getColorToken()));
        }

        @Override
        public void update() {
            bulletGroup.setX(coordToScreen(model.getX()), Curve.LINEAR);
            bulletGroup.setY(coordToScreen(model.getY()), Curve.LINEAR);
        }

        @Override
        public boolean isActive() {
            return model.isActive();
        }

        @Override
        public void onRemove() {

        }

        @Override
        public Group getSprite() {
            return bulletGroup;
        }

        @Override
        public boolean isShown() {
            return true;
        }
    }
}
