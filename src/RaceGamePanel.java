import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.*;

/**
 * Panneau principal du jeu Racing avec graphisme 3D
 */
public class RaceGamePanel extends JPanel {

    private static final int WIDTH = 1000;
    private static final int HEIGHT = 700;
    private static final int LANE_WIDTH = 300;
    private static final int NUM_LANES = 3;

    private PlayerCar player;
    private java.util.List<Enemy> enemies;
    private int score = 0;
    private int bestScore = 0;
    private boolean running = true;
    private boolean gameover = false;
    private int gameSpeed = 0;

    private boolean[] keys = new boolean[256];

    public RaceGamePanel() {
        setPreferredSize(new Dimension(WIDTH, HEIGHT));
        setBackground(new Color(20, 20, 25));
        setFocusable(true);

        player = new PlayerCar();
        enemies = new java.util.ArrayList<>();

        addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                keys[e.getKeyCode()] = true;
                if (e.getKeyCode() == KeyEvent.VK_SPACE && gameover) {
                    resetGame();
                }
            }

            @Override
            public void keyReleased(KeyEvent e) {
                keys[e.getKeyCode()] = false;
            }
        });

        startGameLoop();
    }

    private void startGameLoop() {
        javax.swing.Timer timer = new javax.swing.Timer(16, e -> {
            if (running) {
                update();
                repaint();
            }
        });
        timer.start();
    }

    private void update() {
        if (gameover) return;

        if (keys[KeyEvent.VK_LEFT] || keys['A']) player.moveLeft();
        if (keys[KeyEvent.VK_RIGHT] || keys['D']) player.moveRight();
        if (keys[KeyEvent.VK_UP] || keys['W']) player.accelerate();
        if (keys[KeyEvent.VK_DOWN] || keys['S']) player.brake();

        gameSpeed = Math.min(gameSpeed + 1, 800);
        player.updateSpeed(gameSpeed);

        if (Math.random() < 0.015) {
            spawnEnemy();
        }

        java.util.List<Enemy> toRemove = new java.util.ArrayList<>();
        for (Enemy enemy : enemies) {
            enemy.update(gameSpeed);
            if (enemy.y > HEIGHT) {
                toRemove.add(enemy);
                score += 50;
            }
            if (checkCollision(player, enemy)) {
                gameover = true;
                if (score > bestScore) bestScore = score;
            }
        }
        enemies.removeAll(toRemove);
    }

    private void spawnEnemy() {
        int lane = (int) (Math.random() * NUM_LANES);
        enemies.add(new Enemy(lane * LANE_WIDTH + LANE_WIDTH / 2));
    }

    private boolean checkCollision(PlayerCar player, Enemy enemy) {
        double dx = Math.abs(player.x - enemy.x);
        double dy = Math.abs(player.y - enemy.y);
        return dx < 60 && dy < 100;
    }

    private void resetGame() {
        player = new PlayerCar();
        enemies.clear();
        score = 0;
        gameSpeed = 0;
        gameover = false;
        requestFocus();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g;
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        drawBackground(g2d);
        drawRoad3D(g2d);

        for (Enemy enemy : enemies) {
            drawEnemy3D(g2d, enemy);
        }

        drawPlayer3D(g2d, player);
        drawHUD(g2d);

        if (gameover) {
            drawGameOver(g2d);
        }
    }

    private void drawBackground(Graphics2D g2d) {
        GradientPaint skyGradient = new GradientPaint(
                0, 0, new Color(10, 20, 40),
                0, HEIGHT / 3, new Color(20, 50, 100));
        g2d.setPaint(skyGradient);
        g2d.fillRect(0, 0, WIDTH, HEIGHT / 3);

        g2d.setColor(new Color(30, 40, 60, 150));
        g2d.fillRect(0, HEIGHT / 3 - 50, WIDTH, 100);

        g2d.setColor(new Color(20, 20, 25));
        g2d.fillRect(0, HEIGHT / 3, WIDTH, 2 * HEIGHT / 3);
    }

    private void drawRoad3D(Graphics2D g2d) {
        float horizon = HEIGHT / 3;
        float roadBottom = HEIGHT;

        for (int i = 0; i < 30; i++) {
            float progress = (float) i / 30;
            float y = horizon + progress * (roadBottom - horizon);
            float width = progress * WIDTH;

            int alpha = (int) (255 * (1 - progress * 0.5f));
            g2d.setColor(new Color(60, 60, 65, alpha));

            float x = (WIDTH - width) / 2;
            g2d.fillRect((int) x, (int) y, (int) width, 15);

            g2d.setColor(new Color(255, 200, 50, alpha));
            g2d.setStroke(new BasicStroke(2));
            for (int lane = 1; lane < NUM_LANES; lane++) {
                float laneX = x + (width / NUM_LANES) * lane;
                if (i % 2 == 0) {
                    g2d.drawLine((int) laneX, (int) y, (int) laneX, (int) (y + 15));
                }
            }
        }
    }

    private void drawPlayer3D(Graphics2D g2d, PlayerCar car) {
        float scale = 1.2f;
        int carWidth = (int) (60 * scale);
        int carHeight = (int) (100 * scale);

        g2d.setColor(new Color(0, 0, 0, 100));
        g2d.fillOval((int) car.x - carWidth / 2, (int) car.y + carHeight / 2 - 5, carWidth, 15);

        GradientPaint carGradient = new GradientPaint(
                (int) car.x - carWidth / 2, (int) car.y - carHeight / 2,
                new Color(150, 220, 255),
                (int) car.x + carWidth / 2, (int) car.y + carHeight / 2,
                new Color(80, 180, 255));
        g2d.setPaint(carGradient);
        g2d.fillOval((int) car.x - carWidth / 2, (int) car.y - carHeight / 2, carWidth, carHeight);

        g2d.setColor(new Color(40, 100, 200));
        g2d.setStroke(new BasicStroke(3));
        g2d.drawOval((int) car.x - carWidth / 2, (int) car.y - carHeight / 2, carWidth, carHeight);

        g2d.setColor(Color.YELLOW);
        g2d.fillOval((int) car.x - carWidth / 3, (int) car.y - carHeight / 2 - 5, 15, 8);
        g2d.fillOval((int) car.x + carWidth / 3 - 15, (int) car.y - carHeight / 2 - 5, 15, 8);

        float speedRatio = (float) Math.min(car.speed / 10.0f, 1.0f);
        g2d.setColor(new Color(255, (int) (200 * (1 - speedRatio)), 50));
        g2d.fillRect((int) car.x - carWidth / 2 + 5, (int) car.y + carHeight / 2 - 10, (int) ((carWidth - 10) * speedRatio), 5);
    }

    private void drawEnemy3D(Graphics2D g2d, Enemy enemy) {
        float scale = 0.8f;
        int carWidth = (int) (50 * scale);
        int carHeight = (int) (80 * scale);

        g2d.setColor(new Color(0, 0, 0, 80));
        g2d.fillOval((int) enemy.x - carWidth / 2, (int) enemy.y + carHeight / 2 - 5, carWidth, 12);

        GradientPaint enemyGradient = new GradientPaint(
                (int) enemy.x - carWidth / 2, (int) enemy.y - carHeight / 2,
                new Color(255, 150, 100),
                (int) enemy.x + carWidth / 2, (int) enemy.y + carHeight / 2,
                new Color(200, 50, 50));
        g2d.setPaint(enemyGradient);
        g2d.fillOval((int) enemy.x - carWidth / 2, (int) enemy.y - carHeight / 2, carWidth, carHeight);

        g2d.setColor(new Color(150, 30, 30));
        g2d.setStroke(new BasicStroke(2));
        g2d.drawOval((int) enemy.x - carWidth / 2, (int) enemy.y - carHeight / 2, carWidth, carHeight);
    }

    private void drawHUD(Graphics2D g2d) {
        g2d.setColor(new Color(255, 255, 255, 200));
        g2d.setFont(new Font("Arial", Font.BOLD, 20));

        g2d.drawString("Score: " + score, 30, 40);
        g2d.drawString("Record: " + bestScore, 30, 70);
        g2d.drawString("Speed: " + (int) player.speed, 30, 100);

        g2d.setFont(new Font("Arial", Font.PLAIN, 14));
        g2d.drawString("← → ou A/D: Diriger", WIDTH - 280, 40);
        g2d.drawString("↑/W: Accélerer | ↓/S: Freiner", WIDTH - 280, 65);

        g2d.setColor(new Color(100, 200, 255, 150));
        int barWidth = 200;
        int barHeight = 30;
        g2d.fillRect(WIDTH / 2 - barWidth / 2, HEIGHT - 60, barWidth, barHeight);

        g2d.setColor(new Color(100, 200, 255, 255));
        float speedRatio = (float) Math.min(player.speed / 10.0f, 1.0f);
        g2d.fillRect(WIDTH / 2 - barWidth / 2, HEIGHT - 60, (int) (barWidth * speedRatio), barHeight);

        g2d.setColor(Color.WHITE);
        g2d.setFont(new Font("Arial", Font.BOLD, 12));
        g2d.drawString("SPEED", WIDTH / 2 - 30, HEIGHT - 35);
    }

    private void drawGameOver(Graphics2D g2d) {
        g2d.setColor(new Color(0, 0, 0, 150));
        g2d.fillRect(0, 0, WIDTH, HEIGHT);

        g2d.setColor(new Color(255, 50, 50, 255));
        g2d.setFont(new Font("Arial", Font.BOLD, 80));
        FontMetrics fm = g2d.getFontMetrics();
        String gameOverText = "GAME OVER";
        int x = (WIDTH - fm.stringWidth(gameOverText)) / 2;
        g2d.drawString(gameOverText, x, HEIGHT / 2 - 80);

        g2d.setColor(Color.WHITE);
        g2d.setFont(new Font("Arial", Font.BOLD, 32));
        String scoreText = "Score: " + score;
        fm = g2d.getFontMetrics();
        x = (WIDTH - fm.stringWidth(scoreText)) / 2;
        g2d.drawString(scoreText, x, HEIGHT / 2 + 20);

        String recordText = "Record: " + bestScore;
        fm = g2d.getFontMetrics();
        x = (WIDTH - fm.stringWidth(recordText)) / 2;
        g2d.drawString(recordText, x, HEIGHT / 2 + 70);

        g2d.setColor(new Color(100, 200, 255, 255));
        g2d.setFont(new Font("Arial", Font.BOLD, 24));
        String restartText = "Appuyez sur SPACE pour recommencer";
        fm = g2d.getFontMetrics();
        x = (WIDTH - fm.stringWidth(restartText)) / 2;
        g2d.drawString(restartText, x, HEIGHT / 2 + 140);
    }
}
