package runner.internal;

import engine.external.Entity;
import engine.external.component.NextLevelComponent;
import engine.external.component.ProgressionComponent;
import javafx.scene.control.Button;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import java.util.function.Consumer;

/**
 * Creates the screen to be displayed when user clicks pause button
 * @author Louis Jensen
 */
public class PauseScreen {
    private VBox myPauseMenu;
    private Button myResumeButton;
    private Button myRestartButton;
    private Button myExitButton;
    private Consumer myToggler;
    private Stage myStage;
    private LevelRunner myLevelRunner;
    private HeadsUpDisplay myHUD;
    private final String PAUSE_ID = "PauseMenu";
    private final String BUTTON_ID = "button";
    private final double VBOX_SPACING = 8.0;
    private final double PAUSE_X_POSITION = 310.0;
    private final double PAUSE_Y_POSITION = 180.0;
    private final String RESUME = "Resume";
    private final String RESTART = "Restart Level";
    private final String EXIT = "Exit Game";

    /**
     * Constructor for PauseScreen
     * @param levelRunner - LevelRunner so that level can be restarted
     * @param toggle - Allows the level to change
     * @param stage - Stage to be closed on game exit
     * @param translatedX - Current translateX value so that pause menu can be initialized in correct location
     */
    public PauseScreen(LevelRunner levelRunner, Consumer toggle, Stage stage, Double translatedX, HeadsUpDisplay hud){
        myPauseMenu = new VBox(VBOX_SPACING);
        myPauseMenu.setId(PAUSE_ID);
        myLevelRunner = levelRunner;
        myHUD = hud;
        initializeButtons();
        myPauseMenu.getChildren().addAll(myResumeButton, myRestartButton, myExitButton);
        myPauseMenu.setLayoutX(PAUSE_X_POSITION - translatedX);
        myPauseMenu.setLayoutY(PAUSE_Y_POSITION);
        myToggler = toggle;
        myStage = stage;
    }

    private void initializeButtons() {
        myResumeButton = new Button(RESUME);
        myResumeButton.setOnMouseClicked(event ->{
            myToggler.accept(null);
        });
        myResumeButton.setId(BUTTON_ID);
        myRestartButton = new Button(RESTART);
        myRestartButton.setOnMouseClicked(event ->{
            restartLevel();
        });
        myRestartButton.setId(BUTTON_ID);
        myExitButton = new Button(EXIT);
        myExitButton.setOnMouseClicked(event ->{
            myStage.close();
        });
        myExitButton.setId(BUTTON_ID);
    }

    private void restartLevel() {
        for(Entity entity : myLevelRunner.getEntities()){
            entity.addComponent(new NextLevelComponent(myHUD.getLevel()));
            break;
        }
        myToggler.accept(null);
    }

    /**
     * Gets the pause menu
     * @return VBox menu to be placed on screen
     */
    public VBox getPauseMenu(){
        return myPauseMenu;
    }
}