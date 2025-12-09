package org.snakeinc.snake.ui;

import java.awt.Graphics;
import org.snakeinc.snake.model.Cell;
import org.snakeinc.snake.model.Game;

public class UIUtils {

    public static void draw(Graphics g, Game game) {
        for (Cell cell :game.getGrid().getTiles().values()) {
            if (cell.containsFood() || cell.containsASnake()) {
                new CellUI(cell, cell.getX() * GamePanel.TILE_PIXEL_SIZE,
                        cell.getY() * GamePanel.TILE_PIXEL_SIZE).draw(g);
            }
        }
    }


}
