package ui;

import model.difficulty.*;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class MenuPanel extends JPanel {

    private final Runnable onStartGame;

    private JComboBox<String> modeCombo;
    private JComboBox<String> difficultyCombo;
    private JComboBox<String> colorCombo;
    private JComboBox<String> terrainCombo;
    private JTextField distanceField;

    public MenuPanel(Runnable onStartGame) {
        this.onStartGame = onStartGame;

        setLayout(new GridBagLayout());
        setBackground(new Color(34, 34, 34));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.anchor = GridBagConstraints.CENTER;

        // Title
        JLabel title = new JLabel("🏎️ Course de Voiture");
        title.setFont(new Font("Arial", Font.BOLD, 36));
        title.setForeground(Color.WHITE);
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        add(title, gbc);

        // Mode selection
        gbc.gridwidth = 1;
        gbc.gridy = 1;
        gbc.gridx = 0;
        add(new JLabel("Mode:", SwingConstants.RIGHT), gbc);
        gbc.gridx = 1;
        modeCombo = new JComboBox<>(new String[]{"1 Joueur", "2 Joueurs Local", "2 Joueurs vs IA"});
        add(modeCombo, gbc);

        // Difficulty
        gbc.gridy = 2;
        gbc.gridx = 0;
        add(new JLabel("Difficulté:", SwingConstants.RIGHT), gbc);
        gbc.gridx = 1;
        difficultyCombo = new JComboBox<>(new String[]{"Facile", "Moyen", "Difficile"});
        add(difficultyCombo, gbc);

        // Color
        gbc.gridy = 3;
        gbc.gridx = 0;
        add(new JLabel("Couleur voiture:", SwingConstants.RIGHT), gbc);
        gbc.gridx = 1;
        colorCombo = new JComboBox<>(new String[]{"Bleu", "Rouge", "Vert", "Orange"});
        add(colorCombo, gbc);

        // Terrain
        gbc.gridy = 4;
        gbc.gridx = 0;
        add(new JLabel("Terrain:", SwingConstants.RIGHT), gbc);
        gbc.gridx = 1;
        terrainCombo = new JComboBox<>(new String[]{"Ville", "Désert", "Montagne"});
        add(terrainCombo, gbc);

        // Distance goal (for 2 players)
        gbc.gridy = 5;
        gbc.gridx = 0;
        add(new JLabel("Distance (m):", SwingConstants.RIGHT), gbc);
        gbc.gridx = 1;
        distanceField = new JTextField("1000", 10);
        add(distanceField, gbc);

        // Start button
        gbc.gridy = 6;
        gbc.gridx = 0;
        gbc.gridwidth = 2;
        JButton startButton = new JButton("Démarrer la course !");
        startButton.setFont(new Font("Arial", Font.BOLD, 20));
        startButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                startGame();
            }
        });
        add(startButton, gbc);

        // Instructions
        gbc.gridy = 7;
        gbc.gridwidth = 2;
        JLabel instructions = new JLabel("<html><center>Utilisez les flèches pour contrôler la voiture.<br>P pour pause, M pour menu, Q pour quitter.</center></html>");
        instructions.setForeground(Color.LIGHT_GRAY);
        instructions.setHorizontalAlignment(SwingConstants.CENTER);
        add(instructions, gbc);
    }

    private void startGame() {
        // Parse selections
        GamePanel.Mode mode;
        switch ((String) modeCombo.getSelectedItem()) {
            case "1 Joueur" -> mode = GamePanel.Mode.ONE_PLAYER;
            case "2 Joueurs Local" -> mode = GamePanel.Mode.TWO_PLAYERS_LOCAL;
            case "2 Joueurs vs IA" -> mode = GamePanel.Mode.TWO_PLAYERS_AI;
            default -> mode = GamePanel.Mode.ONE_PLAYER;
        }

        Difficulty difficulty;
        switch ((String) difficultyCombo.getSelectedItem()) {
            case "Facile" -> difficulty = new Easy();
            case "Moyen" -> difficulty = new Medium();
            case "Difficile" -> difficulty = new Hard();
            default -> difficulty = new Easy();
        }

        Color color;
        switch ((String) colorCombo.getSelectedItem()) {
            case "Bleu" -> color = Color.BLUE;
            case "Rouge" -> color = Color.RED;
            case "Vert" -> color = Color.GREEN;
            case "Orange" -> color = Color.ORANGE;
            default -> color = Color.BLUE;
        }

        String terrain = (String) terrainCombo.getSelectedItem();

        int distanceGoal;
        try {
            distanceGoal = Integer.parseInt(distanceField.getText());
        } catch (NumberFormatException e) {
            distanceGoal = 1000;
        }

        // Create game panel with parameters
        GamePanel gamePanel = new GamePanel(mode, difficulty, color, terrain, distanceGoal, onStartGame);

        // Replace this panel with game panel
        JFrame frame = (JFrame) SwingUtilities.getWindowAncestor(this);
        frame.setContentPane(gamePanel);
        frame.revalidate();
        frame.repaint();
        gamePanel.requestFocusInWindow();
    }
}