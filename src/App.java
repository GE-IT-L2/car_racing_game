import model.Car.*;
public class App {
    public static void main(String[] args) throws Exception {
         
        System.out.println("=== 2 PLAYERS HUMAN ===");

        PlayerCar1 p1 = new PlayerCar1("Blue");
        PlayerCar2 p2 = new PlayerCar2("Green"); // human

        System.out.println("Player1 lane: " + p1.getCurrentLane());
        System.out.println("Player2 lane: " + p2.getCurrentLane());

        // simulate updates
        System.out.println("\nCar Status : \n");
        for (int i = 0; i < 3; i++) {
            p1.update(1.0);
            p1.moveRight();
            System.out.println(p1.getCarStatus());
        }
    }
}
