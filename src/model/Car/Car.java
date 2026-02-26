package model.Car;

public abstract class Car {

    protected String color;
    protected double currentSpeed;
    protected double initSpeed;
    protected double maxSpeed;
    protected double acceleration;

    protected double positionX;
    protected double positionY;
    protected double distanceTraveled;
    protected boolean alive;

    public Car(String color, double initSpeed, double maxSpeed, double acceleration) {
        this.color = color;
        this.initSpeed = initSpeed;
        this.currentSpeed = initSpeed;
        this.maxSpeed = maxSpeed;
        this.acceleration = acceleration;
        this.positionX = 0;
        this.positionY = 0;
        this.distanceTraveled = 0;
        this.alive = true;
    }

    public void update(double deltaTime) {
        if (!alive) return;
        increaseSpeed(deltaTime);
        move(deltaTime);
    }

    protected void increaseSpeed(double deltaTime) {
        if (currentSpeed < maxSpeed) {
            currentSpeed += acceleration * deltaTime;
            if (currentSpeed > maxSpeed) currentSpeed = maxSpeed;
        }
    }

    protected void move(double deltaTime) {
        double distance = currentSpeed * deltaTime;
        distanceTraveled += distance;
        positionY += distance;
    }

    public void collision() { this.alive = false; }
    public boolean isAlive() { return alive; }
    public int getScore() { return (int) distanceTraveled; }
    public void reset() {
        this.currentSpeed = initSpeed;
        this.positionX = 0;
        this.positionY = 0;
        this.distanceTraveled = 0;
        this.alive = true;
    }

    public String getColor() { return color; }
    public double getCurrentSpeed() { return currentSpeed; }
    public double getPositionX() { return positionX; }
    public double getPositionY() { return positionY; }
    public double getDistanceTraveled() { return distanceTraveled; }

    public String getCarStatus() {
       return "Color: " + color +
           " | Speed: " + currentSpeed +
           " | Distance: " + distanceTraveled +
           " | Lane: " + positionX +
           " | Alive: " + alive;
    }
    public abstract void moveLeft();
    public abstract void moveRight();
}