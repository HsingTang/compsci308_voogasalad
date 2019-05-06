package engine.external.actions;

import engine.external.component.TimerComponent;

/**
 * @author engine
 * Modifies the timer of an entity
 */
public class TimerAction extends NumericAction {
    public TimerAction(ModifyType type, Double time) {
        setAction(type, time, TimerComponent.class);
    }
}
