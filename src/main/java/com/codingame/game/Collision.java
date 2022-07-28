package com.codingame.game;

import com.codingame.game.gameElements.Point;
import com.codingame.game.gameEntities.Robot;

public class Collision implements Comparable<Collision> {
    private final Robot robot1;
    private final Robot robot2;

    public Collision(Robot robot1, Robot robot2) {
        this.robot1 = robot1;
        this.robot2 = robot2;
        assert (robot1.getId() != robot2.getId()); // a robot cannot collide with itself
    }

    public Integer getMaxId() {
        return Math.max(robot1.getId() % Constants.BOT_PER_PLAYER, robot2.getId() % Constants.BOT_PER_PLAYER);
    }

    public Integer getMinId() {
        return Math.min(robot1.getId() % Constants.BOT_PER_PLAYER, robot2.getId() % Constants.BOT_PER_PLAYER);
    }

    @Override
    public int compareTo(Collision collision) {
        int a = collision.getMinId() - this.getMinId();
        // If a > 0 that means that this collision has a higher priority than collision
        int b = collision.getMaxId() - this.getMaxId();
        return a == 0 ? -b : -a;
        // "-" because we use this in a priority queue : the least element will have the highest priority
    }

    public boolean performCollision() {
        if (robot1.checkCollide(robot2)) {
            Point dir = robot1.getDirection(robot2);
            double amount = (robot1.getSize() + robot2.getSize() - robot1.getDist(robot2)) / 2;
            robot1.moveInDirection(dir, -amount);
            robot2.moveInDirection(dir, amount);
            return true;
        } else {
            return false;
        }
    }

    public Robot getRobot1() {
        return robot1;
    }

    public Robot getRobot2() {
        return robot2;
    }
}
