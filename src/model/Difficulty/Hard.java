package model.Difficulty;

public class Hard extends Difficulty {
    private boolean unpredictableObstacles;
    private double unpredictableProbability;

    public Hard() {
        super(
            "Hard",
            130.0,
            320.0,
            60.0,
            800,
            0.70,
            3 
        );
        this.unpredictableObstacles  = true;
        this.unpredictableProbability = 0.40;
    }

    @Override
    public void applyDifficulty() {
        System.out.println("=== Difficulty: HARD ===");
        System.out.println("  Initial speed              : " + initialSpeed);
        System.out.println("  Max speed                  : " + maxSpeed);
        System.out.println("  Acceleration               : " + acceleration);
        System.out.println("  Obstacle frequency         : every " + obstacleFrequency + " ms");
        System.out.println("  Mobile obstacle chance     : " + (int)(mobileObstacleProbability * 100) + "%");
        System.out.println("  Unpredictable obstacles    : " + unpredictableObstacles);
        System.out.println("  Unpredictable probability  : " + (int)(unpredictableProbability * 100) + "%");
        System.out.println("  Score multiplier           : x" + scoreMultiplier);
    }

    /**
     * @param currentSpeed
     * @return
     */
    public double calculateSpeed(double currentSpeed) {
        double newSpeed = currentSpeed + acceleration * 0.016;
        if (Math.random() < 0.10) {
            newSpeed += acceleration * 0.016 * 2;
        }

        return Math.min(newSpeed, maxSpeed);
    }

    /**
     * @return
     */
    public boolean shouldBeUnpredictable() {
        return unpredictableObstacles && Math.random() < unpredictableProbability;
    }

    /**
     * @return
     */
    @Override
    public boolean shouldSpawnMobileObstacle() {
        return Math.random() < mobileObstacleProbability;
    }

    public boolean isUnpredictableObstacles()         { return unpredictableObstacles; }
    public double  getUnpredictableProbability()      { return unpredictableProbability; }

    public void setUnpredictableObstacles(boolean v)  { this.unpredictableObstacles = v; }
    public void setUnpredictableProbability(double p) { this.unpredictableProbability = p; }
}