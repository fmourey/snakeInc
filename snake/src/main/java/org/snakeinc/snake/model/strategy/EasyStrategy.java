package org.snakeinc.snake.model.strategy;

import java.util.Random;
import org.snakeinc.snake.GameParams;
import org.snakeinc.snake.model.Cell;
import org.snakeinc.snake.model.Grid;
import org.snakeinc.snake.model.Snake;

public final class EasyStrategy implements FoodPlacementStrategy {
    private static final Random random = new Random();
    private static final int PROXIMITY_RANGE = 5;

    @Override
    public Cell findPlacementCell(Grid grid, Snake snake) {
        Cell head = snake.getHead();
        Cell cell = null;
        
        int attempts = 0;
        while ((cell == null || cell.containsASnake()) && attempts < 20) {
            int x = head.getX() + random.nextInt(-PROXIMITY_RANGE, PROXIMITY_RANGE + 1);
            int y = head.getY() + random.nextInt(-PROXIMITY_RANGE, PROXIMITY_RANGE + 1);
            
            x = Math.max(0, Math.min(x, GameParams.TILES_X - 1));
            y = Math.max(0, Math.min(y, GameParams.TILES_Y - 1));
            
            cell = grid.getTile(x, y);
            attempts++;
        }
        
        if (cell == null || cell.containsASnake()) {
            return new RandomStrategy().findPlacementCell(grid, snake);
        }
        
        return cell;
    }
}
