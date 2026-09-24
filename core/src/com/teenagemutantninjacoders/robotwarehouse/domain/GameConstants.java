package com.teenagemutantninjacoders.robotwarehouse.domain;

/**
 * Created by Kusfo on 07/03/2015.
 */
public  class GameConstants {
    public static int SAVED_GAME_VERSION = 2;
    public static String CLOUD_GAME_ID = "CLOUDSAVE";
    //public static String CLOUD_GAME_ID = "CLOUDSAVE_DEV"; //Debug Development
    public static int HORIZONTAL_RESOLUTION = 640;
    public static int VERTICAL_RESOLUTION = 384; //360;
    public static int VIRTUAL_HORIZONTAL_RESOLUTION = 640;
    public static int VIRTUAL_VERTICAL_RESOLUTION = 384; //360;

    public static int BOARD_COLUMNS = 18;
    public static int BOARD_ROWS = 12;
    public static int BOARD_ORIGIN_X = 81;//85;
    public static int BOARD_ORIGIN_Y = VERTICAL_RESOLUTION;// + 4

    public static int CELL_WIDTH = 32;
    public static int CELL_HEIGHT = 32;
    public static int CELL_SIDE = 10;      // Altura en perspectiva de los bloques
    public static int Y_DIRECTION = -1;

    public static int GAMEZONE_X_CENTER = 385;

    public static int ABOVE_LIGHT_DEPTH = -4000;
    public static int ABOVE_ALL_DEPTH = -3000;
    public static int SPACESHIP_DEPTH = -1000;
    public static int ABOVE_DEPTH = 1000;
    public static int WALL_DEPTH = 1000;
    public static int OVERBASE_DEPTH = 1032;//1010 1900;
    public static int FLOOR_DEPTH = 1033;//2000;
    public static int ABYSS_DEPTH = 1100;
    public static int BOARD_EFFECT = -1500;

    public static int FLOOR_OBSTACLE_LEVEL = 0;
    public static int WALL_OBSTACLE_LEVEL = 10;
    public static int BOX_OBSTACLE_LEVEL = 5;
    public static int ENEMY_OBSTACLE_LEVEL = 2;

    public static int BASE_BOX_POINTS = 25;
    public static int BASE_TIME_POINTS = 5;
    public static int BASE_SAVE_BOXES_POINTS = 20;
    public static int BASE_LOST_BOXES_POINTS = 20;
    public static int BASE_OVERBASE_POINTS = 50;

    public static final int CONTENT_STATUS_BLOCKED = 0;
    public static final int CONTENT_STATUS_UNBLOCKED = 1;
    public static final int CONTENT_STATUS_COMPLETED = 2;
    public static final int CONTENT_STATUS_COMPLETED_ALL = 3;
    public static final int CONTENT_STATUS_UNBLOCKING = 15;

    public static int POWER_CANNON_STOP = 1;
    public static int POWER_OTHER_POWER = 2;

}
