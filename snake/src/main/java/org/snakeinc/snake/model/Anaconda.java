package org.snakeinc.snake.model;

import java.awt.Color;
import org.snakeinc.snake.exception.DiedOfStarvationException;
import org.snakeinc.snake.model.state.GoodHealth;
import org.snakeinc.snake.model.state.Poisoned;
import org.snakeinc.snake.model.state.PermanentlyDamaged;

public final class Anaconda extends Snake {
    public Anaconda(AppleEatenListener listener, Grid grid) {
        super(listener, grid);
    }

    @Override
    public void eat(Food food, Cell cell) throws DiedOfStarvationException {
        if (food instanceof Apple apple) {
            if (apple.isPoisoned()) {
                if (state instanceof GoodHealth) {
                    setState(new Poisoned());
                } else if (state instanceof Poisoned) {
                    setState(new PermanentlyDamaged());
                }
            }
        } else if (food instanceof Broccoli) {
            if (state instanceof Poisoned) {
                setState(new GoodHealth());
            }
            if (body.size() <= 3) {
                throw new DiedOfStarvationException();
            }
            body.getLast().removeSnake();
            body.removeLast();
            body.getLast().removeSnake();
            body.removeLast();
            body.getLast().removeSnake();
            body.removeLast();
        }
        onAppleEatenListener.onAppleEaten(food, cell);
    }

    @Override
    public Color getColor() {
        return Color.GRAY;
    }
}
