package org.snakeinc.snake.model;

public class AppleFactory {

    public static Apple createAppleInCell(Cell cell) {
        Apple apple = new Apple(cell);
        cell.addGameObject(apple);
        return apple;
    }

}
