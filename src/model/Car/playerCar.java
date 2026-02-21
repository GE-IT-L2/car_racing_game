package model.Car;

/**
 * PlayerCar represents the main player in single-player infinite mode.
 * 
 * Uses default balanced speed values.
 */
public class PlayerCar extends LaneCar {

    public PlayerCar(String color) {
        super(color, 100, 300, 50);
    }

}