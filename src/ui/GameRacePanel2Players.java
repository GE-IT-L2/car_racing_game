package ui;

import engine.GameEngine2Players;
import model.car.PlayerCar;
import model.car.AIPlayer;
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
import java.util.List;
import java.util.ArrayList;

/**
 * Panel du jeu pour mode 2 joueurs (local ou vs IA).
 * Gère le rendu et la logique pour deux voitures avec scores indépendants.
 */
public class GameRacePanel2Players extends JPanel {
    private static final int WIDTH = 800;
    private static final int HEIGHT = 600;
    private static final int FPS = 60;
    private static final int LANE_WIDTH = WIDTH / 3;

    private GameEngine2Players engine;
    private PlayerCar player1;
    private PlayerCar player2;
    private Difficulty difficulty;
    private Paysage paysage;
    private Runnable onGameOver;

    // Contrôles
    private Set<Integer> keysPressed = new HashSet<>();

    // Affichage
    private VoitureView player1View;
    private VoitureView player2View;
    private Map<Obstacle, ObstacleView> obstacleViews = new HashMap<>();

    // État
    private long gameStartTime;
    private boolean gameRunning = true;

    public GameRacePanel2Players(PlayerCar player1, PlayerCar player2, Difficulty difficulty,
                                Paysage paysage, double targetDistance, Runnable onGameOver) {
        this.player1 = player1;
        this.player2 = player2;
        this.difficulty = difficulty;
        this.paysage = paysage;
        this.onGameOver = onGameOver;

        setPreferredSize(new Dimension(WIDTH, HEIGHT));
        setBackground(paysage.getPrimaryColor());
        setFocusable(true);

        // Initialiser le moteur
        boolean isAI = player2 instanceof AIPlayer;
        engine = new GameEngine2Players(WIDTH, HEIGHT, difficulty, targetDistance, isAI);
        engine.setPlayer1(player1);
        engine.setPlayer2(player2);
        gameStartTime = System.currentTimeMillis();

        // Vues des joueurs
        player1View = new VoitureView(player1, player1.getColor() != null ? 
                                     new Color(100, 150, 255) : new Color(100, 150, 255));
        player2View = new VoitureView(player2, player2.getColor() != null ? 
                                     new Color(255, 100, 100) : new Color(255, 100, 100));

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
        boolean collision = engine.checkPlayer1Collisions() || engine.checkPlayer2Collisions();
        if (collision) {
            gameRunning = false;
            engine.setGameState(GameState.GAME_OVER);
            if (onGameOver != null) {
                onGameOver.run();
            }
            return;
        }

        // Vérifier si le jeu est terminé (quelqu'un a atteint la distance)
        if (engine.getGameState() == GameState.FINISHED) {
            gameRunning = false;
            if (onGameOver != null) {
                onGameOver.run();
            }
        }

        // Créer les vues des obstacles
        for (Obstacle obstacle : engine.getObstacles()) {
            if (!obstacleViews.containsKey(obstacle)) {
                obstacleViews.put(obstacle, new ObstacleView(obstacle));
            }
        }

        // Nettoyer les vues des obstacles supprimés (itération sécurisée)
        List<Obstacle> obstaclesToRemove = new ArrayList<>();
        for (Obstacle obstacle : obstacleViews.keySet()) {
            if (!engine.getObstacles().contains(obstacle)) {
                obstaclesToRemove.add(obstacle);
            }
        }
        for (Obstacle obstacle : obstaclesToRemove) {
            obstacleViews.remove(obstacle);
        }
    }

