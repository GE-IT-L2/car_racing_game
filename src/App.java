import model.car.*;
import model.difficulty.*;
import model.game.GameConfiguration;
import ui.*;
import ui.menu.*;
import ui.scene.*;

import javax.swing.*;
import java.awt.*;

/**
 * Application principale du Racing Game v4.0.
 * Coordonne les menus, les panels de jeu, la navigation et la configuration.
 */
public class App extends JFrame {
    private JPanel currentPanel;
    private CardLayout cardLayout;
    private JPanel cardPanel;

    private Difficulty selectedDifficulty = new Medium();
    private String selectedTerrain = "Ville";
    private Color selectedColor = new Color(100, 150, 255);
    private GameConfiguration lastConfiguration = null;

    public App() {
        setTitle("🏎️ RACING GAME v4.0 - Amélioré");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setResizable(false);
        setLocationRelativeTo(null);

        // CardLayout pour naviguer entre les écrans
        cardLayout = new CardLayout();
        cardPanel = new JPanel(cardLayout);

        // Écran du menu principal
        MenuPrincipal menuPrincipal = new MenuPrincipal(
            e -> showConfiguration("SinglePlayer"),
            e -> showConfiguration("TwoPlayers"),
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

    /**
     * Affiche le dialogue de configuration.
     */
    private void showConfiguration(String gameMode) {
        GameConfiguration.Builder builder = new GameConfiguration.Builder()
                .difficulty("Medium")
                .terrain("Ville")
                .gameMode(gameMode.equals("SinglePlayer") ? "SinglePlayer" : "PlayerVsAI");

        PlayerConfigurationDialog configDialog = new PlayerConfigurationDialog(this, config -> {
            this.lastConfiguration = config;
            startGame(config);
        });
        configDialog.setVisible(true);
    }

    /**
     * Démarre le jeu avec la configuration spécifiée.
     */
    private void startGame(GameConfiguration config) {
        // Obtenir la difficulté
        Difficulty difficulty = getDifficultyFromString(config.getDifficultyMode());
        selectedDifficulty = difficulty;

        // Créer le paysage
        Paysage paysage = createPaysage(config.getTerrain());

        if (config.isSinglePlayer()) {
            startSinglePlayer(config, difficulty, paysage);
        } else if (config.isPlayerVsAI()) {
            startPlayerVsAI(config, difficulty, paysage);
        } else if (config.isTwoPlayersLocal()) {
            startTwoPlayersLocal(config, difficulty, paysage);
        }
    }

    /**
     * Démarre une partie en solo.
     */
    private void startSinglePlayer(GameConfiguration config, Difficulty difficulty, Paysage paysage) {
        // Créer la voiture du joueur
        PlayerCar player = new PlayerCar(config.getPlayerCarColor().toString());
        difficulty.applyToCar(player);
        player.setPositionY(500.0);
        player.resetLane();

        // Créer le panel de jeu
        GameRacePanel gamePanel = new GameRacePanel(player, difficulty, paysage, () -> {
            endGame(config.getPlayerName(), (int) player.getDistanceTraveled(), false);
        });

        // Afficher le jeu
        setContentPane(gamePanel);
        revalidate();
        gamePanel.requestFocus();
    }

    /**
     * Démarre une partie Joueur vs IA.
     */
    private void startPlayerVsAI(GameConfiguration config, Difficulty difficulty, Paysage paysage) {
        // Joueur 1
        PlayerCar player1 = new PlayerCar(config.getPlayerCarColor().toString());
        difficulty.applyToCar(player1);
        player1.setPositionY(500.0);
        player1.resetLane();

        // IA (Joueur 2)
        AIPlayer aiPlayer = new AIPlayer(difficulty);
        difficulty.applyToCar(aiPlayer);
        aiPlayer.setPositionY(500.0);
        aiPlayer.resetLane();

        // Créer le panel 2 joueurs
        final GameRacePanel2Players[] gamePanel = new GameRacePanel2Players[1];
        gamePanel[0] = new GameRacePanel2Players(
                player1, aiPlayer, difficulty, paysage,
                config.getTargetDistance(),
                () -> {
                    endGame2Players(config.getPlayerName(), gamePanel[0].getEngine());
                }
        );

        // Afficher le jeu
        setContentPane(gamePanel[0]);
        revalidate();
        gamePanel[0].requestFocus();
    }

    /**
     * Démarre une partie 2 Joueurs locaux.
     */
    private void startTwoPlayersLocal(GameConfiguration config, Difficulty difficulty, Paysage paysage) {
        // Joueur 1
        PlayerCar player1 = new PlayerCar1(config.getPlayerCarColor().toString());
        difficulty.applyToCar(player1);
        player1.setPositionY(500.0);
        player1.resetLane();

        // Joueur 2
        PlayerCar player2 = new PlayerCar2("Red");
        difficulty.applyToCar(player2);
        player2.setPositionY(500.0);
        player2.resetLane();

        // Créer le panel 2 joueurs
        final GameRacePanel2Players[] gamePanel = new GameRacePanel2Players[1];
        gamePanel[0] = new GameRacePanel2Players(
                player1, player2, difficulty, paysage,
                config.getTargetDistance(),
                () -> {
                    endGame2Players(config.getPlayerName(), gamePanel[0].getEngine());
                }
        );

        // Afficher le jeu
        setContentPane(gamePanel[0]);
        revalidate();
        gamePanel[0].requestFocus();
    }

    private void showSettings() {
        JOptionPane.showMessageDialog(this, "⚙️ Paramètres\n\nVersion 4.0\n\nModes de jeu:\n- Solo\n- Joueur vs IA\n- Joueur 1 vs Joueur 2\n\nDifficultés: Facile, Moyen, Difficile\nTerrains: Ville, Désert, Campagne, Paysage");
    }

    /**
     * Affiche l'écran de fin de jeu pour mode solo.
     */
    private void endGame(String playerName, int score, boolean isWin) {
        MenuGameOver menuGameOver = new MenuGameOver(score, playerName, e -> {
            if (lastConfiguration != null) {
                startGame(lastConfiguration);
            } else {
                returnToMainMenu();
            }
        }, e -> returnToMainMenu());

        setContentPane(menuGameOver);
        revalidate();
    }

    /**
     * Affiche l'écran de fin de jeu pour mode 2 joueurs.
     */
    private void endGame2Players(String player1Name, engine.GameEngine2Players engine) {
        int winner = engine.getWinner();
        String winnerMessage = "";
        if (winner == 1) {
            winnerMessage = player1Name + " GAGNE!";
        } else if (winner == 2) {
            winnerMessage = "JOUEUR 2 GAGNE!";
        } else {
            winnerMessage = "ÉGALITÉ!";
        }

        int choice = JOptionPane.showConfirmDialog(this,
                winnerMessage + "\n\nScore P1: " + engine.getScore1().getScoreInt() +
                "\nScore P2: " + engine.getScore2().getScoreInt() +
                "\n\nVoulez-vous rejouer?",
                "Fin de la partie", JOptionPane.YES_NO_OPTION);

        if (choice == JOptionPane.YES_OPTION) {
            if (lastConfiguration != null) {
                startGame(lastConfiguration);
            }
        } else {
            returnToMainMenu();
        }
    }

    private void returnToMainMenu() {
        MenuPrincipal menuPrincipal = new MenuPrincipal(
            e -> showConfiguration("SinglePlayer"),
            e -> showConfiguration("TwoPlayers"),
            e -> showSettings(),
            e -> System.exit(0)
        );

        setContentPane(menuPrincipal);
        revalidate();
        menuPrincipal.requestFocus();
    }

    /**
     * Crée un objet Paysage selon le terrain spécifié.
     */
    private Paysage createPaysage(String terrain) {
        switch (terrain) {
            case "Ville":
                return new Ville();
            case "Desert":
                return new Desert();
            case "Campagne":
                return new Campagne();
            case "Paysage":
                return new Paysage("Paysage") {
                    @Override
                    public void draw(java.awt.Graphics2D g, int width, int height) {
                        g.setColor(getPrimaryColor());
                        g.fillRect(0, 0, width, height);
                    }
                    
                    @Override
                    public java.awt.Color getPrimaryColor() {
                        return new java.awt.Color(34, 34, 34);
                    }

                    @Override
                    public java.awt.Color getRoadColor() {
                        return new java.awt.Color(100, 100, 100);
                    }
                };
            default:
                return new Ville();
        }
    }

    /**
     * Retourne un objet Difficulty selon le nom.
     */
    private Difficulty getDifficultyFromString(String diffName) {
        switch (diffName) {
            case "Easy":
                return new Easy();
            case "Medium":
                return new Medium();
            case "Hard":
                return new Hard();
            default:
                return new Medium();
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new App());
    }
}