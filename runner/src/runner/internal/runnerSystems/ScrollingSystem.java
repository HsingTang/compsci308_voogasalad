package runner.internal.runnerSystems;

import engine.external.Entity;
import engine.external.component.*;
import javafx.scene.Group;
import javafx.scene.Scene;
import runner.internal.LevelRunner;
import java.util.Collection;

/**
 * System that scrolls around the main entity
 * Moves stuff in game if main entity is in left or right edge of the screen
 * @author Louis Jensen
 */
public class ScrollingSystem extends RunnerSystem {
    private Group myGroup;
    private Scene myScene;
    private final double LEFT_BUFFER_ZONE = 1.0 / 5.0;
    private final double RIGHT_BUFFER_ZONE = 3.0 / 5.0;

    /**
     * Constructor for ScrollingSystem
     * @param requiredComponents - list of all components necessary for system
     * @param levelRunner - LevelRunner object so that system can modify the level
     * @param group - Group so system can modify items in group
     * @param scene - Scene so system can access size of scene
     */
    public ScrollingSystem (Collection<Class<? extends Component>> requiredComponents, LevelRunner levelRunner, Group group, Scene scene) {
        super(requiredComponents, levelRunner);
        myGroup = group;
        myScene = scene;
    }

    /**
     * Scrolls screen if entity in buffer zones
     */
    @Override
    public void run(){
        for(Entity entity:this.getEntities()){
            if(entity.hasComponents(CameraComponent.class)&&(Boolean) getComponentValue(CameraComponent.class,entity)) {
                scrollOnMainCharacter(entity);
                break;
            }
        }
    }

    private void scrollOnMainCharacter(Entity entity){
        Double x = (Double) entity.getComponent(XPositionComponent.class).getValue();
        Double origin = myGroup.getTranslateX();
        Double xMinBoundary = myScene.getWidth() * LEFT_BUFFER_ZONE;
        Double xMaxBoundary = myScene.getWidth() * RIGHT_BUFFER_ZONE;
        if (x < xMinBoundary - origin) {
            myGroup.setTranslateX(-1 * x + xMinBoundary);
        }
        if (x > xMaxBoundary - origin) {
            myGroup.setTranslateX(-1 * x + xMaxBoundary);
        }
    }
}
