package org.snakeinc.snake.model;

import lombok.Getter;
import java.util.Random;
import org.snakeinc.snake.GameParams;
import org.snakeinc.snake.exception.OutOfPlayException;
import org.snakeinc.snake.exception.SelfCollisionException;
import org.snakeinc.snake.exception.DiedOfStarvationException;

import org.snakeinc.snake.model.strategy.FoodPlacementStrategy;
import org.snakeinc.snake.model.strategy.RandomStrategy;
import org.snakeinc.snake.model.strategy.EasyStrategy;
import org.snakeinc.snake.model.strategy.DifficultStrategy;

@Getter
public class Game {

    private final Grid grid;
    private final Basket basket;
    private final Snake snake;
    private int moveCount = 0;
    private int foodEatenCount = 0;
    private int points = 0;

    public Game() {
        grid = new Grid();
        snake = switch (new Random().nextInt(0,3)) {
            case 0 -> new Anaconda(this::onFoodEaten, grid);
            case 1 -> new Python(this::onFoodEaten, grid);
            case 2 -> new BoaConstrictor(this::onFoodEaten, grid);
            default -> new Anaconda(this::onFoodEaten, grid);
        };
        
        FoodPlacementStrategy strategy = switch (new Random().nextInt(0,3)) {
            case 0 -> new RandomStrategy();
            case 1 -> new EasyStrategy();
            case 2 -> new DifficultStrategy();
            default -> new DifficultStrategy();
        };
        
        basket = new Basket(grid, snake, strategy);
        basket.refillIfNeeded(1);
    }

    private void onFoodEaten(Food food, Cell cell) {
        foodEatenCount++;
        if (food instanceof Apple apple) {
            points += apple.isPoisoned() ? 0 : 2;
        } else if (food instanceof Broccoli broccoli) {
            points += broccoli.isSteamed() ? 0 : 1;
        }
        basket.removeFoodInCell(food, cell);
    }

    public void handleFoodEaten(Food food, Cell cell) {
        onFoodEaten(food, cell);
    }

    public void iterate(Direction direction) throws OutOfPlayException, SelfCollisionException, DiedOfStarvationException {
        moveCount++;
        snake.move(direction);
        basket.notifySnakeApproach(
            snake.getHead(), 
            grid, 
            GameParams.PROXIMITY_THRESHOLD,
            GameParams.MOVEMENT_PROBABILITY
        );
        
        basket.refillIfNeeded(1);
    }

}

