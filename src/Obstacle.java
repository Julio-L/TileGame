
import javafx.scene.control.Label;

import javafx.scene.shape.Rectangle;

/**
 *
 * @author JulioL
 */
public class Obstacle {

    private int health;
    private double startingHealth;
    private Rectangle obstacle;
    private double x;
    private double y;
    private double height;
    private double width;
    private double opacity = 1;
    private Label healthText;
    private int minHealth = 1;
    private int maxHealth = 5;
    
    public Obstacle(double x, double y, double width, double height){
        this.x = x;
        this.y = y;
        this.height = height;
        this.width = width;
        obstacle = new Rectangle(x, y, width, height);
        obstacle.setStyle("-fx-fill: white; -fx-stroke: black");

        
        healthText = new Label();
        healthText.setLayoutX(x + (width /2));
        healthText.setLayoutY(y);
        healthText.setStyle("-fx-text-fill: black");
        
    }

    public int getHealth() {
        return health;
    }

    public Rectangle getObstacle() {
        return obstacle;
    }

    public double getX() {
        return x;
    }

    public double getY() {
        return y;
    }

    public double getHeight() {
        return height;
    }

    public double getWidth() {
        return width;
    }

    public void setHealth(int health) {
        this.health = health;
        this.healthText.setText(health + "");
        this.obstacle.setOpacity(health / startingHealth);
    }

    public void setObstacle(Rectangle obstacle) {
        this.obstacle = obstacle;
    }

    public void setX(double x) {
        this.x = x;
        obstacle.setX(x);
    }

    public void setY(double y) {
        this.y = y;
    }

    public void setHeight(double height) {
        this.height = height;
    }

    public void setWidth(double width) {
        this.width = width;
    }

    public double getOpacity() {
        return opacity;
    }

    public void setOpacity(double opacity) {
        this.opacity = opacity;
        obstacle.setOpacity(opacity);
        healthText.setOpacity(opacity);
    }
    
    public void setRandomHealth(){
        int rand = (int)(Math.random()*maxHealth) + 1;
        health = rand;
        startingHealth = rand;
        healthText.setText(health + "");
        
    }

    public Label getHealthText() {
        return healthText;
    }

    public void setHealthText(Label healthText) {
        this.healthText = healthText;
    }
    
    

    
    
}
