package engine;

import model.car.*;
import model.obstacle.Obstacle;
import model.difficulty.Difficulty;
import model.game.GameState;
import java.util.*;

/**
 * Moteur du jeu : orchestre la logique de jeu, les collisions,
 * les obstacles et la physique des voitures.
 */
public class GameEngine {
    private GameState gameState;
    private CollisionManager collisionManager;
    private ObstacleManager obstacleManager;

    private PlayerCar player;
    private List<Car> enemies;
    private double gameSpeed;
    private double gravity = 0.5; // Accélération due à la gravité

    private int screenWidth;
    private int screenHeight;
    private int laneWidth;

    private long lastUpdate;
    private double deltaTime;

    public GameEngine(int screenWidth, int screenHeight, Difficulty difficulty) {
        this.screenWidth = screenWidth;
        this.screenHeight = screenHeight;
        this.laneWidth = screenWidth / 3;
        this.gameState = GameState.RUNNING;
        this.gameSpeed = 0;
        this.enemies = new ArrayList<>();

        this.collisionManager = new CollisionManager();
        this.obstacleManager = new ObstacleManager(screenWidth, screenHeight, difficulty);
        this.lastUpdate = System.currentTimeMillis();
    }

    /**
     * Met à jour le moteur du jeu à chaque frame.
     */
    public void update() {
        if (gameState != GameState.RUNNING) return;

        long now = System.currentTimeMillis();
        deltaTime = (now - lastUpdate) / 1000.0;
        lastUpdate = now;

        // Augmente la vitesse du jeu progressivement
        gameSpeed += gravity * deltaTime;
        gameSpeed = Math.min(gameSpeed, 800); // Cap à 800

        // Met à jour le joueur
        if (player != null) {
            player.update(deltaTime);
        }

        // Met à jour les obstacles
        obstacleManager.update(gameSpeed);

        // Limite les obstacles pour éviter les lags
        if (obstacleManager.getObstacleCount() > 50) {
            List<Obstacle> obstacles = obstacleManager.getObstacles();
            if (!obstacles.isEmpty()) {
                obstacles.remove(0);
            }
        }
    }

    /**
     * Vérifie s'il y a une collision avec les obstacles.
     */
    public boolean checkObstacleCollisions(PlayerCar player) {
        for (Obstacle obstacle : obstacleManager.getObstacles()) {
            if (collisionManager.isColliding(player, obstacle)) {
                return true;
            }
        }
        return false;
    }

    /**
     * Vérifie les collisions entre deux voitures.
     */
    public boolean checkCarCollisions(Car car1, Car car2) {
        return collisionManager.isCollidingCars(car1, car2);
    }

    // Getters et Setters
    public GameState getGameState() { return gameState; }
    public void setGameState(GameState state) { this.gameState = state; }

    public double getGameSpeed() { return gameSpeed; }
    public void setGameSpeed(double speed) { this.gameSpeed = speed; }

    public PlayerCar getPlayer() { return player; }
    public void setPlayer(PlayerCar player) { this.player = player; }

    public List<Obstacle> getObstacles() {
        return obstacleManager.getObstacles();
    }

    public ObstacleManager getObstacleManager() {
        return obstacleManager;
    }

    public CollisionManager getCollisionManager() {
        return collisionManager;
    }

    public void reset() {
        gameSpeed = 0;
        gameState = GameState.RUNNING;
        obstacleManager.clear();
    }

    public void pause() {
        gameState = GameState.PAUSED;
    }

    public void resume() {
        gameState = GameState.RUNNING;
    }
}
