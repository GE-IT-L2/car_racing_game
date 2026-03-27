package ui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Random;

import model.car.*;
import model.game.*;
import model.difficulty.*;

public class GamePanel extends JPanel {

    public enum Mode { ONE_PLAYER, TWO_PLAYERS_LOCAL, TWO_PLAYERS_AI }

    private static final int WIDTH = 800;
    private static final int HEIGHT = 600;
    private static final int LANE_WIDTH = WIDTH / 3;

    private final Mode mode;
    private final Difficulty difficulty;
    private final String terrain;
    private final Color playerColor;
    private final int distanceGoal;
    private final Runnable onReturnToMenu;

    private PlayerCar1 player1;
    private PlayerCar2 player2;

    private Score score;
    private Score scoreP1;
    private Score scoreP2;

    private List<Obstacle> obstacles;

    private boolean running;
    private boolean paused;
    private boolean gameOver;
    private boolean countdownActive;

    private long lastFrameTime;
    private long obstacleTimer;
    private int countdownValue;

    private final Random random;

    public GamePanel(Mode mode, Difficulty difficulty, Color playerColor,
                     String terrain, int distanceGoal, Runnable onReturnToMenu) {
        this.mode = mode;
        this.difficulty = difficulty;
        this.terrain = terrain;
        this.playerColor = playerColor;
        this.distanceGoal = distanceGoal;
        this.onReturnToMenu = onReturnToMenu;

        setPreferredSize(new Dimension(WIDTH, HEIGHT));
        setBackground(getTerrainColor(terrain));
        setFocusable(true);

        obstacles = new ArrayList<>();
        random = new Random();

        score = new Score();
        scoreP1 = new Score();
        scoreP2 = new Score();

        initPlayers();
        setupKeyListeners();
        startCountdown();
        startGameLoop();
    }

    private void initPlayers() {
        player1 = new PlayerCar1(colorName(playerColor));
        difficulty.applyToCar(player1);
        player1.reset();
        player1.resetLane();
        player1.setPositionY(HEIGHT - 100);

        if (mode != Mode.ONE_PLAYER) {
            boolean ai = mode == Mode.TWO_PLAYERS_AI;
            player2 = new PlayerCar2("Red", ai);
            difficulty.applyToCar(player2);
            player2.reset();
            player2.resetLane();
            player2.setPositionY(HEIGHT - 200);
        }

        running = false;
        paused = false;
        gameOver = false;
        obstacleTimer = 0;
    }

    private void startCountdown() {
        countdownActive = true;
        countdownValue = 3;
        lastFrameTime = System.nanoTime();

        Timer countdownTimer = new Timer(1000, e -> {
            Toolkit.getDefaultToolkit().beep();
            countdownValue--;
            if (countdownValue <= 0) {
                countdownActive = false;
                running = true;
                ((Timer) e.getSource()).stop();
            }
            repaint();
        });
        countdownTimer.start();
    }

