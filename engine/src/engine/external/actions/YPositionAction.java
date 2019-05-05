package engine.external.actions;

import engine.external.component.YPositionComponent;

/**
 * @author engine
 * Adjusts the YPosition of an entity
 */
public class YPositionAction extends NumericAction {
    public YPositionAction(ModifyType type, Double yPosition) {
        setAction(type, yPosition, YPositionComponent.class);
    }
}
