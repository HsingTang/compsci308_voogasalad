package engine.external.actions;

import engine.external.component.XAccelerationComponent;

/**
 * @author engine
 * changes the x-acceleration of an entity
 */
public class XAccelerationAction extends NumericAction {
    public XAccelerationAction(ModifyType type, Double xAcceleration) {
        setAction(type, xAcceleration, XAccelerationComponent.class);
    }
}
