package runner.internal;

import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

/**
 * This creates the screen that appears when the level is completed
 * Boolean value input into constructor tells screen whether to display
 * won or lost
 * @author Louis Jensen
 */
public class GameBeatenScreen {
    private Stage myStage;
    private VBox myNode;
    private final String EXIT_GAME = "Exit Game";
    private final String BUTTON_ID = "button";
    private final String TEXT_ID = "text";
    private final String MENU_ID = "PauseMenu";
    private final String YOU_WIN = "Congratulations! \n you have completed \n the game";
    private final String YOU_LOSE = "You have lost the game \n good luck next time!";
    private final Integer VBOX_SPACING = 8;
    private final Double X_POSITION = 260.0;

    /**
     * Constructor for the game over screen
     * @param stage - Stage to put screen
     * @param translatedX - Main group's translateX value to ensure screen is placed correctly
     * @param win - Boolean win or lose the game
     */
    public GameBeatenScreen(Stage stage, Double translatedX, Boolean win, AudioManager audioManager) {
        audioManager.shutdown();
        myStage = stage;
        Button exit = new Button(EXIT_GAME);
        exit.setOnMouseClicked(event ->{
            myStage.close();
        });
        exit.setId(BUTTON_ID);
        String text;
        if (win){
            text = YOU_WIN;
        } else {
            text = YOU_LOSE;
        }
        Label congrats = new Label(text);
        congrats.setId(TEXT_ID);
        myNode = new VBox(VBOX_SPACING);
        myNode.setId(MENU_ID);
        myNode.setLayoutX(X_POSITION - translatedX);
        myNode.setLayoutY(X_POSITION);
        myNode.getChildren().addAll(congrats, exit);
    }

    /**
     * Gets the VBox of the screen
     * @return VBox to display on stage
     */
    public VBox getNode(){
        return myNode;
    }

}
