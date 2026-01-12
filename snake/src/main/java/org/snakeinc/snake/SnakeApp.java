package org.snakeinc.snake;

import javax.swing.JFrame;
import org.snakeinc.snake.api.ApiClient;
import org.snakeinc.snake.api.ApiClient.PlayerDTO;
import org.snakeinc.snake.ui.GamePanel;
import org.snakeinc.snake.ui.PlayerSelectionDialog;

public class SnakeApp {

    public static void main(String[] args) {
        JFrame frame = new JFrame("Snake Inc");
        GamePanel gamePanel = new GamePanel();
        frame.add(gamePanel);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setVisible(true);
        frame.setResizable(false);
        
        // Show player selection dialog
        ApiClient apiClient = new ApiClient();
        PlayerSelectionDialog playerDialog = new PlayerSelectionDialog(frame, apiClient);
        playerDialog.setVisible(true);
        
        // Get selected player and start game
        PlayerDTO selectedPlayer = playerDialog.getSelectedPlayer();
        if (selectedPlayer != null) {
            gamePanel.startGameWithPlayer(selectedPlayer);
        } else {
            System.exit(0);
        }
    }
}