    private void handleInput() {
        // Joueur 1 (Flèches ou WASD)
        if (keysPressed.contains(KeyEvent.VK_LEFT) || keysPressed.contains(KeyEvent.VK_A)) {
            player1.moveLeft(LANE_WIDTH, WIDTH);
        }
        if (keysPressed.contains(KeyEvent.VK_RIGHT) || keysPressed.contains(KeyEvent.VK_D)) {
            player1.moveRight(LANE_WIDTH, WIDTH);
        }
        // Joueur 1: ESPACE pour revenir au centre
        if (keysPressed.contains(KeyEvent.VK_SPACE)) {
            player1.resetLane();
        }

        // Joueur 2 (Devrait être contrôlé par l'IA ou un deuxième joueur)
        // Pour le mode local 2 joueurs, on peut ajouter des contrôles additionnels
        if (!(player2 instanceof AIPlayer)) {
            // Mode 2 joueurs local - contrôles pour joueur 2
            if (keysPressed.contains(KeyEvent.VK_Q)) {
                player2.moveLeft(LANE_WIDTH, WIDTH);
            }
            if (keysPressed.contains(KeyEvent.VK_E)) {
                player2.moveRight(LANE_WIDTH, WIDTH);
            }
            // Joueur 2: W pour revenir au centre
            if (keysPressed.contains(KeyEvent.VK_W)) {
                player2.resetLane();
            }
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

        // Dessiner les voitures
        player1View.draw(g2d);
        player2View.draw(g2d);

        // Dessiner l'HUD
        drawHUD(g2d);
    }

    private void drawHUD(Graphics2D g) {
        g.setColor(Color.WHITE);
        g.setFont(new Font("Arial", Font.BOLD, 14));

        // Joueur 1 (gauche)
        g.drawString("P1: " + player1.getClass().getSimpleName(), 20, 30);
        g.drawString("Score: " + engine.getScore1().getScoreInt(), 20, 50);
        g.drawString("Dist: " + (int) engine.getScore1().getDistance() + "m", 20, 70);

        // Joueur 2 (droite)
        g.drawString("P2: " + player2.getClass().getSimpleName(), WIDTH - 150, 30);
        g.drawString("Score: " + engine.getScore2().getScoreInt(), WIDTH - 150, 50);
        g.drawString("Dist: " + (int) engine.getScore2().getDistance() + "m", WIDTH - 150, 70);

        // Vitesse du jeu (centre)
        g.drawString("Speed: " + (int) engine.getGameSpeed(), WIDTH / 2 - 50, 30);
        g.drawString("Distance cible: " + (int) engine.getTargetDistance() + "m", WIDTH / 2 - 100, 50);

        // État du jeu
        if (engine.getGameState() == GameState.PAUSED) {
            g.setColor(new Color(255, 255, 0, 200));
            g.setFont(new Font("Arial", Font.BOLD, 30));
            g.drawString("PAUSE", WIDTH / 2 - 50, HEIGHT / 2);
        } else if (engine.getGameState() == GameState.FINISHED) {
            drawGameOverScreen(g);
        }
    }

    private void drawGameOverScreen(Graphics2D g) {
        g.setColor(new Color(0, 0, 0, 150));
        g.fillRect(0, 0, WIDTH, HEIGHT);

        g.setColor(Color.WHITE);
        g.setFont(new Font("Arial", Font.BOLD, 40));

        int winner = engine.getWinner();
        if (winner == 1) {
            g.drawString("JOUEUR 1 GAGNE!", WIDTH / 2 - 150, HEIGHT / 2 - 50);
        } else if (winner == 2) {
            g.drawString("JOUEUR 2 GAGNE!", WIDTH / 2 - 150, HEIGHT / 2 - 50);
        } else {
            g.drawString("ÉGALITÉ!", WIDTH / 2 - 100, HEIGHT / 2 - 50);
        }

        g.setFont(new Font("Arial", Font.PLAIN, 16));
        g.drawString("Score P1: " + engine.getScore1().getScoreInt(), WIDTH / 2 - 100, HEIGHT / 2 + 20);
        g.drawString("Score P2: " + engine.getScore2().getScoreInt(), WIDTH / 2 - 100, HEIGHT / 2 + 50);
        g.drawString("Appuyez sur ESPACE pour rejouer...", WIDTH / 2 - 150, HEIGHT / 2 + 100);
    }

    public int getWinner() {
        return engine.getWinner();
    }

    public GameEngine2Players getEngine() {
        return engine;
    }
}
