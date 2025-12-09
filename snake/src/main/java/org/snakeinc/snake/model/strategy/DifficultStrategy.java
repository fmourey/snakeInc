package org.snakeinc.snake.model.strategy;

import java.util.Random;
import org.snakeinc.snake.GameParams;
import org.snakeinc.snake.model.Cell;
import org.snakeinc.snake.model.Grid;
import org.snakeinc.snake.model.Snake;

public final class DifficultStrategy implements FoodPlacementStrategy {
    private static final Random random = new Random();

    @Override
    public Cell findPlacementCell(Grid grid, Snake snake) {
        Cell head = snake.getHead();
        Cell cell = null;
        int attempts = 0;
        
        if (snake.getSize() <= 15) {
            while ((cell == null || cell.containsASnake() || isTooClose(cell, head, 40)) && attempts < 30) {
                cell = grid.getTile(
                    random.nextInt(0, GameParams.TILES_X),
                    random.nextInt(0, GameParams.TILES_Y)
                );
                attempts++;
            }
        } else {
            while ((cell == null || cell.containsASnake()) && attempts < 20) {
                int x = head.getX() + random.nextInt(-5, 6);
                int y = head.getY() + random.nextInt(-5, 6);
                
                x = Math.max(0, Math.min(x, GameParams.TILES_X - 1));
                y = Math.max(0, Math.min(y, GameParams.TILES_Y - 1));
                
                cell = grid.getTile(x, y);
                attempts++;
            }
        }
        
        if (cell == null || cell.containsASnake()) {
            return new RandomStrategy().findPlacementCell(grid, snake);
        }
        
        return cell;
    }

    private boolean isTooClose(Cell cell, Cell head, int minDistance) {
        int dx = Math.abs(cell.getX() - head.getX());
        int dy = Math.abs(cell.getY() - head.getY());
        int distance = dx + dy;
        return distance < minDistance;
    }
}
