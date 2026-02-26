package model.difficulty;

public class Moyen extends Difficulty {

    public Moyen() {
        super("Medium", 4.0, 0.6, 5.0, 30);
    }

    @Override
    public void applySettings() {
        System.out.println("[Medium] Moderate speed and obstacles.");
        // TODO: connect to ObstacleManager and GameEngine when UI is ready
    }

    @Override
    public void applyAIBehavior() {
        System.out.println("[AI - Medium] Balanced AI behavior.");
        // TODO: connect to AI car controller when UI is ready
    }
}