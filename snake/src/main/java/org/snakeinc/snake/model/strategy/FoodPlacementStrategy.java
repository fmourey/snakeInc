package org.snakeinc.snake.model.strategy;

import org.snakeinc.snake.model.Cell;
import org.snakeinc.snake.model.Grid;
import org.snakeinc.snake.model.Snake;

public sealed interface FoodPlacementStrategy permits RandomStrategy, EasyStrategy, DifficultStrategy {
    Cell findPlacementCell(Grid grid, Snake snake);
}
