package engine.internal.systems;

import engine.external.Engine;
import engine.external.Entity;
import engine.external.component.Component;
import engine.external.component.TimerComponent;

import java.util.Collection;

/**
 * The TimerSystem updates the timerComponent of each entity containing one on each call to update
 * While the timerComponent's value is 0, the system no longer updates it.
 */
public class TimerSystem extends VoogaSystem {
    private static final double timerStep = -1.0;
    private static final double timerZero = 0.0;

    /**
     * Accepts a reference to the Engine in charge of all Systems in current game, and a Collection of Component classes
     * that this System would require from an Entity in order to interact with its relevant Components
     *
     * @param requiredComponents collection of Component classes required for an Entity to be processed by this System
     * @param engine the main Engine which initializes all Systems for a game and makes update() calls on each game loop
     */
    public TimerSystem(Collection<Class<? extends Component>> requiredComponents, Engine engine) {
        super(requiredComponents, engine);
    }

    /**
     * Loops through all Entities with a TimerComponent, and updates the timerComponent as long as the value is not 0.
     */
    @Override
    protected void run() {
        for(Entity entity:this.getEntities()){
            if(entity.hasComponents(TIMER_COMPONENT_CLASS) &&  !entity.getComponent(TimerComponent.class).getValue().equals(timerZero)){
                Component timerComponent = entity.getComponent(TimerComponent.class);
                timerComponent.setValue((double) timerComponent.getValue() + timerStep);
            }
        }
    }
}
