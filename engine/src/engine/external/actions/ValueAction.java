package engine.external.actions;

import engine.external.component.ValueComponent;

/**
 * @author engine
 * Modifies the value stored in the value component of an entity
 */
public class ValueAction extends NumericAction {
    public ValueAction(ModifyType type, Double newValue) {
        setAction(type, newValue, ValueComponent.class);
    }

}
