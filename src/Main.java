import javax.swing.*;

/**
 * Point d'entrée du Racing Game.
 * Lance l'application principale.
 */
public class Main {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new App());
    }
}
