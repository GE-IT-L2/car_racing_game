package engine;

import model.obstacle.Obstacle;
import model.obstacle.ObstacleFixe;
import model.obstacle.ObstacleMobile;
import model.Difficulty.Difficulty;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class ObstacleManager {

    private static final int SCREEN_HEIGHT = 650;

    private List<Obstacle> obstacles;
    private Difficulty difficulty;
    private Random random;
    private double timer;

    public ObstacleManager(Difficulty difficulty) {
        this.obstacles  = new ArrayList<>();
        this.difficulty = difficulty;
        this.random     = new Random();
        this.timer      = 0;
    }

    public void update(double deltaTime) {
        timer += deltaTime;

        double intervalle = switch (difficulty.getName().toLowerCase()) {
            case "hard"   -> 1.5;
            case "medium" -> 2.5;
            default       -> 3.5;
        };

        if (timer >= intervalle) {
            genererObstacle();
            timer = 0;
        }

        // Faire descendre les obstacles
        for (Obstacle o : obstacles) {
            o.descendre(150 * deltaTime); // vitesse de descente fixe
        }

        // Supprimer les obstacles sortis de l'écran
        obstacles.removeIf(o -> o.getPositionY() > SCREEN_HEIGHT);
    }

    private void genererObstacle() {
        int lane = random.nextInt(3);
        double x = lane * 100;
        double y = -60;

        if (difficulty.shouldSpawnMobileObstacle()) {
            obstacles.add(new ObstacleMobile(x, y, 30 + random.nextInt(50)));
        } else {
            obstacles.add(new ObstacleFixe(x, y));
        }
    }

    public List<Obstacle> getObstacles() { return obstacles; }

    public void reinitialiser() {
        obstacles.clear();
        timer = 0;
    }
}