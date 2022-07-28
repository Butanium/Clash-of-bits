package com.codingame.game;

import com.codingame.game.gameEntities.Robot;

import java.util.HashMap;
import java.util.PriorityQueue;

public class CollisionManager {
    public static void performCollisions(Robot[] robots) {
        PriorityQueue<Collision> collisions = new PriorityQueue<>();
        boolean[][] collision_map = new boolean[robots.length][robots.length];
        HashMap<Integer, Integer> indexOfId = new HashMap<>();
        for (int i = 0; i < robots.length; i++) {
            indexOfId.put(robots[i].getId(), i);
        }
        // check first collisions
        for (int i = 0; i < robots.length; i++) {
            for (int j = i + 1; j < robots.length; j++) {
                if (!collision_map[i][j] && robots[i].checkCollide(robots[j])) {
                    collisions.add(new Collision(robots[i], robots[j]));
                    collision_map[i][j] = true;
                    collision_map[j][i] = true;
                }
            }
        }
        int debug = 0;
        while (!collisions.isEmpty()) {
            debug++;
            // If there are collisions left
            Collision collision = collisions.poll();
            int index1 = indexOfId.get(collision.getRobot1().getId());
            int index2 = indexOfId.get(collision.getRobot2().getId());
            collision_map[index1][index2] = false;
            collision_map[index2][index1] = false;
            if (collision.performCollision()) {
                // If the collision is performed, check if new collisions are possible
                for (int i = 0; i < robots.length; i++) {
                    if (i != index1 && !collision_map[i][index1] && collision.getRobot1().checkCollide(robots[i])) {
                        collisions.add(new Collision(robots[i], collision.getRobot1()));
                        collision_map[i][index1] = true;
                        collision_map[index1][i] = true;
                    }
                    if (i != index2 && !collision_map[i][index2] && collision.getRobot2().checkCollide(robots[i])) {
                        collisions.add(new Collision(robots[i], collision.getRobot2()));
                        collision_map[i][index2] = true;
                        collision_map[index2][i] = true;
                    }
                }
            }

        }
    }

}
