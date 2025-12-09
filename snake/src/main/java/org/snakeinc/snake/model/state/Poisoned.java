package org.snakeinc.snake.model.state;

import org.snakeinc.snake.model.Direction;

public final class Poisoned implements SnakeState {
    @Override
    public Direction processDirection(Direction direction) {
        return switch (direction) {
            case U -> Direction.D;
            case D -> Direction.U;
            default -> direction;
        };
    }

    @Override
    public String getStateName() {
        return "Poisoned";
    }
}
