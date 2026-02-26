package model.Car;

/**
 * Classe abstraite représentant une voiture circulant sur des voies fixes.
 * 
 * Cette classe spécialise Car en ajoutant la notion de "lane" (voie).
 * Une voiture peut uniquement être sur 3 positions horizontales :
 * gauche, centre ou droite.
 */
public abstract class LaneCar extends Car {

    // Constantes représentant les différentes voies possibles
    protected static final int LEFT_LANE = 0;
    protected static final int CENTER_LANE = 1;
    protected static final int RIGHT_LANE = 2;

    // Voie actuelle de la voiture
    protected int currentLane;

    /**
     * Constructeur de LaneCar.
     *
     * @param color        Couleur de la voiture
     * @param initSpeed    Vitesse initiale
     * @param maxSpeed     Vitesse maximale
     * @param acceleration Accélération de la voiture
     */
    public LaneCar(String color, double initSpeed, double maxSpeed, double acceleration) {
        // Appel du constructeur de la classe parent (Car)
        super(color, initSpeed, maxSpeed, acceleration);

        // La voiture démarre toujours sur la voie centrale
        this.currentLane = CENTER_LANE;

        // Synchronisation de la position horizontale avec la voie
        // (Ici on suppose que positionX représente la voie)
        this.positionX = currentLane;
    }

    /**
     * Déplace la voiture vers la gauche si possible.
     * 
     * La voiture ne peut pas dépasser la voie la plus à gauche.
     */
    @Override
    public void moveLeft() {
        // Vérifie qu'on n'est pas déjà sur la voie la plus à gauche
        if (currentLane > LEFT_LANE) {
            currentLane--;           // Change la voie
            positionX = currentLane; // Met à jour la position horizontale
        }
    }

    /**
     * Déplace la voiture vers la droite si possible.
     * 
     * La voiture ne peut pas dépasser la voie la plus à droite.
     */
    @Override
    public void moveRight() {
        // Vérifie qu'on n'est pas déjà sur la voie la plus à droite
        if (currentLane < RIGHT_LANE) {
            currentLane++;           // Change la voie
            positionX = currentLane; // Met à jour la position horizontale
        }
    }

    /**
     * Retourne la voie actuelle de la voiture.
     *
     * @return un entier correspondant à LEFT_LANE, CENTER_LANE ou RIGHT_LANE
     */
    public int getCurrentLane() {
        return currentLane;
    }
}