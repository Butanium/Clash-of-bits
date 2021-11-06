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
    private final double horizontal_x = center.getX();

    public Spawner(long seed, int botCount, int teamCount) {
        this.teamCount = teamCount;
        this.botCount = botCount;
        this.seed = seed;
        this.random = new Random(seed);
        symmetry = random.nextBoolean();

    }

    public ArrayList<Point>[] getSpawnsPosition() {
        ArrayList<Point>[] result = new ArrayList[2];
        int c = 0;
        while (c < botCount) {
            Point rnd = randomPoint();

        }

        return result;
    }

    private Point symmetric(Point p) {
        if (symmetry) {
            return p.centeredSymmetric(center);
        } else {
            return p.horizontalSymmetric(horizontal_x);
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



}
