package com.codingame.game;

import java.util.*;


public class Robot extends Entity {


    public Robot() {
    }


    private double health;

    private double shieldHealth;

    private double maxHealth;

    private double maxShieldHealth;

    /**
     * number of frame to aim
     */
    private int aimTime;


    private double speed;


    private Robot attackTarget;


    private int shotState;


    private double[] shotRangeProb;

    /**
     * number of bullet fired per frame
     */
    private int bulletPerShot;

    /**
     * number of frame during the shot
     */
    private int shotTime;


    private double damagePerBullet;


    private double shieldRegenPerFrame;


    private int shieldRegenCooldown;


    private int shieldState;


    private int teamId;

    /**
     * @return
     */
    public double getHealth() {
        // TODO implement here
        return 0.0f;
    }

    /**
     * @return
     */
    public double getShield() {
        // TODO implement here
        return 0.0f;
    }

    /**
     * @param targ
     */
    public void attack(Robot targ) {
        // TODO implement here
    }

    /**
     * @param entities : entities the bot has to flee from
     */
    public void flee(Set<Point> entities) {
        Point target = getAveragePoint(entities);
        updatePos(getDirection(target).multiply(-1));
    }

    /**
     * @param entities : entities the bot has to move to
     */
    public void move(Set<Point> entities) {
        Point target = getAveragePoint(entities);
        if (getDist(target) >= Constants.MOVE_PRECISION) {
            updatePos(getDirection(target));
        }
    }

    private void updatePos(Point direction) {
        Point s = new Point(getSize());
        setXY(addPoint(direction.multiply(Constants.DELTA_TIME * speed)).
                clamp(s, Constants.MAP_SIZE.addPoint(s.multiply(-1))));


    }

    /**
     * @param robot
     */
    public void checkCollision(Robot robot) {
        // TODO implement here
    }

    /**
     * @param amount : amount of damage taken
     */
    public void takeDamage(double amount) {
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
        return teamId;
    }

}