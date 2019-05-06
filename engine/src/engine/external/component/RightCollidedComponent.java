package engine.external.component;

import engine.external.Entity;

import java.util.Collection;

/**
 * @author Hsingchih Tang
 * Record the Entities currently colliding with the owner Entity from the right
 */
public class RightCollidedComponent extends Component<Collection<Entity>> {
    public RightCollidedComponent(Collection<Entity> collideEntities) {
        super(collideEntities);
    }

}
