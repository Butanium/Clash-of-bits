package com.codingame.game.entities;

import com.codingame.game.*;

import java.util.*;


public class Robot extends InGameEntity {


    /**
     * robot class parameters
     */
    private final double spriteSize;
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
    /**
     * current state variables
     */
    private double health;
    private double shieldHealth;
    private Robot attackTarget = null;
    private int shotState = 0;
    private int shieldCooldownState = 0;
    private Point currentSpeed;
    private String lastAction = "IDLE";
    private Set<InGameEntity> lastTargets = new HashSet<>(Collections.singletonList(this));
    private final String robotType;

    public Robot(double x, double y, RobotType type, Player owner) {
        super(x, y, type.getSize(), type.getSpeed());
        this.owner = owner;
        spriteSize = type.getSpriteSize();
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
        setType(EntityType.ROBOT);
        robotType = type.toString();
    }

    public Robot(Point pos, RobotType type, Player owner) {
        super(pos, type.getSize(), type.getSpeed());
        this.owner = owner;
        spriteSize = type.getSpriteSize();
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
        setType(EntityType.ROBOT);
        robotType = type.toString();

    }

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
        lastAction = "ATTACK";
        if (target != this.attackTarget || shotState == 0) {
            shotState = aimTime + shotTime;
        } else {
            if (shotState <= shotTime) {
                for (int i = 0; i < bulletPerShot; i++) {
                    Bullet bullet = new Bullet(this, target,
                            owner.getRNG().nextDouble() < shotRangeProb[getRange(target)], damagePerBullet);
                    Bullet.bulletSet.add(bullet);
                }
            }
        }
        attackTarget = target;
        lastTargets.clear();
        lastTargets.add(target);
        shotState = shotState > 0 ? shotState - 1 : aimTime + shotTime;
    }

    /**
     * @param entities : entities the bot has to flee from
     */
    public void FLEE(Set<InGameEntity> entities) {
        lastAction = "FLEE";
        lastTargets = entities;
        Set<Point> points = new HashSet<>(entities);
        Point target = getAveragePoint(points);
        updatePos(getDirection(target).multiply(-1), getSpeed() * Constants.DELTA_TIME);
        restAttack();
    }

    /**
     * @param entities : entities the bot has to move to
     */
    public void MOVE(Set<InGameEntity> entities) {
        lastAction = "MOVE";
        lastTargets = entities;
        Set<Point> points = new HashSet<>(entities);
        shotState = aimTime + shotTime;
        Point target = getAveragePoint(points);
        double frameDist = Constants.DELTA_TIME * getSpeed();
        if (getDist(target) >= frameDist) {
            Point dir = getDirection(target);
            updatePos(dir, frameDist);
        } else if (getDist(target) > Constants.MOVE_PRECISION) {
            setXY(clampToMap(target));
        }
    }

    public void IDLE() {
        restAttack();
        currentSpeed = new Point();
        lastAction = "IDLE";
        lastTargets.clear();
        lastTargets.add(this);
    }


    private void restAttack() {
        shotState = aimTime + shotTime;
        attackTarget = null;
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
                updatePos(dir, getDist(robot) - getSize() - robot.getSize());
            }
            return true;
        } else {
            return false;
        }
    }

    /**
     * @param amount : amount of damage taken
     */
    public void takeDamage(double amount, Player attacker) {
        if (health <= 0) {
            return;
        }
        shieldCooldownState = shieldRegenCooldown;
        if (amount < shieldHealth) {
            shieldHealth -= amount;
        } else {
            health -= amount - shieldHealth;
            shieldHealth = 0;
            if (health <= 0) {
                setActive(false);
                attacker.setScore(attacker.getScore() + 1);
                Referee.debug(String.format("%d robot got destroyed", getId()));
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

    public double getHealthRatio() {
        return health / maxHealth;
    }

    public double getShieldRatio() {
        return shieldHealth / maxShieldHealth;
    }

    @Override
    public String giveInfo(int league, Robot asker, int distRank, Set<Robot> enemies) {
        if (asker.equals(this)) {
            return getSelfSelfInfo(league);
        }
        String shieldComp, healthComp, totComp;
        shieldComp = healthComp = totComp = "";
        String id = getId() + "";
        String type = asker.getTeam() == getTeam() ? "ALLY" : "ENEMY";
        String distToAsker = getRange(asker) + "";
        String distAskerRank = distRank + "";
        if (league > 1) {
            shieldComp = asker.compareShield(this) + "";
            healthComp = asker.compareHealth(this) + "";
            totComp = asker.compareTotal(this) + "";
        }
        return String.join(" ", id, type, distToAsker, distAskerRank,
                shieldComp, healthComp, totComp);
    }

    @Override
    public String getSelfInfo(int league, Set<Robot> enemies, int playerId) {
        String healthRank, shieldRank, distEn, distEnRank, totalRank; //ok
        String borderDist, borderDistRank;
        healthRank = shieldRank = distEn = distEnRank = totalRank = "";
        borderDist = borderDistRank = "";
        String id = getId() + "";
        String type = playerId == getTeam() ? "ALLY" : "ENEMY";
        String appHealth = getApproximateHealth() + "";
        String appShield = getApproximateShield() + "";
        String actionWithTarg = getLastActionWithTarget();
        if (league > 1) {
            int acc = 3;
            for (Robot enemy : enemies) {
                acc = Math.min(acc, getRange(enemy));
            }
            distEn = "" + acc;
            borderDist = getRange(getBoarderDist()) + "";
        }
        if (league > 2) {
            healthRank = shieldRank = totalRank = borderDistRank = distEnRank = "%d";
        }
        return String.join(" ", id, type, appHealth, appShield, actionWithTarg,
                distEn, borderDist, borderDistRank, distEnRank, healthRank, shieldRank, totalRank);
    }

    public String getSelfSelfInfo(int league) {
        String shieldComp, healthComp, totComp;
        shieldComp = healthComp = totComp = "";
        String id = getId() + "";
        String type = "SELF";
        String distMe = "0";
        String distMeRank = "1";
        if (league > 1) {
            shieldComp = healthComp = totComp = "0";
        }
        return String.join(" ", id, type, distMe, distMeRank,
                shieldComp, healthComp, totComp);
    }

    public int getApproximateHealth() {
        double hr = getHealthRatio();
        if (hr < .25) {
            return 0;
        }
        if (hr < .5) {
            return 25;
        }
        if (hr < .75) {
            return 50;
        }
        if (hr < 1) {
            return 75;
        }
        return 100;
    }

    public int getApproximateShield() {
        double sr = getShieldRatio();
        if (sr < .01) {
            return 0;
        }
        if (sr < .25) {
            return 1;
        }
        if (sr < .5) {
            return 25;
        }
        if (sr < .75) {
            return 50;
        }
        if (sr < 1) {
            return 75;
        }
        return 100;
    }

    public String getLastActionWithTarget() {
        List<String> targets = new ArrayList<>();
        for (InGameEntity InGameEntity : lastTargets) {
            targets.add(InGameEntity.getId() + "");
        }
        return lastAction + " " + String.join(";", targets);
    }

    public int compareHealth(Robot target) {
        if (target.health > this.health) {
            return -1;
        } else if (target.health == this.health) {
            return 0;
        }
        return 1;
    }

    public int compareShield(Robot target) {
        if (target.shieldHealth > this.shieldHealth) {
            return -1;
        } else if (target.shieldHealth == this.shieldHealth) {
            return 0;
        }
        return 1;
    }

    public int compareTotal(Robot target) {
        if (target.health + target.shieldHealth > this.health + this.shieldHealth) {
            return -1;
        } else if (target.health + target.shieldHealth == this.health + this.shieldHealth) {
            return 0;
        }
        return 1;
    }

    public InGameEntity getClosestEntity(Set<InGameEntity> entities) {
        Set<InGameEntity> InGameEntitySet = new HashSet<>(entities);
        InGameEntitySet.remove(this);
        Optional<InGameEntity> res = InGameEntitySet.stream().min(Comparator.comparingDouble(this::getDist));
        return res.orElse(null);
    }

    public String getRobotType() {
        return robotType;
    }

    public Player getOwner() {
        return owner;
    }

    public double getSpriteSize() {
        return spriteSize;
    }

    public Set<Point> getTargets() {
        return new HashSet<>(lastTargets);
    }
}