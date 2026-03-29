package model.game;

public class Score {

    public static final int    DOUBLE_SCORE_MULTIPLIER  = 2;
    public static final double COLLISION_PENALTY_METERS = 50.0;

    private double  distance;
    private double  bestScore;
    private int     multiplier;
    private boolean doubleScoreActive;

    public Score(double savedBestScore) {
        this.distance         = 0.0;
        this.bestScore        = savedBestScore;
        this.multiplier       = 1;
        this.doubleScoreActive = false;
    }

    public Score() {
        this(0.0);
    }

    public void addDistance(double delta) {
        if (delta > 0) {
            distance += delta * multiplier;
            updateBestScore();
        }
    }

    public void applyCollisionPenalty() {
        distance = Math.max(0, distance - COLLISION_PENALTY_METERS);
    }

    public void reset() {
        distance          = 0.0;
        multiplier        = 1;
        doubleScoreActive = false;
    }

    public void activateDoubleScore() {
        doubleScoreActive = true;
        multiplier        = DOUBLE_SCORE_MULTIPLIER;
    }

    public void deactivateDoubleScore() {
        doubleScoreActive = false;
        multiplier        = 1;
    }

    private void updateBestScore() {
        if (distance > bestScore) {
            bestScore = distance;
        }
    }

    public boolean isNewRecord() {
        return distance >= bestScore && distance > 0;
    }

    public int     getScoreInt()          { return (int) distance; }
    public double  getDistance()          { return distance; }
    public double  getBestScore()         { return bestScore; }
    public int     getMultiplier()        { return multiplier; }
    public boolean isDoubleScoreActive()  { return doubleScoreActive; }

    @Override
    public String toString() {
        return String.format(
            "Score[distance=%.1fm | best=%.1fm | x%d]",
            distance, bestScore, multiplier
        );
    }
}
