package org.snakeinc.snake.model;

import lombok.Getter;

@Getter
public class Apple {

    private final Cell cell;

    protected Apple(Cell cell) {
        this.cell = cell;
    }

}
