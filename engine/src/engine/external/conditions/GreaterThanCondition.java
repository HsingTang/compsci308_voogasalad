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
    private static final String DISPLAY = " Greater Than ";
    private static final String COMPONENT = "Component";
    public GreaterThanCondition(Class<? extends Component> component, Double value) {
        setPredicate((Predicate<Entity> & Serializable) entity -> (Double) entity.getComponent(component).getValue() > value);
        myComponentName = component.getSimpleName();
        myComponentClass = component;
        myValue = value;
    }

    @Override
    public String toString(){
        return myComponentName.replaceAll(COMPONENT,"") + DISPLAY + myValue;
    }
}
