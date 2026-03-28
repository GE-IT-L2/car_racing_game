import javax.swing.*;

/**
 * Application Racing Game v3.0
 * Jeu de course automobile avec graphisme 3D et couleurs modernes
 */
public class Main {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            JFrame frame = new JFrame("🏎️ RACING GAME v3.0");
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setResizable(false);
            
            // Créer le panel de jeu
            RaceGamePanel gamePanel = new RaceGamePanel();
            frame.add(gamePanel);
            frame.pack();
            
            // Centrer et afficher
            frame.setLocationRelativeTo(null);
            frame.setVisible(true);
            
            // Demander le focus
            frame.setFocusable(true);
            gamePanel.requestFocus();
        });
    }
}
