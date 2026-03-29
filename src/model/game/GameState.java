package model.game;

public enum GameState {

    RUNNING,
    PAUSED,
    GAME_OVER,
    MENU,
    COUNTDOWN,
    FINISHED;

    public boolean isActive() {
        return this == RUNNING;
    }

    public boolean isOver() {
        return this == GAME_OVER || this == FINISHED;
    }

    public boolean acceptsInput() {
        return this == RUNNING || this == PAUSED;
    }
}
