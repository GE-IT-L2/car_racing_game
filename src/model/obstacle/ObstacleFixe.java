package model.obstacle;

public class ObstacleFixe extends Obstacle {

    public ObstacleFixe(double positionX, double positionY) {
        super(positionX, positionY, 50, 50);
    }

    @Override
    public void update(double deltaTime) {
        // Un obstacle fixe ne bouge pas
    }

    @Override
    public String toString() {
        return "ObstacleFixe[x=" + positionX + ", y=" + positionY + "]";
    }
}