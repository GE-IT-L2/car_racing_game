package ui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import model.Difficulty.*;

public class MenuChoixConfig extends JFrame {

    private String mode;
    private String difficulte = "easy";
    private String couleurJ1  = "blue";
    private String couleurJ2  = "red";
    private String modeJ2     = "ia";

    public MenuChoixConfig(String mode) {
        this.mode = mode;
        setTitle("Configuration");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(500, 650);
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
        gbc.gridx = 0;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(8, 40, 8, 40);

        // Titre
        JLabel titre = new JLabel("⚙️ CONFIGURATION", SwingConstants.CENTER);
        titre.setFont(new Font("Arial", Font.BOLD, 28));
        titre.setForeground(new Color(255, 200, 0));
        gbc.gridy = 0;
        gbc.insets = new Insets(20, 40, 30, 40);
        panel.add(titre, gbc);

        // Choix difficulté
        gbc.insets = new Insets(8, 40, 8, 40);
        panel.add(creerLabel("🎯 Difficulté :"), gbc);
        gbc.gridy = 2;
        String[] diffs = {"Easy", "Medium", "Hard"};
        JComboBox<String> comboDiff = creerCombo(diffs);
        comboDiff.addActionListener(e -> difficulte = comboDiff.getSelectedItem().toString().toLowerCase());
        panel.add(comboDiff, gbc);

        // Choix couleur J1
        gbc.gridy = 3;
        panel.add(creerLabel("🚗 Couleur voiture" + (mode.equals("2joueurs") ? " Joueur 1" : "") + " :"), gbc);
        gbc.gridy = 4;
        String[] couleurs = {"Blue", "Red", "Green", "Yellow"};
        JComboBox<String> comboC1 = creerCombo(couleurs);
        comboC1.addActionListener(e -> couleurJ1 = comboC1.getSelectedItem().toString().toLowerCase());
        panel.add(comboC1, gbc);

        // Si mode 2 joueurs
        if (mode.equals("2joueurs")) {
            gbc.gridy = 5;
            panel.add(creerLabel("🚗 Couleur voiture Joueur 2 :"), gbc);
            gbc.gridy = 6;
            JComboBox<String> comboC2 = creerCombo(new String[]{"Red", "Green", "Yellow", "Blue"});
            comboC2.addActionListener(e -> couleurJ2 = comboC2.getSelectedItem().toString().toLowerCase());
            panel.add(comboC2, gbc);

            gbc.gridy = 7;
            panel.add(creerLabel("👥 Mode 2 joueurs :"), gbc);
            gbc.gridy = 8;
            JComboBox<String> comboMode = creerCombo(new String[]{"Contre IA", "2 Joueurs Local"});
            comboMode.addActionListener(e -> modeJ2 = comboMode.getSelectedItem().toString().equals("Contre IA") ? "ia" : "local");
            panel.add(comboMode, gbc);
        }

        // Bouton Jouer
        gbc.gridy = 9;
        gbc.insets = new Insets(25, 40, 10, 40);
        JButton btnJouer = creerBouton("▶  JOUER !", new Color(50, 180, 50));
        btnJouer.addActionListener(e -> lancerJeu(comboDiff));
        panel.add(btnJouer, gbc);

        // Bouton Retour
        gbc.gridy = 10;
        gbc.insets = new Insets(5, 40, 10, 40);
        JButton btnRetour = creerBouton("⬅  RETOUR", new Color(100, 100, 100));
        btnRetour.addActionListener(e -> {
            dispose();
            new MenuPrincipal();
        });
        panel.add(btnRetour, gbc);

        add(panel);
    }

    private void lancerJeu(JComboBox<String> comboDiff) {
        Difficulty diff = switch (difficulte) {
            case "medium" -> new Medium();
            case "hard"   -> new Hard();
            default       -> new Easy();
        };
        dispose();
        new GameWindow(mode, couleurJ1, couleurJ2, modeJ2, diff);
    }

    private JLabel creerLabel(String texte) {
        JLabel label = new JLabel(texte);
        label.setFont(new Font("Arial", Font.BOLD, 16));
        label.setForeground(Color.WHITE);
        return label;
    }

    private JComboBox<String> creerCombo(String[] items) {
        JComboBox<String> combo = new JComboBox<>(items);
        combo.setFont(new Font("Arial", Font.PLAIN, 16));
        combo.setBackground(new Color(40, 40, 80));
        combo.setForeground(Color.WHITE);
        combo.setPreferredSize(new Dimension(300, 35));
        return combo;
    }

    private JButton creerBouton(String texte, Color couleur) {
        JButton btn = new JButton(texte);
        btn.setFont(new Font("Arial", Font.BOLD, 18));
        btn.setBackground(couleur);
        btn.setForeground(Color.WHITE);
        btn.setFocusPainted(false);
        btn.setBorderPainted(false);
        btn.setPreferredSize(new Dimension(300, 50));
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btn.addMouseListener(new MouseAdapter() {
            public void mouseEntered(MouseEvent e) { btn.setBackground(couleur.brighter()); }
            public void mouseExited(MouseEvent e)  { btn.setBackground(couleur); }
        });
        return btn;
    }
}