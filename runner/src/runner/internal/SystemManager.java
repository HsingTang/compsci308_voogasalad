package runner.internal;

import engine.external.Engine;
import engine.external.Level;
import engine.external.component.*;
import javafx.animation.Timeline;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.stage.Stage;
import runner.external.Game;
import runner.internal.runnerSystems.*;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.function.Consumer;

/**
 * Creates and stores all systems used by runner in the game loop
 * @author Louis Jensen
 */
public class SystemManager {
    private List<RunnerSystem> mySystems;
    private LevelRunner myLevelRunner;
    private Group myGroup;
    private Stage myStage;
    private Timeline myAnimation;
    private int mySceneWidth;
    private int mySceneHeight;
    private Consumer myLevelChanger;
    private Scene myScene;
    private HeadsUpDisplay myHUD;
    private AudioManager myAudioManger;
    private int myLevelCount;
    private Engine myEngine;
    private Level myLevel;
    private String myUsername;
    private String myAuthorName;
    private String myGameName;
    private Game myGame;

    /**
     * Constructor for SystemManager
     * Must hold all things the various systems need to do their job
     * @param levelRunner
     * @param group
     * @param stage
     * @param animation
     * @param width
     * @param height
     * @param changer
     * @param scene
     * @param hud
     * @param audioManager
     * @param numLevels
     */
    public SystemManager(LevelRunner levelRunner, Group group, Stage stage, Timeline animation,
                         int width, int height, Consumer changer, Scene scene, HeadsUpDisplay hud,
                         AudioManager audioManager, int numLevels, String authorName, String gameName,
                         String username, Engine engine, Level level, Game game){
        myLevelRunner = levelRunner;
        myGroup = group;
        myStage = stage;
        myAnimation = animation;
        mySceneWidth = width;
        mySceneHeight = height;
        myLevelChanger = changer;
        myScene = scene;
        myHUD = hud;
        myAudioManger = audioManager;
        myLevelCount = numLevels;
        myEngine = engine;
        myLevel = level;
        myUsername = username;
        myAuthorName = authorName;
        myGameName = gameName;
        myGame = game;
        mySystems = new ArrayList<>();
        addSystems();
    }

    private void addSystems() {
        Collection<Class<? extends Component>> components2 = new ArrayList<>();
        components2.add(CameraComponent.class);
        mySystems.add(new ScrollingSystem(components2, myLevelRunner, myGroup, myScene));
        Collection<Class<? extends Component>> components3 = new ArrayList<>();
        components3.add(ImageViewComponent.class);
        mySystems.add(new ImageDisplaySystem(components3, myLevelRunner, myGroup));
        Collection<Class<? extends Component>> components4 = new ArrayList<>();
        components4.add(ScoreComponent.class);
        mySystems.add(new ScoringSystem(components4, myLevelRunner, myHUD));
        Collection<Class<? extends Component>> components5 = new ArrayList<>();
        components5.add(ScoreComponent.class);
        mySystems.add(new LivesSystem(components5, myLevelRunner, myHUD));
        Collection<Class<? extends Component>> components7 = new ArrayList<>();
        components7.add(PlayAudioComponent.class);
        mySystems.add(new SoundSystem(components7, myLevelRunner, myAudioManger));
        Collection<Class<? extends Component>> components = new ArrayList<>();
        components.add(ProgressionComponent.class);
        mySystems.add(new GameOverSystem(components, myLevelRunner, myGroup, myStage, myAnimation, mySceneWidth, mySceneHeight, myLevelChanger, myLevelCount, myAudioManger));
        Collection<Class<? extends Component>> components9 = new ArrayList<>();
        components9.add(SaveComponent.class);
        mySystems.add(new SavingSystem(components9, myLevelRunner, myEngine, myLevel, myGame, myUsername, myGameName, myAuthorName));
        Collection<Class<? extends Component>> components8 = new ArrayList<>();
        components8.add(NextLevelComponent.class);
        mySystems.add(new NextLevelSystem(components8, myLevelRunner, myGroup, myStage, myAnimation, mySceneWidth, mySceneHeight, myLevelChanger, myLevelCount, myHUD, myAudioManger));
    }

    /**
     * Gets all the systems
     * @return List of Systems so that LevelRunner can access them all to update level
     */
    public List<RunnerSystem> getSystems(){
        return mySystems;
    }
}
