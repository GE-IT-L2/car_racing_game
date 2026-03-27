package ui.view;

import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import model.car.PlayerCar1;

public class VoitureView {

    private Rectangle shape;
    private PlayerCar1 voiture;

    public VoitureView(PlayerCar1 voiture) {
        this.voiture = voiture;

        shape = new Rectangle(40, 80);
        shape.setFill(Color.BLUE);
    }

    public Rectangle getNode() {
        return shape;
    }

    public void update() {
        shape.setX(voiture.getPositionX());
        shape.setY(voiture.getPositionY());
    }
}