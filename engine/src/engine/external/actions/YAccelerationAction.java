package engine.external.actions;

import engine.external.component.YAccelerationComponent;

/**
 * @author engine
 * Adjusts the yacceleration of an entity
 */
public class YAccelerationAction extends NumericAction {
    public YAccelerationAction(ModifyType type, Double yAcceleration) {
        setAction(type, yAcceleration, YAccelerationComponent.class);
    }
}
