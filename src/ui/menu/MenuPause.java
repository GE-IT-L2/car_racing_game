package ui.menu;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;

/**
 * Menu de pause du jeu.
 */
public class MenuPause extends JPanel {
    private JButton buttonResume;
    private JButton buttonMainMenu;

    public MenuPause(ActionListener onResume, ActionListener onMainMenu) {
        setLayout(new GridBagLayout());
        setBackground(new Color(0, 0, 0, 180)); // Semi-transparent

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(20, 20, 20, 20);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // Titre
        JLabel title = new JLabel("PAUSE");
        title.setFont(new Font("Arial", Font.BOLD, 40));
        title.setForeground(Color.WHITE);
        gbc.gridx = 0;
        gbc.gridy = 0;
        add(title, gbc);

        // Bouton Reprendre
        gbc.gridy = 1;
        buttonResume = createButton("▶ Reprendre", onResume);
        add(buttonResume, gbc);

        // Bouton Menu Principal
        gbc.gridy = 2;
        buttonMainMenu = createButton("🏠 Menu Principal", onMainMenu);
        add(buttonMainMenu, gbc);
    }

    private JButton createButton(String text, ActionListener listener) {
        JButton button = new JButton(text);
        button.setFont(new Font("Arial", Font.BOLD, 18));
        button.setBackground(new Color(100, 160, 230));
        button.setForeground(Color.WHITE);
        button.setBorder(BorderFactory.createEmptyBorder(12, 30, 12, 30));
        button.setFocusPainted(false);
        button.addActionListener(listener);
        return button;
    }
}
