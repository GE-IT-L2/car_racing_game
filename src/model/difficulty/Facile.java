package model.difficulty;

public class Facile extends Difficulty {

    public Facile() {
        super("Easy", 2.0, 0.3, 3.0, 60);
    }

    @Override
    public void applySettings() {
        System.out.println("[Easy] Slow speed, few obstacles.");
        // TODO: connect to ObstacleManager and GameEngine when UI is ready
    }

    @Override
    public void applyAIBehavior() {
        System.out.println("[AI - Easy] Slow AI with frequent mistakes.");
        // TODO: connect to AI car controller when UI is ready
    }
}