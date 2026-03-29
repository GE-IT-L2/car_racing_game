package ui.scene;

import java.awt.*;

/**
 * Classe abstraite pour les paysages/terrains du jeu.
 */
public abstract class Paysage {
    protected String name;
    protected Color primaryColor;
    protected Color secondaryColor;
    protected Color roadColor;

    public Paysage(String name) {
        this.name = name;
    }

    /**
     * Retourne la couleur principale du terrain.
     */
    public abstract Color getPrimaryColor();

    /**
     * Retourne la couleur de la route.
     */
    public abstract Color getRoadColor();

    /**
     * Dessine les éléments de fond du paysage.
     */
    public abstract void draw(Graphics2D g, int width, int height);

    public String getName() { return name; }
}
