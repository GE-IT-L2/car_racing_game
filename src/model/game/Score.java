package model.game;

/**
 * Système de score amélioré.
 * Le score est calculé à partir de :
 * - distance parcourue (base)
 * - vitesse actuelle (bonus)
 * - multiplicateurs actifs (power-ups, difficulté)
 * - pénalités (collisions)
 */
public class Score {

    public static final int    DOUBLE_SCORE_MULTIPLIER  = 2;
    public static final double COLLISION_PENALTY_METERS = 50.0;
    public static final double SPEED_SCORE_FACTOR = 0.1; // Bonus basé sur la vitesse

    private double  distance;
    private double  bestScore;
    private int     multiplier;
    private boolean doubleScoreActive;
    private int     bonusScore;

    public Score(double savedBestScore) {
        this.distance         = 0.0;
        this.bestScore        = savedBestScore;
        this.multiplier       = 1;
        this.doubleScoreActive = false;
        this.bonusScore       = 0;
    }

    public Score() {
        this(0.0);
    }

    /**
     * Ajoute de la distance au score avec le multiplicateur actif.
     */
    public void addDistance(double delta) {
        if (delta > 0) {
            distance += delta * multiplier;
            updateBestScore();
        }
    }

    /**
     * Ajoute un bonus de score basé sur la vitesse actuelle.
     * Plus on va vite, plus on gagne de points.
     */
    public void addSpeedBonus(double currentSpeed) {
        if (currentSpeed > 0) {
            int speedBonus = (int) (currentSpeed * SPEED_SCORE_FACTOR * multiplier);
            bonusScore += speedBonus;
            updateBestScore();
        }
    }

    /**
     * Ajoute directement un bonus ponctuel (power-ups, objets, etc).
     */
    public void addBonusPoints(int points) {
        bonusScore += points * multiplier;
        updateBestScore();
    }

    /**
     * Applique une pénalité de collision.
     */
    public void applyCollisionPenalty() {
        distance = Math.max(0, distance - COLLISION_PENALTY_METERS);
    }

    /**
     * Réinitialise le score.
     */
    public void reset() {
        distance          = 0.0;
        multiplier        = 1;
        doubleScoreActive = false;
        bonusScore        = 0;
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
        if (getTotalScore() > bestScore) {
            bestScore = getTotalScore();
        }
    }

    /**
     * Calcule le score total (distance + bonus).
     */
    public int getTotalScore() {
        return (int) (distance + bonusScore);
    }

    public boolean isNewRecord() {
        return getTotalScore() >= bestScore && getTotalScore() > 0;
    }

    public int     getScoreInt()          { return getTotalScore(); }
    public double  getDistance()          { return distance; }
    public double  getBestScore()         { return bestScore; }
    public int     getMultiplier()        { return multiplier; }
    public boolean isDoubleScoreActive()  { return doubleScoreActive; }
    public int     getBonusScore()        { return bonusScore; }

    @Override
    public String toString() {
        return String.format(
            "Score[distance=%.1fm | bonus=%d | total=%d | best=%.1fm | x%d]",
            distance, bonusScore, getTotalScore(), bestScore, multiplier
        );
    }
}
