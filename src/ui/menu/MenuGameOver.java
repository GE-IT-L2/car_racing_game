package ui.menu;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;

/**
 * Menu de fin de jeu (game over).
 * Affiche le score final et propose de rejouer ou retourner au menu.
 */
public class MenuGameOver extends JPanel {
    private JButton buttonRestart;
    private JButton buttonMainMenu;
    private int finalScore;
    private String playerName;

    public MenuGameOver(int finalScore, String playerName, 
                       ActionListener onRestart, ActionListener onMainMenu) {
        this.finalScore = finalScore;
        this.playerName = playerName;

        setLayout(new GridBagLayout());
        setBackground(new Color(34, 34, 34));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(20, 20, 20, 20);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.gridwidth = 2;

        // Titre
        JLabel title = new JLabel("GAME OVER 💥");
        title.setFont(new Font("Arial", Font.BOLD, 48));
        title.setForeground(new Color(255, 100, 100));
        gbc.gridx = 0;
        gbc.gridy = 0;
        add(title, gbc);

        // Score
        JLabel scoreLabel = new JLabel("Score Final: " + finalScore);
        scoreLabel.setFont(new Font("Arial", Font.BOLD, 28));
        scoreLabel.setForeground(Color.WHITE);
        gbc.gridy = 1;
        add(scoreLabel, gbc);

        // Joueur
        JLabel playerLabel = new JLabel("Joueur: " + playerName);
        playerLabel.setFont(new Font("Arial", Font.PLAIN, 16));
        playerLabel.setForeground(new Color(200, 200, 200));
        gbc.gridy = 2;
        add(playerLabel, gbc);

        // Bouton Recommencer
        gbc.gridwidth = 1;
        gbc.gridy = 3;
        gbc.gridx = 0;
        buttonRestart = createButton("🔄 Recommencer", onRestart);
        add(buttonRestart, gbc);

        // Bouton Menu Principal
        gbc.gridx = 1;
        buttonMainMenu = createButton("🏠 Menu Principal", onMainMenu);
        add(buttonMainMenu, gbc);
    }

    private JButton createButton(String text, ActionListener listener) {
        JButton button = new JButton(text);
        button.setFont(new Font("Arial", Font.BOLD, 16));
        button.setBackground(new Color(70, 130, 200));
        button.setForeground(Color.WHITE);
        button.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
        button.setFocusPainted(false);
        button.addActionListener(listener);
        return button;
    }
}
