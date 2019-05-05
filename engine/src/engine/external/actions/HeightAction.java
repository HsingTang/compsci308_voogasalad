package engine.external.actions;

import engine.external.component.HeightComponent;

/**
 * @author Feroze
 * Modifies the height component of an entity
 */
public class HeightAction extends NumericAction {
    public HeightAction(ModifyType type, Double height) {
        setAction(type, height, HeightComponent.class);
    }
}

