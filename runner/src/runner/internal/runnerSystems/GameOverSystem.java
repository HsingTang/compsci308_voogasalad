package runner.internal.runnerSystems;

import engine.external.Entity;
import engine.external.component.Component;
import engine.external.component.ProgressionComponent;
import javafx.animation.Animation;
import javafx.scene.Group;
import javafx.stage.Stage;
import runner.internal.AudioManager;
import runner.internal.GameBeatenScreen;
import runner.internal.LevelRunner;
import java.util.Collection;
import java.util.function.Consumer;

/**
 * System that checks if the game is over and updates accordingly
 * @author Louis Jensen
 */
public class GameOverSystem extends RunnerSystem {
    private Group myGroup;
    private Stage myStage;
    private Animation myAnimation;
    private int myWidth;
    private int myHeight;
    private Consumer myLevelChanger;
    private int myLevelCount;
    private AudioManager myAudioManager;

    /**
     * Constructor for the progression system
     * @param requiredComponents - list of all components necessary for system
     * @param levelRunner - LevelRunner object so that system can modify the level
     * @param group - Group so that system can modify things on screen
     * @param stage - Stage of level to be modified
     * @param animation - Timeline that runs game loop
     * @param width - width of screen
     * @param height - height of screen
     * @param consumer - allows the system to change level
     * @param numLevels - total number of levels in the game
     */
    public GameOverSystem(Collection<Class<? extends Component>> requiredComponents, LevelRunner levelRunner,
                          Group group, Stage stage, Animation animation, int width, int height,
                          Consumer consumer, int numLevels, AudioManager audioManager) {
        super(requiredComponents, levelRunner);
        myGroup = group;
        myStage = stage;
        myAnimation = animation;
        myWidth = width;
        myHeight = height;
        myLevelChanger = consumer;
        myLevelCount = numLevels;
        myAudioManager = audioManager;
    }

    /**
     * Goes to next level or resets level if necessary
     */
    @Override
    public void run() {
        for(Entity entity:this.getEntities()){
            if(entity.hasComponents(ProgressionComponent.class)){
                progressIfNecessary(entity);
                break;
            }
        }
    }

    private void progressIfNecessary(Entity entity){
            myAnimation.stop();
            myGroup.getChildren().add(new GameBeatenScreen(myStage, myGroup.getTranslateX(), (Boolean) entity.getComponent(ProgressionComponent.class).getValue(), myAudioManager).getNode());
    }

}
