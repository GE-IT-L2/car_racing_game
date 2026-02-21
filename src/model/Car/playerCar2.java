package model.Car;

import java.util.Random;

/**
 * PlayerCar3 represents an AI-controlled car.
 * 
 * The AI periodically makes random lane decisions.
 */
public class PlayerCar2 extends LaneCar {

    private double decisionTimer;
    private Random random;

    public PlayerCar2(String color) {
        super(color, 95, 270, 40);
        this.random = new Random();
        this.decisionTimer = 0;
    }

    @Override
    public void update(double deltaTime) {
        super.update(deltaTime);

        decisionTimer += deltaTime;

        // AI makes a decision every 1.5 seconds
        if (decisionTimer >= 1.5) {
            makeDecision();
            decisionTimer = 0;
        }
    }

    private void makeDecision() {
        int choice = random.nextInt(3);

        if (choice == 0) {
            moveLeft();
        } else if (choice == 1) {
            moveRight();
        }
        // Otherwise stays in current lane
    }
}