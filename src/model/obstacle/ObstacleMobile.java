package model.obstacle;

/**
 * Obstacle mobile : d'autres voitures ennemies qui se déplacent.
 * Peut changer de lane et avoir une intelligence basique.
 */
public class ObstacleMobile extends Obstacle {
    private double speedVariation;
    private int currentLane;
    private long lastLaneChange = 0;
    private static final int LANE_CHANGE_INTERVAL = 2000; // ms

    public ObstacleMobile(double x, double y, int lane) {
        super(x, y, "MOBILE");
        this.currentLane = lane;
        this.speedVariation = 0.8 + Math.random() * 0.4; // 0.8 à 1.2
    }

    @Override
    public void update(double gameSpeed) {
        // Descend avec variation de vitesse
        y += gameSpeed * speedVariation * 0.016;

        // Change occasionnellement de lane
        long now = System.currentTimeMillis();
        if (now - lastLaneChange > LANE_CHANGE_INTERVAL && Math.random() < 0.1) {
            currentLane = (currentLane + (Math.random() > 0.5 ? 1 : -1)) % 3;
            if (currentLane < 0) currentLane = 2;
            lastLaneChange = now;
        }
    }

    @Override
    public boolean isOutOfBounds(int screenHeight) {
        return y > screenHeight;
    }

    public int getLane() {
        return currentLane;
    }

    public void setLane(int lane) {
        this.currentLane = lane;
    }

    public double getSpeedMultiplier() {
        return speedVariation;
    }
}
