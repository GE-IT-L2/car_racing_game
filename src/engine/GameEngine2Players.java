package engine;

import model.car.*;
import model.obstacle.Obstacle;
import model.difficulty.Difficulty;
import model.game.GameState;
import model.game.Score;
import model.game.DynamicSpeedSystem;
import java.util.*;

/**
 * Moteur du jeu pour mode 2 joueurs.
 * Gère deux voitures (joueur vs joueur ou joueur vs IA) avec scores indépendants.
 */
public class GameEngine2Players {
    private GameState gameState;
    private CollisionManager collisionManager;
    private ObstacleManager obstacleManager;

    private PlayerCar player1;
    private PlayerCar player2;
    private List<Car> enemies;
    private double gameSpeed;

    // Systèmes de score et vitesse pour chaque joueur
    private Score score1;
    private Score score2;
    private DynamicSpeedSystem speedSystem1;
    private DynamicSpeedSystem speedSystem2;

    private boolean player1Accelerating = false;
    private boolean player2Accelerating = false;

    private int screenWidth;
    private int screenHeight;
    private int laneWidth;

    private long lastUpdate;
    private double deltaTime;
    
    private double targetDistance; // Distance à atteindre pour gagner
    private boolean isAIGame; // true si joueur vs IA

    public GameEngine2Players(int screenWidth, int screenHeight, Difficulty difficulty,
                             double targetDistance, boolean isAIGame) {
        this.screenWidth = screenWidth;
        this.screenHeight = screenHeight;
        this.laneWidth = screenWidth / 3;
        this.gameState = GameState.RUNNING;
        this.gameSpeed = 0;
        this.enemies = new ArrayList<>();
        this.targetDistance = targetDistance;
        this.isAIGame = isAIGame;

        this.collisionManager = new CollisionManager();
        this.obstacleManager = new ObstacleManager(screenWidth, screenHeight, difficulty);
        this.score1 = new Score();
        this.score2 = new Score();
        this.speedSystem1 = new DynamicSpeedSystem(100, 800);
        this.speedSystem2 = new DynamicSpeedSystem(100, 800);
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

        // Mise à jour des systèmes de vitesse dynamique
        speedSystem1.update(score1.getScoreInt(), player1Accelerating);
        speedSystem2.update(score2.getScoreInt(), player2Accelerating);

        // La vitesse du jeu est la moyenne des deux joueurs
        // (ou la vitesse du joueur 1 si tous les deux au même rythme)
        gameSpeed = (speedSystem1.getCurrentGameSpeed() + speedSystem2.getCurrentGameSpeed()) / 2.0;

        // Mise à jour des joueurs
        if (player1 != null) {
            player1.update(deltaTime);
        }
        if (player2 != null) {
            if (isAIGame && player2 instanceof AIPlayer) {
                AIPlayer ai = (AIPlayer) player2;
                ai.makeDecision(deltaTime);
            }
            player2.update(deltaTime);
        }

        // Mise à jour des obstacles
        obstacleManager.update(gameSpeed);

        // Limite les obstacles pour éviter les lags
        if (obstacleManager.getObstacleCount() > 50) {
            List<Obstacle> obstacles = obstacleManager.getObstacles();
            if (!obstacles.isEmpty()) {
                obstacles.remove(0);
            }
        }

        // Mise à jour des scores
        double distanceThisFrame = gameSpeed * deltaTime;
        score1.addDistance(distanceThisFrame);
        score1.addSpeedBonus(speedSystem1.getCurrentGameSpeed());
        score2.addDistance(distanceThisFrame);
        score2.addSpeedBonus(speedSystem2.getCurrentGameSpeed());

        // Vérification si quelqu'un a atteint la distance cible
        if (score1.getDistance() >= targetDistance || score2.getDistance() >= targetDistance) {
            gameState = GameState.FINISHED;
        }
    }

    /**
     * Vérifie les collisions du joueur 1.
     */
    public boolean checkPlayer1Collisions() {
        if (player1 == null) return false;
        for (Obstacle obstacle : obstacleManager.getObstacles()) {
            if (collisionManager.isColliding(player1, obstacle)) {
                score1.applyCollisionPenalty();
                return true;
            }
        }
        // Collision entre les deux joueurs
        if (player2 != null && checkCarCollisions(player1, player2)) {
            score1.applyCollisionPenalty();
            return true;
        }
        return false;
    }

    /**
     * Vérifie les collisions du joueur 2.
     */
    public boolean checkPlayer2Collisions() {
        if (player2 == null) return false;
        for (Obstacle obstacle : obstacleManager.getObstacles()) {
            if (collisionManager.isColliding(player2, obstacle)) {
                score2.applyCollisionPenalty();
                return true;
            }
        }
        // Collision entre les deux joueurs (déjà vérifiée pour player1)
        return false;
    }

    /**
     * Vérifie les collisions entre deux voitures.
     */
    public boolean checkCarCollisions(Car car1, Car car2) {
        return collisionManager.isCollidingCars(car1, car2);
    }

    /**
     * Retourne le gagnant (1 ou 2) ou 0 si le jeu n'est pas terminé.
     */
    public int getWinner() {
        if (gameState != GameState.FINISHED) return 0;
        if (score1.getDistance() > score2.getDistance()) return 1;
        if (score2.getDistance() > score1.getDistance()) return 2;
        return 0; // Égalité
    }

    // Getters et Setters
    public GameState getGameState() { return gameState; }
    public void setGameState(GameState state) { this.gameState = state; }

    public double getGameSpeed() { return gameSpeed; }

    public PlayerCar getPlayer1() { return player1; }
    public void setPlayer1(PlayerCar player) { this.player1 = player; }

    public PlayerCar getPlayer2() { return player2; }
    public void setPlayer2(PlayerCar player) { this.player2 = player; }

    public List<Obstacle> getObstacles() {
        return obstacleManager.getObstacles();
    }

    public ObstacleManager getObstacleManager() {
        return obstacleManager;
    }

    public CollisionManager getCollisionManager() {
        return collisionManager;
    }

    public Score getScore1() { return score1; }
    public Score getScore2() { return score2; }
    public DynamicSpeedSystem getSpeedSystem1() { return speedSystem1; }
    public DynamicSpeedSystem getSpeedSystem2() { return speedSystem2; }

    public void setPlayer1Accelerating(boolean accelerating) { player1Accelerating = accelerating; }
    public void setPlayer2Accelerating(boolean accelerating) { player2Accelerating = accelerating; }

    public double getTargetDistance() { return targetDistance; }
    public boolean isAIGame() { return isAIGame; }

    public void reset() {
        gameSpeed = 0;
        gameState = GameState.RUNNING;
        score1.reset();
        score2.reset();
        speedSystem1.reset();
        speedSystem2.reset();
        obstacleManager.clear();
        player1Accelerating = false;
        player2Accelerating = false;
    }

    public void pause() {
        gameState = GameState.PAUSED;
    }

    public void resume() {
        gameState = GameState.RUNNING;
    }
}
