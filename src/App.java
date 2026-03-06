import model.Car.*;
import model.Difficulty.*;
import model.game.Score;
import java.util.Scanner;  // pour lire le choix de l'utilisateur

public class App {
    public static void main(String[] args) throws Exception {

        // ── existing code ────────────────────────────────────────────────────
        System.out.println("=== 2 PLAYERS HUMAN ===");
        PlayerCar1 p1 = new PlayerCar1("Blue");
        PlayerCar2 p2 = new PlayerCar2("Green");
        System.out.println("Player1 lane: " + p1.getCurrentLane());
        System.out.println("Player2 lane: " + p2.getCurrentLane());

        // ── difficulty choice ────────────────────────────────────────────────
        Scanner scanner = new Scanner(System.in);
        // Scanner lit ce que l'utilisateur tape au clavier

        System.out.println("\n=== CHOOSE YOUR DIFFICULTY ===");
        System.out.println("1 - Easy");
        System.out.println("2 - Medium");
        System.out.println("3 - Hard");
        System.out.print("Your choice (1/2/3) : ");

        int choice = scanner.nextInt();
        Difficulty diff;
        // on déclare diff sans l'initialiser encore

        if (choice == 1) {
            diff = new Easy();
        } else if (choice == 2) {
            diff = new Medium();
        } else if (choice == 3) {
            diff = new Hard();
        } else {
            System.out.println("Invalid choice, Easy selected by default.");
            diff = new Easy();
            // si l'utilisateur tape autre chose → Easy par défaut
        }

        diff.applyDifficulty();
        // affiche les paramètres du niveau choisi

        diff.applyToCar(p1);
        diff.applyToCar(p2);
        // applique la difficulté aux deux voitures

        System.out.println("\nP1 speed : " + p1.getCurrentSpeed());
        System.out.println("P2 speed : " + p2.getCurrentSpeed());

        Score score = new Score();
        score.ajouterPoints(diff.calculatePoints(p1.getScore()));
        System.out.println(score.toString());

        scanner.close();
    }
}