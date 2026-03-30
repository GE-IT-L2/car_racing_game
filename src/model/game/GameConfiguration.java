package model.game;

import java.awt.*;

/**
 * Configuration centralisée du jeu.
 * Regroupe tous les paramètres sélectionnés par le joueur avant une partie.
 */
public class GameConfiguration {
    private String playerName;
    private Color playerCarColor;
    private String terrain;
    private String difficultyMode; // "Easy", "Medium", "Hard"
    private String gameMode; // "SinglePlayer", "TwoPlayersLocal", "PlayerVsAI"
    private double targetDistance; // Distance à atteindre en mode 2 joueurs
    private boolean aiEnabled;

    public static class Builder {
        private String playerName = "Joueur 1";
        private Color playerCarColor = new Color(100, 150, 255);
        private String terrain = "Ville";
        private String difficultyMode = "Medium";
        private String gameMode = "SinglePlayer";
        private double targetDistance = 1000.0;
        private boolean aiEnabled = false;

        public Builder playerName(String name) {
            this.playerName = name;
            return this;
        }

        public Builder playerCarColor(Color color) {
            this.playerCarColor = color;
            return this;
        }

        public Builder terrain(String terrain) {
            this.terrain = terrain;
            return this;
        }

        public Builder difficulty(String difficulty) {
            this.difficultyMode = difficulty;
            return this;
        }

        public Builder gameMode(String mode) {
            this.gameMode = mode;
            return this;
        }

        public Builder targetDistance(double distance) {
            this.targetDistance = distance;
            return this;
        }

        public Builder aiEnabled(boolean enabled) {
            this.aiEnabled = enabled;
            return this;
        }

        public GameConfiguration build() {
            return new GameConfiguration(this);
        }
    }

    private GameConfiguration(Builder builder) {
        this.playerName = builder.playerName;
        this.playerCarColor = builder.playerCarColor;
        this.terrain = builder.terrain;
        this.difficultyMode = builder.difficultyMode;
        this.gameMode = builder.gameMode;
        this.targetDistance = builder.targetDistance;
        this.aiEnabled = builder.aiEnabled;
    }

    // Getters
    public String getPlayerName() { return playerName; }
    public Color getPlayerCarColor() { return playerCarColor; }
    public String getTerrain() { return terrain; }
    public String getDifficultyMode() { return difficultyMode; }
    public String getGameMode() { return gameMode; }
    public double getTargetDistance() { return targetDistance; }
    public boolean isAiEnabled() { return aiEnabled; }

    public boolean isSinglePlayer() { return gameMode.equals("SinglePlayer"); }
    public boolean isTwoPlayersLocal() { return gameMode.equals("TwoPlayersLocal"); }
    public boolean isPlayerVsAI() { return gameMode.equals("PlayerVsAI"); }
}
