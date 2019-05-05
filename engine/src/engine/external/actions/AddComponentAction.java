package engine.external.actions;

import engine.external.Entity;
import engine.external.component.Component;
import engine.external.component.TimerComponent;

import java.io.Serializable;
import java.util.function.Consumer;

/**
 * The abstract AddComponentAction class gives the functionality of adding a component to an entity as part of an action
 * that executes during game play.
 * The option to create this action in addition to a timer is offered so that the common often-desired functionality of periodically adding a component is available.
 *
 * @param <T> the Action Type
 * @author Dima Fayyad
 * @author Anna Darwish
 */
public abstract class AddComponentAction<T> extends Action<T>{
    private static final double timerValue = 10.0;

    /**
     * The setAbsoluteAction method adds the component to the entity upon which it is executing.
     * @param component the component to add to the entity
     */
    protected void setAbsoluteAction(Component component) {
        setAction((Consumer<Entity> & Serializable) entity -> {
            entity.addComponent(component);
        });
    }

    /**
     * The setActionWithTimer is a method that allows for adding a component to an entity to be combined with a Timer.
     * The need for this method is from a common functionality across games: periodically adding a component. This is most often seen in periodically re-spawning an entity.
     * The method first checks that the entity has a timer component, adds if not present, then  adds the Component to the entity.
     *
     * @param component the component to add to the entity
     */
    protected void setActionWithTimer(Component component){
        setAction((Consumer<Entity> & Serializable) entity -> {
            if(!entity.hasComponents(TimerComponent.class)){
                entity.addComponent(new TimerComponent(timerValue));
                entity.addComponent(component);
            } else if(entity.getComponent(TimerComponent.class).getValue().equals(0.0)){
                entity.getComponent(TimerComponent.class).resetToOriginal();
                entity.addComponent(component);
            }
        });
    }
}
