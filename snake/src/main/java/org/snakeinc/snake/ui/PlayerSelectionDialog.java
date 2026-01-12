package org.snakeinc.snake.ui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;

import org.snakeinc.snake.api.ApiClient;
import org.snakeinc.snake.api.ApiClient.PlayerDTO;

public class PlayerSelectionDialog extends JDialog {
    
    private final ApiClient apiClient;
    private PlayerDTO selectedPlayer;
    private final JTextField playerNameField;
    private final JSpinner ageSpinner;
    private final JButton createButton;
    private final JButton loadButton;
    private final JButton playButton;
    private final JLabel statusLabel;
    
    public PlayerSelectionDialog(Frame owner, ApiClient apiClient) {
        super(owner, "Select or Create Player", true);
        this.apiClient = apiClient;
        this.setDefaultCloseOperation(JDialog.DO_NOTHING_ON_CLOSE);
        
        // Create UI components
        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BorderLayout(10, 10));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        
        // Top panel - input fields
        JPanel inputPanel = new JPanel();
        inputPanel.setLayout(new GridLayout(2, 2, 10, 10));
        inputPanel.setBorder(BorderFactory.createTitledBorder("New Player"));
        
        inputPanel.add(new JLabel("Player Name:"));
        playerNameField = new JTextField(15);
        inputPanel.add(playerNameField);
        
        inputPanel.add(new JLabel("Age:"));
        ageSpinner = new JSpinner(new SpinnerNumberModel(25, 1, 120, 1));
        inputPanel.add(ageSpinner);
        
        // Middle panel - buttons
        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 10, 10));
        
        createButton = new JButton("Create New Player");
        createButton.addActionListener(this::createNewPlayer);
        buttonPanel.add(createButton);
        
        loadButton = new JButton("Load Player");
        loadButton.addActionListener(this::loadPlayer);
        buttonPanel.add(loadButton);
        
        // Bottom panel - status and play button
        JPanel bottomPanel = new JPanel();
        bottomPanel.setLayout(new BorderLayout(10, 10));
        
        statusLabel = new JLabel("No player selected");
        statusLabel.setForeground(Color.RED);
        bottomPanel.add(statusLabel, BorderLayout.CENTER);
        
        playButton = new JButton("Play Game");
        playButton.setEnabled(false);
        playButton.addActionListener(e -> dispose());
        bottomPanel.add(playButton, BorderLayout.EAST);
        
        // Assemble main panel
        mainPanel.add(inputPanel, BorderLayout.NORTH);
        mainPanel.add(buttonPanel, BorderLayout.CENTER);
        mainPanel.add(bottomPanel, BorderLayout.SOUTH);
        
        this.add(mainPanel);
        this.pack();
        this.setLocationRelativeTo(owner);
    }
    
    private void createNewPlayer(ActionEvent e) {
        String name = playerNameField.getText().trim();
        if (name.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please enter a player name", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        int age = (Integer) ageSpinner.getValue();
        
        try {
            selectedPlayer = apiClient.createPlayer(name, age);
            updateStatus();
            playerNameField.setText("");
        } catch (Exception ex) {
            String errorMsg = ex.getMessage() != null ? ex.getMessage() : ex.getClass().getSimpleName() + ": " + ex.toString();
            System.err.println("Error creating player: " + errorMsg);
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error creating player: " + errorMsg, "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    private void loadPlayer(ActionEvent e) {
        String name = playerNameField.getText().trim();
        if (name.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please enter a player name to load", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        try {
            selectedPlayer = apiClient.getPlayerByName(name);
            updateStatus();
            playerNameField.setText("");
        } catch (Exception ex) {
            String errorMsg = ex.getMessage() != null ? ex.getMessage() : ex.getClass().getSimpleName() + ": " + ex.toString();
            System.err.println("Error loading player: " + errorMsg);
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error loading player: " + errorMsg, "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    private void updateStatus() {
        if (selectedPlayer != null) {
            statusLabel.setText("Selected: " + selectedPlayer.name() + " (Age: " + selectedPlayer.age() + ")");
            statusLabel.setForeground(Color.GREEN);
            playButton.setEnabled(true);
        }
    }
    
    public PlayerDTO getSelectedPlayer() {
        return selectedPlayer;
    }
}
