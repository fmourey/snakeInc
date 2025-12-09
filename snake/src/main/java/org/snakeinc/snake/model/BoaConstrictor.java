package org.snakeinc.snake.model;

import java.awt.Color;
import org.snakeinc.snake.exception.DiedOfStarvationException;
import org.snakeinc.snake.model.state.GoodHealth;
import org.snakeinc.snake.model.state.Poisoned;
import org.snakeinc.snake.model.state.PermanentlyDamaged;

public final class BoaConstrictor extends Snake {
    public BoaConstrictor(AppleEatenListener listener, Grid grid) {
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
            if (body.size() <= 2) {
                throw new DiedOfStarvationException();
            }
            body.getLast().removeSnake();
            body.removeLast();
        } 
        else if (food instanceof Broccoli) {
            if (state instanceof Poisoned) {
                setState(new GoodHealth());
            }
        }
        onAppleEatenListener.onAppleEaten(food, cell);
        body.getLast().removeSnake();
        body.removeLast();
    }

    @Override
    public Color getColor() {
        return Color.BLUE;
    }

}


