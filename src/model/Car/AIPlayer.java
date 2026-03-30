package model.car;

import model.difficulty.Difficulty;
import java.util.Random;

/**
 * Voiture contrôlée par l'IA.
 * Comportement adapté en fonction de la difficulté sélectionnée.
 */
public class AIPlayer extends PlayerCar {
    private Difficulty difficulty;
    private Random random;
    
    // Comportement de l'IA
    private int laneChangeCounter;
    private int laneChangeInterval;
    private double reactionTime;
    
    public AIPlayer(Difficulty difficulty) {
        super("Green");
        this.difficulty = difficulty;
        this.random = new Random();
        this.laneChangeCounter = 0;
        this.reactionTime = calculateReactionTime();
        updateBehavior();
    }

    public AIPlayer(String color, Difficulty difficulty) {
        super(color);
        this.difficulty = difficulty;
        this.random = new Random();
        this.laneChangeCounter = 0;
        this.reactionTime = calculateReactionTime();
        updateBehavior();
    }

    /**
     * Calcule le temps de réaction de l'IA selon la difficulté.
     * Facile : temps long (lent à réagir)
     * Moyen : temps moyen
     * Difficile : temps court (rapide à réagir)
     */
    private double calculateReactionTime() {
        String diffName = difficulty.getClass().getSimpleName();
        if (diffName.equals("Easy")) {
            return 0.5 + random.nextDouble() * 0.5; // 0.5-1.0 secondes
        } else if (diffName.equals("Medium")) {
            return 0.2 + random.nextDouble() * 0.3; // 0.2-0.5 secondes
        } else { // Hard
            return 0.05 + random.nextDouble() * 0.1; // 0.05-0.15 secondes
        }
    }

    /**
     * Met à jour le comportement de l'IA chaque seconde.
     */
    private void updateBehavior() {
        String diffName = difficulty.getClass().getSimpleName();
        if (diffName.equals("Easy")) {
            laneChangeInterval = 80 + random.nextInt(40); // 80-120 frames
        } else if (diffName.equals("Medium")) {
            laneChangeInterval = 60 + random.nextInt(30); // 60-90 frames
        } else { // Hard
            laneChangeInterval = 40 + random.nextInt(20); // 40-60 frames
        }
    }

    /**
     * Prend une décision d'IA à chaque frame.
     */
    public void makeDecision(double deltaTime) {
        laneChangeCounter++;

        if (laneChangeCounter >= laneChangeInterval) {
            makeDecisionToChangeLane();
            laneChangeCounter = 0;
            updateBehavior(); // Recalcule l'intervalle
        }
    }

    /**
     * L'IA décide si elle change de voie.
     */
    private void makeDecisionToChangeLane() {
        String diffName = difficulty.getClass().getSimpleName();
        int decision = random.nextInt(3); // 0: left, 1: center, 2: right

        if (diffName.equals("Easy")) {
            // Facile : 30% chance d'erreur (mauvais choix)
            if (random.nextDouble() < 0.3) {
                decision = random.nextInt(3);
            }
        } else if (diffName.equals("Medium")) {
            // Moyen : pas d'erreur, juste des mouvements basiques
        } else { // Hard
            // Difficile : l'IA essaie toujours d'optimiser sa position
            // Elle évite le centre si possible (moins de collisions)
            if (random.nextDouble() < 0.4) {
                decision = decision == 0 ? 2 : 0; // Préfère les côtés
            }
        }

        // Applique la décision
        if (decision == 0 && getCurrentLane() > 0) {
            moveLeft();
        } else if (decision == 2 && getCurrentLane() < 2) {
            moveRight();
        }
    }

    /**
     * Retourne le niveau de compétence de l'IA (0.0-1.0).
     * Utilisé pour qualifier les décisions.
     */
    public double getSkillLevel() {
        String diffName = difficulty.getClass().getSimpleName();
        if (diffName.equals("Easy")) {
            return 0.3;
        } else if (diffName.equals("Medium")) {
            return 0.6;
        } else {
            return 0.9;
        }
    }

    @Override
    public String toString() {
        return "AIPlayer{" +
                "color='" + color + '\'' +
                ", difficulty='" + difficulty.getName() + '\'' +
                ", skillLevel=" + getSkillLevel() +
                ", speed=" + currentSpeed +
                ", position=(" + positionX + "," + positionY + ")" +
                '}';
    }
}
