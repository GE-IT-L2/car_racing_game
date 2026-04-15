package ui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import engine.GameEngine;
import model.game.GameState;
import model.obstacle.Obstacle;
import model.Difficulty.Difficulty;

public class GamePanel extends JPanel implements Runnable, KeyListener {

    // Dimensions pour 1 joueur
    private static final int WIDTH_1P    = 420;
    // Dimensions pour 2 joueurs
    private static final int WIDTH_2P    = 840;
    private static final int HEIGHT      = 650;
    private static final int FPS         = 60;
    private static final int LANE_WIDTH  = 100;
    private static final int ROAD_LEFT   = 60;
    private static final int CAR_Y       = HEIGHT - 150;

    private GameEngine engine1, engine2;
    private String mode, couleurJ1, couleurJ2, modeJ2;
    private Difficulty difficulty;
    private Thread gameThread;
    private int roadOffset = 0;

    // Compte à rebours
    private int countdown = 3;
    private boolean countdownDone = false;
    private long lastCountdownTime;

    public GamePanel(GameEngine engine1, GameEngine engine2, String mode,
                     Difficulty difficulty, String couleurJ1, 
                     String couleurJ2, String modeJ2) {
        this.engine1    = engine1;
        this.engine2    = engine2;
        this.mode       = mode;
        this.difficulty = difficulty;
        this.couleurJ1  = couleurJ1;
        this.couleurJ2  = couleurJ2;
        this.modeJ2     = modeJ2;

        int width = mode.equals("2joueurs") ? WIDTH_2P : WIDTH_1P;
        setPreferredSize(new Dimension(width, HEIGHT));
        setBackground(new Color(30, 30, 30));
        setFocusable(true);
        addKeyListener(this);
    }

    public void startGame() {
        lastCountdownTime = System.currentTimeMillis();
        gameThread = new Thread(this);
        gameThread.start();
    }

    @Override
    public void run() {
        double deltaTime = 1.0 / FPS;
        while (true) {
            if (!countdownDone) {
                long now = System.currentTimeMillis();
                if (now - lastCountdownTime >= 1000) {
                    countdown--;
                    lastCountdownTime = now;
                    if (countdown <= 0) countdownDone = true;
                }
            } else {
                engine1.update(deltaTime);
                if (engine2 != null) engine2.update(deltaTime);
                roadOffset = (roadOffset + (int)(engine1.getCar().getCurrentSpeed() 
                             * deltaTime * 2)) % 60;
            }
            repaint();
            try { Thread.sleep(1000 / FPS); }
            catch (InterruptedException e) { e.printStackTrace(); }
        }
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g;
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, 
                            RenderingHints.VALUE_ANTIALIAS_ON);

