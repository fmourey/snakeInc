package org.snakeinc.snake.model.observer;

import org.snakeinc.snake.model.Cell;

public interface FoodObserver {
    /**
     * @param currentCell
     * @param probability
     * @return
     */
    boolean onSnakeApproach(Cell currentCell, double probability);
}
