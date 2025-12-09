package org.snakeinc.snake.model.observer;

import org.snakeinc.snake.model.Cell;
import org.snakeinc.snake.model.Grid;

public interface SnakeProximitySubject {
    /**
     * @param snakeHeadCell
     * @param grid
     * @param proximityThreshold
     * @param movementProbability 
     */
    void notifySnakeApproach(Cell snakeHeadCell, Grid grid, int proximityThreshold, double movementProbability);
}
