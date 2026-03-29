package ui.scene;

import java.awt.*;

/**
 * Terrain de campagne : route verte, herbe, arbres.
 */
public class Campagne extends Paysage {

    public Campagne() {
        super("Campagne");
        this.primaryColor = new Color(50, 150, 50);
        this.roadColor = new Color(60, 60, 50);
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
        // Herbe verte
        g.setColor(primaryColor);
        g.fillRect(0, 0, width, height);

        // Route grise
        g.setColor(roadColor);
        g.fillRect(0, 0, width, height);

        // Traits blanc de la route
        g.setColor(Color.WHITE);
        g.setStroke(new BasicStroke(2));
        for (int i = 0; i < height; i += 40) {
            g.drawLine(width / 3, i, width / 3, i + 20);
            g.drawLine(2 * width / 3, i, 2 * width / 3, i + 20);
        }
    }
}
