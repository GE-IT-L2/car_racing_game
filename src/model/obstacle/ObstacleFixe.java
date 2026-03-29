package model.obstacle;

/**
 * Obstacle fixe : rochers, débris, pneus, etc.
 * Descend verticalement avec la vitesse du jeu.
 */
public class ObstacleFixe extends Obstacle {
    private String obstacleShape; // "rock", "debris", "tire", etc.

    public ObstacleFixe(double x, double y, String obstacleShape) {
        super(x, y, "FIXE");
        this.obstacleShape = obstacleShape;
    }

    @Override
    public void update(double gameSpeed) {
        // L'obstacle descend avec la vitesse du jeu
        y += gameSpeed * 0.016; // 0.016 = 1/60 frame time
    }

    @Override
    public boolean isOutOfBounds(int screenHeight) {
        return y > screenHeight;
    }

    public String getObstacleShape() {
        return obstacleShape;
    }
}