    private void setupKeyListeners() {
        addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (countdownActive) return;

                if (gameOver) {
                    if (e.getKeyCode() == KeyEvent.VK_SPACE) {
                        resetGame();
                    } else if (e.getKeyCode() == KeyEvent.VK_M) {
                        onReturnToMenu.run();
                    } else if (e.getKeyCode() == KeyEvent.VK_Q) {
                        System.exit(0);
                    }
                    return;
                }

                switch (e.getKeyCode()) {
                    case KeyEvent.VK_LEFT -> player1.moveLeft();
                    case KeyEvent.VK_RIGHT -> player1.moveRight();
                    case KeyEvent.VK_UP -> {
                        double newSpeed = Math.min(player1.getMaxSpeed(), player1.getCurrentSpeed() + player1.getAcceleration() * 0.1);
                        player1.setCurrentSpeed(newSpeed);
                    }
                    case KeyEvent.VK_DOWN -> {
                        double newSpeed = Math.max(0, player1.getCurrentSpeed() - player1.getAcceleration() * 0.1);
                        player1.setCurrentSpeed(newSpeed);
                    }
                    case KeyEvent.VK_P -> paused = !paused;
                    case KeyEvent.VK_A -> {
                        if (player2 != null) player2.moveLeft();
                    }
                    case KeyEvent.VK_D -> {
                        if (player2 != null) player2.moveRight();
                    }
                    case KeyEvent.VK_W -> {
                        if (player2 != null) {
                            double newSpeed = Math.min(player2.getMaxSpeed(), player2.getCurrentSpeed() + player2.getAcceleration() * 0.1);
                            player2.setCurrentSpeed(newSpeed);
                        }
                    }
                    case KeyEvent.VK_S -> {
                        if (player2 != null) {
                            double newSpeed = Math.max(0, player2.getCurrentSpeed() - player2.getAcceleration() * 0.1);
                            player2.setCurrentSpeed(newSpeed);
                        }
                    }
                }
            }
        });
    }

    private void startGameLoop() {
        Timer timer = new Timer(16, e -> {
            long now = System.nanoTime();
            double deltaTime = (now - lastFrameTime) / 1_000_000_000.0;
            lastFrameTime = now;

            if (running && !paused && !gameOver) {
                updateGame(deltaTime);
            }
            repaint();
        });
        timer.start();
    }

    private void updateGame(double deltaTime) {
        player1.update(deltaTime);
        if (player2 != null) player2.update(deltaTime);

        obstacleTimer += (long) (deltaTime * 1000);
        if (obstacleTimer >= difficulty.getObstacleFrequency()) {
            obstacleTimer = 0;
            createObstacle();
        }

        Iterator<Obstacle> iterator = obstacles.iterator();
        while (iterator.hasNext()) {
            Obstacle obs = iterator.next();
            obs.update(deltaTime);
            if (obs.isOutOfBounds(HEIGHT)) {
                iterator.remove();
                score.ajouterPoints(10);
                scoreP1.ajouterPoints(10);
                if (scoreP2 != null) scoreP2.ajouterPoints(10);
            }

            if (checkCollision(player1, obs)) {
                triggerGameOver("Joueur 1 s'écrase !");
                return;
            }
            if (player2 != null && checkCollision(player2, obs)) {
                if (mode == Mode.TWO_PLAYERS_LOCAL || mode == Mode.TWO_PLAYERS_AI) {
                    // En mode 2 joueurs, collision donne ralentissement temporaire
                    player2.setCurrentSpeed(player2.getCurrentSpeed() * 0.7);
                }
                if (mode == Mode.ONE_PLAYER) {
                    triggerGameOver("Collision !");
                    return;
                }
            }
        }

        if (mode != Mode.ONE_PLAYER) {
            if (player1.getDistanceTraveled() >= distanceGoal) {
                triggerGameOver("Joueur 1 gagne !");
            } else if (player2.getDistanceTraveled() >= distanceGoal) {
                triggerGameOver("Joueur 2 gagne !");
            }
        }
    }

    private void triggerGameOver(String reason) {
        Toolkit.getDefaultToolkit().beep();
        running = false;
        gameOver = true;
        paused = false;

        score.finDePartie();
        scoreP1.finDePartie();
        if (player2 != null) scoreP2.finDePartie();

        System.out.println("Game over: " + reason);
    }

    private void createObstacle() {
        int lane = random.nextInt(3);
        double x = lane * LANE_WIDTH + LANE_WIDTH / 2.0;
        int w = 30 + random.nextInt(40);
        int h = 30 + random.nextInt(30);
        boolean mobile = difficulty.shouldSpawnMobileObstacle();
        obstacles.add(new Obstacle(x, -h, w, h, mobile));
    }

    private boolean checkCollision(Car player, Obstacle obstacle) {
        double left = player.getPositionX() - 20;
        double right = player.getPositionX() + 20;
        double top = player.getPositionY() - 40;
        double bottom = player.getPositionY() + 40;

        double obsLeft = obstacle.getPositionX() - obstacle.getWidth() / 2.0;
        double obsRight = obstacle.getPositionX() + obstacle.getWidth() / 2.0;
        double obsTop = obstacle.getPositionY() - obstacle.getHeight() / 2.0;
        double obsBottom = obstacle.getPositionY() + obstacle.getHeight() / 2.0;

        return left < obsRight && right > obsLeft && top < obsBottom && bottom > obsTop;
    }

    private void resetGame() {
        initPlayers();
        obstacles.clear();
        score.reinitialiser();
        scoreP1.reinitialiser();
        if (scoreP2 != null) scoreP2.reinitialiser();
        gameOver = false;
        paused = false;
        startCountdown();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g;
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        drawRoad(g2);
        drawObstacles(g2);
        drawPlayer(g2);
        if (player2 != null) drawPlayer2(g2);
        drawHud(g2);

        if (countdownActive) drawCountdown(g2);
        if (paused) drawPaused(g2);
        if (gameOver) drawGameOver(g2);
    }

    private void drawRoad(Graphics2D g2) {
        g2.setColor(new Color(50, 50, 50));
        g2.fillRect(0, 0, WIDTH, HEIGHT);

        // Road edges
        g2.setColor(Color.GRAY);
        g2.fillRect(0, 0, 10, HEIGHT);
        g2.fillRect(WIDTH - 10, 0, 10, HEIGHT);

        // Moving lane lines
        g2.setColor(Color.YELLOW);
        g2.setStroke(new BasicStroke(3));
        int offset = (int) (System.currentTimeMillis() / 50) % 80; // Moving effect
        for (int y = -20 - offset; y < HEIGHT; y += 80) {
            g2.drawLine(LANE_WIDTH, y, LANE_WIDTH, y + 40);
            g2.drawLine(LANE_WIDTH * 2, y, LANE_WIDTH * 2, y + 40);
        }

        // Add some road texture
        g2.setColor(new Color(60, 60, 60));
        for (int i = 0; i < HEIGHT; i += 20) {
            g2.drawLine(10, i, WIDTH - 10, i);
        }
    }

    private void drawPlayer(Graphics2D g2) {
        int x = (int) player1.getPositionX() - 20;
        int y = (int) player1.getPositionY() - 40;

        // Car body
        g2.setColor(playerColor);
        g2.fillRoundRect(x, y, 40, 80, 10, 10);

        // Car outline
        g2.setColor(Color.BLACK);
        g2.setStroke(new BasicStroke(2));
        g2.drawRoundRect(x, y, 40, 80, 10, 10);

        // Wheels
        g2.setColor(Color.BLACK);
        g2.fillOval(x - 5, y + 10, 10, 15);
        g2.fillOval(x + 35, y + 10, 10, 15);
        g2.fillOval(x - 5, y + 55, 10, 15);
        g2.fillOval(x + 35, y + 55, 10, 15);

        // Windshield
        g2.setColor(new Color(200, 200, 255, 150));
        g2.fillRoundRect(x + 5, y + 5, 30, 25, 5, 5);

        // Headlights
        g2.setColor(Color.YELLOW);
        g2.fillOval(x + 2, y - 5, 8, 8);
        g2.fillOval(x + 30, y - 5, 8, 8);
    }

    private void drawPlayer2(Graphics2D g2) {
        int x = (int) player2.getPositionX() - 20;
        int y = (int) player2.getPositionY() - 40;

        // Car body
        g2.setColor(Color.RED);
        g2.fillRoundRect(x, y, 40, 80, 10, 10);

        // Car outline
        g2.setColor(Color.BLACK);
        g2.setStroke(new BasicStroke(2));
        g2.drawRoundRect(x, y, 40, 80, 10, 10);

        // Wheels
        g2.setColor(Color.BLACK);
        g2.fillOval(x - 5, y + 10, 10, 15);
        g2.fillOval(x + 35, y + 10, 10, 15);
        g2.fillOval(x - 5, y + 55, 10, 15);
        g2.fillOval(x + 35, y + 55, 10, 15);

        // Windshield
        g2.setColor(new Color(200, 200, 255, 150));
        g2.fillRoundRect(x + 5, y + 5, 30, 25, 5, 5);

        // Headlights
        g2.setColor(Color.YELLOW);
        g2.fillOval(x + 2, y - 5, 8, 8);
        g2.fillOval(x + 30, y - 5, 8, 8);
    }

    private void drawObstacles(Graphics2D g2) {
        for (Obstacle obs : obstacles) {
            g2.setColor(obs.isMobile() ? Color.ORANGE : Color.RED);
            g2.fillRect((int) (obs.getPositionX() - obs.getWidth() / 2.0), (int) (obs.getPositionY() - obs.getHeight() / 2.0), obs.getWidth(), obs.getHeight());
            g2.setColor(Color.BLACK);
            g2.drawRect((int) (obs.getPositionX() - obs.getWidth() / 2.0), (int) (obs.getPositionY() - obs.getHeight() / 2.0), obs.getWidth(), obs.getHeight());
        }
    }

    private void drawHud(Graphics2D g2) {
        g2.setColor(Color.WHITE);
        g2.setFont(new Font("Arial", Font.BOLD, 16));
        if (mode == Mode.ONE_PLAYER) {
            g2.drawString("Score: " + (int) player1.getDistanceTraveled(), 20, 30);
            g2.drawString("Record: " + score.getMeilleurScore(), 20, 60);
            g2.drawString("Parties: " + score.getPartiesJouees(), 20, 90);
            g2.drawString(String.format("Moyenne: %.1f", score.getScoreMoyen()), 20, 120);
        } else {
            g2.drawString("P1: " + (int) player1.getDistanceTraveled(), 20, 30);
            g2.drawString("P2: " + (int) player2.getDistanceTraveled(), 20, 60);
            g2.drawString("Cible: " + distanceGoal, 20, 90);
        }
        g2.drawString("Vitesse: " + (int) player1.getCurrentSpeed(), 600, 30);
        g2.drawString("Difficulté: " + difficulty.getName(), 600, 60);
        g2.drawString("Terrain: " + terrain, 600, 90);
        g2.drawString("P=Pause, M=Menu, Q=Quit", 20, 570);
    }

    private void drawCountdown(Graphics2D g2) {
        g2.setColor(new Color(0, 0, 0, 180));
        g2.fillRect(0, 0, WIDTH, HEIGHT);
        g2.setColor(Color.WHITE);
        g2.setFont(new Font("Arial", Font.BOLD, 72));
        String text = countdownValue > 0 ? String.valueOf(countdownValue) : "GO";
        FontMetrics fm = g2.getFontMetrics();
        int x = (WIDTH - fm.stringWidth(text)) / 2;
        int y = HEIGHT / 2;
        g2.drawString(text, x, y);
    }

    private void drawPaused(Graphics2D g2) {
        g2.setColor(new Color(0, 0, 0, 160));
        g2.fillRect(0, 0, WIDTH, HEIGHT);
        g2.setColor(Color.YELLOW);
        g2.setFont(new Font("Arial", Font.BOLD, 40));
        String text = "PAUSE";
        FontMetrics fm = g2.getFontMetrics();
        int x = (WIDTH - fm.stringWidth(text)) / 2;
        int y = HEIGHT / 2;
        g2.drawString(text, x, y);
    }

    private void drawGameOver(Graphics2D g2) {
        g2.setColor(new Color(0, 0, 0, 160));
        g2.fillRect(0, 0, WIDTH, HEIGHT);
        g2.setColor(Color.RED);
        g2.setFont(new Font("Arial", Font.BOLD, 36));
        String title = "GAME OVER";
        FontMetrics fm = g2.getFontMetrics();
        int x = (WIDTH - fm.stringWidth(title)) / 2;
        int y = HEIGHT / 2 - 80;
        g2.drawString(title, x, y);

        String result;
        if (mode == Mode.ONE_PLAYER) {
            result = "Score final: " + (int) player1.getDistanceTraveled();
        } else {
            result = player1.getDistanceTraveled() >= distanceGoal ? "Joueur 1 gagne !" : "Joueur 2 gagne !";
        }
        g2.setColor(Color.WHITE);
        g2.setFont(new Font("Arial", Font.PLAIN, 24));
        y += 50;
        x = (WIDTH - g2.getFontMetrics().stringWidth(result)) / 2;
        g2.drawString(result, x, y);

        String actions = "SPACE=Rejouer | M=Menu | Q=Quitter";
        y += 40;
        x = (WIDTH - g2.getFontMetrics().stringWidth(actions)) / 2;
        g2.drawString(actions, x, y);
    }

    private Color getTerrainColor(String terrain) {
        return switch (terrain.toLowerCase()) {
            case "desert", "désert" -> new Color(210, 185, 120);
            case "montagne" -> new Color(110, 115, 120);
            case "ville" -> new Color(70, 70, 80);
            default -> new Color(34, 34, 34);
        };
    }

    private static String colorName(Color color) {
        if (Color.BLUE.equals(color)) return "Blue";
        if (Color.RED.equals(color)) return "Red";
        if (Color.GREEN.equals(color)) return "Green";
        if (Color.ORANGE.equals(color)) return "Orange";
        return "Blue";
    }

    private static class Obstacle {
        private double x, y;
        private int width, height;
        private boolean mobile;
        private double dx;
        private double speedY;

        public Obstacle(double x, double y, int width, int height, boolean mobile) {
            this.x = x;
            this.y = y;
            this.width = width;
            this.height = height;
            this.mobile = mobile;
            this.speedY = 220;
            this.dx = mobile ? (new Random().nextBoolean() ? 40 : -40) : 0;
        }

        public void update(double deltaTime) {
            y += speedY * deltaTime;
            if (mobile) {
                x += dx * deltaTime;
                if (x < width / 2.0) {
                    x = width / 2.0;
                    dx = -dx;
                } else if (x > WIDTH - width / 2.0) {
                    x = WIDTH - width / 2.0;
                    dx = -dx;
                }
            }
        }

        public boolean isOutOfBounds(int heightLimit) {
            return y - height / 2.0 > heightLimit;
        }

        public double getPositionX() { return x; }
        public double getPositionY() { return y; }
        public int getWidth() { return width; }
        public int getHeight() { return height; }
        public boolean isMobile() { return mobile; }
    }
}

