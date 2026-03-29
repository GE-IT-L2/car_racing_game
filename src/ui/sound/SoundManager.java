package ui.sound;

/**
 * Gestionnaire des sons du jeu.
 * Simple mais extensible pour ajouter des clips sonores.
 */
public class SoundManager {
    private boolean soundEnabled = true;

    public SoundManager() {
        // Initialisation du gestionnaire audio
    }

    /**
     * Joue un son d'effet.
     */
    public void playSoundEffect(String soundName) {
        if (!soundEnabled) return;

        // Ici on peut ajouter la logique pour jouer des sons
        // Pour l'instant, c'est un placeholder
        switch (soundName) {
            case "collision":
                // Jouer le son de collision
                break;
            case "powerup":
                // Jouer le son de powerup
                break;
            case "obstacle_evaded":
                // Jouer le son d'obstacle évité
                break;
            default:
                break;
        }
    }

    /**
     * Joue la musique de fond.
     */
    public void playBackgroundMusic(String trackName) {
        if (!soundEnabled) return;

        // Logique pour jouer la musique de fond
    }

    /**
     * Arrête toute la musique.
     */
    public void stopMusic() {
        // Arrête la musique courante
    }

    /**
     * Active/désactive le son.
     */
    public void setSoundEnabled(boolean enabled) {
        this.soundEnabled = enabled;
    }

    /**
     * Retourne l'état du son.
     */
    public boolean isSoundEnabled() {
        return soundEnabled;
    }
}
