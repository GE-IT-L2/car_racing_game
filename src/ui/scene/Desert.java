package ui.scene;

import java.awt.*;

/**
 * Terrain désertique : dunes de sable, couleurs chaudes.
 */
public class Desert extends Paysage {

    public Desert() {
        super("Désert");
        this.primaryColor = new Color(210, 180, 140);
        this.roadColor = new Color(190, 160, 120);
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
        // Dégradé de désert
        g.setColor(primaryColor);
        g.fillRect(0, 0, width, height);

        // Route du désert
        g.setColor(roadColor);
        g.fillRect(0, 0, width, height);

        // Traits de la route
        g.setColor(new Color(255, 200, 0));
        g.setStroke(new BasicStroke(2));
        for (int i = 0; i < height; i += 40) {
            g.drawLine(width / 3, i, width / 3, i + 20);
            g.drawLine(2 * width / 3, i, 2 * width / 3, i + 20);
        }
    }
}
