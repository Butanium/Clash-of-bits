package com.codingame.game;

import com.codingame.game.entities.Robot;
import com.codingame.gameengine.module.entities.*;

import java.util.*;


public class ViewManager {
    public static double sizeRatio;
    public static int X0;
    public static int Y0;
    private final GraphicEntityModule graphicEntityModule;
    private final Set<ViewPart> viewParts = new HashSet<>();
    private Group playerField;
    private final Set<Rectangle> backgroundRects = new HashSet<>();
    private int backGroundColor = 0x00FF00;
    private final Group gameGroup;

    public ViewManager(GraphicEntityModule graphicEntityModule, Group gameGroup) {
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
        this.gameGroup = gameGroup;

    }

    public void init(Set<Robot> robots) {
        playerField = graphicEntityModule.createGroup()
                .setX(X0)
                .setY(Y0);
        gameGroup.add(playerField);
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
        //Referee.debug("bullet instanced");
    }

    public void update() {
        Iterator<ViewPart> it = viewParts.iterator();
        while (it.hasNext()) {
            ViewPart viewPart = it.next();
            if (!viewPart.isActive()) {
                it.remove();
                playerField.remove(viewPart.getSprite());
                viewPart.getSprite().setVisible(false);
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

    public RGB toRGB(int hex) {
        return new RGB(hex);
    }

    public int toHex(int r, int g, int b) {
        int rgb = r;
        rgb = (rgb << 8) + g;
        rgb = (rgb << 8) + b;
        return rgb;
    }

    public int randomColorChange(int hex, int shift) {
        Random random = new Random();
        RGB rgb = toRGB(hex);
        int r = rgb.r +(int) (random.nextDouble() * (Math.min(255 - rgb.r, shift) + Math.min(rgb.r, shift))) - Math.min(rgb.r, shift);
        int g = rgb.g+(int) (random.nextDouble() * (Math.min(255 - rgb.g, shift) + Math.min(rgb.g, shift))) - Math.min(rgb.g, shift);
        int b = rgb.b+(int) (random.nextDouble() * (Math.min(255 - rgb.b, shift) + Math.min(rgb.b, shift))) - Math.min(rgb.b, shift);
        return toHex(r,g,b);
    }

    private class ProgressBar {
        private final Group barGroup;
        private final Entity bar;

        public ProgressBar(int color) {
            bar = graphicEntityModule.createRectangle().setFillColor(color).setLineWidth(0).setAlpha(0.8)
                    .setWidth(Constants.HEALTH_BAR_WIDTH).setHeight(Constants.HEALTH_BAR_HEIGHT);
            Group group = graphicEntityModule.createGroup(graphicEntityModule.createRectangle().setFillColor(0x777777).setAlpha(.5)
                    .setWidth(Constants.HEALTH_BAR_WIDTH).setHeight(Constants.HEALTH_BAR_HEIGHT), bar);
            for (int i = 0; i < 4; i++) {
                group.add(graphicEntityModule.createRectangle().setHeight(Constants.HEALTH_BAR_HEIGHT).setWidth(2)
                        .setX(group.getX() + i * Constants.HEALTH_BAR_WIDTH / 4).setFillColor(0).setAlpha(0.5));
            }
            barGroup = group;
        }

        public Group getBarGroup() {
            return barGroup;
        }

        public void setBar(double v) {
            bar.setScaleX(v);
        }


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
        private final ProgressBar shieldBar;
        private final ProgressBar healthBar;

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
                            .setTint(color));
            robotGroup.add(graphicEntityModule.createSprite().setImage(model.getRobotType() + "WIREFRAME.png")
                    .setAnchor(0.5)
                    .setBaseWidth(size)
                    .setBaseHeight(size)
                    .setAlpha(1.0)
                    .setTint(0));
            playerField.add(robotGroup);
            robotGroup.setX(coordToScreen(robot.getX()));
            robotGroup.setY(coordToScreen(robot.getY()));
            shieldBar = new ProgressBar(0x00E1F5);
            healthBar = new ProgressBar(0xAB0098);
            robotGroup.add(shieldBar.barGroup, healthBar.barGroup);
            shieldBar.getBarGroup().setX(shieldBar.getBarGroup().getX()+15);
            healthBar.getBarGroup().setX(healthBar.getBarGroup().getX()+ 15)
                    .setY((int) (shieldBar.getBarGroup().getY() + Constants.HEALTH_BAR_HEIGHT*1.5));
            //shieldBar.getBarGroup().setY(shieldBar.getBarGroup().getY()+10);
            robotGroup.setRotation(Math.PI * (1 - robot.getTeam()));


        }

        @Override
        public void update() {
            try {
                robotGroup.setRotation(Math.PI / 2 + model.getDirection(model.getAveragePoint(model.getTargets())).getRotation(), Curve.EASE_OUT);
            } catch (ZeroDivisionException ignored) {}
            robotGroup.setX(coordToScreen(model.getX()), Curve.LINEAR);
            robotGroup.setY(coordToScreen(model.getY()), Curve.LINEAR);
            shieldBar.setBar(Math.max(0, model.getShieldRatio()));
            healthBar.setBar(Math.max(0, model.getHealthRatio()));
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
        private boolean active = true;

        public BulletSprite(Bullet bullet) {

            model = bullet;
            bulletGroup = graphicEntityModule.createGroup(graphicEntityModule.createCircle().
                    setRadius(coordToScreen(Constants.BULLET_SIZE)).setAlpha(1).
                    setFillColor(bullet.getOwner().getColorToken())).setZIndex(1);
            playerField.add(bulletGroup);
            bulletGroup.setX(coordToScreen(model.getX()), Curve.IMMEDIATE)
                    .setY(coordToScreen(model.getY()), Curve.IMMEDIATE);
        }

        @Override
        public void update() {

            bulletGroup.setX(coordToScreen(model.getX()), Curve.LINEAR);
            bulletGroup.setY(coordToScreen(model.getY()), Curve.LINEAR);
        }

        @Override
        public boolean isActive() {
             if (!model.isActive() && active) {
                   active = false;
                   return true;
             }
             return active;
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

    private class RGB {
        private final int r;
        private int g;
        private int b;

        RGB(int r, int g, int b) {
            this.r = r;
            this.g = g;
            this.b = b;
        }

        RGB(int hex) {
            r = hex / (256 * 256);
            g = (hex / 256) % 256;
            b = hex % 256;
        }

        public int R() {
            return r;
        }

        public int B() {
            return b;
        }

        public int G() {
            return g;
        }

    }
    private void changeMapColor() {
        int color = randomColorChange(backGroundColor, Constants.NEON_SHIFT);
        for (Rectangle rectangle: backgroundRects) {
            rectangle.setLineColor(color, Curve.LINEAR);
        }
        backGroundColor = color;
    }

}
