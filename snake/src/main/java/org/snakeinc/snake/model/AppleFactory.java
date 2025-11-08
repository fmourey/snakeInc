package org.snakeinc.snake.model;

public class AppleFactory {

    public static Apple createAppleInCell(Cell cell) {
        Apple apple = new Apple(cell);
        cell.addApple(apple);
        return apple;
    }

}
