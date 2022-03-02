
import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.image.Image;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.Shape;
import javafx.util.Duration;

/**
 *
 * @author JulioL
 */
public class Character {
    private Rectangle body;
    private int health;
    private double height;
    private double width;
    private double x;
    private double y;
    private Image character = new Image("Character.png");
    private ProgressBar healthBar;
    private double maxHealth;
    private Timeline collisionTimeLine;
    private Label healthLabel;
    private Timer collisionTimer;
    private int ammo = 1000;
    private Timeline bulletTimeLine;
    private int bulletRadius = 10;
    private int bulletSpeed = 3;

    public Character(double x, double y, double height, double width){  
        this.x = y;
        this.y = y;
        this.height = height;
        this.width = width;
        this.body = new Rectangle(x, y, width, height);
        this.health = 100;
        this.maxHealth = 100;
        body.setFill(new ImagePattern(character));
        body.setStyle("-fx-stroke: white");
        
        healthBar = new ProgressBar(health / maxHealth);
        
        healthLabel = new Label(health + "");
        healthLabel.setStyle("-fx-text-fill: white;");
        
    }
    
    public void shootAnimation(Pane gamePane, Obstacle[] obstacles){
         if(ammo == 0) return;
         
            Circle bullet = new Circle(0, (y + (height/2)), bulletRadius, Color.RED);
            gamePane.getChildren().add(bullet);

            bullet.setStyle("-fx-fill: white");

            bulletTimeLine = new Timeline();

            int cycleCount = 1;
            Duration duration = Duration.seconds(bulletSpeed);

            bulletTimeLine.setCycleCount(1);


            KeyValue kv = new KeyValue(bullet.centerXProperty(), gamePane.getWidth());
            KeyFrame kf = new KeyFrame(duration, kv);

            KeyFrame remove = new KeyFrame(duration, e ->{gamePane.getChildren().removeAll(bullet);});



            bulletTimeLine.getKeyFrames().addAll(kf, remove);

            bulletTimeLine.play();
            startBulletCollisionTesting(obstacles, bullet, cycleCount, bulletSpeed);
            ammo--;

    }
    
    public void bulletCollision(Obstacle[] obstacles, Circle bullet){
        for(int i=0; i < obstacles.length; i++){
            Rectangle obstacle = obstacles[i].getObstacle();
            
            boolean collision = obstacle.intersects(bullet.getBoundsInParent());
            
            if(collision && bullet.getOpacity() != 0 && obstacle.getOpacity() != 0){
                bullet.setOpacity(0);
                obstacles[i].setHealth(obstacles[i].getHealth() - 1);
            }
            
            
        }
    }
    
    public void startBulletCollisionTesting(Obstacle[] obstacles, Circle bullet, int timeLineCycle, int timeLineDuration){
        Timeline bulletCollisionTL = new Timeline();
        
        bulletCollisionTL.setCycleCount(Timeline.INDEFINITE);
        
        KeyFrame collisionTest = new KeyFrame(Duration.millis(10), e-> bulletCollision(obstacles, bullet));
        bulletCollisionTL.getKeyFrames().add(collisionTest);
        
        Timer stopCollision = new Timer();
        
        TimerTask stop = new TimerTask(){
             public void run(){
                    bulletCollisionTL.stop();
                    stopCollision.cancel();
//                    System.out.println("Stopped");
                }
        };
        
        stopCollision.schedule(stop, ((timeLineCycle * timeLineDuration)  * 1000) + 100);
        
        bulletCollisionTL.play();
    }
    
    public void collision(Obstacle[] obstacles){
        
        
        for(int i =0; i < obstacles.length; i++){
            Rectangle other = obstacles[i].getObstacle();
            boolean collision = body.getBoundsInParent().intersects(other.getBoundsInParent());
            if(collision && obstacles[i].getOpacity() != 0){
                obstacles[i].setOpacity(0); 
                health -= obstacles[i].getHealth();
                healthBar.setProgress(health / maxHealth);
                healthLabel.setText(health + "");
            }
        }
    }
    
    public ProgressBar positionHealthBar(double width, double height){
        healthBar.setLayoutX(width /2 - 50);
        healthBar.setLayoutY(height /2);
        return healthBar;
    }
    
    
    public Label positionHealthLabel(double width, double height){
        healthLabel.setLayoutX(width /2 - 10);
        healthLabel.setLayoutY(height /2 - 13);
        return healthLabel;
    }

    public void resetCollided(){
        collisionTimeLine.play();
    }
    
        public void startCollisionTest(Obstacle[] obstacles, int timeLineCycle, int timeLineDuration){
            collisionTimer = new Timer();
            TimerTask task = new TimerTask(){
                public void run(){
                    collisionTimeLine.stop();
                    collisionTimer.cancel();
                }
            };
            collisionTimer.schedule(task, (timeLineCycle * timeLineDuration) * 1000 + 1000);


            KeyFrame kv = new KeyFrame(Duration.millis(10), e -> collision(obstacles));
            collisionTimeLine = new Timeline();
            collisionTimeLine.setCycleCount(Timeline.INDEFINITE);
            collisionTimeLine.getKeyFrames().add(kv);
            collisionTimeLine.play();
    }
    
    public Rectangle getBody() {
        return body;
    }

    public int getHealth() {
        return health;
    }

    public double getHeight() {
        return height;
    }

    public double getWidth() {
        return width;
    }

    public double getX() {
        return x;
    }

    public double getY() {
        return y;
    }

    public void setBody(Rectangle body) {
        this.body = body;
    }

    public void setHealth(int health) {
        this.health = health;
    }

    public void setHeight(double height) {
        this.height = height;
    }

    public void setWidth(double width) {
        this.width = width;
    }

    public void setX(double x) {
        System.out.println(x);
        this.x = x;
        body.setX(this.x);
    }

    public void setY(double y) {
        this.y = y;
        body.setY(y);
    }

    public ProgressBar getHealthBar() {
        return healthBar;
    }

    public double getMaxHealth() {
        return maxHealth;
    }

    public void setHealthBar(ProgressBar healthBar) {
        this.healthBar = healthBar;
    }

    public void setMaxHealth(double maxHealth) {
        this.maxHealth = maxHealth;
    }
    
    
    
    
}
