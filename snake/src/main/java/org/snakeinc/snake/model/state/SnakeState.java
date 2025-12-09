package org.snakeinc.snake.model.state;

import org.snakeinc.snake.model.Direction;

public sealed interface SnakeState permits GoodHealth, Poisoned, PermanentlyDamaged {
    Direction processDirection(Direction direction);
    String getStateName();
}
