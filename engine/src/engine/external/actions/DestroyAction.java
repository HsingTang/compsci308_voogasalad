package engine.external.actions;

import engine.external.component.DestroyComponent;

/**
 * @author engine
 * Appends a destroy component to the entity so that it gets destroyed by the clean up system
 */
public class DestroyAction extends AddComponentAction {
    public DestroyAction(Boolean destroy) {
        setAbsoluteAction(new DestroyComponent(destroy));
        myComponentClass = DestroyComponent.class;
    }
}
