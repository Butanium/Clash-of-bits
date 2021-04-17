package com.codingame.game;

import java.util.*;


public class Robot extends Entity {


    public Robot(double x, double y, RobotType type, Player owner) {
        super(x, y, type.getSize(), type.getSpeed());
        this.owner = owner;
        maxHealth = type.getHealth();
        maxShieldHealth = type.getShield();
        shotRangeProb = type.getShotRangeProb();
        bulletPerShot = type.getBulletPerShot();
        aimTime = type.getAimTime();
        shotTime = type.getShotTime();
        damagePerBullet = type.getDamagePerBullet();
        shieldRegenCooldown = type.getShieldRegenCooldown();
        shieldRegenDuration = type.getShieldRegenDuration();

        shieldRegenPerFrame = maxShieldHealth / shieldRegenDuration;
        health = maxHealth;
        shieldHealth = maxShieldHealth;
        currentSpeed = new Point();
    }

    /**
     * current state variables
     */
    private double health;
    private double shieldHealth;
    private Robot attackTarget = null;
    private int shotState = 0;
    private int shieldCooldownState = 0;
    private Point currentSpeed;
    /**
     * robot class parameters
     */
    private final double maxHealth;
    private final double maxShieldHealth;
    private final double[] shotRangeProb;
    private final int bulletPerShot;  // number of bullet fired per frame
    private final int aimTime;  // number of frame to aim
    private final int shotTime;  // number of frame during the shot
    private final double damagePerBullet;
    private final int shieldRegenCooldown;
    private final int shieldRegenDuration;

    private final double shieldRegenPerFrame;
    private final Player owner;

    public double getHealth() {
        return health;
    }

    public double getShield() {
        return shieldHealth;
    }

    /**
     * @param target : robot you shot at
     */
    public void ATTACK(Robot target) {
        // TODO implement here
        if (target != this.attackTarget || shotState == 0){
            shotState = aimTime + shotTime -1;
        } else {
            if (shotState <= aimTime){
                for (int i = 0; i < bulletPerShot; i++){
                    Bullet bullet = new Bullet(this, target,
                            owner.getRNG().nextDouble() < shotRangeProb[getRange(target)], damagePerBullet);
                    Bullet.bulletSet.add(bullet);
                }
            }
        }
    }

    /**
     * @param entities : entities the bot has to flee from
     */
    public void FLEE(Set<Point> entities) {
        Point target = getAveragePoint(entities);
        updatePos(getDirection(target).multiply(-1), getSpeed()*Constants.DELTA_TIME);
        restAttack();

    }

    private void restAttack(){
        shotState = aimTime + shotTime;
        attackTarget = null;
    }
    /**
     * @param entities : entities the bot has to move to
     */
    public void MOVE(Set<Point> entities) {
        shotState = aimTime + shotTime;
        Point target = getAveragePoint(entities);
        double frameDist = Constants.DELTA_TIME*getSpeed();
        if (getDist(target) >= frameDist) {
            Point dir = getDirection(target);
            updatePos(dir, frameDist);
        } else if (getDist(target) > Constants.MOVE_PRECISION) {
            setXY(clampToMap(target));
        }
    }



    public void updatePos(Point direction, double amount) {
        setXY(clampToMap(addPoint(direction.multiply(amount))));
    }

    public boolean performCollision(Robot robot) {
        if (checkCollide(robot) && !equals(robot)) {
            Point dir = robot.addPoint(this.multiply(-1)).normalize();
            if (currentSpeed.isZero() == robot.currentSpeed.isZero()) {
                double amount = (getSize() + robot.getSize()) / 2;
                updatePos(dir, -amount);
                robot.updatePos(dir, amount);
            } else if (currentSpeed.isZero()) {
                robot.updatePos(dir, getSize() + robot.getSize() - getDist(robot));
            } else if (robot.currentSpeed.isZero()) {
                updatePos(dir, getDist(robot) -getSize() - robot.getSize());
            }
            return true;
        } else {
            return false;
        }
    }

    /**
     * @param amount : amount of damage taken
     */
    public void takeDamage(double amount) {
        shieldCooldownState = shieldRegenCooldown;
        if (amount < shieldHealth) {
            shieldHealth -= amount;
        } else {
            health -= amount - shieldHealth;
            shieldHealth = 0;
            if (health <= 0) {
                setActive(false);
            }
        }
    }

    public int getTeam() {
        return owner.getIndex();
    }

    public void updateShield() {
        if (shieldHealth != maxShieldHealth) {
            if (shieldCooldownState == 0) {
                shieldHealth = Math.min(maxShieldHealth, shieldHealth + shieldRegenPerFrame);
            } else {
                shieldCooldownState--;
            }
        }
    }

}