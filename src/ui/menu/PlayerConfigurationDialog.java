package ui.menu;

import model.game.GameConfiguration;
import model.difficulty.*;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.function.Consumer;

/**
 * Dialogue de configuration du joueur.
 * Permet de choisir :
 * - Nom du joueur
 * - Couleur de la voiture
 * - Terrain
 * - Difficulté
 * - Mode de jeu
 */
public class PlayerConfigurationDialog extends JDialog {
    private GameConfiguration configuration;
    private Consumer<GameConfiguration> onConfigured;
    private boolean confirmed = false;

    // Composants UI
    private JTextField playerNameField;
    private JComboBox<String> colorCombo;
    private JComboBox<String> terrainCombo;
    private JComboBox<String> difficultyCombo;
    private JComboBox<String> gameModeCombo;
    private JSpinner targetDistanceSpinner;

    private static final String[] COLORS = {"Bleu", "Rouge", "Vert", "Jaune", "Mauve", "Orange"};
    private static final String[] TERRAINS = {"Ville", "Desert", "Campagne", "Paysage"};
    private static final String[] DIFFICULTIES = {"Facile", "Moyen", "Difficile"};
    private static final String[] GAME_MODES = {"1 Joueur", "Joueur vs IA", "Joueur 1 vs Joueur 2"};

    public PlayerConfigurationDialog(JFrame parent, Consumer<GameConfiguration> onConfigured) {
        super(parent, "Configuration du Jeu", true);
        this.onConfigured = onConfigured;

        setDefaultCloseOperation(JDialog.DO_NOTHING_ON_CLOSE);
        setResizable(false);
        setSize(500, 500);
        setLocationRelativeTo(parent);

        // Initialiser la configuration par défaut
        this.configuration = new GameConfiguration.Builder().build();

        setupUI();
    }

    private void setupUI() {
        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        mainPanel.setBackground(new Color(240, 240, 240));

        // Titre
        JLabel titleLabel = new JLabel("⚙️ Configuration du Jeu");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 20));
        mainPanel.add(titleLabel);
        mainPanel.add(Box.createVerticalStrut(15));

        // Nom du joueur
        mainPanel.add(createLabeledField("Nom du joueur:", playerNameField = new JTextField(20)));

        // Couleur
        colorCombo = new JComboBox<>(COLORS);
        mainPanel.add(createLabeledComponent("Couleur de voiture:", colorCombo));

        // Terrain
        terrainCombo = new JComboBox<>(TERRAINS);
        mainPanel.add(createLabeledComponent("Terrain:", terrainCombo));

        // Difficulté
        difficultyCombo = new JComboBox<>(DIFFICULTIES);
        mainPanel.add(createLabeledComponent("Difficulté:", difficultyCombo));

        // Mode de jeu
        gameModeCombo = new JComboBox<>(GAME_MODES);
        gameModeCombo.addActionListener(e -> updateTargetDistanceVisibility());
        mainPanel.add(createLabeledComponent("Mode de jeu:", gameModeCombo));

        // Distance cible (pour mode 2 joueurs)
        SpinnerNumberModel model = new SpinnerNumberModel(1000.0, 100.0, 10000.0, 100.0);
        targetDistanceSpinner = new JSpinner(model);
        mainPanel.add(createLabeledComponent("Distance cible:", targetDistanceSpinner));

        mainPanel.add(Box.createVerticalStrut(20));

        // Boutons
        JPanel buttonPanel = new JPanel();
        buttonPanel.setBackground(new Color(240, 240, 240));
        JButton startButton = new JButton("Démarrer");
        JButton cancelButton = new JButton("Annuler");

        startButton.addActionListener(e -> onStartClicked());
        cancelButton.addActionListener(e -> onCancelClicked());

        buttonPanel.add(startButton);
        buttonPanel.add(cancelButton);
        mainPanel.add(buttonPanel);

        setContentPane(mainPanel);
    }

    /**
     * Affiche ou cache le spinner de distance selon le mode de jeu.
     */
    private void updateTargetDistanceVisibility() {
        boolean visible = gameModeCombo.getSelectedIndex() != 0; // Visible si pas "1 Joueur"
        // Vous pouvez implémenter une meilleure visibilité ici
    }

    private JPanel createLabeledField(String label, JTextField field) {
        JPanel panel = new JPanel(new BorderLayout(10, 0));
        panel.setBackground(new Color(240, 240, 240));
        JLabel labelComponent = new JLabel(label);
        labelComponent.setPreferredSize(new Dimension(150, 25));
        panel.add(labelComponent, BorderLayout.WEST);
        panel.add(field, BorderLayout.CENTER);
        return panel;
    }

    private JPanel createLabeledComponent(String label, JComponent component) {
        JPanel panel = new JPanel(new BorderLayout(10, 0));
        panel.setBackground(new Color(240, 240, 240));
        JLabel labelComponent = new JLabel(label);
        labelComponent.setPreferredSize(new Dimension(150, 25));
        panel.add(labelComponent, BorderLayout.WEST);
        panel.add(component, BorderLayout.CENTER);
        return panel;
    }

    private void onStartClicked() {
        // Construire la configuration à partir des sélections
        String playerName = playerNameField.getText().trim();
        if (playerName.isEmpty()) {
            playerName = "Joueur " + (System.currentTimeMillis() % 100);
        }

        Color color = getColorFromSelection(colorCombo.getSelectedIndex());
        String terrain = (String) terrainCombo.getSelectedItem();
        String difficulty = getDifficultyString((String) difficultyCombo.getSelectedItem());
        String gameMode = getGameModeString((String) gameModeCombo.getSelectedItem());
        double targetDistance = (Double) targetDistanceSpinner.getValue();

        this.configuration = new GameConfiguration.Builder()
                .playerName(playerName)
                .playerCarColor(color)
                .terrain(terrain)
                .difficulty(difficulty)
                .gameMode(gameMode)
                .targetDistance(targetDistance)
                .aiEnabled(gameMode.equals("PlayerVsAI"))
                .build();

        confirmed = true;
        if (onConfigured != null) {
            onConfigured.accept(configuration);
        }
        dispose();
    }

    private void onCancelClicked() {
        confirmed = false;
        dispose();
    }

    private Color getColorFromSelection(int index) {
        switch (index) {
            case 0: return new Color(100, 150, 255);  // Bleu
            case 1: return new Color(255, 100, 100);  // Rouge
            case 2: return new Color(100, 255, 100);  // Vert
            case 3: return new Color(255, 255, 100);  // Jaune
            case 4: return new Color(200, 100, 200);  // Mauve
            case 5: return new Color(255, 165, 0);    // Orange
            default: return new Color(100, 150, 255);
        }
    }

    private String getDifficultyString(String display) {
        switch (display) {
            case "Facile": return "Easy";
            case "Moyen": return "Medium";
            case "Difficile": return "Hard";
            default: return "Medium";
        }
    }

    private String getGameModeString(String display) {
        switch (display) {
            case "1 Joueur": return "SinglePlayer";
            case "Joueur vs IA": return "PlayerVsAI";
            case "Joueur 1 vs Joueur 2": return "TwoPlayersLocal";
            default: return "SinglePlayer";
        }
    }

    public GameConfiguration getConfiguration() {
        return configuration;
    }

    public boolean isConfirmed() {
        return confirmed;
    }
}
