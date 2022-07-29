package view.entitiesSprites;

import com.codingame.game.exceptions.ZeroDivisionException;
import com.codingame.game.gameElements.Point;
import com.codingame.game.gameEntities.Robot;
import com.codingame.gameengine.module.entities.*;
import view.UI.ProgressBar;
import view.fx.Animation;
import view.fx.AnimationType;
import view.managers.ViewManager;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Objects;

import static com.codingame.game.Constants.*;
import static com.codingame.gameengine.module.entities.Curve.*;


public class RobotSprite extends ViewPart {
    private final int Z_INDEX_BASE = 0;
    private final int Z_INDEX_SURFACE = Z_INDEX_BASE + 1;
    private final int Z_INDEX_MOVE = Z_INDEX_SURFACE + 1;
    private final int Z_INDEX_CANON = Z_INDEX_MOVE + 1;
    private final int Z_INDEX_DEBUG = Z_INDEX_CANON + 1;
    private final Robot model;
    private final Group robotGroup;
    private final Group robotSprite;
    private final Group debugGroup;
    private final Sprite attackSprite;
    private final Sprite moveSprite;
    private final Sprite idleSprite;
    private final Sprite fleeSprite;

    private final Text idText;
    private final TilingSprite targetSprite;
    private final HashMap<String, Sprite> debugActionSpriteMap = new HashMap<>();
    private final Circle mouseHitbox;
    private final ProgressBar shieldBar;
    private final ProgressBar healthBar;
    private final Animation attackAnim;
    private final Animation moveAnim;
    private final Sprite hitMarker;
    private final ViewManager viewManager;
    private double damageTaken = 0;
    private int impactColor = 0xFFFFFF;

