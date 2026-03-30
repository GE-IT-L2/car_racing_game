package model.car;

/**
 * Voiture du Joueur 2 en mode 2 joueurs.
 * Hérite de PlayerCar avec configuration spécifique au joueur 2.
 */
public class PlayerCar2 extends PlayerCar {
    public PlayerCar2() {
        super("Red");
    }

    public PlayerCar2(String color) {
        super(color);
    }

    @Override
    public String toString() {
        return "PlayerCar2{" +
                "color='" + color + '\'' +
                ", speed=" + currentSpeed +
                ", position=(" + positionX + "," + positionY + ")" +
                '}';
    }
}
