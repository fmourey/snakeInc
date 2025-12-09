package org.snakeinc.snake.ui;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import javax.swing.JPanel;
import javax.swing.Timer;
import org.snakeinc.snake.GameParams;
import org.snakeinc.snake.exception.OutOfPlayException;
import org.snakeinc.snake.exception.SelfCollisionException;
import org.snakeinc.snake.exception.DiedOfStarvationException;
import org.snakeinc.snake.model.Game;
import org.snakeinc.snake.model.Direction;

public class GamePanel extends JPanel implements ActionListener, KeyListener {

    public static final int TILE_PIXEL_SIZE = 20;
    public static final int GAME_PIXEL_WIDTH = TILE_PIXEL_SIZE * GameParams.TILES_X;
    public static final int GAME_PIXEL_HEIGHT = TILE_PIXEL_SIZE * GameParams.TILES_Y;

    private Timer timer;
    private Game game;
    private boolean running = false;
    private Direction direction = Direction.R;

    public GamePanel() {
        this.setPreferredSize(new Dimension(GAME_PIXEL_WIDTH, GAME_PIXEL_HEIGHT));
        this.setBackground(Color.BLACK);
        this.setFocusable(true);
        this.addKeyListener(this);
        startGame();
    }

    private void startGame() {
        game = new Game();
        timer = new Timer(100, this);
        timer.start();
        running = true;
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        if (running) {
            UIUtils.draw(g, game);
        } else {
            gameOver(g);
        }
    }

    private void gameOver(Graphics g) {
        g.setColor(Color.RED);
        g.setFont(new Font("Arial", Font.BOLD, 40));
        FontMetrics metrics = getFontMetrics(g.getFont());
        String gameOverText = "Game Over";
        g.drawString(gameOverText, (GAME_PIXEL_WIDTH - metrics.stringWidth(gameOverText)) / 2, GAME_PIXEL_HEIGHT / 2 - 60);

        // Display stats
        g.setColor(Color.WHITE);
        g.setFont(new Font("Arial", Font.PLAIN, 20));
        metrics = getFontMetrics(g.getFont());
        
        String pointsText = "Points: " + game.getPoints();
        g.drawString(pointsText, (GAME_PIXEL_WIDTH - metrics.stringWidth(pointsText)) / 2, GAME_PIXEL_HEIGHT / 2 + 60);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (running) {
            try {
                game.iterate(direction);
            } catch (OutOfPlayException | SelfCollisionException | DiedOfStarvationException exception) {
                timer.stop();
                running = false;
            }
        }
        repaint();
    }

    @Override
    public void keyPressed(KeyEvent e) {
        switch (e.getKeyCode()) {
            case KeyEvent.VK_LEFT:
                if (direction != Direction.R) {
                    direction = Direction.L;
                }
                break;
            case KeyEvent.VK_RIGHT:
                if (direction != Direction.L) {
                    direction = Direction.R;
                }
                break;
            case KeyEvent.VK_UP:
                if (direction != Direction.D) {
                    direction = Direction.U;
                }
                break;
            case KeyEvent.VK_DOWN:
                if (direction != Direction.U) {
                    direction = Direction.D;
                }
                break;
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {
    }

    @Override
    public void keyTyped(KeyEvent e) {
    }
}
