import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.*;

/**
 * Version avancée du jeu avec power-ups et niveaux
 */
public class RaceGamePanelAdvanced extends JPanel {

    private static final int WIDTH = 1000;
    private static final int HEIGHT = 700;
    private static final int LANE_WIDTH = 300;
    private static final int NUM_LANES = 3;

    private PlayerCar player;
    private java.util.List<Enemy> enemies;
    private java.util.List<PowerUp> powerups;
    private java.util.List<Particle> particles;
    private int score = 0;
    private int bestScore = 0;
    private boolean running = true;
    private boolean gameover = false;
    private int gameSpeed = 0;
    private int level = 1;
    private boolean shielded = false;
    private long shieldEndTime = 0;
    private int boostMultiplier = 1;

    private boolean[] keys = new boolean[256];

    public RaceGamePanelAdvanced() {
        setPreferredSize(new Dimension(WIDTH, HEIGHT));
        setBackground(new Color(20, 20, 25));
        setFocusable(true);

        player = new PlayerCar();
        enemies = new java.util.ArrayList<>();
        powerups = new java.util.ArrayList<>();
        particles = new java.util.ArrayList<>();

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

        if (System.currentTimeMillis() > shieldEndTime) {
            shielded = false;
            boostMultiplier = 1;
        }

        gameSpeed = Math.min(gameSpeed + (level / 5 + 1), 800 + level * 100);
        player.updateSpeed(gameSpeed);

        double spawnRate = 0.015 + (level * 0.005);
        if (Math.random() < spawnRate) {
            spawnEnemy();
        }

        if (Math.random() < 0.003 * level * boostMultiplier) {
            spawnPowerUp();
        }

        java.util.List<Enemy> toRemoveEnemies = new java.util.ArrayList<>();
        for (Enemy enemy : enemies) {
            enemy.update(gameSpeed);
            if (enemy.y > HEIGHT) {
                toRemoveEnemies.add(enemy);
                score += 50 * level * boostMultiplier;
            }
            if (checkCollision(player, enemy)) {
                if (shielded) {
                    toRemoveEnemies.add(enemy);
                    score += 25 * boostMultiplier;
                    createExplosion(enemy.x, enemy.y);
                } else {
                    gameover = true;
                    if (score > bestScore) bestScore = score;
                }
            }
        }
        enemies.removeAll(toRemoveEnemies);

        java.util.List<PowerUp> toRemovePowerups = new java.util.ArrayList<>();
        for (PowerUp pu : powerups) {
            pu.update(gameSpeed);
            if (pu.y > HEIGHT) {
                toRemovePowerups.add(pu);
            }
            if (checkCollision(player, pu)) {
                toRemovePowerups.add(pu);
                activatePowerUp(pu);
                createParticles(pu.x, pu.y, 10, new Color(255, 215, 0));
            }
        }
        powerups.removeAll(toRemovePowerups);

        java.util.List<Particle> toRemoveParticles = new java.util.ArrayList<>();
        for (Particle p : particles) {
            p.update();
            if (p.life <= 0) {
                toRemoveParticles.add(p);
            }
        }
        particles.removeAll(toRemoveParticles);

        if (score > level * 1000) {
            level++;
        }
    }

    private void spawnEnemy() {
        int lane = (int) (Math.random() * NUM_LANES);
        enemies.add(new Enemy(lane * LANE_WIDTH + LANE_WIDTH / 2));
    }

    private void spawnPowerUp() {
        int lane = (int) (Math.random() * NUM_LANES);
        int type = (int) (Math.random() * 3);
        powerups.add(new PowerUp(lane * LANE_WIDTH + LANE_WIDTH / 2, type));
    }

    private void activatePowerUp(PowerUp pu) {
        switch (pu.type) {
            case 0:
                shielded = true;
                shieldEndTime = System.currentTimeMillis() + 5000;
                score += 100;
                break;
            case 1:
                gameSpeed = Math.min(gameSpeed + 200, 1200);
                score += 200;
                break;
            case 2:
                boostMultiplier = Math.min(boostMultiplier + 1, 5);
                shieldEndTime = System.currentTimeMillis() + 3000;
                score += 150;
                break;
        }
    }

