package engine.external.actions;

import engine.external.component.XVelocityComponent;

/**
 * @author engine
 * adjusts the xvelocity of an entity
 */
public class XVelocityAction extends NumericAction {
    public XVelocityAction(ModifyType type, Double xVelocity) {
        setAction(type, xVelocity, XVelocityComponent.class);
    }
}
