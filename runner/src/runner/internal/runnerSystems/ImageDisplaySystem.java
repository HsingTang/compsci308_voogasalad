package runner.internal.runnerSystems;

import engine.external.Entity;
import engine.external.component.*;
import javafx.scene.Group;
import javafx.scene.image.ImageView;
import runner.internal.LevelRunner;
import java.util.Collection;

/**
 * System that shows the ImageViews
 * @author Louis Jensen
 */
public class ImageDisplaySystem extends RunnerSystem {
    private Group myGroup;

    /**
     * Constructor for ImageDisplaySystem
     * @param requiredComponents - all components an entity needs to have to be affected by the system
     * @param levelRunner - LevelRunner object so that system can modify the level
     * @param group - Group so that system can add and remove ImageViews
     */
    public ImageDisplaySystem (Collection<Class<? extends Component>> requiredComponents, LevelRunner levelRunner, Group group) {
        super(requiredComponents, levelRunner);
        myGroup = group;
    }

    /**
     * Displays images of desired entities
     * and positions them in the front or back based on their z components
     */
    @Override
    public void run() {
        for(Entity entity:this.getEntities()){
            if(entity.hasComponents(ImageViewComponent.class)){
                displayImage(entity);
            }
        }
    }

    private void displayImage(Entity entity) {
        ImageView image = (ImageView) entity.getComponent(ImageViewComponent.class).getValue();
        if (entity.hasComponents(ZPositionComponent.class) && entity.getComponent(ZPositionComponent.class).getValue().equals(1.0)) {
            image.toFront();
        } else {
            image.toBack();
        }
        myGroup.getChildren().add(image);
    }
}