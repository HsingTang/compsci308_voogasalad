package engine.external.actions;

import engine.external.Entity;
import engine.external.component.SpawnEntityComponent;

/**
 * @author Feroze
 *
 * This class was added to differentiate between entities that were to be added after a delay with entities that were
 * wished to be added immediately. In retrospect, it would have been more efficient to have a distinguishing method
 * in 'AddEntityAction', but because of the serialization done in adding games to the database, modifying that class
 * would have required everyone else to refactor their games, or they would no longer run. Therefore, I added this
 * class so that nobody's games would break.
 */
public class AddEntityInstantAction extends AddComponentAction {
    public AddEntityInstantAction(Entity entity){
        SpawnEntityComponent entityComponent = new SpawnEntityComponent(entity);
        setAbsoluteAction(entityComponent);
        myComponentClass = SpawnEntityComponent.class;
    }

}
