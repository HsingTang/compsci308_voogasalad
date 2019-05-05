package ui.panes;
import engine.external.events.Event;
import javafx.collections.ObservableList;
import javafx.scene.control.ScrollPane;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import ui.manager.*;

import java.util.HashMap;
import java.util.Map;
/**
 * This is the main scroll pane the user views when they select to modify events. It maintains a mapping between a
 * set of events and the display to ensure the two are visually updated for the user when the events are updated
 * internally
 * @author Anna Darwish
 */
public class CurrentEventsPane extends ScrollPane {
    private static final String CSS_CLASS = "current-events-pane";
    private ObservableList<Event> myCurrentEvents;
    private RefreshEvents myCurrentEventsRefresher = this::refresh;
    private RefreshEvents myEventModifier = this::refreshCreatedEvents;
    private VBox myEventsListing;

    private Editor myEditor;
    private Editor myRemover;
    private AddKeyCode myKeyCodeEditor;
    private Map<Event, CurrentEventDisplay> myMap = new HashMap<>();
    public CurrentEventsPane(ObservableList<Event> myEvents){
        myCurrentEvents = myEvents;

        myEventsListing = new VBox();
        this.setHbarPolicy(ScrollBarPolicy.NEVER);
        this.setVbarPolicy(ScrollBarPolicy.AS_NEEDED);

        myEventsListing.getStyleClass().add(CSS_CLASS);
        myEditor = this::editCurrentEvent;
        myRemover = this::removeCurrentEvent;
        myKeyCodeEditor = this::addKeyCode;

        for (Event event: myEvents) {
            CurrentEventDisplay currEventDisplay = new CurrentEventDisplay(event.getEventInformation(),event,myRemover,
                    myEditor, myKeyCodeEditor);
            if (currEventDisplay.getChildren().size() != 0) {
                myEventsListing.getChildren().add(currEventDisplay);
                myMap.put(event,currEventDisplay);
            }
        }
        this.setContent(myEventsListing);
        this.getStyleClass().add(CSS_CLASS);
        this.setFitToWidth(true);

    }
    /**
     * This method adds another CurrentEventDisplay when an Event is created, added to the scrollpane to be displayed
     */
    public RefreshEvents getRefreshEvents(){
        return myCurrentEventsRefresher;
    }

    private void refresh(Event addedEvent){
        CurrentEventDisplay currEventDisplay = new CurrentEventDisplay(addedEvent.getEventInformation(),addedEvent,myRemover,
                myEditor, myKeyCodeEditor);
        if (currEventDisplay.getChildren().size() != 0) {
            myMap.put(addedEvent,currEventDisplay);
            myEventsListing.getChildren().add(currEventDisplay);
        }
    }

    private void editCurrentEvent(Event unfinishedEvent){
        EventEditorPane myPane = new EventEditorPane(unfinishedEvent,myEventModifier);
        myPane.initModality(Modality.WINDOW_MODAL);
        myPane.show();

    }

    private void refreshCreatedEvents(Event modifiedEvent){
        int eventDisplayIndex = myEventsListing.getChildren().indexOf(myMap.get(modifiedEvent));
        if (eventDisplayIndex == -1)
            return;
        myEventsListing.getChildren().remove(myMap.get(modifiedEvent));
        CurrentEventDisplay modified = new CurrentEventDisplay(modifiedEvent.getEventInformation(),modifiedEvent,myRemover,
                myEditor, myKeyCodeEditor);
        myEventsListing.getChildren().add(eventDisplayIndex, modified);
        myMap.put(modifiedEvent,modified);
    }


    private void removeCurrentEvent(Event obsoleteEvent){
        myEventsListing.getChildren().remove(myMap.get(obsoleteEvent));
        myMap.remove(obsoleteEvent);
        myCurrentEvents.remove(obsoleteEvent);
    }

    private void addKeyCode(Event unfinishedEvent, KeyCode keyCode){
        unfinishedEvent.clearInputs();
        unfinishedEvent.addInputs(keyCode);
        //myCurrentEventsRefresher.refresh();
    }

}
