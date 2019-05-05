package engine.external.component;

import engine.external.Entity;

/**
 * SpawnEntityComponent serves as a container for storing an entity to add to the game. Simply has a getter and setter.
 * @author Dima Fayyad
 */
public class SpawnEntityComponent extends Component<Entity> {

    /**
     * Default constructor has no parameters. Creates a default Entity to add to the game
     */
    public SpawnEntityComponent(){
        super(new Entity());
    }

    /**
     * Constructor takes the entity that will be added to the game (spawned)
     * @param entity the entity to be added to the game
     */
    public SpawnEntityComponent(Entity entity) {
        super(entity);
    }

}
