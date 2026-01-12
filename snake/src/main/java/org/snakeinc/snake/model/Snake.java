package org.snakeinc.snake.model;

import java.util.ArrayList;
import org.snakeinc.snake.GameParams;
import org.snakeinc.snake.exception.DiedOfStarvationException;
import org.snakeinc.snake.exception.OutOfPlayException;
import org.snakeinc.snake.exception.SelfCollisionException;

import org.snakeinc.snake.model.state.SnakeState;
import org.snakeinc.snake.model.state.GoodHealth;

public abstract sealed class Snake permits Anaconda, Python, BoaConstrictor{

    protected final ArrayList<Cell> body;
    protected final AppleEatenListener onAppleEatenListener;
    protected final Grid grid;
    protected SnakeState state;

    public Snake(AppleEatenListener listener, Grid grid) {
        this.body = new ArrayList<>();
        this.onAppleEatenListener = listener;
        this.grid = grid;
        this.state = new GoodHealth();
        Cell head = grid.getTile(GameParams.SNAKE_DEFAULT_X, GameParams.SNAKE_DEFAULT_Y);
        head.addSnake(this);
        body.add(head);
        initializeBody();
    }

    private void initializeBody() {
        for (int i = 0; i < 2; i++) {
            Cell cell = grid.getTile(GameParams.SNAKE_DEFAULT_X - i - 1, GameParams.SNAKE_DEFAULT_Y);
            cell.addSnake(this);
            body.add(cell);
        }
    }

    public abstract java.awt.Color getColor();
    
    public SnakeState getState() {
        return state;
    }
    
    public void setState(SnakeState newState) {
        this.state = newState;
    }

    public int getSize() {
        return body.size();
    }

    public Cell getHead() {
        return body.getFirst();
    }

    public void eat(Food food, Cell cell) throws DiedOfStarvationException {
    }

    public void move(Direction direction) throws OutOfPlayException, SelfCollisionException, DiedOfStarvationException {
        direction = state.processDirection(direction);
        
        int x = getHead().getX();
        int y = getHead().getY();
        switch (direction) {
            case U:
                y--;
                break;
            case D:
                y++;
                break;
            case L:
                x--;
                break;
            case R:
                x++;
                break;
        }
        Cell newHead = grid.getTile(x, y);
        if (newHead == null) {
            throw new OutOfPlayException();
        }
        if (newHead.containsASnake()) {
            throw new SelfCollisionException();
        }

        newHead.addSnake(this);
        body.addFirst(newHead);

        if (newHead.containsFood()) {
            this.eat(newHead.getFood(), newHead);
            return;
        }

        body.getLast().removeSnake();
        body.removeLast();

    }


}
