package org.snakeinc.snake.model;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.Getter;


@Data
@EqualsAndHashCode
public class Cell {

    @Getter
    private int x;

    @Getter
    private int y;

    Snake snake;
    Apple apple;

    protected Cell(int x, int y) {
        setX(x);
        setY(y);
    }

    public void addApple(Apple apple) {
        this.apple = apple;
    }

    public void addSnake(Snake snake) {
        this.snake = snake;
    }

    public void removeSnake() {
        this.snake = null;
    }

    public void removeApple() {
        this.apple = null;
    }

    public boolean containsASnake() {
        return this.snake != null;
    }


    public boolean containsAnApple() {
        return this.apple != null;
    }

}
