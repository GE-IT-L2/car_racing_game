import javax.swing.*;
import java.awt.*;

/**
 * Lanceur pour choisir entre les modes de jeu
 */
public class GameLauncher {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            JFrame launcherFrame = new JFrame("Racing Game Launcher v3.0");
            launcherFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            launcherFrame.setResizable(false);
            launcherFrame.setSize(600, 400);
            
            JPanel panel = new JPanel() {
                @Override
                protected void paintComponent(Graphics g) {
                    super.paintComponent(g);
                    Graphics2D g2d = (Graphics2D) g;
                    g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                    
                    // Gradient background
                    GradientPaint gradient = new GradientPaint(
                            0, 0, new Color(20, 20, 30),
                            0, getHeight(), new Color(40, 40, 60));
                    g2d.setPaint(gradient);
                    g2d.fillRect(0, 0, getWidth(), getHeight());
                    
                    // Title
                    g2d.setColor(new Color(100, 200, 255));
                    g2d.setFont(new Font("Arial", Font.BOLD, 40));
                    g2d.drawString("🏎️ RACING GAME v3.0", 120, 60);
                    
                    // Subtitle
                    g2d.setColor(Color.WHITE);
                    g2d.setFont(new Font("Arial", Font.PLAIN, 16));
                    g2d.drawString("Choisissez votre mode de jeu:", 150, 120);
                }
            };
            panel.setLayout(null);
            panel.setBackground(new Color(20, 20, 30));
            
            // Mode classique button
            JButton classicButton = new JButton("Mode Classique");
            classicButton.setBounds(100, 180, 150, 60);
            classicButton.setFont(new Font("Arial", Font.BOLD, 14));
            classicButton.setBackground(new Color(100, 150, 255));
            classicButton.setForeground(Color.WHITE);
            classicButton.setFocusPainted(false);
            classicButton.addActionListener(e -> {
                launcherFrame.dispose();
                launchClassicMode();
            });
            panel.add(classicButton);
            
            // Mode avancé button  
            JButton advancedButton = new JButton("Mode Avancé ⭐");
            advancedButton.setBounds(350, 180, 150, 60);
            advancedButton.setFont(new Font("Arial", Font.BOLD, 14));
            advancedButton.setBackground(new Color(255, 200, 50));
            advancedButton.setForeground(Color.BLACK);
            advancedButton.setFocusPainted(false);
            advancedButton.addActionListener(e -> {
                launcherFrame.dispose();
                launchAdvancedMode();
            });
            panel.add(advancedButton);
            
            // Description
            JLabel descLabel = new JLabel("<html><b>Mode Classique:</b> Jeu simple et rapide<br><b>Mode Avancé:</b> Niveaux, power-ups et challenges</html>");
            descLabel.setBounds(100, 270, 400, 80);
            descLabel.setFont(new Font("Arial", Font.PLAIN, 12));
            descLabel.setForeground(new Color(200, 200, 200));
            panel.add(descLabel);
            
            launcherFrame.add(panel);
            launcherFrame.setLocationRelativeTo(null);
            launcherFrame.setVisible(true);
        });
    }
    
    private static void launchClassicMode() {
        SwingUtilities.invokeLater(() -> {
            JFrame frame = new JFrame("🏎️ RACING GAME v3.0 - Mode Classique");
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setResizable(false);
            
            RaceGamePanel gamePanel = new RaceGamePanel();
            frame.add(gamePanel);
            frame.pack();
            frame.setLocationRelativeTo(null);
            frame.setVisible(true);
            frame.setFocusable(true);
            gamePanel.requestFocus();
        });
    }
    
    private static void launchAdvancedMode() {
        SwingUtilities.invokeLater(() -> {
            JFrame frame = new JFrame("🏎️ RACING GAME v3.0 - Mode Avancé ⭐");
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setResizable(false);
            
            RaceGamePanelAdvanced gamePanel = new RaceGamePanelAdvanced();
            frame.add(gamePanel);
            frame.pack();
            frame.setLocationRelativeTo(null);
            frame.setVisible(true);
            frame.setFocusable(true);
            gamePanel.requestFocus();
        });
    }
}
