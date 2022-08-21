package com.codingame.game;

import com.codingame.game.gameElements.Point;

public class Constants {

    //Game\\
    public static final String Attack = "ATTACK";
    public static final String Idle = "IDLE";
    public static final String Flee = "FLEE";
    public static final String Move = "MOVE";
    public static final double MOVE_PRECISION = 0.0001;
    public static final double INVERSE_MOVE_PRECISION = 1 / MOVE_PRECISION;
    public static final double COLLISION_PRECISION = MOVE_PRECISION;
    public static final double MAX_BULLET_DEVIATION = 2;
    public static final double MIN_BULLET_DEVIATION = 0.5;
    public static final double SHORT_RANGE = 3;
    public static final double MEDIUM_RANGE = 8;
    public static final double LONG_RANGE = 15;
    public static final double[] RANGES = new double[]{
            SHORT_RANGE, MEDIUM_RANGE, LONG_RANGE
    };
    public static final double DELTA_TIME = 0.25;
    public static final int FRAME_DURATION = (int) (Constants.DELTA_TIME * 1000 / 2);
    public static final Point MAP_SIZE = new Point(40);
    public static final double BULLET_SPEED = 20;

    //Spawn\\
    public static final int BOT_PER_PLAYER = 7;
    public static final double MIN_BOT_SPAWN_DIST = 2;
    public static final double MIN_BORDER_DIST = 2;
    public static final double MIN_ENEMY_SPAWN_DIST = MEDIUM_RANGE + 0.1;
    public static final Point GRID_SIZE = new Point(10, 10);
    public static final Point INNER_GRID_SIZE = new Point(6.8, 6.8);
    public static final Point GRID_PADDING = GRID_SIZE.subtract(INNER_GRID_SIZE);
    public static final int GRID_X_COUNT = (int) (MAP_SIZE.getX() / GRID_SIZE.getX());
    public static final int GRID_Y_COUNT = (int) (MAP_SIZE.getY() / GRID_SIZE.getY());
    public static final int TOTAL_GRID_COUNT = GRID_X_COUNT * GRID_Y_COUNT;
    public static final Point[] GRIDS = getGrids();
    public static final int GRID_PER_TEAM = 3;
    public static final int MAX_BOT_PER_GRID = 4;


    //View\\
    // viewParts settings
    public static final double BULLET_SIZE = 0.1;
    public static final double BULLET_SCALE = 0.3;

    public static final String BULLET_SPRITE = "bbr.png";

    public static final String ATTACK_TARGET_SPRITE = "sword.png";
    public static final int BULLET_ANIMATION_DURATION = 25; // ms
    // crater
    public static final double CRATER_ALPHA = 0.8;
    public static final double CRATER_SIZE = 1;
    public static final double CRATER_HITBOX_SIZE = .6;
    public static final String CRATER_IMAGE = "c.png";


    // UI settings
    public static final int HEALTH_BAR_WIDTH = 20;
    public static final int HEALTH_BAR_HEIGHT = 5;
    public static final int CAMERA_OFFSET = 8;
    public static final double TARGET_TILE_SCALE = 0.1 / 1.5; // 150 px size
    public static final double TARGET_THICKNESS = 0.1;
    /*  h3 params :
        public static final String HITMARKER_IMAGE = "h3.png";
        public static final double HITMARKER_SIZE = .5;
        public static final double HITMARKER_ANGLE = Math.PI / 4;
    */
    public static final String HITMARKER_IMAGE = "hb.png";
    public static final double HITMARKER_SIZE = .1; // 0/1 for default
    public static final double HITMARKER_ANGLE = 0;

    // Arena settings
    public static final String ARENA_TILE_IMAGE = "s.png";
    public static final String BACKGROUND_TILE_IMAGE = "sb.png";
    public static final double ARENA_TILE_SCALE = 3.2 * .2;
    public static final double BACKGROUND_TILE_SCALE = 2 * .2;
    public static final double WALL_TILE_SCALE = 0.5;
    public static final double WALL_THICKNESS = .3;
    public static final Point WALL_SIZE = MAP_SIZE.add(WALL_THICKNESS * 2);
    public static final double ARENA_PADDING = 1;
    public static final Point ARENA_SIZE = WALL_SIZE.add(ARENA_PADDING * 2);

    // Zindexs
    public static final int Z_INDEX_BACKGROUND = 0;
    public static final int Z_INDEX_ARENA_FLOOR = Z_INDEX_BACKGROUND + 1;
    public static final int Z_INDEX_CRATER = Z_INDEX_ARENA_FLOOR + 1;
    public static final int Z_INDEX_WALL0 = Z_INDEX_CRATER + 1;
    public static final int Z_INDEX_WALLS = Z_INDEX_WALL0 + 1;
    public static final int Z_INDEX_RANGES = Z_INDEX_WALLS + 1;
    public static final int Z_INDEX_TARGETS = Z_INDEX_RANGES + 1;
    public static final int Z_INDEX_ROBOTS = Z_INDEX_TARGETS + 1;
    public static final int Z_INDEX_EXPLOSIONS = Z_INDEX_ROBOTS + 1;
    public static final int Z_INDEX_BULLETS = Z_INDEX_EXPLOSIONS + 1;
    public static final int Z_INDEX_HITMARKER = Z_INDEX_BULLETS + 1;
    public static final int Z_INDEX_ID = Z_INDEX_HITMARKER + 1;
    public static final int Z_INDEX_BARS = Z_INDEX_ID + 1;


    // Utils
    public static final int[] playerColors =
            new int[]{
                    0xff0000, // radical red
                    0x0f6a00, // green
                    0xff8f16, // west side orange
                    0x0254ff, // blue
                    0x9975e2, // medium purple
                    0x3ac5ca, // scooter blue
                    0xde6ddf, // lavender pink
            };
    public static int GLOBAL_ID = 0;

    public static int getPlayerColor(int player) {
        return playerColors[player];
    }

    private static Point[] getGrids() {
        Point[] result = new Point[TOTAL_GRID_COUNT];
        int i = 0;
        for (double x = 0; x < MAP_SIZE.getX(); x += GRID_SIZE.getX()) {
            for (double y = 0; y < MAP_SIZE.getY(); y += GRID_SIZE.getY()) {
                result[i++] = new Point(x, y);
            }
        }
        return result;
    }
}
