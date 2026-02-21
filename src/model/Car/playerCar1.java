package model.Car;

/**
 * PlayerCar2 represents a second human-controlled player
 * in multiplayer mode.
 * 
 * Slightly different speed tuning for balance.
 */
public class PlayerCar1 extends LaneCar {

    public PlayerCar1(String color) {
        super(color, 100, 280, 45);
    }

}