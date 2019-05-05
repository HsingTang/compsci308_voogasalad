package runner.internal;

import javafx.animation.Animation;
import javafx.scene.Group;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;
import java.util.function.Consumer;

/**
 * This contains the Pause button and its functionality
 * @author Louis Jensen
 */
public class PauseButton extends ImageView {
    private boolean myPlayingStatus = true;
    private Animation myAnimation;
    private AudioManager myAudioManager;
    private final Double X_LOCATION = 460.0;
    private final Double Y_LOCATION = 6.0;
    private static final Double WIDTH = 30.0;
    private static final Double HEIGHT = 30.0;
    private static final String PAUSE_BUTTON = "pause.png";
    private Group myGroup;
    private HeadsUpDisplay myHUD;
    private Stage myStage;
    private Consumer myToggle;
    private LevelRunner myLevelRunner;

    /**
     * Constructor for pause button
     * @param levelRunner - LevelRunner object so the pause button can modify level
     * @param animation - Timeline that will be paused and resumed
     * @param group - Group of nodes displayed in level
     * @param stage - Stage to be closed on game exit
     * @param audioManager - AudioManager that pauses and resumes sounds
     */
    public PauseButton(LevelRunner levelRunner, Animation animation, Group group, Stage stage, AudioManager audioManager, HeadsUpDisplay hud){
        super(new Image(PAUSE_BUTTON, WIDTH, HEIGHT, true, false));
        this.setLayoutX(X_LOCATION);
        this.setLayoutY(Y_LOCATION);
        myLevelRunner = levelRunner;
        myHUD = hud;
        myAnimation = animation;
        myAudioManager = audioManager;
        this.setOnMouseClicked(event ->{
            toggleAnimation();
        });
        myGroup = group;
        myStage = stage;
        myToggle = (event)->{
            toggleAnimation();
        };
    }

    private void toggleAnimation() {
        if(myPlayingStatus){
            pauseGame();
        } else {
            resumeGame();
        }
        myPlayingStatus = !myPlayingStatus;
    }

    private void pauseGame() {
        myAnimation.pause();
        myGroup.getChildren().add(new PauseScreen(myLevelRunner, myToggle, myStage, myGroup.getTranslateX(), myHUD).getPauseMenu());
        myAudioManager.pauseAllSound();
    }

    private void resumeGame(){
        myAnimation.play();
        myAudioManager.resumeAllSound();
    }

    /**
     * Gets Pause Button image
     * @return ImageView of Pause button
     */
    public ImageView getPauseButton(){
        return this;
    }

    /**
     * Gets x coordinate so it can stay stationary while scrolling
     * @return Double x position
     */
    public Double getButtonX() {
        return X_LOCATION;
    }

}