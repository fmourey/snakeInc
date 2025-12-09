package org.snakeinc.snake;

public class GameParams {

    // Grid
    public static Integer TILES_X = 30;
    public static Integer TILES_Y = 30;


    // Snake default position
    public static Integer SNAKE_DEFAULT_X = 5;
    public static Integer SNAKE_DEFAULT_Y = 5;

    // Food movement behavior (Observer pattern)
    public static final int PROXIMITY_THRESHOLD = 2;
    public static final double MOVEMENT_PROBABILITY = 0.15;

    // Food factory probabilities
    public static final double APPLE_PROBABILITY = 0.9;
    public static final double POISONED_PROBABILITY = 0.0;
    public static final double STEAMED_PROBABILITY = 0.3;
}