package model.game;

/**
 * Système de vitesse dynamique du jeu.
 * La vitesse augmente progressivement en fonction :
 * - du temps de jeu écoulé
 * - du score actuel
 * - de l'accélération du joueur
 */
public class DynamicSpeedSystem {
    private double baseSpeed;
    private double currentGameSpeed;
    private double maxGameSpeed;
    private double gameSpeedMultiplier; // Augmente avec le score
    
    private long gameStartTime;
    private double elapsedTimeSeconds;
    
    private double scoreInfluence = 0.0005; // Score influence la vitesse
    private double timeInfluence = 0.05; // Temps influence la vitesse
    private double accelerationFactor = 1.0; // Multiplicateur d'accélération du joueur

    public DynamicSpeedSystem(double baseSpeed, double maxGameSpeed) {
        this.baseSpeed = baseSpeed;
        this.currentGameSpeed = baseSpeed;
        this.maxGameSpeed = maxGameSpeed;
        this.gameSpeedMultiplier = 1.0;
        this.gameStartTime = System.currentTimeMillis();
    }

    /**
     * Met à jour la vitesse du jeu en fonction du temps et du score.
     * @param currentScore Score actuel du joueur
     * @param isPlayerAccelerating Si le joueur appuie sur accélération
     */
    public void update(int currentScore, boolean isPlayerAccelerating) {
        // Temps écoulé en secondes
        elapsedTimeSeconds = (System.currentTimeMillis() - gameStartTime) / 1000.0;
        
        // Augmentation de base liée au temps (progression naturelle)
        double timeBasedIncrease = timeInfluence * elapsedTimeSeconds;
        
        // Augmentation liée au score (plus le joueur avance, plus il va vite)
        double scoreBasedIncrease = scoreInfluence * currentScore;
        
        // Multiplicateur d'accélération du joueur
        this.accelerationFactor = isPlayerAccelerating ? 1.5 : 1.0;
        
        // Calcul de la nouvelle vitesse
        this.gameSpeedMultiplier = 1.0 + timeBasedIncrease + scoreBasedIncrease;
        this.currentGameSpeed = baseSpeed * gameSpeedMultiplier * accelerationFactor;
        
        // Limitation au maximum
        this.currentGameSpeed = Math.min(currentGameSpeed, maxGameSpeed);
    }

    /**
     * Réinitialise le système à l'état initial.
     */
    public void reset() {
        this.currentGameSpeed = baseSpeed;
        this.gameSpeedMultiplier = 1.0;
        this.accelerationFactor = 1.0;
        this.gameStartTime = System.currentTimeMillis();
        this.elapsedTimeSeconds = 0;
    }

    // Getters
    public double getCurrentGameSpeed() { return currentGameSpeed; }
    public double getGameSpeedMultiplier() { return gameSpeedMultiplier; }
    public double getAccelerationFactor() { return accelerationFactor; }
    public double getElapsedTimeSeconds() { return elapsedTimeSeconds; }
    public double getBaseSpeed() { return baseSpeed; }
    public double getMaxGameSpeed() { return maxGameSpeed; }

    @Override
    public String toString() {
        return String.format("Speed[base=%.1f, current=%.1f, multiplier=x%.2f, time=%.1fs]",
                baseSpeed, currentGameSpeed, gameSpeedMultiplier, elapsedTimeSeconds);
    }
}
