package ui;

import engine.GameEngine;
import model.car.PlayerCar;
import model.difficulty.Difficulty;
import model.game.GameState;
import model.obstacle.Obstacle;
import ui.scene.Paysage;
import ui.view.VoitureView;
import ui.view.ObstacleView;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.*;

/**
 * Panel principal du jeu avec le GameEngine intégré.
 * Gère le rendu, la logique du jeu et les entrées clavier de manière optimisée.
 */
public class GameRacePanel extends JPanel {
    private static final int WIDTH = 800;
    private static final int HEIGHT = 600;
    private static final int FPS = 60;
    private static final int LANE_WIDTH = WIDTH / 3;

    private GameEngine engine;
    private PlayerCar player;
    private Difficulty difficulty;
    private Paysage paysage;
    private Runnable onGameOver;

    // Contrôles
    private Set<Integer> keysPressed = new HashSet<>();

    // Affichage
    private VoitureView playerView;
    private Map<Obstacle, ObstacleView> obstacleViews = new HashMap<>();
    private int score = 0;
    private int distanceTraveled = 0;

    // État
    private long gameStartTime;
    private boolean gameRunning = true;

    public GameRacePanel(PlayerCar player, Difficulty difficulty, Paysage paysage, Runnable onGameOver) {
        this.player = player;
        this.difficulty = difficulty;
        this.paysage = paysage;
        this.onGameOver = onGameOver;

        setPreferredSize(new Dimension(WIDTH, HEIGHT));
        setBackground(paysage.getPrimaryColor());
        setFocusable(true);

        // Initialiser le moteur
        engine = new GameEngine(WIDTH, HEIGHT, difficulty);
        engine.setPlayer(player);
        gameStartTime = System.currentTimeMillis();

        // Vue du joueur
        playerView = new VoitureView(player, new Color(100, 150, 255));

        // Configuration des entrées clavier
        setupKeyBindings();

        // Démarrer la boucle de jeu
        startGameLoop();
    }

    private void setupKeyBindings() {
        addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                keysPressed.add(e.getKeyCode());
            }

            @Override
            public void keyReleased(KeyEvent e) {
                keysPressed.remove(e.getKeyCode());
            }
        });

        // Focus sur le panel
        addHierarchyListener(e -> {
            if (isShowing()) {
                requestFocusInWindow();
            }
        });
    }

    private void startGameLoop() {
        javax.swing.Timer gameTimer = new javax.swing.Timer(1000 / FPS, e -> {
            update();
            repaint();
        });
        gameTimer.start();
    }

    private void update() {
        if (!gameRunning) return;

        // Gestion des entrées clavier
        handleInput();

        // Mise à jour du moteur
        engine.update();

        // Vérification des collisions
        if (engine.checkObstacleCollisions(player)) {
            gameRunning = false;
            engine.setGameState(GameState.GAME_OVER);
            if (onGameOver != null) {
                onGameOver.run();
            }
            return;
        }

        // Mise à jour du score et distance
        distanceTraveled = (int) (engine.getGameSpeed() / 100);
        score += Math.max(0, (int) (engine.getGameSpeed() / 20));

        // Créer les vues des obstacles
        for (Obstacle obstacle : engine.getObstacles()) {
            if (!obstacleViews.containsKey(obstacle)) {
                obstacleViews.put(obstacle, new ObstacleView(obstacle));
            }
        }

        // Nettoyer les vues des obstacles supprimés
        obstacleViews.keySet().removeIf(obstacle -> !engine.getObstacles().contains(obstacle));
    }

    private void handleInput() {
        // Déplacement latéral
        if (keysPressed.contains(KeyEvent.VK_LEFT) || keysPressed.contains(KeyEvent.VK_A) ||
            keysPressed.contains(KeyEvent.VK_Q)) { // AZERTY
            player.moveLeft(LANE_WIDTH, WIDTH);
        }
        if (keysPressed.contains(KeyEvent.VK_RIGHT) || keysPressed.contains(KeyEvent.VK_D)) {
            player.moveRight(LANE_WIDTH, WIDTH);
        }

        // Accélération / Frein (optionnel)
        if (keysPressed.contains(KeyEvent.VK_UP) || keysPressed.contains(KeyEvent.VK_W) ||
            keysPressed.contains(KeyEvent.VK_Z)) { // AZERTY
            // Accélérer
        }
        if (keysPressed.contains(KeyEvent.VK_DOWN) || keysPressed.contains(KeyEvent.VK_S)) {
            // Freiner
        }

        // Pause
        if (keysPressed.contains(KeyEvent.VK_ESCAPE) || keysPressed.contains(KeyEvent.VK_P)) {
            if (engine.getGameState() == GameState.RUNNING) {
                engine.pause();
            } else if (engine.getGameState() == GameState.PAUSED) {
                engine.resume();
            }
        }
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g;
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        // Dessiner le paysage
        paysage.draw(g2d, WIDTH, HEIGHT);

        // Dessiner les obstacles
        for (Obstacle obstacle : engine.getObstacles()) {
            if (obstacleViews.containsKey(obstacle)) {
                obstacleViews.get(obstacle).draw(g2d);
            }
        }

        // Dessiner la voiture du joueur
        playerView.draw(g2d);

        // Dessiner l'HUD
        drawHUD(g2d);
    }

    private void drawHUD(Graphics2D g) {
        g.setColor(Color.WHITE);
        g.setFont(new Font("Arial", Font.BOLD, 16));

        // Score
        g.drawString("Score: " + score, 20, 30);

        // Distance
        g.drawString("Distance: " + distanceTraveled + "m", 20, 60);

        // Vitesse
        g.drawString("Speed: " + (int) engine.getGameSpeed(), 20, 90);

        // État du jeu
        if (engine.getGameState() == GameState.PAUSED) {
            g.setColor(new Color(255, 255, 255, 180));
            g.setFont(new Font("Arial", Font.BOLD, 36));
            String pauseText = "PAUSE";
            FontMetrics metrics = g.getFontMetrics();
            int x = (WIDTH - metrics.stringWidth(pauseText)) / 2;
            g.drawString(pauseText, x, HEIGHT / 2);
        }
    }

    // Getters
    public int getScore() { return score; }
    public int getDistanceTraveled() { return distanceTraveled; }
    public GameState getGameState() { return engine.getGameState(); }
    public GameEngine getEngine() { return engine; }
}
