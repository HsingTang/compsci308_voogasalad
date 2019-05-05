package engine.external.actions;

import engine.external.Entity;
import engine.external.component.AssociatedEntityComponent;
import engine.external.component.Component;

import java.io.Serializable;
import java.util.function.Consumer;

/**
 * @author Feroze
 * The purpose of this action is to modify the component of an entity associated with the entity enacting this action
 * . In order to use this action, the acting entity must have an AssociatedEntity Component with the directed entity
 * as its value. The methods of this action are very similar to methods found in the generalized Action, except they
 * act indirectly.
 */
public class AssociatedEntityAction extends NumericAction {
    private final Class ASSOCIATED_ENTITY = AssociatedEntityComponent.class;
    /**
     * This method scales the current value of a component of an associated entity by a scaleFactor
     *
     * @param scaleFactor
     * @param componentClass
     */
    @SuppressWarnings("unchecked")
    @Override
    protected void setScaledAction(Number scaleFactor, Class<? extends Component<Double>> componentClass) {
        myComponentClass = componentClass;

        super.setAction((Consumer<Entity> & Serializable) entity -> {
            double oldValue =
                    ((Number) ((Entity) entity.getComponent(ASSOCIATED_ENTITY)
                            .getValue())
                            .getComponent(componentClass)
                            .getValue())
                            .doubleValue();
            Component component = ((Entity) entity.getComponent(ASSOCIATED_ENTITY)
                    .getValue()).getComponent(componentClass);
            component.setValue(oldValue * scaleFactor.doubleValue());
        });
    }

    /**
     * This method adds a displacementFactor to the current value of a component of an associated entity
     *
     * @param displacementFactor
     * @param componentClass
     */
    protected void setRelativeAction(Number displacementFactor, Class<? extends Component<Double>> componentClass) {
        myComponentClass = componentClass;

        setAction((Consumer<Entity> & Serializable) entity -> {
            Component component = ((Entity) entity.getComponent(ASSOCIATED_ENTITY)
                    .getValue())
                    .getComponent(componentClass);
            component.setValue((double) component.getValue() + displacementFactor.doubleValue());
        });
    }

    /**
     * This methood sets the string of a component of the Associated entity
     * @param newValue
     * @param componentClass
     */
    public void setStringAction(String newValue, Class<? extends Component<String>> componentClass) {
        super.setAction((Consumer<Entity> & Serializable) entity -> {
            Component component = ((Entity) entity.getComponent(ASSOCIATED_ENTITY)
                    .getValue())
                    .getComponent(componentClass);
            component.setValue(newValue);
        });
    }
}

