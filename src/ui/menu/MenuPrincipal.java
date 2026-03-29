package ui.menu;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;

/**
 * Menu principal du jeu.
 * Permet de configurer le. mode de jeu, la difficulté, et de démarrer.
 */
public class MenuPrincipal extends JPanel {
    private JButton buttonSinglePlayer;
    private JButton buttonTwoPlayers;
    private JButton buttonSettings;
    private JButton buttonQuit;

    public MenuPrincipal(ActionListener onStartSinglePlayer, ActionListener onStartTwoPlayers,
                        ActionListener onSettings, ActionListener onQuit) {
        setLayout(new GridBagLayout());
        setBackground(new Color(34, 34, 34));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(15, 20, 15, 20);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // Titre
        JLabel title = new JLabel("🏎️ RACING GAME");
        title.setFont(new Font("Arial", Font.BOLD, 48));
        title.setForeground(Color.WHITE);
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        add(title, gbc);

        // Sous-titre
        JLabel subtitle = new JLabel("Choisis ton jeu");
        subtitle.setFont(new Font("Arial", Font.PLAIN, 18));
        subtitle.setForeground(new Color(200, 200, 200));
        gbc.gridy = 1;
        add(subtitle, gbc);

        // Bouton 1 Joueur
        gbc.gridwidth = 1;
        gbc.gridy = 2;
        buttonSinglePlayer = createButton("1 Joueur", onStartSinglePlayer);
        add(buttonSinglePlayer, gbc);

        // Bouton 2 Joueurs
        gbc.gridy = 3;
        buttonTwoPlayers = createButton("2 Joueurs", onStartTwoPlayers);
        add(buttonTwoPlayers, gbc);

        // Bouton Paramètres
        gbc.gridy = 4;
        buttonSettings = createButton("⚙️ Paramètres", onSettings);
        add(buttonSettings, gbc);

        // Bouton Quitter
        gbc.gridy = 5;
        buttonQuit = createButton("Quitter", onQuit);
        add(buttonQuit, gbc);
    }

    private JButton createButton(String text, ActionListener listener) {
        JButton button = new JButton(text);
        button.setFont(new Font("Arial", Font.BOLD, 16));
        button.setBackground(new Color(70, 130, 200));
        button.setForeground(Color.WHITE);
        button.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
        button.setFocusPainted(false);
        button.addActionListener(listener);

        // Hover effect
        button.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseEntered(java.awt.event.MouseEvent e) {
                button.setBackground(new Color(100, 160, 230));
            }

            @Override
            public void mouseExited(java.awt.event.MouseEvent e) {
                button.setBackground(new Color(70, 130, 200));
            }
        });

        return button;
    }
}
