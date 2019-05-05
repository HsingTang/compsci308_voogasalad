package engine.external.actions;

import engine.external.component.DestroyComponent;

public class DestroyAction extends AddComponentAction {
    public DestroyAction(Boolean destroy) {
        setAbsoluteAction(new DestroyComponent(destroy));
        myComponentClass = DestroyComponent.class;
    }
}
