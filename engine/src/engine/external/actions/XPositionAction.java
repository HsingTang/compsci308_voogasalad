package engine.external.actions;

import engine.external.component.XPositionComponent;

/**
 * @author engine
 * adjusts the x-position of an entity
 */
public class XPositionAction extends NumericAction {
    public XPositionAction(ModifyType type, Double xPosition) {
        setAction(type, xPosition, XPositionComponent.class);
    }
}
