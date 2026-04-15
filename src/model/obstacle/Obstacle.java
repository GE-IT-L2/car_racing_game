package model.obstacle;

public abstract class Obstacle {
    protected double positionX;
    protected double positionY;
    protected double width;
    protected double height;
    protected boolean active;

    public Obstacle(double positionX, double positionY, double width, double height) {
        this.positionX = positionX;
        this.positionY = positionY;
        this.width     = width;
        this.height    = height;
        this.active    = true;
    }

    public abstract void update(double deltaTime);

    // Fait descendre l'obstacle sur l'écran
    public void descendre(double vitesse) {
        this.positionY += vitesse;
    }

    public boolean isActive()    { return active; }
    public void desactiver()     { this.active = false; }
    public double getPositionX() { return positionX; }
    public double getPositionY() { return positionY; }
    public double getWidth()     { return width; }
    public double getHeight()    { return height; }

    @Override
    public String toString() {
        return String.format("Obstacle[x=%.1f, y=%.1f, actif=%b]", positionX, positionY, active);
    }
}