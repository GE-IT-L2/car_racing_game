package model.difficulty;

public abstract class Difficulty {

    protected String name;
    protected double obstacleSpeed;
    protected double obstacleFrequency;
    protected double carSpeed;
    protected int aiErrorRate;

    public Difficulty(String name, double obstacleSpeed, double obstacleFrequency, double carSpeed, int aiErrorRate) {
        this.name = name;
        this.obstacleSpeed = obstacleSpeed;
        this.obstacleFrequency = obstacleFrequency;
        this.carSpeed = carSpeed;
        this.aiErrorRate = aiErrorRate;
    }

    // AI behavior — implemented by each subclass
    public abstract void applyAIBehavior();

    // Apply settings to game — implemented by each subclass
    public abstract void applySettings();

    // Getters
    public String getName() { return name; }
    public double getObstacleSpeed() { return obstacleSpeed; }
    public double getObstacleFrequency() { return obstacleFrequency; }
    public double getCarSpeed() { return carSpeed; }
    public int getAiErrorRate() { return aiErrorRate; }

    @Override
    public String toString() {
        return "[" + name + "] CarSpeed=" + carSpeed +
               " | ObstacleSpeed=" + obstacleSpeed +
               " | Frequency=" + obstacleFrequency +
               " | AI ErrorRate=" + aiErrorRate + "%";
    }
}