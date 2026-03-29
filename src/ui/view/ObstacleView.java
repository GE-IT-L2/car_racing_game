package ui.view;

import java.awt.*;
import model.obstacle.Obstacle;
import model.obstacle.ObstacleFixe;
import model.obstacle.ObstacleMobile;

/**
 * Vue de rendu pour les obstacles.
 * Dessine les obstacles avec des styles différents selon leur type.
 */
public class ObstacleView {
    private Obstacle obstacle;
    private Color color;

    public ObstacleView(Obstacle obstacle) {
        this.obstacle = obstacle;

        // Couleur selon le type d'obstacle
        if (obstacle instanceof ObstacleFixe) {
            this.color = new Color(139, 69, 19); // Marron (rocher)
        } else if (obstacle instanceof ObstacleMobile) {
            this.color = new Color(200, 50, 50); // Rouge (voiture ennemie)
        } else {
            this.color = Color.GRAY;
        }
    }

    /**
     * Dessine l'obstacle à l'écran.
     */
    public void draw(Graphics2D g) {
        if (obstacle == null) return;

        double x = obstacle.getX();
        double y = obstacle.getY();
        int width = (int) obstacle.getWidth();
        int height = (int) obstacle.getHeight();

        if (obstacle instanceof ObstacleFixe) {
            drawFixedObstacle(g, x, y, width, height);
        } else if (obstacle instanceof ObstacleMobile) {
            drawMobileObstacle(g, x, y, width, height);
        }
    }

    /**
     * Dessine un obstacle fixe (rocher).
     */
    private void drawFixedObstacle(Graphics2D g, double x, double y, int width, int height) {
        g.setColor(color);
        g.fillRect((int)x - width/2, (int)y - height/2, width, height);

        // Texture
        g.setColor(new Color(100, 40, 0));
        g.drawOval((int)x - width/2 + 5, (int)y - height/2 + 5, width - 10, height - 10);

        // Contour
        g.setColor(Color.BLACK);
        g.setStroke(new BasicStroke(1));
        g.drawRect((int)x - width/2, (int)y - height/2, width, height);
    }

    /**
     * Dessine un obstacle mobile (voiture ennemie).
     */
    private void drawMobileObstacle(Graphics2D g, double x, double y, int width, int height) {
        g.setColor(color);
        g.fillRect((int)x - width/2, (int)y - height/2, width, height);

        // Fenêtres
        g.setColor(new Color(255, 100, 100));
        g.fillRect((int)x - width/2 + 3, (int)y - height/2 + 10, width - 6, 25);

        // Contour
        g.setColor(Color.BLACK);
        g.setStroke(new BasicStroke(1.5f));
        g.drawRect((int)x - width/2, (int)y - height/2, width, height);
    }

    public Obstacle getObstacle() { return obstacle; }
    public Color getColor() { return color; }
}