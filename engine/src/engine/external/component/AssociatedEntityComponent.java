package engine.external.component;

import engine.external.Entity;

/**
 * @author Feroze
 *
 * The AssociatedEntity component holds an entity and it can be used on actions that work indirectly, not just on the
 * entity that is using the action.
 */
public class AssociatedEntityComponent extends Component<Entity> {
    public AssociatedEntityComponent(Entity e) {
        super(e);
    }
}
