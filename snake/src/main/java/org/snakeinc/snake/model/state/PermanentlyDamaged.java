package org.snakeinc.snake.model.state;

import org.snakeinc.snake.model.Direction;

public final class PermanentlyDamaged implements SnakeState {
    @Override
    public Direction processDirection(Direction direction) {
        return switch (direction) {
            case U -> Direction.D;
            case D -> Direction.U;
            case L -> Direction.R;
            case R -> Direction.L;
        };
    }

    @Override
    public String getStateName() {
        return "Permanently Damaged";
    }
}
