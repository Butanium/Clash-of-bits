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
    private static final Random random = new Random();
    public static Set<Bullet> bulletSet = new HashSet<>();
    private final Player owner;
    private final Robot shooter;
    private final Robot target;
    private final double damage;
    private boolean hasExplode;
    private boolean isInstanced = false;
    private boolean willHit;
    private Point direction;

    public Bullet(Robot shooter, Robot target, boolean willHit, double damage) {
        super(shooter, 0, Constants.BULLET_SPEED);
        this.shooter = shooter;
        this.target = target;
        direction = willHit ? shooter.getDirectionNoEx(target) : getDeviation(target);

        this.willHit = willHit;
        this.damage = damage;
        owner = shooter.getOwner();

    }

    private Point getDeviation(CircularHitBox target) {
        int sign = random.nextBoolean() ? 1 : -1;
        double rnd = random.nextDouble();
        if (getDist(target) == 0) {
            return new Point();
        }
        Point newTarget = target.add(getDirection(target).orthogonal().normalize().multiply(
                target.getSize() * sign * (Constants.MAX_BULLET_DEVIATION * rnd +
                        (1 - rnd) * Constants.MIN_BULLET_DEVIATION)));
        return getDirection(newTarget);
    }

    public boolean updatePos(ViewManager viewManager) {
        //Referee.debug(String.format("bullet fired at %f, %f ",getX(),getY()));
        if (!isInstanced) {
            viewManager.instantiateBullet(this,
                    shooter.getDirectionNoEx(target).multiply(shooter.getRobotType().getCanon_size()));
            isInstanced = true;
        }
        if (!target.checkActive()) {
            this.willHit = false;
        }
        if (willHit) {
            direction = getDirectionNoEx(target);
            if (getDist(target) < Constants.DELTA_TIME * Constants.BULLET_SPEED + target.getSize()) {
                target.takeDamage(damage, owner);
                hasExplode = true;
                setXY(target);
                return true;
            } else {
                move(direction.
                        multiply(Constants.DELTA_TIME * Constants.BULLET_SPEED));
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