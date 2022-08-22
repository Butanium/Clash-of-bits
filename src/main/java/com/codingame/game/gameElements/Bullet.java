package com.codingame.game.gameElements;

import com.codingame.game.Constants;
import com.codingame.game.Player;
import com.codingame.game.gameEntities.Robot;
import view.managers.ViewManager;

import java.util.HashSet;
import java.util.Random;
import java.util.Set;

import static com.codingame.game.Constants.MAP_SIZE;

/**
 *
 */
public class Bullet extends CircularHitBox {
    private static Random random;
    public static Set<Bullet> bulletSet = new HashSet<>();
    private final Player owner;
    private final Robot shooter;
    private final Robot target;
    private final double damage;
    private boolean hasExplode;
    private boolean isInstanced = false;
    private boolean willHit;
    private Point direction;
    private Point deviation;

    public static void initiateRandom(long seed) {
        random = new Random(seed);
    }

    public Bullet(Robot shooter, Robot target, boolean willHit, double damage) {
        super(shooter, 0, Constants.BULLET_SPEED);
        this.shooter = shooter;
        this.target = target;
        deviation = getDeviation(target, willHit ? 0 : Math.max(1.5 * target.getSize(), Constants.MIN_BULLET_DEVIATION),
                willHit ? target.getSize() * 0.75 : Constants.MAX_BULLET_DEVIATION);
        direction = willHit ? shooter.getDirectionNoEx(target.add(deviation)) : getTargetWithDeviation(target, deviation);
        this.willHit = willHit;
        this.damage = damage;
        owner = shooter.getOwner();

    }

    private Point getTargetWithDeviation(CircularHitBox target, Point deviation) {
        if (getDist(target) == 0) {
            return new Point();
        }
        Point newTarget = target.add(deviation);
        return getDirection(newTarget);
    }

    private Point getDeviation(CircularHitBox target, double minDeviation, double maxDeviation) {
        int sign = random.nextBoolean() ? 1 : -1;
        double rnd = random.nextDouble();
        return getDirection(target).orthogonal().normalize().multiply(
                target.getSize() * sign * (maxDeviation * rnd +
                        (1 - rnd) * minDeviation));
    }

    public boolean updatePos(ViewManager viewManager) {
        if (!isInstanced) {
            viewManager.instantiateBullet(this,
                    shooter.getDirectionNoEx(target).multiply(shooter.getRobotType().getCanon_size()));
            isInstanced = true;
        }
        if (!target.checkActive()) {
            this.willHit = false;
        }
        if (willHit) {
            direction = getDirectionNoEx(target.add(deviation));
            if (getDist(target) < Constants.DELTA_TIME * Constants.BULLET_SPEED + target.getSize()) {
                target.takeDamage(damage, owner);
                hasExplode = true;
                setXY(target.add(deviation));
                return true;
            } else {
                move(direction.multiply(Constants.DELTA_TIME * Constants.BULLET_SPEED));
            }
        } else {
            move(direction.multiply(Constants.DELTA_TIME * Constants.BULLET_SPEED));
            if (!isInsideMap()) {
                if (getX() >= 0 && MAP_SIZE.getX() - getX() > 0) {
                    int s = getY() < 0 ? 1 : -1;
                    move(direction.multiply(s / direction.getY() * getBorderDistOut()));
                } else {
                    int s = getX() < 0 ? 1 : -1;
                    move(direction.multiply(s / direction.getX() * getBorderDistOut()));
                }
                hasExplode = true;
                return true;
                //Referee.debug(String.format("ball hit at : %f, %f", getX(), getY()));
            }
        }
        return false;
    }

    public boolean isActive() {
        return !hasExplode;
    }

    public Player getOwner() {
        return owner;
    }

    public boolean willHit() {
        return willHit;
    }

    public Robot getTarget() {
        return target;
    }

    public double getDamage() {
        return damage;
    }

    public Point getDirection() {
        return direction;
    }

}