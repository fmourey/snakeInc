package org.snakeinc.snake.model.state;

import org.snakeinc.snake.model.Direction;

public final class GoodHealth implements SnakeState {
    @Override
    public Direction processDirection(Direction direction) {
        return direction;
    }

    @Override
    public String getStateName() {
        return "Good Health";
    }
}
