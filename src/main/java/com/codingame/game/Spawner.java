package com.codingame.game;


import com.codingame.game.exceptions.SpawnFailure;
import com.codingame.game.gameElements.Point;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;

import static com.codingame.game.Constants.*;

public class Spawner {
    private final int teamCount;
    private final int botCount;
    private final long seed;
    private final Random random;
    private boolean symmetry;
    private final Point center = MAP_SIZE.multiply(.5);
    private final double horizontal_y = center.getX();
    private int debug;

    public Spawner(long seed, int botCount, int teamCount) {
        this.teamCount = teamCount;
        this.botCount = botCount;
        this.seed = seed;
        this.random = new Random(seed);
        symmetry = random.nextBoolean();

    }



    public ArrayList<Point>[] getGridSpawnPositions(int league) {
        if (league < 3 ) {
            return getDefaultSpawn();
        }
        debug = 0;
        Point[] grids = new Point[GRID_PER_TEAM];
        ArrayList<Point> availableGrid = new ArrayList(Arrays.asList(GRIDS));
        for (int i = 0; i < GRID_PER_TEAM; i++) {
            Point g = availableGrid.remove(random.nextInt(availableGrid.size()));
            availableGrid.remove(symmetricGrid(g));
            grids[i] = g;
        }

        final ArrayList<Point>[] spawns = getSpawns(grids);
        System.out.printf("spawn succeed after %d tries\n", debug);
        return spawns;

    }

    private ArrayList<Point>[] getSpawns(Point[] grids) {
        if (debug > 2000) {
            return getDefaultSpawn();
        }
        ArrayList<Point>[] result = new ArrayList[teamCount];
        for (int i = 0; i < teamCount; i++) {
            result[i] = new ArrayList<>();
        }
        int[] botPerGrid = new int[GRID_PER_TEAM];
        int acc = botCount;
        for (int i = 0; i < GRID_PER_TEAM; i++) {
            int mini = Math.max(0, acc - (GRID_PER_TEAM - i - 1) * MAX_BOT_PER_GRID);
            int rnd = mini + random.nextInt(Math.min(MAX_BOT_PER_GRID, acc) + 1 - mini);
            acc -= rnd;
            botPerGrid[i] = rnd;
        }
        for (int i = 0; i < GRID_PER_TEAM; i++) {
            for (int j = 0; j < botPerGrid[i]; j++) {
                try {
                Point spawn = getNextSpawn(grids[i], result[0], result[1]);

                result[0].add(spawn);
                result[1].add(symmetric(spawn));
                } catch (SpawnFailure e) {
                    changeGrid(i, grids);
                    return getSpawns(grids);
                }
            }
        }
        return result;
    }

    private void changeGrid(int index, Point[] grids) {
        ArrayList<Point> availableGrid = new ArrayList(Arrays.asList(GRIDS));
        for (int i = 0; i < grids.length; i++) {
            availableGrid.remove(grids[i]);
            if (i != index) {
                availableGrid.remove(symmetricGrid(grids[i]));
            }
        }
        grids[index] = availableGrid.get(random.nextInt(availableGrid.size()));
    }

    private Point symmetricGrid(Point grid) {
        if (symmetry) {
            return grid.centeredSymmetric(center).subtract(GRID_SIZE);
        } else {
            return grid.horizontalSymmetric(horizontal_y).subtract(new Point(0, GRID_SIZE.getY()));
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

    private Point randomPoint(Point grid) {
        double rx = random.nextDouble();
        double ry = random.nextDouble();
        return new Point(rx * INNER_GRID_SIZE.getX() + grid.getX() + GRID_PADDING.getX(),
                ry * INNER_GRID_SIZE.getY() + grid.getY() + GRID_PADDING.getY());
    }

    private boolean check_enemies(Point point, ArrayList<Point> enemies) {
        return point.getDist(symmetric(point)) >= MIN_ENEMY_SPAWN_DIST &&
                enemies.stream().noneMatch(p -> p.getDist(point) < MIN_ENEMY_SPAWN_DIST);
    }

    private boolean check_allies(Point point, ArrayList<Point> allies) {
        return allies.stream().noneMatch(p -> p.getDist(point) < MIN_BOT_SPAWN_DIST);
    }

    private boolean check_border(Point point) {
        return point.getBorderDist() >= MIN_BORDER_DIST;
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
                    (shift - (botCount / 2)) * 2 * Constants.MIN_BOT_SPAWN_DIST, y));
        }
        return spawns;
    }

    private Point getNextSpawn(Point grid, ArrayList<Point> allies, ArrayList<Point> enemies) {
        int debugE = 0, debugA = 0, debugB = 0;
        int d = 0;
        while (d < 150) {
            debug++;
            Point rnd = randomPoint(grid);
            if (!check_enemies(rnd, enemies)) {
                debugE++;
            }
            if (!check_allies(rnd, allies)) {
                debugA++;
            }
            if (!check_border(rnd)) {
                debugB++;
            }
            if (check_enemies(rnd, enemies) && check_allies(rnd, allies) && check_border(rnd)) {
                return rnd;
            }
            d++;
        }
        throw new SpawnFailure();

    }

        /*public ArrayList<Point>[] getSpawnsPosition(int league) {
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
            if (check_enemies(rnd, result[1]) && check_allies(rnd, result[0]) && check_border(rnd)) {
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
    }*/


}
