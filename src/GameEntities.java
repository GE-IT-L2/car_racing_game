import java.awt.*;

/**
 * Classe représentant les obstacles (vieux code - non utilisé, voir model/obstacle/)
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
 * Classe représentant les power-ups (vieux code - non utilisé)
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