        if (mode.equals("1joueur")) {
            dessinerTerrain(g2, 0);
            dessinerObstacles(g2, engine1, 0);
            dessinerVoiture(g2, (int) engine1.getCar().getPositionX(), 
                           CAR_Y, couleurJ1, 0);
            dessinerHUD(g2, engine1, "Joueur 1", 0);
            if (!countdownDone) dessinerCompteARebours(g2, 0, WIDTH_1P);
            if (engine1.getState() == GameState.GAME_OVER) 
                dessinerGameOver(g2, engine1, 0, WIDTH_1P);
            if (engine1.getState() == GameState.PAUSED) 
                dessinerPause(g2, 0, WIDTH_1P);
        } else {
            // Côté Joueur 1
            dessinerTerrain(g2, 0);
            dessinerObstacles(g2, engine1, 0);
            dessinerVoiture(g2, (int) engine1.getCar().getPositionX(), 
                           CAR_Y, couleurJ1, 0);
            dessinerHUD(g2, engine1, "Joueur 1", 0);

            // Séparateur
            g2.setColor(new Color(255, 200, 0));
            g2.setStroke(new BasicStroke(3));
            g2.drawLine(WIDTH_2P / 2, 0, WIDTH_2P / 2, HEIGHT);
            g2.setStroke(new BasicStroke(1));

            // Côté Joueur 2
            dessinerTerrain(g2, WIDTH_2P / 2);
            dessinerObstacles(g2, engine2, WIDTH_2P / 2);
            dessinerVoiture(g2, (int) engine2.getCar().getPositionX(), 
                           CAR_Y, couleurJ2, WIDTH_2P / 2);
            String labelJ2 = modeJ2.equals("ia") ? "Ordinateur" : "Joueur 2";
            dessinerHUD(g2, engine2, labelJ2, WIDTH_2P / 2);

            if (!countdownDone) dessinerCompteARebours(g2, 0, WIDTH_2P);

            // Game Over 2 joueurs
            if (engine1.getState() == GameState.GAME_OVER || 
                engine2.getState() == GameState.GAME_OVER) {
                dessinerGameOver2Joueurs(g2);
            }
        }
    }

    private void dessinerTerrain(Graphics2D g, int offsetX) {
        // Fond
        GradientPaint sky = new GradientPaint(
            offsetX, 0, new Color(20, 20, 60),
            offsetX, HEIGHT, new Color(10, 10, 30));
        g.setPaint(sky);
        g.fillRect(offsetX, 0, WIDTH_1P, HEIGHT);

        // Trottoirs
        g.setColor(new Color(80, 80, 80));
        g.fillRect(offsetX, 0, ROAD_LEFT, HEIGHT);
        g.fillRect(offsetX + ROAD_LEFT + 3 * LANE_WIDTH, 0, 
                   WIDTH_1P - (ROAD_LEFT + 3 * LANE_WIDTH), HEIGHT);

        // Route
        g.setColor(new Color(60, 60, 60));
        g.fillRect(offsetX + ROAD_LEFT, 0, 3 * LANE_WIDTH, HEIGHT);

        // Bords blancs
        g.setColor(Color.WHITE);
        g.setStroke(new BasicStroke(4));
        g.drawLine(offsetX + ROAD_LEFT, 0, offsetX + ROAD_LEFT, HEIGHT);
        g.drawLine(offsetX + ROAD_LEFT + 3 * LANE_WIDTH, 0, 
                   offsetX + ROAD_LEFT + 3 * LANE_WIDTH, HEIGHT);

        // Lignes pointillées
        g.setColor(new Color(255, 200, 0));
        g.setStroke(new BasicStroke(2, BasicStroke.CAP_BUTT, 
            BasicStroke.JOIN_BEVEL, 0, new float[]{20, 20}, roadOffset));
        g.drawLine(offsetX + ROAD_LEFT + LANE_WIDTH, 0, 
                   offsetX + ROAD_LEFT + LANE_WIDTH, HEIGHT);
        g.drawLine(offsetX + ROAD_LEFT + 2 * LANE_WIDTH, 0, 
                   offsetX + ROAD_LEFT + 2 * LANE_WIDTH, HEIGHT);
        g.setStroke(new BasicStroke(1));
    }

    private void dessinerVoiture(Graphics2D g, int lane, int y, 
                                  String couleur, int offsetX) {
        int x = offsetX + ROAD_LEFT + lane * LANE_WIDTH + 25;
        Color carColor = getCouleur(couleur);

        g.setColor(carColor);
        g.fillRoundRect(x, y, 50, 80, 10, 10);
        g.setColor(carColor.darker());
        g.fillRoundRect(x + 8, y + 10, 34, 35, 8, 8);
        g.setColor(new Color(150, 220, 255, 180));
        g.fillRoundRect(x + 10, y + 12, 30, 20, 5, 5);
        g.setColor(new Color(255, 255, 150));
        g.fillOval(x + 5, y + 65, 12, 8);
        g.fillOval(x + 33, y + 65, 12, 8);
        g.setColor(new Color(30, 30, 30));
        g.fillRoundRect(x - 8, y + 10, 12, 22, 4, 4);
        g.fillRoundRect(x + 46, y + 10, 12, 22, 4, 4);
        g.fillRoundRect(x - 8, y + 48, 12, 22, 4, 4);
        g.fillRoundRect(x + 46, y + 48, 12, 22, 4, 4);
    }

    private void dessinerObstacles(Graphics2D g, GameEngine engine, int offsetX) {
        for (Obstacle o : engine.getObstacleManager().getObstacles()) {
            int lane = (int)(o.getPositionX() / 100);
            if (lane < 0) lane = 0;
            if (lane > 2) lane = 2;
            int x = offsetX + ROAD_LEFT + lane * LANE_WIDTH + 15;
            int y = (int) o.getPositionY();

            g.setColor(new Color(180, 80, 20));
            g.fillRoundRect(x, y, 70, 50, 15, 15);
            g.setColor(new Color(220, 120, 40));
            g.fillRoundRect(x + 5, y + 5, 60, 20, 10, 10);
            g.setColor(new Color(120, 50, 10));
            g.setStroke(new BasicStroke(2));
            g.drawRoundRect(x, y, 70, 50, 15, 15);
            g.setStroke(new BasicStroke(1));
        }
    }

    private void dessinerHUD(Graphics2D g, GameEngine engine, 
                              String label, int offsetX) {
        g.setColor(new Color(0, 0, 0, 150));
        g.fillRoundRect(offsetX + 5, 5, 220, 80, 10, 10);
        g.setColor(new Color(100, 200, 255));
        g.setFont(new Font("Arial", Font.BOLD, 13));
        g.drawString(label, offsetX + 15, 22);
        g.setColor(new Color(255, 200, 0));
        g.setFont(new Font("Arial", Font.BOLD, 18));
        g.drawString("Score : " + engine.getScore().getScore(), offsetX + 15, 45);
        g.setColor(Color.WHITE);
        g.setFont(new Font("Arial", Font.PLAIN, 14));
        g.drawString("Vitesse : " + (int)engine.getCar().getCurrentSpeed() 
                     + " km/h", offsetX + 15, 68);
    }

    private void dessinerCompteARebours(Graphics2D g, int offsetX, int width) {
        g.setColor(new Color(0, 0, 0, 150));
        g.fillRect(0, 0, width, HEIGHT);
        g.setColor(new Color(255, 200, 0));
        g.setFont(new Font("Arial", Font.BOLD, 120));
        String txt = countdown > 0 ? String.valueOf(countdown) : "GO !";
        FontMetrics fm = g.getFontMetrics();
        int x = (width - fm.stringWidth(txt)) / 2;
        g.drawString(txt, x, HEIGHT / 2 + 40);
    }

    private void dessinerGameOver(Graphics2D g, GameEngine engine, 
                                   int offsetX, int width) {
        g.setColor(new Color(0, 0, 0, 180));
        g.fillRect(0, 0, width, HEIGHT);
        g.setColor(new Color(220, 50, 50));
        g.setFont(new Font("Arial", Font.BOLD, 48));
        g.drawString("GAME OVER", offsetX + 55, HEIGHT / 2 - 60);
        g.setColor(Color.WHITE);
        g.setFont(new Font("Arial", Font.BOLD, 22));
        g.drawString("Score : " + engine.getScore().getScore(), 
                     offsetX + 130, HEIGHT / 2 - 10);
        g.drawString("Record : " + engine.getScore().getMeilleurScore(), 
                     offsetX + 125, HEIGHT / 2 + 30);
        g.setColor(new Color(50, 200, 50));
        g.setFont(new Font("Arial", Font.BOLD, 18));
        g.drawString("R → Rejouer", offsetX + 150, HEIGHT / 2 + 80);
        g.setColor(new Color(200, 200, 50));
        g.drawString("M → Menu", offsetX + 155, HEIGHT / 2 + 115);
        g.setColor(new Color(200, 50, 50));
        g.drawString("Q → Quitter", offsetX + 150, HEIGHT / 2 + 150);
    }

    private void dessinerGameOver2Joueurs(Graphics2D g) {
        g.setColor(new Color(0, 0, 0, 180));
        g.fillRect(0, 0, WIDTH_2P, HEIGHT);

        // Déterminer le gagnant
        String gagnant;
        if (engine1.getState() == GameState.GAME_OVER && 
            engine2.getState() == GameState.GAME_OVER) {
            gagnant = "ÉGALITÉ !";
        } else if (engine1.getState() == GameState.GAME_OVER) {
            gagnant = "🏆 JOUEUR 2 GAGNE !";
        } else {
            gagnant = "🏆 JOUEUR 1 GAGNE !";
        }

        g.setColor(new Color(255, 200, 0));
        g.setFont(new Font("Arial", Font.BOLD, 40));
        FontMetrics fm = g.getFontMetrics();
        g.drawString(gagnant, (WIDTH_2P - fm.stringWidth(gagnant)) / 2, 
                     HEIGHT / 2 - 60);

        g.setColor(Color.WHITE);
        g.setFont(new Font("Arial", Font.BOLD, 20));
        g.drawString("J1 Score : " + engine1.getScore().getScore(), 
                     WIDTH_2P / 2 - 200, HEIGHT / 2);
        g.drawString("J2 Score : " + engine2.getScore().getScore(), 
                     WIDTH_2P / 2 + 50, HEIGHT / 2);

        g.setColor(new Color(50, 200, 50));
        g.setFont(new Font("Arial", Font.BOLD, 18));
        g.drawString("R → Rejouer", WIDTH_2P / 2 - 70, HEIGHT / 2 + 80);
        g.setColor(new Color(200, 200, 50));
        g.drawString("M → Menu", WIDTH_2P / 2 - 55, HEIGHT / 2 + 115);
        g.setColor(new Color(200, 50, 50));
        g.drawString("Q → Quitter", WIDTH_2P / 2 - 65, HEIGHT / 2 + 150);
    }

    private void dessinerPause(Graphics2D g, int offsetX, int width) {
        g.setColor(new Color(0, 0, 0, 150));
        g.fillRect(0, 0, width, HEIGHT);
        g.setColor(new Color(255, 200, 0));
        g.setFont(new Font("Arial", Font.BOLD, 60));
        g.drawString("PAUSE", offsetX + 120, HEIGHT / 2);
        g.setColor(Color.WHITE);
        g.setFont(new Font("Arial", Font.PLAIN, 18));
        g.drawString("P → Reprendre", offsetX + 130, HEIGHT / 2 + 50);
    }

    private Color getCouleur(String couleur) {
        return switch (couleur.toLowerCase()) {
            case "red"    -> new Color(220, 50,  50);
            case "green"  -> new Color(50,  200, 50);
            case "yellow" -> new Color(220, 180, 50);
            default       -> new Color(50,  100, 220);
        };
    }

    @Override
    public void keyPressed(KeyEvent e) {
        // Joueur 1 — flèches
        if (engine1.getState() == GameState.RUNNING && countdownDone) {
            if (e.getKeyCode() == KeyEvent.VK_LEFT)  engine1.getCar().moveLeft();
            if (e.getKeyCode() == KeyEvent.VK_RIGHT) engine1.getCar().moveRight();
        }

        // Joueur 2 local — touches Q et D
        if (engine2 != null && modeJ2.equals("local") && 
            engine2.getState() == GameState.RUNNING && countdownDone) {
            if (e.getKeyCode() == KeyEvent.VK_Q) engine2.getCar().moveLeft();
            if (e.getKeyCode() == KeyEvent.VK_D) engine2.getCar().moveRight();
        }

        // Pause
        if (e.getKeyCode() == KeyEvent.VK_P) {
            if (engine1.getState() == GameState.RUNNING) engine1.pause();
            else if (engine1.getState() == GameState.PAUSED) engine1.reprendre();
            if (engine2 != null) {
                if (engine2.getState() == GameState.RUNNING) engine2.pause();
                else if (engine2.getState() == GameState.PAUSED) engine2.reprendre();
            }
        }

        // Game Over actions
        boolean gameOver = engine1.getState() == GameState.GAME_OVER ||
                          (engine2 != null && engine2.getState() == GameState.GAME_OVER);
        if (gameOver) {
            if (e.getKeyCode() == KeyEvent.VK_R) {
                engine1.reinitialiser();
                if (engine2 != null) engine2.reinitialiser();
                countdown = 3;
                countdownDone = false;
                lastCountdownTime = System.currentTimeMillis();
            }
            if (e.getKeyCode() == KeyEvent.VK_M) {
                SwingUtilities.getWindowAncestor(this).dispose();
                new MenuPrincipal();
            }
            if (e.getKeyCode() == KeyEvent.VK_Q) System.exit(0);
        }
    }

    @Override public void keyReleased(KeyEvent e) {}
    @Override public void keyTyped(KeyEvent e) {}
}