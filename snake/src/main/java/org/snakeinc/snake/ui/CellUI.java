package org.snakeinc.snake.ui;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import lombok.AllArgsConstructor;
import org.snakeinc.snake.model.Apple;
import org.snakeinc.snake.model.Cell;

@AllArgsConstructor
public class CellUI implements Drawable {

    private Cell cell;
    private int upperPixelX;
    private int upperPixelY;

    public void drawRectangle(Graphics g) {
        g.fillRect(upperPixelX, upperPixelY, GamePanel.TILE_PIXEL_SIZE, GamePanel.TILE_PIXEL_SIZE);
        // Contour du rectangle
        Graphics2D g2 = (Graphics2D) g; // pour pouvoir définir l'épaisseur du contour
        g2.setColor(Color.GREEN.darker()); // contour plus foncé
        g2.setStroke(new BasicStroke(2));  // épaisseur du contour (optionnel)
        g2.drawRect(upperPixelX, upperPixelY, GamePanel.TILE_PIXEL_SIZE, GamePanel.TILE_PIXEL_SIZE);
    }

    public void drawOval(Graphics g) {
        g.fillOval(upperPixelX, upperPixelY, GamePanel.TILE_PIXEL_SIZE, GamePanel.TILE_PIXEL_SIZE);
    }

    @Override
    public void draw(Graphics g) {

        if (cell.containsAnApple()) {
            g.setColor(Color.RED);
            drawOval(g);
        }
        if (cell.containsASnake()) {
            g.setColor(Color.GREEN);
            drawRectangle(g);
        }

    }

}
