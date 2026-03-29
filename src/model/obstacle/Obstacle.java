package model.obstacle;

/**
 * Classe abstraite représentant un obstacle sur la route.
 * Peut être fixe (rochers, débris) ou mobile (autres voitures).
 */
public abstract class Obstacle {
    protected double x;
    protected double y;
    protected double width = 60;
    protected double height = 80;
    protected String type;

    public Obstacle(double x, double y, String type) {
        this.x = x;
        this.y = y;
        this.type = type;
    }

    /**
     * Mise à jour de la position de l'obstacle.
     * @param gameSpeed Vitesse du jeu
     */
    public abstract void update(double gameSpeed);

    /**
     * Retourne true si l'obstacle est hors de l'écran.
     */
    public abstract boolean isOutOfBounds(int screenHeight);

    public double getX() { return x; }
    public double getY() { return y; }
    public double getWidth() { return width; }
    public double getHeight() { return height; }
    public String getType() { return type; }

    public void setPosition(double x, double y) {
        this.x = x;
        this.y = y;
    }
}
