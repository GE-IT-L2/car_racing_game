package model.Car;

public abstract class LaneCar extends Car {

    protected static final int LEFT_LANE = 0;
    protected static final int CENTER_LANE = 1;
    protected static final int RIGHT_LANE = 2;

    protected int currentLane;

    public LaneCar(String color, double initSpeed, double maxSpeed, double acceleration) {
        super(color, initSpeed, maxSpeed, acceleration);
        this.currentLane = CENTER_LANE;
        this.positionX = currentLane;
    }

    @Override
    public void moveLeft() {
        if (currentLane > LEFT_LANE) {
            currentLane--;
            positionX = currentLane;
        }
    }

    @Override
    public void moveRight() {
        if (currentLane < RIGHT_LANE) {
            currentLane++;
            positionX = currentLane;
        }
    }

    public int getCurrentLane() { return currentLane; }
}