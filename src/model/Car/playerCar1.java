package model.car;

/**
 * Voiture du Joueur 1 en mode 2 joueurs.
 * Hérite de PlayerCar avec configuration spécifique au joueur 1.
 */
public class PlayerCar1 extends PlayerCar {
    public PlayerCar1() {
        super("Blue");
    }

    public PlayerCar1(String color) {
        super(color);
    }

    @Override
    public String toString() {
        return "PlayerCar1{" +
                "color='" + color + '\'' +
                ", speed=" + currentSpeed +
                ", position=(" + positionX + "," + positionY + ")" +
                '}';
    }
}
