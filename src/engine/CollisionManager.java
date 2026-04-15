package engine;

import model.Car.Car;
import model.obstacle.Obstacle;

public class CollisionManager {

    private static final int CAR_Y = 650 - 150;

    public boolean detecterCollision(Car car, Obstacle obstacle) {
        if (!obstacle.isActive()) return false;

        // Voie de la voiture (0, 1 ou 2)
        int carLane = (int) car.getPositionX();

        // Voie de l'obstacle (0, 1 ou 2)
        int obsLane = (int)(obstacle.getPositionX() / 100);
        if (obsLane < 0) obsLane = 0;
        if (obsLane > 2) obsLane = 2;

        // Position Y de l'obstacle sur l'écran
        int obsY = (int) obstacle.getPositionY();

        // Collision si même voie ET obstacle chevauche la voiture
        boolean memeVoie  = (carLane == obsLane);
        boolean collisionY = obsY + 50 >= CAR_Y && obsY <= CAR_Y + 80;

        return memeVoie && collisionY;
    }
}