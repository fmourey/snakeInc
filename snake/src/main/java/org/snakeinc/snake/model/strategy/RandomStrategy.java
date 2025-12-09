package org.snakeinc.snake.model.strategy;

import java.util.Random;
import org.snakeinc.snake.GameParams;
import org.snakeinc.snake.model.Cell;
import org.snakeinc.snake.model.Grid;
import org.snakeinc.snake.model.Snake;

public final class RandomStrategy implements FoodPlacementStrategy {
    private static final Random random = new Random();

    @Override
    public Cell findPlacementCell(Grid grid, Snake snake) {
        Cell cell = null;
        while (cell == null || cell.containsASnake()) {
            cell = grid.getTile(
                random.nextInt(0, GameParams.TILES_X),
                random.nextInt(0, GameParams.TILES_Y)
            );
        }
        return cell;
    }
}