    public RobotSprite(Robot robot, Group playerField, ViewManager viewManager) {
        this.viewManager = viewManager;
        this.model = robot;
        GraphicEntityModule graphicEntityModule = viewManager.graphicEntityModule;
        int spriteSize = (viewManager.sizeToScreen(robot.getSpriteSize()));
        int robotSize = (viewManager.sizeToScreen(robot.getSize()));
        int color = model.getOwner().getColorToken();
        robotGroup = graphicEntityModule.createGroup().setZIndex(Z_INDEX_ROBOTS);
        robotSprite = graphicEntityModule.createGroup(
                graphicEntityModule.createSprite().setImage(model.getRobotType().toString().charAt(0) + "B.png")
                        .setAnchor(0.5)
                        .setBaseWidth(spriteSize)
                        .setBaseHeight(spriteSize)
                        .setAlpha(1.0)
                        .setZIndex(Z_INDEX_BASE)
                        .setTint(color));
        robotSprite.add(graphicEntityModule.createSprite().setImage(model.getRobotType().toString().charAt(0) + "R.png")
                .setAnchor(0.5)
                .setBaseWidth(spriteSize)
                .setBaseHeight(spriteSize)
                .setAlpha(1.0).setZIndex(Z_INDEX_SURFACE));
        robotGroup.add(robotSprite);
        int animAttackLength = model.getRobotType().getAttackAnimLength();
        Sprite[] canonSprites = new Sprite[animAttackLength];
        for (int i = 0; i < animAttackLength; i++) {
            Sprite canon;
            robotSprite.add(canon = graphicEntityModule.createSprite().setImage(model.getRobotType().toString().charAt(0) + "A" + (i + 1) + ".png")
                    .setAnchor(0.5)
                    .setBaseWidth(spriteSize)
                    .setBaseHeight(spriteSize)
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
            robotSprite.add(animFrame = graphicEntityModule.createSprite().setImage(model.getRobotType().toString().charAt(0) + "M" + (i + 1) + ".png")
                    .setAnchor(0.5)
                    .setBaseWidth(spriteSize)
                    .setBaseHeight(spriteSize)
                    .setAlpha(i == 0 ? 1.0 : 0)
                    .setTint(0x00FFFF, Curve.IMMEDIATE)
                    .setZIndex(Z_INDEX_MOVE)
            );//.setTint(0));
            moveSprites[i] = animFrame;
        }

        moveAnim = new Animation(moveSprites, LINEAR).setLooping(true);//.setFrameLength(3);
        playerField.add(robotGroup);
        robotGroup.setX(viewManager.coordToScreen(robot.getX()));
        robotGroup.setY(viewManager.coordToScreen(robot.getY()));

        // Init UI
        shieldBar = new ProgressBar(0x00E1F5, graphicEntityModule);
        healthBar = new ProgressBar(0xAB0098, graphicEntityModule);
        hitMarker = viewManager.graphicEntityModule.createSprite().setImage(HITMARKER_IMAGE).setScale(HITMARKER_SIZE)
                .setAnchor(.5).setVisible(false).setRotation(HITMARKER_ANGLE).setZIndex(Z_INDEX_HITMARKER);
        viewManager.addToArena(shieldBar.getBarGroup());
        viewManager.addToArena(healthBar.getBarGroup());
        viewManager.followEntityModule.followEntity(healthBar.getBarGroup(), robotGroup, 15, 0);
        viewManager.followEntityModule.followEntity(shieldBar.getBarGroup(), robotGroup, 15, HEALTH_BAR_HEIGHT * 1.5);
        viewManager.addToArena(hitMarker);
        viewManager.followEntityModule.followEntity(hitMarker, robotGroup);

        int s = (model.getY() > MAP_SIZE.getY() / 2) ? 0 : 1;
        robotGroup.setRotation(Math.PI * s);
        debugGroup = viewManager.graphicEntityModule.createGroup();
        Group rangeGroup = viewManager.graphicEntityModule.createGroup();
        viewManager.tooltips.setTooltipText(robotGroup, getTooltip());
        this.model.setRobotSprite(this);

        // Hitbox for mouse interaction
        mouseHitbox = graphicEntityModule.createCircle().setRadius(spriteSize * 2 / 3).setFillColor(0xFF0000)
                .setZIndex(Z_INDEX_DEBUG).setLineColor(0x000000).setVisible(true).setAlpha(0);
        // Ranges
        for (double range : RANGES) {
            rangeGroup.add(graphicEntityModule.createCircle().setRadius(viewManager.sizeToScreen(range))
                    .setFillColor(0x0000FF).setFillAlpha(0.1).setLineColor(0x00008D));
        }
        rangeGroup.setZIndex(Z_INDEX_RANGES).setVisible(false);
        viewManager.addToArena(rangeGroup);
        viewManager.followEntityModule.followEntity(rangeGroup, robotGroup);
        // Debug mode
        Circle debug_circle = graphicEntityModule.createCircle().setRadius(robotSize).setFillColor(color)
                .setZIndex(Z_INDEX_BASE).setLineColor(0x000000).setVisible(true);
        idText = viewManager.graphicEntityModule.createText(model.getId() + "").setZIndex(Z_INDEX_ID).setAnchor(0.5);
        viewManager.followEntityModule.followEntity(idText, robotGroup);
        viewManager.addToArena(idText);
        viewManager.addDebug(idText);
        idleSprite = graphicEntityModule.createSprite().setImage("idle.png").setAnchor(0.5)
                .setBaseWidth(spriteSize).setBaseHeight(spriteSize).setZIndex(Z_INDEX_SURFACE);
        attackSprite = graphicEntityModule.createSprite().setImage("attack.png").setAnchor(0.5)
                .setBaseWidth(spriteSize).setBaseHeight(spriteSize).setZIndex(Z_INDEX_SURFACE).setVisible(false);
        moveSprite = graphicEntityModule.createSprite().setImage("arrow.png").setAnchor(0.5).setBaseWidth(spriteSize)
                .setBaseHeight(spriteSize).setZIndex(Z_INDEX_SURFACE).setVisible(false);
        fleeSprite = graphicEntityModule.createSprite().setImage("flee.png").setAnchor(0.5).setBaseWidth(spriteSize)
                .setBaseHeight(spriteSize).setZIndex(Z_INDEX_SURFACE).setVisible(false);
        debugGroup.add(debug_circle, idleSprite, moveSprite, attackSprite, fleeSprite);
        debugActionSpriteMap.put("IDLE", idleSprite);
        debugActionSpriteMap.put("MOVE", moveSprite);
        debugActionSpriteMap.put("ATTACK", attackSprite);
        debugActionSpriteMap.put("FLEE", fleeSprite);
        robotGroup.add(debugGroup, mouseHitbox);
        viewManager.addDebug(debugGroup);
        viewManager.removeForDebug(robotSprite);
        double scaleX = TARGET_THICKNESS;
        targetSprite = graphicEntityModule.createTilingSprite().setAnchor(0.5)
                .setZIndex(Z_INDEX_TARGETS).setTint(color)//.setAlpha(0.5)
                .setScaleX(scaleX)
                .setTileScaleX(1. / scaleX * TARGET_TILE_SCALE).setVisible(false);
        viewManager.addToArena(targetSprite);
        viewManager.addDebug(targetSprite);
        viewManager.displayOnHoverModule.setDisplayHover(mouseHitbox, new Entity[]{targetSprite, rangeGroup});
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
        Point target = model.getAveragePoint(new HashSet<>(model.getTargets()));

        robotGroup.setX(viewManager.coordToScreen(model.getX()), LINEAR);
        robotGroup.setY(viewManager.coordToScreen(model.getY()), LINEAR);
        shieldBar.setBar(Math.max(0, model.getShieldRatio()));
        healthBar.setBar(Math.max(0, model.getHealthRatio()));
        if (!Objects.equals(viewManager.tooltips.getTooltipText(robotGroup), getTooltip())) {
            viewManager.tooltips.setTooltipText(robotGroup, getTooltip());
        }
        debugActionSpriteMap.forEach((action, sprite) -> {
            sprite.setVisible(model.getLastAction().equals(action));
        });

        attackAnim.update(!model.getLastAction().equals("ATTACK"));
        moveAnim.setActive(!model.getLastAction().equals("ATTACK") && !model.getLastAction().equals("IDLE"));
        moveAnim.update();
        Curve curve = IMMEDIATE;
        try {
            Point pos = target.add(model).divide(2);
            double rotation = Math.PI / 2 + model.getDirection(target).getRotation();
            robotGroup.setRotation(rotation, EASE_OUT);
            double scale = viewManager.getSizeRatio() * (model.getDist(target) / 100.);
            targetSprite.setX(viewManager.coordToScreen(pos.getX()), curve)
                    .setY(viewManager.coordToScreen(pos.getY()), curve)
                    .setScaleY(scale, curve)
                    .setTileScaleY(1. / scale * TARGET_TILE_SCALE, curve)
                    // .setScaleY(scale, curve).setTileScaleY(1. / scale)
                    .setRotation(rotation, curve);
            if (model.getLastAction().equals("MOVE")) {
                targetSprite.setImage("arrowtile.png");
            }
            if (model.getLastAction().equals("FLEE")) {
                targetSprite.setRotation(rotation + Math.PI, curve);
                targetSprite.setImage("arrowtile.png");
            }
            if (model.getLastAction().equals("ATTACK")) {
                targetSprite.setImage(ATTACK_TARGET_SPRITE);
            }
        } catch (ZeroDivisionException ignored) {
            targetSprite.setTileScaleY(0, curve);
        }
        if (model.getLastAction().equals("IDLE")) {
            targetSprite.setTileScaleY(0, curve);
        }

        if (damageTaken > 0) {
//            double t = Math.min(1, damageTaken * HITMARKER_RATIO);
//            Curve hit_curve = hitMarker.isVisible() ? EASE_OUT : IMMEDIATE;
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
        shieldBar.remove();
        healthBar.remove();
        hitMarker.setVisible(false);
        targetSprite.setVisible(false);
        viewManager.displayOnHoverModule.untrack(mouseHitbox);
        idText.setVisible(false);
    }

    @Override
    public Group getSprite() {
        return robotGroup;
    }

    @Override
    public Entity<?> getDebugSprite() {
        return debugGroup;
    }

}

