import java.awt.*;

/**
 * Classe représentant le joueur
 */
class PlayerCar {
    public double x;
    public double y;
    public double speed = 0;
    public int lane = 1; // 0, 1, 2

    private static final int LANE_WIDTH = 300;
    private static final int WIDTH = 1000;
    private static final int HEIGHT = 700;
    private static final double MAX_SPEED = 10;
    private static final double ACCELERATION = 0.3;
    private static final double FRICTION = 0.95;

    public PlayerCar() {
        this.x = WIDTH / 2;
        this.y = HEIGHT - 150;
    }

    public void moveLeft() {
        if (lane > 0) lane--;
        updateX();
    }

    public void moveRight() {
        if (lane < 2) lane++;
        updateX();
    }

    public void accelerate() {
        speed = Math.min(speed + ACCELERATION, MAX_SPEED);
    }

    public void brake() {
        speed = Math.max(speed - ACCELERATION * 1.5, 0);
    }

    public void updateSpeed(int gameSpeed) {
        speed *= FRICTION;
    }

    private void updateX() {
        x = lane * LANE_WIDTH + LANE_WIDTH / 2;
    }
}

/**
 * Classe représentant les obstacles
 */
class Enemy {
    public double x;
    public double y;
    public int lane;

    private static final int LANE_WIDTH = 300;
    private static final int WIDTH = 1000;

    public Enemy(double initialX) {
        this.lane = (int) (initialX / LANE_WIDTH);
        this.x = initialX;
        this.y = -50;
    }

    public void update(int speed) {
        y += speed * 0.015;
    }
}

/**
 * Classe représentant les power-ups
 */
class PowerUp {
    public double x, y;
    public int type; // 0: shield, 1: boost, 2: multiplier

    public PowerUp(double initialX, int type) {
        this.x = initialX;
        this.y = -50;
        this.type = type;
    }

    public void update(int speed) {
        y += speed * 0.015;
        x += Math.sin(y / 50) * 2; // Mouvement oscillant
    }
}

/**
 * Classe pour les particules d'effet
 */
class Particle {
    public double x, y, vx, vy;
    public int life = 60;
    public Color color;

    public Particle(double x, double y, double angle, double speed, Color color) {
        this.x = x;
        this.y = y;
        this.vx = Math.cos(angle) * speed;
        this.vy = Math.sin(angle) * speed;
        this.color = color;
    }

    public void update() {
        x += vx;
        y += vy;
        vy += 0.1; // gravité
        life--;
    }

    public void draw(Graphics2D g2d) {
        int size = (life / 3) + 2;
        g2d.setColor(new Color(color.getRed(), color.getGreen(), color.getBlue(), life * 4));
        g2d.fillOval((int) (x - size / 2), (int) (y - size / 2), size, size);
    }
}
