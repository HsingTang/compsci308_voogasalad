package runner.internal;

import engine.external.Engine;
import engine.external.Entity;
import engine.external.Level;
import engine.external.actions.NumericAction;
import engine.external.actions.SoundAction;
import engine.external.actions.ValueAction;
import engine.external.component.ValueComponent;
import engine.external.conditions.EqualToCondition;
import engine.external.conditions.StringEqualToCondition;
import engine.external.events.Event;
import engine.external.component.LivesComponent;
import engine.external.component.NameComponent;
import engine.external.component.ScoreComponent;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.util.Duration;
import runner.external.Game;
import runner.internal.runnerSystems.*;
import java.util.*;
import java.util.function.Consumer;

/**
 * LevelRunner runs the game loop and displays level on screen
 * @author Louis Jensen
 */
public class LevelRunner {
    private PauseButton myPauseButton;
    private Node myPause;
    private Collection<Entity> myEntities;
    private int mySceneWidth;
    private int mySceneHeight;
    private Stage myStage;
    private Group myGroup;
    private Scene myScene;
    private Engine myEngine;
    private Timeline myAnimation;
    private static final int FRAMES_PER_SECOND = 60;
    private static final int MILLISECOND_DELAY = 1000 / FRAMES_PER_SECOND;
    private static final double SECOND_DELAY = 1.0 / FRAMES_PER_SECOND;
    private Set<KeyCode> myCurrentKeys;
    private boolean canPause = false;
    private Consumer<Double> myLevelChanger;
    private List<RunnerSystem> mySystems;
    private HeadsUpDisplay myHUD;
    private Text myLabel;
    private Rectangle myHudBackground;
    private int myLevelCount;
    private AudioManager myAudioManager;
    private Image myBackground;
    private ImageView myImageViewBackground;
    private Level myLevel;
    private String myUsername;
    private String myAuthorName;
    private String myGameName;
    private Game myGame;
    private final String RUNNER_ID = "runnerStyle.css";
    private final String GOOGLE_FONT_LINK = "https://fonts.googleapis.com/css?family=Gugi";
    private final double HUD_HEIGHT = 40.0;

    /**
     * Constructor for level runner
     * @param level - current level to be played
     * @param width - width of screen
     * @param height - height of screen
     * @param stage - stage to create level on
     * @param playNext - consumer to change levels
     * @param numLevels - total number of levels in the current game
     */
    public LevelRunner(Level level, int width, int height, Stage stage, Consumer playNext, int numLevels,
                       Image image, Double score, Double lives, String authorName, String gameName,
                       String username, Game game){
        myLevel = level;
        myUsername = username;
        myGameName = gameName;
        myAuthorName = authorName;
        myGame = game;
        myLevelCount = numLevels;
        mySceneWidth = width;
        mySceneHeight = height;
        myCurrentKeys = new HashSet<>();
        addMusic(level);
        myEngine = new Engine(level);
        myHUD = new HeadsUpDisplay(width);
        myEntities = myEngine.updateState(myCurrentKeys);
        if(score!=null && lives!=null)keepScoreAndLives(score, lives);
        myAudioManager = new AudioManager(5);
        myLevelChanger = playNext;
        myAnimation = new Timeline();
        myBackground = image;
        buildStage(stage);
        startAnimation();
        addButtonsAndHUD();
        myStage.show();
    }

    private void addMusic(Level level) {
        Entity soundEntity = new Entity();
        soundEntity.addComponent(new NameComponent("###sound"));
        soundEntity.addComponent(new ValueComponent(1.0));

        Event makeSound = new Event();
        makeSound.addConditions(new StringEqualToCondition(NameComponent.class, "###sound"));
        makeSound.addConditions(new EqualToCondition(ValueComponent.class, 1.0));

        if (level.getMusic() != null) makeSound.addActions(new SoundAction(level.getMusic()));
        makeSound.addActions(new ValueAction(NumericAction.ModifyType.ABSOLUTE, 0.0));

        level.addEntity(soundEntity);
        level.addEvent(makeSound);
    }

    private void keepScoreAndLives(Double score, Double lives) {
        for(Entity entity : myEntities){
            if (entity.hasComponents(ScoreComponent.class)){
                ((ScoreComponent)entity.getComponent(ScoreComponent.class)).setValue(score);
            }
            if (entity.hasComponents(LivesComponent.class)){
                ((LivesComponent)entity.getComponent(LivesComponent.class)).setValue(lives);
            }
        }
    }

    private void initializeSystems() {
        SystemManager systems = new SystemManager(this, myGroup, myStage, myAnimation,
                mySceneWidth, mySceneHeight, myLevelChanger, myScene, myHUD, myAudioManager, myLevelCount,
                myAuthorName, myGameName, myUsername, myEngine, myLevel, myGame);
        mySystems = systems.getSystems();
    }

    private void addButtonsAndHUD() {
        myHudBackground = new Rectangle(0,0, mySceneWidth, HUD_HEIGHT);
        myGroup.getChildren().add(myHudBackground);
        myPauseButton = new PauseButton(this, myAnimation, myGroup, myStage, myAudioManager, myHUD);
        myPause = myPauseButton.getPauseButton();
        myGroup.getChildren().add(myPause);
        canPause = true;
        myLabel = myHUD.getLabel();
        myGroup.getChildren().add(myLabel);
    }

    private void buildStage(Stage stage) {
        myStage = stage;
        myStage.setOnCloseRequest(windowEvent -> {
            myAnimation.stop();
        });
        myStage.setResizable(false);
        myGroup = new Group();
        myScene = new Scene(myGroup, mySceneWidth, mySceneHeight);
        myImageViewBackground = new ImageView(myBackground);
        myGroup.getChildren().addAll(myImageViewBackground);
        myScene.setOnKeyPressed(e -> handleKeyPress(e.getCode()));
        myScene.setOnKeyReleased(e -> handleKeyRelease(e.getCode()));
        myScene.getStylesheets().add(RUNNER_ID);
        myScene.getStylesheets().add(GOOGLE_FONT_LINK);
        initializeSystems();
        updateGUI();
        myStage.setScene(myScene);
    }

    private void startAnimation(){
        var frame = new KeyFrame(Duration.millis(MILLISECOND_DELAY), e -> step(SECOND_DELAY));
        myAnimation.setCycleCount(Timeline.INDEFINITE);
        myAnimation.getKeyFrames().add(frame);
        myAnimation.play();
    }

    private void handleKeyPress(KeyCode code) {
        myCurrentKeys.add(code);
    }

    private void handleKeyRelease(KeyCode code){
        myCurrentKeys.remove(code);
    }

    private void step (double elapsedTime) {
        myEntities = myEngine.updateState(myCurrentKeys);

        updateGUI();
    }

    private void updateGUI(){
        myGroup.getChildren().retainAll(myPause, myLabel, myHudBackground, myImageViewBackground);
        for(RunnerSystem system : mySystems){
            system.update(myEntities);
        }
        if (canPause) updateButtonsAndHUD();
    }

    private void updateButtonsAndHUD(){
        myPause.setLayoutX(myPauseButton.getButtonX() - myGroup.getTranslateX());
        myLabel.setLayoutX(myHUD.getX() - myGroup.getTranslateX());
        myHudBackground.setLayoutX(myHudBackground.getX() - myGroup.getTranslateX());
        myHUD.updateLabel();
    }

    /**
     * Gets entities currently in the level
     * @return Collection of current entities
     */
    public Collection<Entity> getEntities(){
        return myEntities;
    }

    /**
     * Gets the engine being used to create level
     * @return Engine
     */
    public Engine getEngine(){
        return myEngine;
    }
}