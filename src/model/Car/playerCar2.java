package model.Car;

import java.util.Random;

public class PlayerCar2 extends LaneCar {

    private boolean isAI;
    private double decisionTimer;
    private Random random;

    // Constructor for AI or human
    public PlayerCar2(String color, boolean isAI) {
        super(color, 95, 270, 40);
        this.isAI = isAI;
        if (isAI) random = new Random();
        decisionTimer = 0;
    }

    // Optional convenience constructor for human players (default isAI = false)
    public PlayerCar2(String color) {
        this(color, false);
    }

    @Override
    public void update(double deltaTime) {
        super.update(deltaTime);
        if (isAI) {
            decisionTimer += deltaTime;
            if (decisionTimer >= 1.5) {
                makeDecision();
                decisionTimer = 0;
            }
        }
    }

    private void makeDecision() {
        int choice = random.nextInt(3);
        if (choice == 0) moveLeft();
        else if (choice == 1) moveRight();
        // else stay in current lane
    }
}