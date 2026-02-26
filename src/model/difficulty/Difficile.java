package model.difficulty;

public class Difficile extends Difficulty {

    public Difficile() {
        super("Hard", 7.0, 0.9, 8.0, 5);
    }

    @Override
    public void applySettings() {
        System.out.println("[Hard] High speed, frequent obstacles.");
        // TODO: connect to ObstacleManager and GameEngine when UI is ready
    }

    @Override
    public void applyAIBehavior() {
        System.out.println("[AI - Hard] Fast AI with almost no mistakes.");
        // TODO: connect to AI car controller when UI is ready
    }
}