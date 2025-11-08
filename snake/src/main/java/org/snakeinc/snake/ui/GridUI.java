package org.snakeinc.snake.ui;

import java.awt.Graphics;
import org.snakeinc.snake.model.Grid;
import org.snakeinc.snake.model.Cell;

public class GridUI implements Drawable {

    private static GridUI instance;

    private GridUI() {
    }

    public static GridUI getInstance() {
        if (instance == null) {
            instance = new GridUI();
        }
        return instance;
    }

    @Override
    public void draw(Graphics g) {
        for (Cell cell : Grid.getInstance().getTiles().values()) {
            if (cell.containsAnApple() || cell.containsASnake()) {
                new CellUI(cell, cell.getX() * GamePanel.TILE_PIXEL_SIZE,
                        cell.getY() * GamePanel.TILE_PIXEL_SIZE).draw(g);
            }
        }
    }


}
