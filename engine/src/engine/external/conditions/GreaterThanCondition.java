package engine.external.conditions;

import engine.external.Entity;
import engine.external.component.Component;

import java.io.Serializable;
import java.util.function.Predicate;

/**
 * @author Lucas Liu
 * @author Anna Darwish
 * Condition for Event that checks for Component value > target value
 */
public class GreaterThanCondition extends Condition {
    private String myComponentName;
    private Double myValue;

    public GreaterThanCondition(Class<? extends Component> component, Double value) {
        setPredicate((Predicate<Entity> & Serializable) entity -> (Double) entity.getComponent(component).getValue() > value);
        myComponentName = component.getSimpleName();
        myValue = value;
    }

    @Override
    public String toString() {
        return myComponentName + " Greater Than " + myValue;
    }
}
