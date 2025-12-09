package org.snakeinc.snake.model;

import java.util.ArrayList;
import java.util.List;
import lombok.Data;
import org.snakeinc.snake.GameParams;
import org.snakeinc.snake.model.strategy.FoodPlacementStrategy;
import org.snakeinc.snake.model.observer.SnakeProximitySubject;

@Data
public class Basket implements SnakeProximitySubject {

    private Grid grid;
    private List<Food> foods;
    private FoodPlacementStrategy strategy;
    private Snake snake;

    public Basket(Grid grid, Snake snake, FoodPlacementStrategy strategy) {
        foods = new ArrayList<>();
        this.grid = grid;
        this.snake = snake;
        this.strategy = strategy;
    }

    public void addFood() {
        Cell cell = strategy.findPlacementCell(grid, snake);
        Food food = FoodFactory.createFoodInCell(cell);
        foods.add(food);
    }

    public void removeFoodInCell(Food food, Cell cell) {
        cell.removeFood();
        foods.remove(food);
    }

    public boolean isEmpty() {
        return foods.isEmpty();
    }

    private void refill(int nFoods) {
        for (int i = 0; i < nFoods; i++) {
            addFood();
        }
    }

    public void refillIfNeeded(int nFoods) {
        int missingFood = nFoods - foods.size();
        if (missingFood > 0) {
            refill(missingFood);
        }
    }

    @Override
    public void notifySnakeApproach(Cell snakeHeadCell, Grid grid, int proximityThreshold, double movementProbability) {
        for (Food food : new ArrayList<>(foods)) {
            Cell foodCell = findFoodCell(food);
            
            if (foodCell != null && isProximityTriggered(snakeHeadCell, foodCell, proximityThreshold)) {
                if (food.onSnakeApproach(foodCell, movementProbability)) {
                    moveFood(food, foodCell);
                }
            }
        }
    }

    private Cell findFoodCell(Food food) {
        for (int x = 0; x < GameParams.TILES_X; x++) {
            for (int y = 0; y < GameParams.TILES_Y; y++) {
                Cell cell = grid.getTile(x, y);
                if (cell != null && cell.getFood() == food) {
                    return cell;
                }
            }
        }
        return null;
    }

    private boolean isProximityTriggered(Cell snakeHead, Cell foodCell, int proximityThreshold) {
        int dx = Math.abs(snakeHead.getX() - foodCell.getX());
        int dy = Math.abs(snakeHead.getY() - foodCell.getY());
        int distance = dx + dy;
        return distance <= proximityThreshold;
    }

    private void moveFood(Food food, Cell currentCell) {
        currentCell.removeFood();
        Cell newCell = strategy.findPlacementCell(grid, snake);
        newCell.addFood(food);
    }
}

