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
     * @param entity
     */
    public void move(ArrayList<Point> entity) {
        // TODO implement here
    }

    /**
     * @param entity
     */
    public void flee(Set<Point> entity) {
        double x = 0;
        double y = 0;
        for (Point p : entity) {
            x += p.getX();
            y += p.getY();
        }
        Point target = new Point(x, y);
        if(!(getDist(target) < Constants.MOVE_PRECISION)) {

        }
    }

    private void updatePos(Point direction) {

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
        }
        else {
            health -= amount - shieldHealth;
            shieldHealth = 0;
            if (health <= 0){
                setActive(false);
            }
        }
    }

    public int getTeam() {
        return teamId;
    }

}