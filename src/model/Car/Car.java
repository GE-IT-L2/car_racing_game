package model.Car;

/**
 * Classe abstraite représentant une voiture générique.
 * 
 * Elle gère :
 * - la vitesse
 * - l'accélération
 * - le déplacement
 * - la distance parcourue
 * - l'état (vivante / détruite)
 * 
 * Les déplacements latéraux (gauche/droite) sont laissés
 * aux classes filles (ex: LaneCar).
 */
public abstract class Car {

    // --- Caractéristiques principales ---
    protected String color;          // Couleur de la voiture

    protected double currentSpeed;   // Vitesse actuelle
    protected double initSpeed;      // Vitesse initiale
    protected double maxSpeed;       // Vitesse maximale
    protected double acceleration;   // Accélération (variation de vitesse par seconde)

    // --- Position et progression ---
    protected double positionX;      // Position horizontale
    protected double positionY;      // Position verticale (avance sur la route)
    protected double distanceTraveled; // Distance totale parcourue

    // --- Etat de la voiture ---
    protected boolean alive;         // Indique si la voiture est active (pas détruite)

    /**
     * Constructeur principal.
     *
     * @param color        Couleur de la voiture
     * @param initSpeed    Vitesse initiale
     * @param maxSpeed     Vitesse maximale
     * @param acceleration Accélération
     */
    public Car(String color, double initSpeed, double maxSpeed, double acceleration) {
        this.color = color;

        this.initSpeed = initSpeed;
        this.currentSpeed = initSpeed;

        this.maxSpeed = maxSpeed;
        this.acceleration = acceleration;

        this.positionX = 0;
        this.positionY = 0;
        this.distanceTraveled = 0;

        this.alive = true; // La voiture démarre "vivante"
    }

    /**
     * Méthode appelée à chaque frame du jeu.
     * 
     * @param deltaTime Temps écoulé depuis la dernière mise à jour (en secondes)
     */
    public void update(double deltaTime) {

        // Si la voiture est détruite, elle ne bouge plus
        if (!alive) return;

        // Augmente la vitesse progressivement
        increaseSpeed(deltaTime);

        // Déplace la voiture
        move(deltaTime);
    }

    /**
     * Augmente la vitesse en fonction de l'accélération.
     * 
     * La vitesse est plafonnée à maxSpeed.
     */
    protected void increaseSpeed(double deltaTime) {
        if (currentSpeed < maxSpeed) {
            currentSpeed += acceleration * deltaTime;

            // Sécurise le dépassement de vitesse max
            if (currentSpeed > maxSpeed) {
                currentSpeed = maxSpeed;
            }
        }
    }

    /**
     * Déplace la voiture vers l'avant (axe Y).
     * 
     * distance = vitesse × temps
     */
    protected void move(double deltaTime) {

        // Calcul de la distance parcourue durant ce frame
        double distance = currentSpeed * deltaTime;

        // Mise à jour des métriques
        distanceTraveled += distance;
        positionY += distance;
    }

    /**
     * Déclare une collision.
     * 
     * La voiture devient inactive.
     */
    public void collision() {
        this.alive = false;
    }

    /**
     * @return true si la voiture est encore active
     */
    public boolean isAlive() {
        return alive;
    }

    /**
     * Le score correspond à la distance parcourue.
     * 
     * Conversion en int pour simplifier l'affichage.
     */
    public int getScore() {
        return (int) distanceTraveled;
    }

    /**
     * Réinitialise la voiture à son état initial.
     */
    public void reset() {
        this.currentSpeed = initSpeed;
        this.positionX = 0;
        this.positionY = 0;
        this.distanceTraveled = 0;
        this.alive = true;
    }

    // --- Getters ---

    public String getColor() { return color; }

    public double getCurrentSpeed() { return currentSpeed; }

    public double getPositionX() { return positionX; }

    public double getPositionY() { return positionY; }

    public double getDistanceTraveled() { return distanceTraveled; }

    // --- Méthodes abstraites ---
    // Forcent les classes filles à implémenter la logique latérale

    public abstract void moveLeft();
    public abstract void moveRight();
}