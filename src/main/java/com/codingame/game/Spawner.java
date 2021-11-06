package com.codingame.game;


import com.codingame.game.gameElements.Point;

import java.util.ArrayList;
import java.util.Random;

import static com.codingame.game.Constants.*;

public class Spawner {
    private final int teamCount;
    private final int botCount;
    private final long seed;
    private final Random random;
    private final boolean symmetry;
    private final Point center = MAP_SIZE.multiply(.5);
    private final double horizontal_y = center.getX();

    public Spawner(long seed, int botCount, int teamCount) {
        this.teamCount = teamCount;
        this.botCount = botCount;
        this.seed = seed;
        this.random = new Random(seed);
        symmetry = random.nextBoolean();

    }

    public ArrayList<Point>[] getSpawnsPosition(int league) {
        if (league < 3) {
            return getDefaultSpawn();
        }
        ArrayList<Point>[] result = new ArrayList[teamCount];
        for (int i = 0; i < teamCount; i++) {
            result[i] = new ArrayList<>();
        }
        int c = 0;
        int debug = 0;
        while (c < botCount && debug < 2000) {
            Point rnd = randomPoint();
            if (check_enemies(rnd, result[1]) && check_allies(rnd, result[0])) {
                c++;
                result[0].add(rnd);
                result[1].add(symmetric(rnd));

            }
            debug++;
        }
        System.out.printf("spawn %s after %d tries\n", c == botCount ? "succeed" : "failed", debug);
        if (c == botCount) {
            return result;
        } else {
            return getDefaultSpawn();
        }
    }

    private Point symmetric(Point p) {
        if (symmetry) {
            return p.centeredSymmetric(center);
        } else {
            return p.horizontalSymmetric(horizontal_y);
        }
    }

    private Point randomPoint() {
        double rx = random.nextDouble();
        double ry = random.nextDouble();
        return new Point(rx * MAP_SIZE.getX(), ry * MAP_SIZE.getY());
    }

    private boolean check_enemies(Point point, ArrayList<Point> enemies) {
        return point.getDist(symmetric(point)) >= MIN_ENEMY_SPAWN_DIST &&
                enemies.stream().noneMatch(p -> p.getDist(point) < MIN_ENEMY_SPAWN_DIST);
    }

    private boolean check_allies(Point point, ArrayList<Point> allies) {
        return allies.stream().noneMatch(p -> p.getDist(point) < MIN_SPAWN_DIST);
    }


    private ArrayList<Point>[] getDefaultSpawn() {
        double y = (Constants.MAP_SIZE.getY() - Constants.LONG_RANGE) / 2 - 1;
        return new ArrayList[]{getDefaultTeamSpawn(y), getDefaultTeamSpawn(Constants.MAP_SIZE.getY() - y)};
    }

    private ArrayList<Point> getDefaultTeamSpawn(double y) {
        ArrayList<Point> spawns = new ArrayList<>();
        for (int i = 0; i < botCount; i++) {
            int shift = botCount % 2 == 0 && i > botCount / 2 ? 1 + i : i;
            spawns.add(new Point(Constants.MAP_SIZE.getX() / 2 +
                    (shift - (botCount / 2)) * 2 * Constants.MIN_SPAWN_DIST, y));
        }
        return spawns;
    }


}
