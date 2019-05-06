package engine.external.actions;

import engine.external.component.WidthComponent;

/**
 * @author engine
 * Changes the width of an entity
 */
public class WidthAction extends NumericAction {
    public WidthAction(ModifyType type, Double width) {
        setAction(type, width, WidthComponent.class);
    }
}
