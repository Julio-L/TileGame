
import javafx.application.Application;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.stage.Stage;

/**
 *
 * @author JulioL
 */
public class TileUI extends Application {

    private TileGame game;
    private int gameWidth = 1400; 
    private int gameHeight = 800;
    private int windowWidth = 1800;
    private int windowHeight = 800;
    
    private BorderPane borderPane;
    private Pane pane;
    private Pane leftPane;
    private Pane rightPane;
    
    private double sidePanesWidth = (windowWidth - gameWidth) /2;
        
    public Parent loadGui(){
        
        borderPane = new BorderPane();
        
        pane = new Pane();
        pane.setMaxWidth(gameWidth);
        pane.setMaxHeight(gameHeight);
        pane.setStyle("-fx-background-color: #000000");
        pane.setMinHeight(gameHeight);
        pane.setMinWidth(gameWidth);

        leftPane = new Pane();
        leftPane.setStyle("-fx-background-color: #000000;" + "-fx-border-color: white");
        leftPane.setMinWidth((windowWidth - gameWidth) /2);

        
        
        
        rightPane = new Pane();
        rightPane.setStyle("-fx-background-color: #000000;" + "-fx-border-color: white");
        rightPane.setMinWidth((windowWidth - gameWidth) /2);



        borderPane.setCenter(pane);
        borderPane.setLeft(leftPane);
        borderPane.setRight(rightPane);


        return borderPane;
    }


    public void start(Stage stage) throws Exception {
         Scene scene = new Scene(loadGui(), windowWidth, windowHeight);
         stage.setScene(scene);
         scene.setOnKeyPressed(e -> game.moveCharacter(e.getCode(), pane));
         stage.show();
         game = new TileGame(gameHeight, gameWidth);
         game.loadNodes(pane, leftPane);
    }
    
    
    public static void main(String[] args){
        launch(args);
    }
        
}
