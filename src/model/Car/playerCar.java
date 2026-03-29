package model.car;

/**
 * Voiture du joueur en mode 1 joueur.
 * Hérite de LaneCar pour les déplacements sur les lanes.
 */
public class PlayerCar extends LaneCar {
    public PlayerCar() {
        super("Blue", 100, 300, 50);
    }

    public PlayerCar(String color) {
        super(color, 100, 300, 50);
    }
}
