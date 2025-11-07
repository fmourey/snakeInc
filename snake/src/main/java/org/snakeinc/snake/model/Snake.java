package org.snakeinc.snake.model;

import java.util.ArrayList;
import org.snakeinc.snake.exception.OutOfPlayException;
import org.snakeinc.snake.exception.SelfCollisionException;

public class Snake implements GameObject {

    private final ArrayList<Cell> body;

    public Snake() {
        body = new ArrayList<>();
        Cell head = Grid.getInstance().getTile(1, 1);
        body.add(head);
        head.getGameObjectsInTile().add(this);
    }

    public ArrayList<Cell> getBody() {
        return body;
    }

    public Cell getHead() {
        return body.getFirst();
    }

    public void eat(Apple apple) {
        body.addFirst(apple.getCell());
        apple.getCell().getGameObjectsInTile().add(this);
        Basket.getInstance().removeApple(apple);
    }

    public void move(char direction) throws OutOfPlayException, SelfCollisionException {
        int x = getHead().getX();
        int y = getHead().getY();
        switch (direction) {
            case 'U':
                y--;
                break;
            case 'D':
                y++;
                break;
            case 'L':
                x--;
                break;
            case 'R':
                x++;
                break;
        }
        Cell newHead = Grid.getInstance().getTile(x, y);
        if (newHead == null) {
            throw new OutOfPlayException();
        }
        if (newHead.gameObjectsInTile.contains(this)) {
            throw new SelfCollisionException();
        }

        // Eat apples :
        for (GameObject gameObject : new ArrayList<>(newHead.getGameObjectsInTile())) {
            if (gameObject instanceof Apple) {
                this.eat((Apple) gameObject);
                return;
            }
        }

        // The snake did not eat :
        newHead.getGameObjectsInTile().add(this);
        body.addFirst(newHead);

        body.getLast().getGameObjectsInTile().remove(this);
        body.removeLast();


    }

}
