package com.codingame.game;

public enum RobotType {
    ASSAULT(500, 300, 1.2,0.5, new double[]{0.95, 0.55, 0.15, 0}, 6, 4, 1
            , 300, 12, 12);

    private final int maxHealth;
    private final int maxShield;
    private final double speed;
    private final double size;
    private final double[] shotRangeProb;
    private final int bulletPerShot;  // number of bullet fired per frame
    private final int aimTime;  // number of frame to aim
    private final int shotTime;  // number of frame during the shot
    private final double damagePerBullet;
    private final int shieldRegenCooldown;
    private final int shieldRegenDuration;

    RobotType(int health, int shield, double speed, double size, double[] shotRangeProb, int bulletPerShot, int aimTime,
              int shotTime, double damagePerBullet, int shieldRegenCooldown, int shieldRegenDuration) {
        this.maxHealth = health;
        this.maxShield = shield;
        this.speed = speed;
        this.size = size;
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
}
