package engine;

import model.obstacle.Obstacle;
import model.obstacle.ObstacleMobile;
import model.car.Car;
import model.car.PlayerCar;

/**
 * Gestionnaire des collisions du jeu.
 * Détecte et gère les collisions entre voitures et obstacles.
 */
public class CollisionManager {
    private static final double COLLISION_PADDING = 0.85; // Réduit la hitbox de 15%

    /**
     * Vérifie si deux rectangles (objets) se chevauchent.
     */
    public boolean checkCollision(double x1, double y1, double w1, double h1,
                                   double x2, double y2, double w2, double h2) {
        double padding1 = w1 * (1 - COLLISION_PADDING) / 2;
        double padding2 = w2 * (1 - COLLISION_PADDING) / 2;

        return x1 + padding1 < x2 + w2 - padding2 &&
               x1 + w1 - padding1 > x2 + padding2 &&
               y1 < y2 + h2 &&
               y1 + h1 > y2;
    }

    /**
     * Vérifie la collision entre une voiture et un obstacle.
     */
    public boolean isColliding(Car car, Obstacle obstacle) {
        return checkCollision(
            car.getPositionX(), car.getPositionY(), 50, 70,
            obstacle.getX(), obstacle.getY(), obstacle.getWidth(), obstacle.getHeight()
        );
    }

    /**
     * Vérifie la collision entre deux voitures.
     */
    public boolean isCollidingCars(Car car1, Car car2) {
        return checkCollision(
            car1.getPositionX(), car1.getPositionY(), 50, 70,
            car2.getPositionX(), car2.getPositionY(), 50, 70
        );
    }

    /**
     * Distance entre deux objets (utile pour des calculs avancés).
     */
    public double getDistance(double x1, double y1, double x2, double y2) {
        return Math.sqrt(Math.pow(x2 - x1, 2) + Math.pow(y2 - y1, 2));
    }
}
