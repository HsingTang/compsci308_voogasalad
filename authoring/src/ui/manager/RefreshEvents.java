package ui.manager;

import engine.external.events.Event;
/**
 * This functional interface was meant to help update the display of an Event after it had been created, as a user
 * crafts in an event in a separate pane for ease of display
 */
@FunctionalInterface
public interface RefreshEvents {
    void refreshEventDisplay(Event addedEvent);
}
