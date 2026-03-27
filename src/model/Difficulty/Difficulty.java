package model.difficulty;

import model.car.*;

public abstract class Difficulty {
    protected String name;
    protected double initialSpeed;
    protected double maxSpeed;
    protected double acceleration;
    protected int obstacleFrequency;
    protected double mobileObstacleProbability;
    protected int scoreMultiplier;
    /**
     *
     * @param name                      
     * @param initialSpeed              
     * @param maxSpeed                  
     * @param acceleration              
     * @param obstacleFrequency         
     * @param mobileObstacleProbability 
     * @param scoreMultiplier           
     */
    public Difficulty(String name, double initialSpeed, double maxSpeed,
                      double acceleration, int obstacleFrequency,
                      double mobileObstacleProbability, int scoreMultiplier) {

        this.name                      = name;
        this.initialSpeed              = initialSpeed;
        this.maxSpeed                  = maxSpeed;
        this.acceleration              = acceleration;
        this.obstacleFrequency         = obstacleFrequency;
        this.mobileObstacleProbability = mobileObstacleProbability;
        this.scoreMultiplier           = scoreMultiplier;
    }
    public abstract void applyDifficulty();

    /**
    
     * @param car
     */
    public void applyToCar(Car car) {
        car.setInitSpeed(this.initialSpeed);
        car.setMaxSpeed(this.maxSpeed);
        car.setAcceleration(this.acceleration);
}

    /**
     * @param rawPoints
     * @return
     */
    public int calculatePoints(int rawPoints) {
        return rawPoints * scoreMultiplier;
    }

    /**
     * @return
     */
    public boolean shouldSpawnMobileObstacle() {
        return Math.random() < mobileObstacleProbability;
    }
    public String getName()                      { return name; }
    public double getInitialSpeed()              { return initialSpeed; }
    public double getMaxSpeed()                  { return maxSpeed; }
    public double getAcceleration()              { return acceleration; }
    public int    getObstacleFrequency()         { return obstacleFrequency; }
    public double getMobileObstacleProbability() { return mobileObstacleProbability; }
    public int    getScoreMultiplier()           { return scoreMultiplier; }

    @Override
    public String toString() {
        return "Difficulty{ "
                + "name='"            + name             + "', "
                + "initialSpeed="     + initialSpeed     + ", "
                + "maxSpeed="         + maxSpeed         + ", "
                + "acceleration="     + acceleration     + ", "
                + "obstacleFrequency="+ obstacleFrequency+ "ms, "
                + "scoreMultiplier=x" + scoreMultiplier
                + " }";
    }
}