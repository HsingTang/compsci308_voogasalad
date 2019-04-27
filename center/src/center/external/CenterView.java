/**
 * @Author Megan Phibbons
 * @Date April 2019
 * @Purpose Main display class for the game center
 * @Dependencies javafx, GamePane, and HeaderBar
 * @Uses: This class is called on in Launcher to show the available games that a user can play
 */

package center.external;

import data.external.DatabaseEngine;
import javafx.application.Application;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import frontend.games.GamePane;
import frontend.header.HeaderBar;

public class CenterView extends Application {
    private Scene myScene;

    private static final double STAGE_WIDTH = 1250;
    private static final double STAGE_HEIGHT = 750;
    private static final Color BACKGROUND_COLOR = Color.rgb(46, 43, 51);
    private static final String CSS_LOCATION = "center.css";

    /**
     * @purpose method required for everything that extends Application, this starts up the application and shows the UI
     * on-screen
     * @param stage the stage given to start that holds the display
     */
    public void start (Stage stage) {
        CenterView view = new CenterView();
        stage.setScene(view.getScene());
        stage.setResizable(false);
        stage.show();
    }

    /**
     * @purpose act as the main class that launches the application
     * @param args the standard args passed into main methods
     */
    public static void main(String[] args){
        launch(args);
    }

    /**
     * @purpose constructor for the CenterView class. This calls on a helper method that sets up the main scene.
     */
    public CenterView() {
        initializeScene();
    }

    /**
     * @purpose share the scene so that Launcher can display the game center
     * @return the current scene of the CenterView
     */
    public Scene getScene() {
        return myScene;
    }

    private void initializeScene() {
        BorderPane root = new BorderPane();
        HeaderBar myHeader = new HeaderBar();
        Pane layout = myHeader.getHeaderLayout();
        BorderPane.setAlignment(layout, Pos.TOP_CENTER);
        root.getStylesheets().add(CSS_LOCATION);
        root.setTop(layout);
        root.setCenter((new GamePane()).getDisplay());
        myScene = new Scene(root, STAGE_WIDTH, STAGE_HEIGHT, BACKGROUND_COLOR);
    }

    @Override
    public void stop() throws Exception {
        super.stop();
        DatabaseEngine.getInstance().close();
    }
}
