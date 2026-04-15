package engine;

import model.Car.Car;
import model.Difficulty.Difficulty;
import model.game.GameState;
import model.game.Score;
import java.util.List;
import model.obstacle.Obstacle;

public class GameEngine {

    private Car car;
    private Difficulty difficulty;
    private Score score;
    private GameState state;
    private ObstacleManager obstacleManager;
    private CollisionManager collisionManager;

    public GameEngine(Car car, Difficulty difficulty) {
        this.car               = car;
        this.difficulty        = difficulty;
        this.score             = new Score();
        this.state             = GameState.RUNNING;
        this.obstacleManager   = new ObstacleManager(difficulty);
        this.collisionManager  = new CollisionManager();

        difficulty.applyToCar(car);
    }

    public void update(double deltaTime) {
        if (state != GameState.RUNNING) return;

        car.update(deltaTime);
        obstacleManager.update(deltaTime);

        List<Obstacle> obstacles = obstacleManager.getObstacles();
        for (Obstacle o : obstacles) {
            if (collisionManager.detecterCollision(car, o)) {
                car.collision();
                state = GameState.GAME_OVER;
                return;
            }
        }

        score.ajouterPoints(difficulty.calculatePoints((int) car.getCurrentSpeed()));
    }

    public void pause() {
        if (state == GameState.RUNNING) state = GameState.PAUSED;
    }

    public void reprendre() {
        if (state == GameState.PAUSED) state = GameState.RUNNING;
    }

    public void reinitialiser() {
        car.reset();
        score.reinitialiser();
        obstacleManager.reinitialiser();
        difficulty.applyToCar(car);
        state = GameState.RUNNING;
    }

    public GameState getState()              { return state; }
    public Score getScore()                  { return score; }
    public Car getCar()                      { return car; }
    public Difficulty getDifficulty()        { return difficulty; }
    public ObstacleManager getObstacleManager() { return obstacleManager; }
}