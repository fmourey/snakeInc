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
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.Rectangle;
import javax.swing.JPanel;
import javax.swing.Timer;
import org.snakeinc.snake.GameParams;
import org.snakeinc.snake.api.ApiClient;
import org.snakeinc.snake.api.ApiClient.PlayerDTO;
import org.snakeinc.snake.api.ApiClient.StatsDTO;
import org.snakeinc.snake.api.ApiClient.LeaderboardDTO;
import org.snakeinc.snake.exception.OutOfPlayException;
import org.snakeinc.snake.exception.SelfCollisionException;
import org.snakeinc.snake.exception.DiedOfStarvationException;
import org.snakeinc.snake.model.Game;
import org.snakeinc.snake.model.Direction;

public class GamePanel extends JPanel implements ActionListener, KeyListener, MouseListener {

    public static final int TILE_PIXEL_SIZE = 20;
    public static final int GAME_PIXEL_WIDTH = TILE_PIXEL_SIZE * GameParams.TILES_X;
    public static final int GAME_PIXEL_HEIGHT = TILE_PIXEL_SIZE * GameParams.TILES_Y;

    private Timer timer;
    private Game game;
    private boolean running = false;
    private Direction direction = Direction.R;
    private PlayerDTO currentPlayer;
    private ApiClient apiClient;
    private boolean scoreSent = false;
    private StatsDTO playerStats;
    private LeaderboardDTO leaderboard;
    private Rectangle playAgainButton;

    public GamePanel() {
        this.setPreferredSize(new Dimension(GAME_PIXEL_WIDTH, GAME_PIXEL_HEIGHT));
        this.setBackground(Color.BLACK);
        this.setFocusable(true);
        this.addKeyListener(this);
        this.addMouseListener(this);
        this.apiClient = new ApiClient();
    }

    public void startGameWithPlayer(PlayerDTO player) {
        this.currentPlayer = player;
        this.scoreSent = false;
        startGame();
    }

    private void startGame() {
        game = new Game();
        timer = new Timer(100, this);
        timer.start();
        running = true;
        scoreSent = false;
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        if (game != null && running) {
            UIUtils.draw(g, game);
        } else if (game != null) {
            gameOver(g);
        }
    }

    private void gameOver(Graphics g) {
        g.setColor(Color.RED);
        g.setFont(new Font("Arial", Font.BOLD, 40));
        FontMetrics metrics = getFontMetrics(g.getFont());
        String gameOverText = "Game Over";
        g.drawString(gameOverText, (GAME_PIXEL_WIDTH - metrics.stringWidth(gameOverText)) / 2, 60);

        // Display current score
        g.setColor(Color.WHITE);
        g.setFont(new Font("Arial", Font.PLAIN, 20));
        metrics = getFontMetrics(g.getFont());
        
        String pointsText = "Points: " + game.getPoints();
        g.drawString(pointsText, (GAME_PIXEL_WIDTH - metrics.stringWidth(pointsText)) / 2, 120);
        
        // Display best score if available
        if (playerStats != null) {
            String bestScoreText = "Best Score: " + playerStats.bestScore();
            g.drawString(bestScoreText, (GAME_PIXEL_WIDTH - metrics.stringWidth(bestScoreText)) / 2, 150);
        }
        
        // Display leaderboard
        if (leaderboard != null && !leaderboard.scores().isEmpty()) {
            g.setColor(Color.YELLOW);
            g.setFont(new Font("Arial", Font.BOLD, 18));
            String leaderboardTitle = "Top 3 Scores";
            metrics = getFontMetrics(g.getFont());
            g.drawString(leaderboardTitle, (GAME_PIXEL_WIDTH - metrics.stringWidth(leaderboardTitle)) / 2, 200);
            
            g.setColor(Color.WHITE);
            g.setFont(new Font("Arial", Font.PLAIN, 16));
            int yOffset = 230;
            for (int i = 0; i < Math.min(3, leaderboard.scores().size()); i++) {
                var entry = leaderboard.scores().get(i);
                String leaderboardEntry = (i + 1) + ". " + entry.playerName() + " - " + entry.score();
                g.drawString(leaderboardEntry, (GAME_PIXEL_WIDTH - metrics.stringWidth(leaderboardEntry)) / 2, yOffset);
                yOffset += 25;
            }
        }
        
        // Draw "Play Again" button
        drawPlayAgainButton(g);
        
        // Send score if player is selected and score hasn't been sent yet
        if (currentPlayer != null && !scoreSent) {
            sendScore();
            scoreSent = true;
        }
    }
    
    private void drawPlayAgainButton(Graphics g) {
        int buttonWidth = 150;
        int buttonHeight = 40;
        int buttonX = (GAME_PIXEL_WIDTH - buttonWidth) / 2;
        int buttonY = GAME_PIXEL_HEIGHT - 80;
        
        playAgainButton = new Rectangle(buttonX, buttonY, buttonWidth, buttonHeight);
        
        // Draw button background
        g.setColor(new Color(0, 150, 0));
        g.fillRect(buttonX, buttonY, buttonWidth, buttonHeight);
        
        // Draw button border
        java.awt.Graphics2D g2d = (java.awt.Graphics2D) g;
        g2d.setColor(Color.WHITE);
        g2d.setStroke(new java.awt.BasicStroke(2));
        g2d.drawRect(buttonX, buttonY, buttonWidth, buttonHeight);
        
        // Draw button text
        g.setColor(Color.WHITE);
        g.setFont(new Font("Arial", Font.BOLD, 16));
        String buttonText = "Play Again";
        FontMetrics metrics = g.getFontMetrics();
        int textX = buttonX + (buttonWidth - metrics.stringWidth(buttonText)) / 2;
        int textY = buttonY + ((buttonHeight - metrics.getHeight()) / 2) + metrics.getAscent();
        g.drawString(buttonText, textX, textY);
    }

    private void sendScore() {
        // Get the snake type from the game
        String snakeName = game.getSnake().getClass().getSimpleName();
        // Convert to camelCase format expected by API (anaconda, python, boaConstrictor)
        String snakeNameForApi = snakeName.equals("BoaConstrictor") ? "boaConstrictor" : snakeName.toLowerCase();
        int score = game.getPoints();
        
        try {
            System.out.println("Submitting score: player=" + currentPlayer.id() + ", snake=" + snakeNameForApi + ", score=" + score);
            apiClient.submitScore(currentPlayer.id(), snakeNameForApi, score);
            System.out.println("Score submitted successfully");
            
            // Fetch player stats
            System.out.println("Fetching player stats for player " + currentPlayer.id());
            playerStats = apiClient.getPlayerStats(currentPlayer.id());
            System.out.println("Player stats: " + playerStats);
            
            // Fetch leaderboard
            System.out.println("Fetching top 3 scores");
            leaderboard = apiClient.getTopScores(3);
            System.out.println("Leaderboard: " + leaderboard);
            
            // Trigger repaint to display the stats
            repaint();
        } catch (Exception e) {
            System.err.println("Failed to send score: " + e.getMessage());
            e.printStackTrace();
        }
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
    
    @Override
    public void mouseClicked(MouseEvent e) {
        if (!running && playAgainButton != null && playAgainButton.contains(e.getPoint())) {
            // Reset game state and start new game
            playerStats = null;
            leaderboard = null;
            direction = Direction.R;
            startGame();
            repaint();
        }
    }

    @Override
    public void mousePressed(MouseEvent e) {
    }

    @Override
    public void mouseReleased(MouseEvent e) {
    }

    @Override
    public void mouseEntered(MouseEvent e) {
    }

    @Override
    public void mouseExited(MouseEvent e) {
    }
}
