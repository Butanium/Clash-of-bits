package view.entitiesSprites;

import com.codingame.game.exceptions.ZeroDivisionException;
import com.codingame.game.gameEntities.Robot;
import com.codingame.gameengine.module.entities.*;
import view.UI.ProgressBar;
import view.fx.AnimationType;
import view.managers.ViewManager;
import view.fx.Animation;

import java.util.Objects;

import static com.codingame.game.Constants.*;
import static com.codingame.gameengine.module.entities.Curve.*;


public class RobotSprite extends ViewPart {
    private final int Z_INDEX_BASE = 0;
    private final int Z_INDEX_SURFACE = Z_INDEX_BASE + 1;
    private final int Z_INDEX_MOVE = Z_INDEX_SURFACE + 1;
    private final int Z_INDEX_CANON = Z_INDEX_MOVE + 1;
    private final int Z_INDEX_HITMARKER = Z_INDEX_CANON + 1;
    private final int Z_INDEX_UI0 = Z_INDEX_HITMARKER + 1;
    private final int Z_INDEX_UI1 = Z_INDEX_UI0 + 1;

    private final Robot model;
    private final Group robotGroup;
    private final ProgressBar shieldBar;
    private final ProgressBar healthBar;
    private final Curve curve = LINEAR;
    private final Animation attackAnim;
    private final Animation moveAnim;
    private final Sprite hitMarker;
    private final ViewManager viewManager;
    private double damageTaken = 0;
    private int impactColor = 0xFFFFFF;

    public RobotSprite(Robot robot, Group playerField, ViewManager viewManager) {
        this.viewManager = viewManager;
        hitMarker = viewManager.graphicEntityModule.createSprite().setImage(HITMARKER_IMAGE).setScale(HITMARKER_SIZE)
                .setAnchor(.5).setVisible(false).setRotation(HITMARKER_ANGLE);
        this.model = robot;
        GraphicEntityModule graphicEntityModule = viewManager.graphicEntityModule;
        int size = (viewManager.sizeToScreen(robot.getSpriteSize()));
        int color = model.getOwner().getColorToken();
        robotGroup = graphicEntityModule.createGroup(
                graphicEntityModule.createSprite().setImage(model.getRobotType().toString().charAt(0) + "B.png")
                        .setAnchor(0.5)
                        .setBaseWidth(size)
                        .setBaseHeight(size)
                        .setAlpha(1.0)
                        .setZIndex(Z_INDEX_BASE)
                        .setTint(color)).setZIndex(Z_INDEX_ROBOTS);
        robotGroup.add(graphicEntityModule.createSprite().setImage(model.getRobotType().toString().charAt(0) + "R.png")
                .setAnchor(0.5)
                .setBaseWidth(size)
                .setBaseHeight(size)
                .setAlpha(1.0).setZIndex(Z_INDEX_SURFACE));

        int animAttackLength = model.getRobotType().getAttackAnimLength();
        Sprite[] canonSprites = new Sprite[animAttackLength];
        for (int i = 0; i < animAttackLength; i++) {
            Sprite canon;
            robotGroup.add(canon = graphicEntityModule.createSprite().setImage(model.getRobotType().toString().charAt(0) + "A" + (i + 1) + ".png")
                    .setAnchor(0.5)
                    .setBaseWidth(size)
                    .setBaseHeight(size)
                    .setAlpha(i == 0 ? 1.0 : 0)
                    .setZIndex(Z_INDEX_CANON)
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
                    .setZIndex(Z_INDEX_MOVE)
            );//.setTint(0));
            moveSprites[i] = animFrame;
        }

        moveAnim = new Animation(moveSprites, curve).setLooping(true);//.setFrameLength(3);

        playerField.add(robotGroup);
        robotGroup.setX(viewManager.coordToScreen(robot.getX()));
        robotGroup.setY(viewManager.coordToScreen(robot.getY()));
        shieldBar = new ProgressBar(0x00E1F5, graphicEntityModule);
        healthBar = new ProgressBar(0xAB0098, graphicEntityModule);
        robotGroup.add(shieldBar.getBarGroup(), healthBar.getBarGroup(), hitMarker.setZIndex(Z_INDEX_HITMARKER));
        shieldBar.getBarGroup().setX(shieldBar.getBarGroup().getX() + 15);
        healthBar.getBarGroup().setX(healthBar.getBarGroup().getX() + 15)
                .setY((int) (shieldBar.getBarGroup().getY() + HEALTH_BAR_HEIGHT * 1.5));
        //shieldBar.getBarGroup().setY(shieldBar.getBarGroup().getY()+10);
        int s = model.getY() > MAP_SIZE.getY() / 2 ? 0 : 1;
        robotGroup.setRotation(Math.PI * s);

        viewManager.tooltips.setTooltipText(robotGroup, getTooltip());
        this.model.setRobotSprite(this);
    }

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
        robotGroup.setX(viewManager.coordToScreen(model.getX()), LINEAR);
        robotGroup.setY(viewManager.coordToScreen(model.getY()), LINEAR);
        shieldBar.setBar(Math.max(0, model.getShieldRatio()));
        healthBar.setBar(Math.max(0, model.getHealthRatio()));
        if (!Objects.equals(viewManager.tooltips.getTooltipText(robotGroup), getTooltip())) {
            viewManager.tooltips.setTooltipText(robotGroup, getTooltip());
        }

        attackAnim.update(!model.getLastAction().equals("ATTACK"));
        moveAnim.setActive(!model.getLastAction().equals("ATTACK") && !model.getLastAction().equals("IDLE"));
        moveAnim.update();
        if (damageTaken > 0) {
            double t = Math.min(1, damageTaken * HITMARKER_RATIO);
            Curve hit_curve = hitMarker.isVisible() ? EASE_OUT : IMMEDIATE;
            hitMarker.setVisible(true);
            damageTaken = 0;
        } else {
            hitMarker.setVisible(false);
        }
    }

    public void takeDamage(double amount, int color) {
        damageTaken += amount;
        impactColor = color;
    }

    @Override
    public boolean isActive() {
        return model.checkActive();
    }

    @Override
    public void onRemove() {
        setVisible(false);
        updateVisibility();
        viewManager.camera.removeTrackedEntity(robotGroup);
        viewManager.getAnimManager().createAnimation(AnimationType.Explosion, robotGroup.getX(), robotGroup.getY(), Z_INDEX_EXPLOSIONS,
                0.5, 0.8);
        viewManager.addCrater(model, model.getSpriteSize());

    }

    @Override
    public Group getSprite() {
        return robotGroup;
    }

}

