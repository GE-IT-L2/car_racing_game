package engine;

import java.util.*;
import model.obstacle.*;
import model.difficulty.Difficulty;

/**
 * Gestionnaire des obstacles du jeu.
 * Gère la génération, la mise à jour et la suppression des obstacles.
 */
public class ObstacleManager {
    private List<Obstacle> obstacles;
    private Random random;
    private double spawnRate = 0.015; // Probabilité de spawn par frame
    private int screenWidth = 800;
    private int screenHeight = 600;
    private int nextObstacleId = 0;
    private Difficulty difficulty;
    private int laneWidth;

    public ObstacleManager(int screenWidth, int screenHeight, Difficulty difficulty) {
        this.obstacles = new ArrayList<>();
        this.random = new Random();
        this.screenWidth = screenWidth;
        this.screenHeight = screenHeight;
        this.difficulty = difficulty;
        this.laneWidth = screenWidth / 3;
        updateSpawnRate();
    }

    /**
     * Met à jour le taux de spawn selon la difficulté.
     */
    private void updateSpawnRate() {
        // Plus la difficulté est haute, plus il y a d'obstacles
        String diffName = difficulty.getClass().getSimpleName();
        if (diffName.equals("Easy")) {
            spawnRate = 0.01;
        } else if (diffName.equals("Medium")) {
            spawnRate = 0.015;
        } else {
            spawnRate = 0.025;
        }
    }

    /**
     * Ajoute un nouvel obstacle s'il est temps d'en spawner un.
     */
    public void spawnObstacle() {
        if (random.nextDouble() < spawnRate) {
            int lane = random.nextInt(3);
            double x = lane * laneWidth + laneWidth / 2 - 25;
            double y = -80;

            if (random.nextBoolean()) {
                obstacles.add(new ObstacleFixe(x, y, "rock"));
            } else {
                obstacles.add(new ObstacleMobile(x, y, lane));
            }
        }
    }

    /**
     * Met à jour tous les obstacles et supprime ceux hors limites.
     */
    public void update(double gameSpeed) {
        spawnObstacle();

        obstacles.removeIf(obstacle -> {
            obstacle.update(gameSpeed);
            return obstacle.isOutOfBounds(screenHeight);
        });
    }

    /**
     * Retourne la liste des obstacles actuels.
     */
    public List<Obstacle> getObstacles() {
        return new ArrayList<>(obstacles);
    }

    /**
     * Récupère les obstacles visibles à l'écran.
     */
    public List<Obstacle> getVisibleObstacles() {
        return obstacles;
    }

    /**
     * Limite le nombre d'obstacles pour optimiser les performances.
     */
    public int getObstacleCount() {
        return obstacles.size();
    }

    /**
     * Vide la liste des obstacles (utile pour reset).
     */
    public void clear() {
        obstacles.clear();
    }

    /**
     * Définit la difficulté (et ajuste le spawn rate).
     */
    public void setDifficulty(Difficulty difficulty) {
        this.difficulty = difficulty;
        updateSpawnRate();
    }

    /**
     * Obtient le taux de spawn actuel (pour debug).
     */
    public double getSpawnRate() {
        return spawnRate;
    }
}
