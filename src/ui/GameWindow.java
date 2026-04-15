package ui;

import javax.swing.*;
import engine.GameEngine;
import model.Car.*;
import model.Difficulty.Difficulty;

public class GameWindow extends JFrame {

    public GameWindow(String mode, String couleurJ1, String couleurJ2, 
                      String modeJ2, Difficulty difficulty) {
        setTitle("🚗 Course de Voiture");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setResizable(false);

        GamePanel panel;

        if (mode.equals("1joueur")) {
            PlayerCar1 car = new PlayerCar1(couleurJ1);
            GameEngine engine = new GameEngine(car, difficulty);
            panel = new GamePanel(engine, null, mode, difficulty, 
                                  couleurJ1, couleurJ2, modeJ2);
        } else {
            PlayerCar1 car1 = new PlayerCar1(couleurJ1);
            boolean isAI = modeJ2.equals("ia");
            PlayerCar2 car2 = new PlayerCar2(couleurJ2, isAI);
            GameEngine engine1 = new GameEngine(car1, difficulty);
            GameEngine engine2 = new GameEngine(car2, difficulty);
            panel = new GamePanel(engine1, engine2, mode, difficulty, 
                                  couleurJ1, couleurJ2, modeJ2);
        }

        add(panel);
        pack();
        setLocationRelativeTo(null);
        setVisible(true);
        panel.startGame();
    }
}