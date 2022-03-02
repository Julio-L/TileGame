
import java.util.Timer;
import java.util.TimerTask;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.scene.control.Button;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import javafx.scene.shape.Rectangle;
import javafx.util.Duration;

/**
 *
 * @author JulioL
 */
public class TileGame {
    
    private double windowHeight;
    private double windowWidth;
    
    private Button startRound;
    private Timer enableButtonTimer;
    
    private Character character;
    private double characterWidth = 50;
    private double characterHeight;
    private double characterSpeed = 80; //20
    
    private Obstacle[] obstacles;
    private double  obstacleProb = .9;
    private int obstaclesPerCol = 10;
    private int numOfObstacles = 300;
    private int obstaclesSpacing = 150;
    
    private double obstaclesWidth = 30;
    private double obstaclesHeight;

    
    
    private Timeline timeline;
    
    private int obstaclesCycle = 1;
    private int timeLineDuration = 5;
    private double totalTime;

    private int level = 1;
    private int obstaclesLevel = obstaclesPerCol * level;
    
    private int moveOffset = 1;
    
    public TileGame(double height, double width){
        this.windowHeight = height;
        this.windowWidth = width;
        this.obstaclesHeight= windowHeight / obstaclesPerCol;
        characterHeight = obstaclesHeight - 4;
        characterSpeed = obstaclesHeight;

        
        character = new Character(0, obstaclesHeight * (obstaclesPerCol/2 - 1) + 2, characterHeight, characterWidth);
        
        obstacles = new Obstacle[numOfObstacles];
        
        double ySpacing = 0;
        double xSpacing = 0;
        
    
        for(int i =0; i < obstacles.length; i++){
            
            if(i != 0 && i % obstaclesPerCol == 0){
                ySpacing = 0;
                xSpacing += obstaclesSpacing;
            }
            obstacles[i] = new Obstacle(windowWidth - (obstaclesWidth) + xSpacing, ySpacing, obstaclesWidth,  obstaclesHeight);
            obstacles[i].setOpacity(0);
            
            ySpacing += obstaclesHeight;

            
            obstacles[i].setRandomHealth();
        }
        startRound = new Button("START ROUND");
        enableButtonTimer = new Timer();
    }
    
    public void moveCharacter(KeyCode key, Pane gamePane){
        
         if(key == KeyCode.DOWN){
            character.setY(character.getY() + characterSpeed );

             if(character.getY() > windowHeight - characterHeight){
                character.setY(windowHeight - characterHeight - 2);
            }
             
             
        }else if(key == KeyCode.UP){
            character.setY(character.getY() - characterSpeed );
            if(character.getY() < 0){
                character.setY(2);
            }
        }else if(key == KeyCode.SPACE){
            character.shootAnimation(gamePane, obstacles);
        }
        
    }
    
    
    public void startRound(){
        
        startRound.setDisable(true);
        
        resetObstaclesPosition();
        
        timeline = new Timeline();
        timeline.setCycleCount(obstaclesCycle);

        KeyValue kv , kvText;
        KeyFrame kf, kfText;
        totalTime = timeLineDuration;
        
        
        for(int i =0; i < obstaclesLevel; i++){
            
            
            if(i != 0 &&  i % obstaclesPerCol == 0){
                totalTime = totalTime + .7;
            }
            
            kv = new KeyValue(obstacles[i].getObstacle().xProperty(), 0 - obstaclesWidth - 10);
            kf = new KeyFrame(Duration.seconds(totalTime),  kv);
            kvText = new KeyValue(obstacles[i].getHealthText().layoutXProperty(), 0 - obstaclesWidth - 10);
            kfText = new KeyFrame(Duration.seconds(totalTime),  kvText);
            timeline.getKeyFrames().addAll(kf, kfText);
        }
        

        kf = new KeyFrame(Duration.seconds(timeline.getCycleDuration().toSeconds()), e -> removeObstacles());
        timeline.getKeyFrames().addAll(kf, new KeyFrame(Duration.seconds(timeline.getCycleDuration().toSeconds()), e -> setObstaclesHealth()));
        
        removeObstacles();
         timeline.play();
         character.startCollisionTest(obstacles, obstaclesCycle, (int)timeline.getCycleDuration().toSeconds());
         
         level += 1;
         obstaclesLevel = obstaclesPerCol * level;
         
         if(obstaclesLevel > obstacles.length){
             obstaclesLevel = obstacles.length;
         }
         
         
         TimerTask enableRound = new TimerTask(){
             
             public void run(){
                 startRound.setDisable(false);
             } 
         };
         
         
         
         enableButtonTimer.schedule(enableRound, (int)(totalTime * 1000));
    }
    

    
    public void setObstaclesHealth(){
        for(int i =0; i < obstacles.length; i++){
            obstacles[i].setRandomHealth();
        }
    }
    

    
    public void resetObstaclesPosition(){
       
        double xSpacing = 0;
        
        for(int i =0; i < obstacles.length; i++){
            
            if(i != 0 && i % obstaclesPerCol == 0){
                xSpacing += obstaclesSpacing;
            }
            
            obstacles[i].setX(windowWidth - (obstaclesWidth) + xSpacing);
            obstacles[i].getHealthText().setLayoutX(windowWidth - (obstaclesWidth) / 2 + xSpacing);
        }
    }
    
    public void removeObstacles(){
        
        for(int i = 0; i < obstacles.length; i++){
            if(booleanSource()){
                obstacles[i].setOpacity(0);
            }else{
                obstacles[i].setOpacity(1);
            }
        }
        
    }
    

    
    public boolean booleanSource(){
        double rand = Math.random();
        return rand > obstacleProb;
    }
    
    public void loadNodes(Pane pane, Pane leftPane){
        leftPane.getChildren().add(character.positionHealthBar(leftPane.getWidth(), leftPane.getHeight()));
        leftPane.getChildren().add(character.positionHealthLabel(leftPane.getWidth(), leftPane.getHeight()));
        
        
        startRound.setLayoutX(leftPane.getWidth()/2 - 50);
        startRound.setLayoutY(leftPane.getWidth() /4);
        startRound.setOnAction(e -> startRound());
        
        leftPane.getChildren().add(startRound);
        
        pane.getChildren().addAll(character.getBody());
        for(int i =0; i < obstacles.length; i++){
            pane.getChildren().addAll(obstacles[i].getObstacle(), obstacles[i].getHealthText());
            
        }
    }
    

    
}
