package engine.external.actions;

import engine.external.component.YVelocityComponent;

/**
 * @author engine
 * Adjusts the YVelocity of an entity
 */
public class YVelocityAction extends NumericAction {
    public YVelocityAction(ModifyType type, Double yVelocity) {
        setAction(type, yVelocity, YVelocityComponent.class);
    }
    @Override
    public String toString(){
        return super.toString();
    }
}
