package engine.external.component;

import engine.external.Entity;

import java.util.Collection;

/**
 * @author Hsingchih Tang
 * Record the Entities currently colliding with the owner Entity from any direction
 */
public class AnyCollidedComponent extends Component<Collection<Entity>> {

    public AnyCollidedComponent(Collection<Entity> collideEntities) {
        super(collideEntities);
    }

}
