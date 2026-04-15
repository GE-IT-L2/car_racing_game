package ui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class MenuPrincipal extends JFrame {

    public MenuPrincipal() {
        setTitle("🚗 Course de Voiture");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(500, 600);
        setLocationRelativeTo(null);
        setResizable(false);

        JPanel panel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2 = (Graphics2D) g;
                GradientPaint gp = new GradientPaint(0, 0, new Color(20, 20, 60), 0, getHeight(), new Color(0, 0, 20));
                g2.setPaint(gp);
                g2.fillRect(0, 0, getWidth(), getHeight());
            }
        };
        panel.setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 0, 10, 0);
        gbc.gridx = 0;
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // Titre
        JLabel titre = new JLabel("🚗 COURSE DE VOITURE", SwingConstants.CENTER);
        titre.setFont(new Font("Arial", Font.BOLD, 32));
        titre.setForeground(new Color(255, 200, 0));
        gbc.gridy = 0;
        gbc.insets = new Insets(30, 0, 40, 0);
        panel.add(titre, gbc);

        // Bouton 1 Joueur
        JButton btn1Joueur = creerBouton("🎮  1 JOUEUR", new Color(50, 150, 50));
        btn1Joueur.addActionListener(e -> {
            dispose();
            new MenuChoixConfig("1joueur").setVisible(true);
        });
        gbc.gridy = 1;
        gbc.insets = new Insets(10, 50, 10, 50);
        panel.add(btn1Joueur, gbc);

        // Bouton 2 Joueurs
        JButton btn2Joueurs = creerBouton("👥  2 JOUEURS", new Color(50, 100, 200));
        btn2Joueurs.addActionListener(e -> {
            dispose();
            new MenuChoixConfig("2joueurs").setVisible(true);
        });
        gbc.gridy = 2;
        panel.add(btn2Joueurs, gbc);

        // Bouton Quitter
        JButton btnQuitter = creerBouton("❌  QUITTER", new Color(180, 50, 50));
        btnQuitter.addActionListener(e -> System.exit(0));
        gbc.gridy = 3;
        panel.add(btnQuitter, gbc);

        add(panel);
        setVisible(true);
    }

    private JButton creerBouton(String texte, Color couleur) {
        JButton btn = new JButton(texte);
        btn.setFont(new Font("Arial", Font.BOLD, 20));
        btn.setBackground(couleur);
        btn.setForeground(Color.WHITE);
        btn.setFocusPainted(false);
        btn.setBorderPainted(false);
        btn.setPreferredSize(new Dimension(300, 55));
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btn.addMouseListener(new MouseAdapter() {
            public void mouseEntered(MouseEvent e) { btn.setBackground(couleur.brighter()); }
            public void mouseExited(MouseEvent e)  { btn.setBackground(couleur); }
        });
        return btn;
    }
}