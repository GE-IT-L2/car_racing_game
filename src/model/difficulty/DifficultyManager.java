package model.difficulty;

public class DifficultyManager {

    private Difficulty currentDifficulty;

    // Default difficulty is Easy
    public DifficultyManager() {
        this.currentDifficulty = new Facile();
    }

    // Called when player chooses a difficulty in the menu
    public void setDifficulty(String choice) {
        switch (choice.toLowerCase()) {
            case "easy":
                currentDifficulty = new Facile();
                break;
            case "medium":
                currentDifficulty = new Moyen();
                break;
            case "hard":
                currentDifficulty = new Difficile();
                break;
            default:
                System.out.println("[DifficultyManager] Unknown difficulty. Set to Easy by default.");
                currentDifficulty = new Facile();
        }
        currentDifficulty.applySettings();
        System.out.println("[DifficultyManager] Current difficulty: " + currentDifficulty.getName());
    }

    // Called by GameEngine to apply AI behavior
    public void applyAI() {
        currentDifficulty.applyAIBehavior();
    }

    // Getters — used by GameEngine and ObstacleManager
    public Difficulty getCurrentDifficulty() { return currentDifficulty; }
    public double getCarSpeed() { return currentDifficulty.getCarSpeed(); }
    public double getObstacleSpeed() { return currentDifficulty.getObstacleSpeed(); }
    public double getObstacleFrequency() { return currentDifficulty.getObstacleFrequency(); }
    public int getAiErrorRate() { return currentDifficulty.getAiErrorRate(); }
}