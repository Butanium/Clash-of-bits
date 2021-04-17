package com.codingame.game;

import java.util.*;

/**
 * 
 */
public class Bullet extends CircularHitBox {

    public static Set<Bullet> bulletSet = new HashSet<>();

    public Bullet(Point shooter, Robot target, boolean willHit, double damage) {
        super(shooter, 0, Constants.BULLET_SPEED);
        this.target = target;
        if (willHit) {
            end = new Point(target);
        } else {
            end = getDeviation(target);
        }

        this.willHit = willHit;
        this.damage = damage;
    }

    private final Point end;
    private final Robot target;
    private static final Random random = new Random();
    private final boolean willHit;

    private Point getDeviation(CircularHitBox target) {
        int sign;
        if(random.nextBoolean()){
            sign = 1;
        } else {
            sign = -1;
        }
        Point newTarget = target.addPoint(getDirection(target).orthogonal().normalize().multiply(
                sign * target.getSize() * (1.01 + Constants.MAX_BULLET_DEVIATION * random.nextDouble())));
        return getDirection(newTarget);
    }

    private final double damage;

    /**
     * @return false if the bullet reached his end position, else true
     */
    public boolean updatePos() {
        if (willHit) {
            if (getDist(end) < Constants.DELTA_TIME * Constants.BULLET_SPEED + target.getSize()) {
                target.takeDamage(damage);
                return false;
            } else {
                setXY(end.addPoint(this.multiply(-1)).normalize().
                        multiply(Constants.DELTA_TIME * Constants.BULLET_SPEED));
                return true;
            }
        } else {
            move((end).multiply(Constants.DELTA_TIME * Constants.BULLET_SPEED));
            if (isInsideMap()){
                return true;
            } else {
                setXY(clampToMap(this));
                return false;
            }
        }

    }




}