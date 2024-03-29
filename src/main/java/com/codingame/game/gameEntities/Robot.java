package com.codingame.game.gameEntities;

import com.codingame.game.*;
import com.codingame.game.gameElements.Bullet;
import com.codingame.game.gameElements.Point;
import view.entitiesSprites.RobotSprite;

import java.util.*;

import static com.codingame.game.Constants.*;


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
    private final RobotType robotType;
    private final Referee referee;
    private RobotSprite sprite;
    /**
     * current state variables
     */
    private double health;
    private double shieldHealth;
    private Robot attackTarget = null;
    private int shotState = 0;
    private int shieldCooldownState = 0;
    private String lastAction = Idle;
    private Set<InGameEntity> lastTargets = new HashSet<>(Collections.singletonList(this));
    private Point nextPosition;

    public Robot(double x, double y, RobotType type, Player owner, Referee referee) {
        super(x, y, type.getSize(), type.getSpeed());
        nextPosition = new Point(x, y);
        this.owner = owner;
        this.referee = referee;
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
        setType(EntityType.ROBOT);
        robotType = type;
    }

    public Robot(Point pos, RobotType type, Player owner, Referee referee) {
        super(pos, type.getSize(), type.getSpeed());
        this.owner = owner;
        this.referee = referee;
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
        setType(EntityType.ROBOT);
        robotType = type;

    }

    public double getHealth() {
        return health;
    }

    public double getShield() {
        return shieldHealth;
    }

    public double getTotVitals() {
        return health + shieldHealth;
    }

    /**
     * @param target : robot you shot at
     */
    public void ATTACK(Robot target) {
        lastAction = Attack;
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
        nextPosition = this;
        attackTarget = target;
        lastTargets.clear();
        lastTargets.add(target);
        shotState = shotState > 0 ? shotState - 1 : aimTime + shotTime;
    }

    /**
     * @param entities : entities the bot has to flee from
     */
    public void FLEE(Set<InGameEntity> entities) {
        lastAction = Flee;
        lastTargets = entities;
        Set<Point> points = new HashSet<>(entities);
        Point target = getAveragePoint(points);
        if (this.getDist(target) == 0) {
            return;
        }
        moveInDirectionNextPos(getDirection(target).multiply(-1), getSpeed() * Constants.DELTA_TIME);
        resetAttack();
    }

    /**
     * @param entities : entities the bot has to move to
     */
    public void MOVE(Set<InGameEntity> entities) {
        lastAction = Move;
        lastTargets = entities;
        Set<Point> points = new HashSet<>(entities);
        shotState = aimTime + shotTime;
        Point target = getAveragePoint(points);
        double frameDist = Constants.DELTA_TIME * getSpeed();
        if (getDist(target) >= frameDist) {
            Point dir = getDirection(target);
            moveInDirectionNextPos(dir, frameDist);
        } else if (getDist(target) > Constants.MOVE_PRECISION) {
            nextPosition = clampToMap(target);
        }
    }

    public void IDLE() {
        resetAttack();
        lastAction = Idle;
        lastTargets.clear();
        lastTargets.add(this);
        nextPosition = this;
    }


    private void resetAttack() {
        shotState = aimTime + shotTime;
        attackTarget = null;
    }

    public void moveInDirection(Point direction, double amount) {
        setXY(clampToMap(add(direction.multiply(amount))));
    }

    public void moveInDirectionNextPos(Point direction, double amount) {
        nextPosition = clampToMap(add(direction.multiply(amount)));
    }

    public void updatePosition() {
        setXY(nextPosition);
    }

//    public boolean performCollision(Robot robot) {
//        if (checkCollide(robot) && !equals(robot)) {
//            Point dir = getDirection(robot);
//            if (currentSpeed.isZero() == robot.currentSpeed.isZero()) {
//                double amount = (getSize() + robot.getSize()) / 2;
//                updatePos(dir, -amount);
//                robot.updatePos(dir, amount);
//            } else if (currentSpeed.isZero()) {
//                robot.updatePos(dir, getSize() + robot.getSize() - getDist(robot));
//            } else if (robot.currentSpeed.isZero()) {
//                updatePos(dir, getDist(robot) - getSize() - robot.getSize());
//            }
//            return true;
//        } else {
//            return false;
//        }
//    }

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
                Referee.debug(String.format("robot %d got destroyed", getId()));
                referee.addToGameSummary(attacker, String.format("%s destroyed bot %d",
                        attacker.getNicknameToken(), this.getId()));

            }
        }
    }

    public int getTeam() {
        return owner.getIndex();
    }

    public Player getPlayer() {
        return owner;
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
    public String giveInfo(int league, Robot asker, int distRank) {
        if (asker.equals(this)) {
            return getSelfSelfInfo(league);
        }
        String shieldComp, healthComp, totComp;
        shieldComp = healthComp = totComp = "0";
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
    public String getSelfInfo(int league, ArrayList<Robot> enemies, int playerId) {
        String healthRank, shieldRank, distEn, distEnRank, totalRank;
        String borderDist, borderDistRank;
        healthRank = shieldRank = distEn = distEnRank = totalRank = "0";
        borderDist = borderDistRank = "0";
        String id = getId() + "";
        String type = playerId == getTeam() ? "ALLY" : "ENEMY";
        String appHealth = getApproximateHealth() + "";
        String appShield = getApproximateShield() + "";
        String actionWithTarg = getLastActionWithTarget();
        if (league > 1) {
            int acc = 3;
            for (Robot enemy : enemies) {
                if (enemy.getId() == this.getId()) {
                    continue;
                }
                acc = Math.min(acc, getRange(enemy));
            }
            distEn = "" + acc;
            borderDist = getRange(getBorderDist()) + "";
        }
        if (league > 2) {
            healthRank = shieldRank = totalRank = borderDistRank = distEnRank = "%d";
        }
        return String.join(" ", id, type, appHealth, appShield, actionWithTarg,
                distEn, borderDist, borderDistRank, distEnRank, healthRank, shieldRank, totalRank);
    }

    public String getSelfSelfInfo(int league) {
        String shieldComp, healthComp, totComp;
        shieldComp = healthComp = totComp = "0";
        String id = getId() + "";
        String type = "ON_AIR";
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
        if (sr == 0) {
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


    public RobotType getRobotType() {
        return robotType;
    }

    public Player getOwner() {
        return owner;
    }

    public double getSpriteSize() {
        return spriteSize;
    }

    public Set<InGameEntity> getTargets() {
        return new HashSet<>(lastTargets);
    }

    public double getMaxHealth() {
        return maxHealth;
    }

    public double getMaxShieldHealth() {
        return maxShieldHealth;
    }

    public String getLastAction() {
        return lastAction;
    }


    public String getStringTargets() {
        List<String> targets = new ArrayList<>();
        for (InGameEntity InGameEntity : lastTargets) {
            targets.add(InGameEntity.getId() + "");
        }
        return String.join(",", targets);
    }

    public String getLastActionWithTarget() {
        return lastAction + " " + getStringTargets();
    }

    public void setRobotSprite(RobotSprite sprite) {
        this.sprite = sprite;
    }

    public RobotSprite getSprite() {
        return sprite;
    }

    public String toString() {
        return "Robot " + getId() % Constants.BOT_PER_PLAYER + " of " + getPlayer().getIndex() + " at : " + super.toString() + " with " + health + " health and " + shieldHealth + " shield";
    }
}