package ui.scene;

import java.awt.*;

/**
 * Terrain urbain : route grise, bâtiments en arrière-plan.
 */
public class Ville extends Paysage {

    public Ville() {
        super("Ville");
        this.primaryColor = new Color(100, 100, 120);
        this.roadColor = new Color(80, 80, 90);
    }

    @Override
    public Color getPrimaryColor() {
        return primaryColor;
    }

    @Override
    public Color getRoadColor() {
        return roadColor;
    }

    @Override
    public void draw(Graphics2D g, int width, int height) {
        // Arrière-plan
        g.setColor(primaryColor);
        g.fillRect(0, 0, width, height);

        // Route
        g.setColor(roadColor);
        g.fillRect(0, 0, width, height);

        // Traits de la route
        g.setColor(Color.YELLOW);
        g.setStroke(new BasicStroke(2));
        for (int i = 0; i < height; i += 40) {
            g.drawLine(width / 3, i, width / 3, i + 20);
            g.drawLine(2 * width / 3, i, 2 * width / 3, i + 20);
        }
    }
}
