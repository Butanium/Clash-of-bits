package com.codingame.game;

import com.codingame.game.gameElements.Point;

public class Constants {
    public static final double MOVE_PRECISION = 0.0001;
    public static final int FRAME_DURATION = (int) (Constants.DELTA_TIME * 1000 / 2);
    public static final double MAX_BULLET_DEVIATION = 2;
    public static final double MIN_BULLET_DEVIATION = 0.5;

    //Game\\
    public static final double SHORT_RANGE = 3;
    public static final double MEDIUM_RANGE = 8;
    public static final double LONG_RANGE = 15;
    public static final double DELTA_TIME = 0.25;
    public static final Point MAP_SIZE = new Point(40, 40);
    public static final double BULLET_SPEED = 20;

    //Spawn\\
    public static final int BOT_PER_PLAYER = 7;
    public static final double MIN_SPAWN_DIST = 2;
    public static final double MIN_ENEMY_SPAWN_DIST = MEDIUM_RANGE + 1;

    //View\\
    public static final double BULLET_SIZE = 0.1;
    public static final int HEALTH_BAR_WIDTH = 20;
    public static final int HEALTH_BAR_HEIGHT = 5;
    public static final int BACKGROUND_COLOR = 0x9B9B9B;
    public static final int WALL_COLOR = 0x030056;
    public static final int CAMERA_OFFSET = 8;
    public static final double HITMARKER_RATIO = 1. / 1000.;
    public static final double PADDING = 0.9;
    public static final Point WALL_SIZE = MAP_SIZE.add(new Point(PADDING));



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
}
