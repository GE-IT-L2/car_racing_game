import model.car.*;
import model.difficulty.*;
import ui.*;
import ui.menu.*;
import ui.scene.*;

import javax.swing.*;
import java.awt.*;

/**
 * Application principale du Racing Game.
 * Coordonne les menus, les panels de jeu et la navigation.
 */
public class App extends JFrame {
    private JPanel currentPanel;
    private CardLayout cardLayout;
    private JPanel cardPanel;

    private Difficulty selectedDifficulty = new Medium();
    private String selectedTerrain = "Ville";
    private Color selectedColor = new Color(100, 150, 255);

    public App() {
        setTitle("🏎️ RACING GAME v3.0");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setResizable(false);
        setLocationRelativeTo(null);

        // CardLayout pour naviguer entre les écrans
        cardLayout = new CardLayout();
        cardPanel = new JPanel(cardLayout);

        // Écran du menu principal
        MenuPrincipal menuPrincipal = new MenuPrincipal(
            e -> startSinglePlayer(),
            e -> startTwoPlayers(),
            e -> showSettings(),
            e -> System.exit(0)
        );
        cardPanel.add(menuPrincipal, "MENU");

        setContentPane(cardPanel);
        pack();
        setSize(800, 600);
        setLocationRelativeTo(null);
        setVisible(true);

        // Afficher le menu principal
        cardLayout.show(cardPanel, "MENU");
    }

    private void startSinglePlayer() {
        // Créer la voiture du joueur
        PlayerCar player = new PlayerCar("Blue");
        selectedDifficulty.applyToCar(player);
        player.setPositionY(500.0);
        player.setPositionX(400.0);

        // Créer le paysage
        Paysage paysage = createPaysage(selectedTerrain);

        // Créer le panel de jeu
        GameRacePanel gamePanel = new GameRacePanel(player, selectedDifficulty, paysage, () -> {
            endGame((int) player.getDistanceTraveled());
        });

        // Afficher le jeu en plein écran
        setContentPane(gamePanel);
        revalidate();
        gamePanel.requestFocus();
    }

    private void startTwoPlayers() {
        JOptionPane.showMessageDialog(this, "Mode 2 Joueurs - À implémenter");
        // À implémenter
    }

    private void showSettings() {
        JOptionPane.showMessageDialog(this, "Paramètres - À implémenter");
        // À implémenter avec un dialogue de configuration
    }

    private void endGame(int score) {
        MenuGameOver menuGameOver = new MenuGameOver(score, "Joueur 1", e -> {
            startSinglePlayer();
        }, e -> returnToMainMenu());

        setContentPane(menuGameOver);
        revalidate();
    }

    private void returnToMainMenu() {
        MenuPrincipal menuPrincipal = new MenuPrincipal(
            e -> startSinglePlayer(),
            e -> startTwoPlayers(),
            e -> showSettings(),
            e -> System.exit(0)
        );

        setContentPane(menuPrincipal);
        revalidate();
        menuPrincipal.requestFocus();
    }

    private Paysage createPaysage(String terrain) {
        switch (terrain) {
            case "Ville":
                return new Ville();
            case "Désert":
                return new Desert();
            case "Campagne":
                return new Campagne();
            default:
                return new Ville();
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new App());
    }
}