package view;

import com.codingame.game.Bullet;
import com.codingame.game.Point;
import com.codingame.game.TooltipModule;
import com.codingame.game.ZeroDivisionException;
import com.codingame.game.gameEntities.Robot;
import com.codingame.gameengine.module.entities.*;
import view.modules.CameraModule;

import java.util.*;

import static com.codingame.game.Constants.*;
import static com.codingame.gameengine.module.entities.Curve.*;


public class ViewManager {
    public static double sizeRatio;
    public static int X0;
    public static int Y0;
    private final TooltipModule tooltips;
    private final GraphicEntityModule graphicEntityModule;
    private final CameraModule camera;

    private final Set<ViewPart> viewParts = new HashSet<>();
    private final Group gameGroup;
    private final Point fieldSize;
    private Group playerField;
    private Point oldCameraPosition = new Point();

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
            ViewPart robotSprite = new RobotSprite(robot, playerField);
            viewParts.add(robotSprite);
            camera.addTrackedEntity(robotSprite.getSprite());

        }
        camera.setCameraOffset(CAMERA_OFFSET*sizeRatio);
    }

    public void instantiateBullet(Bullet bullet) {
        viewParts.add(new BulletSprite(bullet, playerField));

    }

    private void updateCameraPosition() {
        Bound bound = new Bound();
        for (ViewPart viewPart : viewParts) {
            if (viewPart.isActive() && viewPart.getClass() == RobotSprite.class) {
                double rx = ((RobotSprite) viewPart).model.getX(),
                        ry = ((RobotSprite) viewPart).model.getY();
                bound.add(new Point(rx, ry));
            }
        }

        Point averagePoint = bound.average();
        Point boundSize = bound.size();
        System.out.println(averagePoint);
        Curve curve = oldCameraPosition.getDist(averagePoint) > CAMERA_OFFSET ? EASE_OUT : LINEAR;
        double scale = Double.min(1080 / sizeRatio / (boundSize.getY() + CAMERA_OFFSET),
                1920 / sizeRatio / (boundSize.getX() + CAMERA_OFFSET));
        playerField
                .setScale(scale, curve)
                .setX((int) (0.5 + X0 - fieldSize.getX() * (scale - 1) / 2 + scale * sizeRatio * (MAP_SIZE.getX() / 2. - averagePoint.getX())), curve)
                .setY((int) (0.5 + Y0 - fieldSize.getY() * (scale - 1) / 2 + scale * sizeRatio * (MAP_SIZE.getY() / 2. - averagePoint.getY())), curve);

        oldCameraPosition = averagePoint;
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
        updatePlayerField(playerField, viewParts);
        //updateCameraPosition();

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
        int r = rgb.r + (int) (random.nextDouble() * (Math.min(255 - rgb.r, shift) + Math.min(rgb.r, shift))) - Math.min(rgb.r, shift);
        int g = rgb.g + (int) (random.nextDouble() * (Math.min(255 - rgb.g, shift) + Math.min(rgb.g, shift))) - Math.min(rgb.g, shift);
        int b = rgb.b + (int) (random.nextDouble() * (Math.min(255 - rgb.b, shift) + Math.min(rgb.b, shift))) - Math.min(rgb.b, shift);
        return toHex(r, g, b);
    }


    private static class Bound {
        private double maxX = -1,
                maxY = -1,
                minX = MAP_SIZE.getX(),
                minY = MAP_SIZE.getY();

        public Bound() {
        }

        public void add(Point p) {
            maxX = Double.max(maxX, p.getX());
            maxY = Double.max(maxY, p.getY());
            minX = Double.min(minX, p.getX());
            minY = Double.min(minY, p.getY());
        }

        public Point average() {
            return new Point((maxX + minX) / 2, (maxY + minY) / 2);
        }

        public Point size() {
            return new Point(maxX - minX, maxY - minY);
        }

    }

    public abstract static class ViewPart {
        public abstract void update();

        public abstract boolean isActive();

        public abstract void onRemove();

        public abstract Group getSprite();

        public abstract boolean isShown();
    }

    private class ProgressBar {
        private final Group barGroup;
        private final Entity<?> bar;

        public ProgressBar(int color) {
            bar = graphicEntityModule.createRectangle().setFillColor(color).setLineWidth(0).setAlpha(0.8)
                    .setWidth(HEALTH_BAR_WIDTH).setHeight(HEALTH_BAR_HEIGHT);
            Group group = graphicEntityModule.createGroup(graphicEntityModule.createRectangle().setFillColor(0x777777).setAlpha(.5)
                    .setWidth(HEALTH_BAR_WIDTH).setHeight(HEALTH_BAR_HEIGHT), bar);
            for (int i = 0; i < 5; i++) {
                group.add(graphicEntityModule.createRectangle().setHeight(HEALTH_BAR_HEIGHT).setWidth(2)
                        .setX(group.getX() + i * HEALTH_BAR_WIDTH / 4).setFillColor(0).setAlpha(0.5));
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

    public class RobotSprite extends ViewPart {
        private final Robot model;
        private final Group robotGroup;
        private final ProgressBar shieldBar;
        private final ProgressBar healthBar;
        private final Curve curve = LINEAR;
        private final Animation attackAnim;
        private final Animation moveAnim;


        public RobotSprite(Robot robot, Group playerField) {

            this.model = robot;
            int size = (int) (robot.getSpriteSize() * sizeRatio);
            int color = model.getOwner().getColorToken();
            robotGroup = graphicEntityModule.createGroup(
                    graphicEntityModule.createSprite().setImage(model.getRobotType().toString().charAt(0) + "B.png")
                            .setAnchor(0.5)
                            .setBaseWidth(size)
                            .setBaseHeight(size)
                            .setAlpha(1.0)
                            .setTint(color));
            robotGroup.add(graphicEntityModule.createSprite().setImage(model.getRobotType().toString().charAt(0) + "R.png")
                    .setAnchor(0.5)
                    .setBaseWidth(size)
                    .setBaseHeight(size)
                    .setAlpha(1.0));

            int animAttackLength = model.getRobotType().getAttackAnimLength();
            Sprite[] canonSprites = new Sprite[animAttackLength];
            for (int i = 0; i < animAttackLength; i++) {
                Sprite canon;
                robotGroup.add(canon = graphicEntityModule.createSprite().setImage(model.getRobotType().toString().charAt(0) + "A" + (i + 1) + ".png")
                        .setAnchor(0.5)
                        .setBaseWidth(size)
                        .setBaseHeight(size)
                        .setAlpha(i == 0 ? 1.0 : 0)
                );//.setTint(0));
                canonSprites[i] = canon;
            }
            attackAnim = new Animation(canonSprites, Curve.IMMEDIATE);

            int animMoveLength = model.getRobotType().getMoveAnimLength();
            Sprite[] moveSprites = new Sprite[animMoveLength];
            for (int i = 0; i < animMoveLength; i++) {
                Sprite animFrame;
                robotGroup.add(animFrame = graphicEntityModule.createSprite().setImage(model.getRobotType().toString().charAt(0) + "M" + (i + 1) + ".png")
                        .setAnchor(0.5)
                        .setBaseWidth(size)
                        .setBaseHeight(size)
                        .setAlpha(i == 0 ? 1.0 : 0)
                        .setTint(0x00FFFF, Curve.IMMEDIATE)
                );//.setTint(0));
                moveSprites[i] = animFrame;
            }

            moveAnim = new Animation(moveSprites, curve).setLooping(true);//.setFrameLength(3);

            playerField.add(robotGroup);
            robotGroup.setX(coordToScreen(robot.getX()));
            robotGroup.setY(coordToScreen(robot.getY()));
            shieldBar = new ProgressBar(0x00E1F5);
            healthBar = new ProgressBar(0xAB0098);
            robotGroup.add(shieldBar.barGroup, healthBar.barGroup);
            shieldBar.getBarGroup().setX(shieldBar.getBarGroup().getX() + 15);
            healthBar.getBarGroup().setX(healthBar.getBarGroup().getX() + 15)
                    .setY((int) (shieldBar.getBarGroup().getY() + HEALTH_BAR_HEIGHT * 1.5));
            //shieldBar.getBarGroup().setY(shieldBar.getBarGroup().getY()+10);
            robotGroup.setRotation(Math.PI * (1 - robot.getTeam()));
            tooltips.setTooltipText(robotGroup, getTooltip());

        }

        //private String getTooltip() {return "";}
        private String getTooltip() {
            return String.format("%d %s %s %s %s %s", model.getId(),
                    model.getHealth() == model.getMaxHealth() ? "M" : model.getHealth() + "",
                    (model.getRobotType() + "").charAt(0),
                    model.getShield() == model.getMaxShieldHealth() ? "M" : model.getShield() + "",
                    (model.getRobotType() + "").charAt(0),
                    model.getLastAction().charAt(0) + model.getStringTargets());
        }

        @Override
        public void update() {
            try {
                robotGroup.setRotation(Math.PI / 2 + model.getDirection(model.getAveragePoint(model.getTargets())).getRotation(), EASE_OUT);
            } catch (ZeroDivisionException ignored) {
            }
            robotGroup.setX(coordToScreen(model.getX()), LINEAR);
            robotGroup.setY(coordToScreen(model.getY()), LINEAR);
            shieldBar.setBar(Math.max(0, model.getShieldRatio()));
            healthBar.setBar(Math.max(0, model.getHealthRatio()));
            if (!Objects.equals(tooltips.getTooltipText(robotGroup), getTooltip())) {
                tooltips.setTooltipText(robotGroup, getTooltip());
            }

            attackAnim.update(!model.getLastAction().equals("ATTACK"));
            moveAnim.setActive(!model.getLastAction().equals("ATTACK") && !model.getLastAction().equals("IDLE"));
            moveAnim.update();
        }

        @Override
        public boolean isActive() {
            return model.checkActive();
        }

        @Override
        public void onRemove() {
            camera.removeTrackedEntity(robotGroup);
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

        public BulletSprite(Bullet bullet, Group playerField) {

            model = bullet;
            bulletGroup = graphicEntityModule.createGroup(graphicEntityModule.createCircle().
                    setRadius(coordToScreen(BULLET_SIZE)).setAlpha(1).
                    setFillColor(bullet.getOwner().getColorToken())).setZIndex(1);
            playerField.add(bulletGroup);
            bulletGroup.setX(coordToScreen(model.getX()), Curve.IMMEDIATE)
                    .setY(coordToScreen(model.getY()), Curve.IMMEDIATE);
        }

        @Override
        public void update() {

            bulletGroup.setX(coordToScreen(model.getX()), LINEAR);
            bulletGroup.setY(coordToScreen(model.getY()), LINEAR);
        }

        @Override
        public boolean isActive() {
            if (!model.isActive() && active) {
                active = false;
                bulletGroup.setAlpha(0., EASE_IN);
                graphicEntityModule.createSprite().setImage("hitmarker.png").setScale(BULLET_SIZE);
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
        private final int g;
        private final int b;

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

    private class Animation {
        private final Sprite[] sprites;
        private boolean reverse = false;
        private boolean looping = false;
        private boolean active = true;
        private int state = 0;
        private Curve curve = LINEAR;

        private int maxFrameLength = 1;
        private int frameState = 0;

        public Animation(Sprite[] sprites, int init) {
            this.sprites = sprites;
            state = init;
        }

        public Animation(Sprite[] sprites) {
            this.sprites = sprites;
        }

        public Animation(Sprite[] sprites, int init, Curve c) {
            this.sprites = sprites;
            state = init;
            curve = c;
        }

        public Animation(Sprite[] sprites, Curve c) {
            this.sprites = sprites;
            curve = c;
        }

        public void setReverse(boolean r) {
            reverse = r;
        }

        public Animation setLooping(boolean l) {
            looping = l;
            return this;
        }

        public Animation setActive(boolean a) {
            active = a;
            return this;
        }

        public Animation setFrameLength(int frameLength) {
            this.maxFrameLength = frameLength;
            return this;
        }

        public void update() {
            if (!active) {
                return;
            }
            frameState++;
            if (frameState < maxFrameLength) {
                return;
            }
            if (!reverse) {
                if (state < sprites.length - 1) {
                    if (state >= 0) {
                        sprites[state].setAlpha(0, curve);
                    }
                    sprites[++state].setAlpha(1, curve);
                }
            } else {
                if (state > 0) {
                    if (state < sprites.length) {
                        sprites[state].setAlpha(0, curve);
                    }
                    sprites[--state].setAlpha(1, curve);
                }
            }
            if (state == 0 && reverse || state == sprites.length - 1) {
                reverse = !reverse;
            }

            if (frameState == maxFrameLength) {
                frameState = 0;
            }
        }

        public void update(boolean r) {
            this.reverse = r;
            update();
        }
    }

}
