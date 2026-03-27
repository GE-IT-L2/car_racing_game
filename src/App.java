import model.car.*;
import model.game.*;
import model.difficulty.*;
import ui.*;

import javax.swing.*;
import java.awt.*;

public class App extends JFrame {

    private JPanel currentPanel;

    public App() {
        setTitle("🏎️  Course de Voiture");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);

        // Start with menu
        showMenu();

        setFocusable(true);
        setVisible(true);
    }

    private void showMenu() {
        currentPanel = new MenuPanel(this::startGame);
        setContentPane(currentPanel);
        revalidate();
        repaint();
    }

    private void startGame() {
        // This will be called from MenuPanel, but since MenuPanel creates GamePanel, perhaps not needed.
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new App());
    }
}