    private void createExplosion(double x, double y) {
        createParticles(x, y, 15, new Color(255, 100, 50));
    }

    private void createParticles(double x, double y, int count, Color color) {
        for (int i = 0; i < count; i++) {
            double angle = Math.random() * Math.PI * 2;
            double speed = Math.random() * 5 + 2;
            particles.add(new Particle(x, y, angle, speed, color));
        }
    }

    private boolean checkCollision(PlayerCar player, Enemy enemy) {
        double dx = Math.abs(player.x - enemy.x);
        double dy = Math.abs(player.y - enemy.y);
        return dx < 60 && dy < 100;
    }

    private boolean checkCollision(PlayerCar player, PowerUp pu) {
        double dx = Math.abs(player.x - pu.x);
        double dy = Math.abs(player.y - pu.y);
        return dx < 50 && dy < 80;
    }

    private void resetGame() {
        player = new PlayerCar();
        enemies.clear();
        powerups.clear();
        particles.clear();
        score = 0;
        gameSpeed = 0;
        level = 1;
        shielded = false;
        boostMultiplier = 1;
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

        for (PowerUp pu : powerups) {
            drawPowerUp(g2d, pu);
        }
        for (Enemy enemy : enemies) {
            drawEnemy3D(g2d, enemy);
        }
        for (Particle p : particles) {
            p.draw(g2d);
        }

        drawPlayer3D(g2d, player);
        drawHUD(g2d);

        if (gameover) {
            drawGameOver(g2d);
        }
    }

    private void drawBackground(Graphics2D g2d) {
        int r = level > 1 ? 20 : 10;
        int g = level > 2 ? 60 : 50;
        int b = level > 3 ? 120 : 100;
        GradientPaint skyGradient = new GradientPaint(
                0, 0, new Color(10, 20, 40),
                0, HEIGHT / 3, new Color(r, g, b));
        g2d.setPaint(skyGradient);
        g2d.fillRect(0, 0, WIDTH, HEIGHT / 3);

        g2d.setColor(new Color(30 + level * 5, 40 + level * 3, 60 + level * 5, 150));
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

        if (shielded) {
            g2d.setColor(new Color(100, 200, 255, 100));
            g2d.setStroke(new BasicStroke(3));
            g2d.drawOval((int) car.x - carWidth / 2 - 10, (int) car.y - carHeight / 2 - 10, carWidth + 20, carHeight + 20);
        }

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
    }

    private void drawPowerUp(Graphics2D g2d, PowerUp pu) {
        int size = 30;
        Color[] colors = {new Color(100, 200, 255), new Color(255, 215, 0), new Color(255, 100, 255)};
        g2d.setColor(colors[pu.type]);
        g2d.fillOval((int) (pu.x - size / 2), (int) (pu.y - size / 2), size, size);

        g2d.setColor(Color.WHITE);
        g2d.setStroke(new BasicStroke(2));
        g2d.drawOval((int) (pu.x - size / 2), (int) (pu.y - size / 2), size, size);
    }

    private void drawHUD(Graphics2D g2d) {
        g2d.setColor(new Color(255, 255, 255, 200));
        g2d.setFont(new Font("Arial", Font.BOLD, 20));

        g2d.drawString("Score: " + score, 30, 40);
        g2d.drawString("Level: " + level, 30, 70);
        g2d.drawString("Speed: " + (int) player.speed, 30, 100);

        if (shielded) {
            g2d.setColor(new Color(100, 200, 255, 255));
            g2d.drawString("⚔ SHIELD", WIDTH - 280, 40);
        }

        if (boostMultiplier > 1) {
            g2d.setColor(new Color(255, 215, 0, 255));
            g2d.drawString("★ x" + boostMultiplier, WIDTH - 280, 70);
        }

        g2d.setColor(Color.WHITE);
        g2d.setFont(new Font("Arial", Font.PLAIN, 12));
        g2d.drawString("Record: " + bestScore, WIDTH - 280, 100);
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

        String levelText = "Level: " + level;
        fm = g2d.getFontMetrics();
        x = (WIDTH - fm.stringWidth(levelText)) / 2;
        g2d.drawString(levelText, x, HEIGHT / 2 + 70);
    }
}
