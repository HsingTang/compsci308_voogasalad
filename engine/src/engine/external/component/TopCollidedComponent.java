package engine.external.component;

import engine.external.Entity;

import java.util.Collection;

/**
 * @author Hsingchih Tang
 * Record the Entities currently colliding with the owner Entity from the top
 */
public class TopCollidedComponent extends Component<Collection<Entity>> {
    public TopCollidedComponent(Collection<Entity> collideEntities) {
        super(collideEntities);
    }

}
