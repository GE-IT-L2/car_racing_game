package model.Difficulty;
public class Medium extends Difficulty {

   private double mobileObstacleThreshold;

    public Medium() {
        super(
            "Medium",
            100.0,
            250.0,
            45.0,
            1800,
            0.35,
            2
        );
        this.mobileObstacleThreshold = 500.0;
        }

    @Override
    public void applyDifficulty() {
        System.out.println("=== Difficulty: MEDIUM ===");
        System.out.println("  Initial speed          : " + initialSpeed);
        System.out.println("  Max speed              : " + maxSpeed);
        System.out.println("  Acceleration           : " + acceleration);
        System.out.println("  Obstacle frequency     : every " + obstacleFrequency + " ms");
        System.out.println("  Mobile obstacles after : " + mobileObstacleThreshold + " units traveled");
        System.out.println("  Mobile obstacle chance : " + (int)(mobileObstacleProbability * 100) + "%");
        System.out.println("  Score multiplier       : x" + scoreMultiplier);
        }

    /**
    * @param distanceTraveled
     * @return
     */
    public boolean shouldSpawnMobileObstacle(double distanceTraveled) {
        if (distanceTraveled < mobileObstacleThreshold) return false;
        return Math.random() < mobileObstacleProbability;
    }

    public double getMobileObstacleThreshold() { return mobileObstacleThreshold; }

    public void setMobileObstacleThreshold(double threshold) {
        this.mobileObstacleThreshold = threshold;
    }
}