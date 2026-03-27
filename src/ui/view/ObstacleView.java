// package ui.view;

// import javafx.scene.paint.Color;
// import javafx.scene.shape.Rectangle;
// import model.obstacle.Obstacle;

// public class ObstacleView {

//     private Rectangle obstacleShape;
//     private Obstacle obstacle;

//     public ObstacleView(Obstacle obstacle) {
//         this.obstacle = obstacle;

//         obstacleShape = new Rectangle(30, 30);
//         obstacleShape.setFill(Color.BLACK);
//     }

//     public Rectangle getNode() {
//         return obstacleShape;
//     }

//     public void update() {
//         obstacleShape.setX(obstacle.getPositionX());
//         obstacleShape.setY(obstacle.getPositionY());
//     }
// }