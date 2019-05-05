package engine.external.actions;

import engine.external.component.DirectionComponent;

/**
 * @author engine
 *
 * Modifies the direction component of an entity
 */
public class DirectionAction extends NumericAction {
    public DirectionAction(ModifyType type, Double direction) {
        setAction(type, direction, DirectionComponent.class);
    }
}