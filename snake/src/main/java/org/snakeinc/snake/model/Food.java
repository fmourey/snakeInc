package org.snakeinc.snake.model;

import java.awt.Color;
import org.snakeinc.snake.model.observer.FoodObserver;

public sealed interface Food extends FoodObserver permits Apple, Broccoli {
    Color getColor();
}
