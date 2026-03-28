package model.car;

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

        // Déplace la voiture en termes de distance parcourue
        move(deltaTime);
    }

    /**
     * Accélère la voiture progressivement (commande UP).
     */
    public void accelerer(double deltaTime) {
        if (!alive) return;
        currentSpeed = Math.min(maxSpeed, currentSpeed + acceleration * deltaTime);
    }

    /**
     * Freine la voiture (commande DOWN).
     */
    public void freiner(double deltaTime) {
        if (!alive) return;
        currentSpeed = Math.max(0, currentSpeed - acceleration * deltaTime * 1.5);
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

        // La voiture reste à un y constant (effet de piste mobile via obstacles)
        // positionY += distance; // retiré pour éviter que la voiture se déplace vers le bas
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

    public double getMaxSpeed() { return maxSpeed; }
    public double getInitSpeed() { return initSpeed; }
    public double getAcceleration() { return acceleration; }

    public void setPositionX(double x) { this.positionX = x; }
    public void setPositionY(double y) { this.positionY = y; }
    
    public void setInitSpeed(double speed) {
        this.initSpeed = speed;
        this.currentSpeed = speed;
    }
    public void setMaxSpeed(double speed) { 
        this.maxSpeed = speed; 
    }
    public void setAcceleration(double acc) { 
        this.acceleration = acc; 
    }
    
    public void setCurrentSpeed(double speed) {
        this.currentSpeed = speed;
    }

    /**
     * Accélère la voiture (moteur de contrôle manuel via deltaTime)
     */
    public void accelerer() {
        accelerer(1.0 / 60.0);
    }

    public void freiner() {
        freiner(1.0 / 60.0);
    }
}