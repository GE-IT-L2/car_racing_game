package model.obstacle;

public class ObstacleMobile extends Obstacle {

    private double vitesse;
    private double direction; // 1 = droite, -1 = gauche

    public ObstacleMobile(double positionX, double positionY, double vitesse) {
        super(positionX, positionY, 50, 50);
        this.vitesse = vitesse;
        this.direction = 1;
    }

    @Override
    public void update(double deltaTime) {
        // L'obstacle se déplace latéralement
        positionX += vitesse * direction * deltaTime;

        // Change de direction si il sort des limites (route de 300 de large)
        if (positionX > 300) {
            direction = -1;
        } else if (positionX < 0) {
            direction = 1;
        }
    }

    public double getVitesse() { return vitesse; }

    @Override
    public String toString() {
        return "ObstacleMobile[x=" + positionX + ", y=" + positionY + 
               ", vitesse=" + vitesse + "]";
    }
}