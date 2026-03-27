package model.difficulty;
public class Easy extends Difficulty {

    public Easy() {
        super(
            "Easy",
            80.0,
            180.0,
            20.0,
            3000,
            0.0,
            1
        );
    }
    @Override
    public void applyDifficulty() {
        System.out.println("=== Difficulty: EASY ===");
        System.out.println("  Initial speed        : " + initialSpeed);
        System.out.println("  Max speed            : " + maxSpeed);
        System.out.println("  Acceleration         : " + acceleration);
        System.out.println("  Obstacle frequency   : every " + obstacleFrequency + " ms");
        System.out.println("  Mobile obstacles     : none (0%)");
        System.out.println("  Score multiplier     : x" + scoreMultiplier);
    }
}