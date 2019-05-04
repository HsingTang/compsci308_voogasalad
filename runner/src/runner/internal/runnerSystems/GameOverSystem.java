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

/**
 * System that checks if the game is over and updates accordingly
 * @author Louis Jensen
 */
public class GameOverSystem extends RunnerSystem {
    private Group myGroup;
    private Stage myStage;
    private Animation myAnimation;
    private AudioManager myAudioManager;

    /**
     * Constructor for the progression system
     * @param requiredComponents - list of all components necessary for system
     * @param levelRunner - LevelRunner object so that system can modify the level
     * @param group - Group so that system can modify things on screen
     * @param stage - Stage of level to be modified
     * @param animation - Timeline that runs game loop
     */
    public GameOverSystem(Collection<Class<? extends Component>> requiredComponents, LevelRunner levelRunner,
                          Group group, Stage stage, Animation animation, AudioManager audioManager) {
        super(requiredComponents, levelRunner);
        myGroup = group;
        myStage = stage;
        myAnimation = animation;
        myAudioManager = audioManager;
    }

    /**
     * Ends the game and displays game won screen or game
     * lost screen depending on the boolean value in the
     * ProgressionComponent
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
