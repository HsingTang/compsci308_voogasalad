package ui.manager;

import engine.external.events.Event;
import javafx.scene.input.KeyCode;
/**
 * This functional interface was meant to help update the display of an Event after a keycode had been added, modified
 * or removed from it, without creating a circular dependency between different prompts presented to the user
 */
@FunctionalInterface
public interface AddKeyCode {
    void refresh(Event eventToModify, KeyCode keycode);
}
