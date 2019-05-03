package engine.external.actions;

import engine.external.Entity;
import engine.external.component.Component;
import engine.external.component.NameComponent;

import java.io.Serializable;
import java.util.function.Consumer;

/**
 * An Action is a wrapper class for a lambda function that takes in a value and a component, and
 * either sets the component value to that value, or scales/displaces the component's existing
 * value by that value. There are 3 types of engine.external.actions: NumericAction, BooleanAction, and StringAction,
 * each of which handle components of the different types.
 *
 * @param <T> Abstract type handled by an action, e.g. NumericAction<Double> can be used to alter the XPositionComponent
 * @author Feroze
 * @author Lucas
 * @author Dima
 */
public abstract class Action<T> {

    private Consumer<Entity> myAction;
    protected Class<? extends Component<T>> myComponentClass;


    public void checkComponents(Entity entity) {
        if (!entity.hasComponents(myComponentClass)) {
            //System.out.println("Action adding missing component: " + myComponentClass);
            try {
                entity.addComponent(myComponentClass.getConstructor().newInstance());
            } catch (Exception e) {
                //Do nothing
                //System.out.println("Could not instantiate new constructor");
            }
        }
    }

    /**
     * Sets the value of a component to a new value.
     *
     * @param newValue       new value for the component
     * @param componentClass class that specifies the component type
     */
    protected void setAbsoluteAction(T newValue, Class<? extends Component<T>> componentClass) {
        myComponentClass = componentClass;
        setAction((Consumer<Entity> & Serializable) entity -> {
            Component component = entity.getComponent(componentClass);
            component.setValue(newValue);
        });
    }

    /**
     * Sets the lambda of this action
     *
     * @param action a lambda
     */
    protected void setAction(Consumer<Entity> action) {
        myAction = action;
    }

    /**
     * Returns the lambda associated with this action
     *
     * @return lambda
     */
    public Consumer<Entity> getAction() {
        return myAction;
    }

    /**
     * Set the required class
     * @param clazz
     */
    protected void setMyComponentClass(Class<? extends Component<T>> clazz) {
        myComponentClass = clazz;
    }

    protected Class<? extends Component<T>> getMyComponentClass(){
        return myComponentClass;
    }

}
