package org.snakeinc.snake.model;

import java.awt.Color;
import java.util.Random;
import lombok.Getter;

@Getter
public final class Broccoli implements Food {
    private final boolean steamed;
    private static final Random random = new Random();

    public Broccoli(boolean steamed) {
        this.steamed = steamed;
    }

    @Override
    public Color getColor() {
        return steamed ? new Color(0, 255, 0) : new Color(0, 100, 0);
    }

    @Override
    public boolean onSnakeApproach(Cell currentCell, double probability) {
        if (random.nextDouble() < probability) {
            return true;
        }
        return false;
    }
}
