package engine.external.actions;


import engine.external.component.HealthComponent;

/**
 * @author Feroze
 * Modifies the health component of an entity (like a brick with variable health)
 */
public class HealthAction extends NumericAction {
    public HealthAction(ModifyType type, Double health) {
        setAction(type, health, HealthComponent.class);
    }

}
