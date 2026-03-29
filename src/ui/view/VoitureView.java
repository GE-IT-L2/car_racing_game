package ui.view;

import java.awt.*;
import model.car.Car;

/**
 * Vue de rendu pour une voiture.
 * Dessine la voiture avec un style simplifié et optimal.
 */
public class VoitureView {
    private Car car;
    private Color color;
    private Color lightColor;

    public VoitureView(Car car, Color color) {
        this.car = car;
        this.color = color;
        this.lightColor = new Color(
            Math.min(255, color.getRed() + 80),
            Math.min(255, color.getGreen() + 80),
            Math.min(255, color.getBlue() + 80)
        );
    }

    /**
     * Dessine la voiture à l'écran.
     */
    public void draw(Graphics2D g) {
        if (car == null) return;

        double x = car.getPositionX();
        double y = car.getPositionY();
        int width = 40;
        int height = 70;

        // Corps de la voiture
        g.setColor(color);
        g.fillRect((int)x - width/2, (int)y - height/2, width, height);

        // Fenêtre
        g.setColor(lightColor);
        g.fillRect((int)x - width/2 + 3, (int)y - height/2 + 10, width - 6, 25);

        // Contour
        g.setColor(Color.BLACK);
        g.setStroke(new BasicStroke(1.5f));
        g.drawRect((int)x - width/2, (int)y - height/2, width, height);
    }

    public Car getCar() { return car; }
    public Color getColor() { return color; }
}