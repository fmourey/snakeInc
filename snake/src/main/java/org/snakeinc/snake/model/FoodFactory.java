package org.snakeinc.snake.model;

import java.util.Random;
import org.snakeinc.snake.GameParams;

public class FoodFactory {
    private static final Random random = new Random();

    public static Food createFoodInCell(Cell cell) {
        Food food;
        
        if (random.nextDouble() < GameParams.APPLE_PROBABILITY) {
            boolean poisoned = random.nextDouble() < GameParams.POISONED_PROBABILITY;
            food = new Apple(poisoned);
        } else {
            boolean steamed = random.nextDouble() < GameParams.STEAMED_PROBABILITY;
            food = new Broccoli(steamed);
        }
        
        cell.addFood(food);
        return food;
    }

}


