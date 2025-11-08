package org.snakeinc.snake.model;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import lombok.Data;
import org.snakeinc.snake.GameParams;

@Data
public class Basket {

    private List<Apple> apples;

    private static Basket instance;

    private Basket() {
        apples = new ArrayList<>();
    }

    public static Basket getInstance() {
        if (instance == null) {
            instance = new Basket();
        }
        return instance;
    }

    public void addApple(Cell cell) {
        if (cell == null) {
            var random = new Random();
            cell = Grid.getInstance()
                    .getTile(random.nextInt(0, GameParams.TILES_X), random.nextInt(0, GameParams.TILES_Y));
        }
        Apple apple = AppleFactory.createAppleInCell(cell);
        apples.add(apple);
    }

    public void removeApple(Apple apple) {
        apple.getCell().removeApple();
        apples.remove(apple);
    }

    public boolean isEmpty() {
        return apples.isEmpty();
    }

    private void refill(int nApples) {
        for (int i = 0; i < nApples; i++) {
            addApple(null);
        }
    }

    public void refillIfNeeded(int nApples) {
        int missingApple = nApples - apples.size();
        if (missingApple > 0) {
            refill(missingApple);
        }
    }

}
