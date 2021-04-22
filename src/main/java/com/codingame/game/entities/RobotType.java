package com.codingame.game.entities;

import java.util.Arrays;

public enum RobotType {
    ASSAULT(5000, 3000, 1.2,0.5,2, new double[]{0.95, 0.55, 0.15, 0}, 3, 4, 2
            , 300, 12, 12);

    private final int maxHealth;
    private final int maxShield;
    private final double speed;
    private final double size;
    private final double spriteSize;
    private final double[] shotRangeProb;
    private final int bulletPerShot;  // number of bullet fired per frame
    private final int aimTime;  // number of frame to aim
    private final int shotTime;  // number of frame during the shot
    private final double damagePerBullet;
    private final int shieldRegenCooldown;
    private final int shieldRegenDuration;


    RobotType(int health, int shield, double speed, double size, double spriteSize,double[] shotRangeProb, int bulletPerShot, int aimTime,
              int shotTime, double damagePerBullet, int shieldRegenCooldown, int shieldRegenDuration) {
        this.maxHealth = health;
        this.maxShield = shield;
        this.speed = speed;
        this.size = size;
        this.spriteSize = spriteSize;
        this.shotRangeProb = shotRangeProb;
        this.bulletPerShot = bulletPerShot;
        this.aimTime = aimTime;
        this.shotTime = shotTime;
        this.damagePerBullet = damagePerBullet;
        this.shieldRegenCooldown = shieldRegenCooldown;
        this.shieldRegenDuration = shieldRegenDuration;
    }

    public int getHealth() {
        return maxHealth;
    }

    public int getShield() {
        return maxShield;
    }

    public double getSpeed() {
        return speed;
    }

    public double[] getShotRangeProb() {
        return shotRangeProb;
    }

    public int getBulletPerShot() {
        return bulletPerShot;
    }

    public int getAimTime() {
        return aimTime;
    }

    public int getShotTime() {
        return shotTime;
    }

    public double getDamagePerBullet() {
        return damagePerBullet;
    }

    public int getShieldRegenCooldown() {
        return shieldRegenCooldown;
    }

    public int getShieldRegenDuration() {
        return shieldRegenDuration;
    }

    public double getSize() {
        return size;
    }

    @Override
    public String toString() {
        switch (this){
            case ASSAULT:
                return "ASSAULT";
        }
        return "OTHER";
    }

    public String infosToString() {
        return "RobotType{" +
                "maxHealth=" + maxHealth +
                ", maxShield=" + maxShield +
                ", speed=" + speed +
                ", shotRangeProb=" + Arrays.toString(shotRangeProb) +
                ", bulletPerShot=" + bulletPerShot +
                ", aimTime=" + aimTime +
                ", shotTime=" + shotTime +
                ", damagePerBullet=" + damagePerBullet +
                ", shieldRegenCooldown=" + shieldRegenCooldown +
                ", shieldRegenDuration=" + shieldRegenDuration +
                '}';
    }


    public double getSpriteSize() {
        return spriteSize;
    }
}
