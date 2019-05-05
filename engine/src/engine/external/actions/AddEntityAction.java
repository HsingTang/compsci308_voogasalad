package engine.external.actions;

import engine.external.Entity;
import engine.external.component.SpawnEntityComponent;

/**
 * The AddEntityAction creates a new SpawnEntityComponent containing an entity and adds the component to the entity upon which the action is being executed
 * To add an entity to the game during game play, this is done via the SpawnEntityComponent. Once the entity is in this component, the AddEntitySystem in engine
 * will handle adding the entity to the game.
 *
 * @author Dima Fayyad
 * @author Lucas Liu
 */
public class AddEntityAction extends AddComponentAction {

    /**
     * The class constructors creates a new SpawnEntityComponent containing the entity to be added to the game. The component is added to the entity on which the action executes.
     * @param entity the entity to be added to the game
     */
    public AddEntityAction(Entity entity){
        SpawnEntityComponent entityComponent = new SpawnEntityComponent(entity);
        setActionWithTimer(entityComponent);
        myComponentClass = SpawnEntityComponent.class;
    }
}
