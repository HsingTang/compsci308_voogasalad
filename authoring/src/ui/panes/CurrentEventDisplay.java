package ui.panes;
import engine.external.actions.Action;
import engine.external.conditions.Condition;
import engine.external.conditions.StringEqualToCondition;
import engine.external.events.Event;
import events.EventFactory;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import ui.UIException;
import ui.manager.AddKeyCode;

import java.util.*;
    /**
     * This class is one of the rows of events displayed when an entity's events pane is opened to be viewed or modified.
     * The constructor takes in a map queried from the event of its component information, which are its conditions and
     * actions. It also sets up an edit tool bar for the user to modify the event, remove it, or add/replace a keycode
     * associated with the event, which is why it takes in several functional interfaces in order to update event when
     * particular controls are invoked. It does depend on receiving this mapping specifically, so it is only used by
     * see @CurrentEventDisplay
     * @author Anna Darwish
     */
    class CurrentEventDisplay extends GridPane {
    private Event myEvent;
    private Editor myEventRemover;
    private Editor myEventModifier;
    private AddKeyCode myKeyCodeAdder;

    private ResourceBundle myKeyCodes = ResourceBundle.getBundle("keycode");
    private ResourceBundle myErrorMessage = ResourceBundle.getBundle("error_messages");
    private ResourceBundle myKeyCodesDisplay = ResourceBundle.getBundle("concrete_keycode");
    private ResourceBundle KEY_DISPLAY = ResourceBundle.getBundle("display_keycodes");

    private static final String EDIT = "Edit";
    private static final String REMOVE = "Remove";
    private static final String IGNORE_NAME_CONDITION = "NameComponent";
    private static final String CSS = "current-events-display";

    CurrentEventDisplay(Map<Class<?>, List<?>> myMap, Event myEvent, Editor eventRemover, Editor eventModifier, AddKeyCode addKeyCode){
        this.myEvent = myEvent;
        this.myEventRemover = eventRemover;
        this.myEventModifier = eventModifier;
        this.myKeyCodeAdder = addKeyCode;
        if (invalidEvent(myMap)){
            return;
        }
        this.getStyleClass().add(CSS);
        setUpEventInformation(myMap);
        setUpEditToolBar();

    }

    private void setUpEventInformation(Map<Class<?>, List<?>> myMap){
        Label myConditions = setUpLabel(myMap.get(Condition.class));
        Label myActions = setUpLabel(myMap.get(Action.class));
        this.add(myConditions,0,0);
        this.add(myActions,1,0);
    }
    private Label setUpLabel(List<?> myEventComponents){
        StringBuilder text = new StringBuilder();
        try {
            for (Object eventComponent : myEventComponents) {
                if (eventComponent instanceof StringEqualToCondition &&
                        ((StringEqualToCondition) eventComponent).getComponentClass().getSimpleName().equals(IGNORE_NAME_CONDITION)){
                    continue;
                }
                text.append(eventComponent.toString());
                text.append("\n");
            }
        }
        catch(ClassCastException e){
            UIException wrongCast = new UIException(myErrorMessage.getString(this.getClass().getSimpleName()));
            wrongCast.displayUIException();
        }
        Label label = new Label(text.toString());
        label.getStyleClass().add(CSS);
        return label;

    }
    private void setUpEditToolBar(){
        Button editButton = new Button(EDIT);
        Button removeButton = new Button(REMOVE);
        ChoiceBox<String> keyCode = new ChoiceBox<>();

        setUpKeyCodes(keyCode);
        removeButton.setOnMouseClicked(mouseEvent -> myEventRemover.editEvent(myEvent));
        editButton.setOnMouseClicked(mouseEvent -> myEventModifier.editEvent(myEvent));

        VBox buttons = new VBox();
        buttons.getChildren().add(editButton);
        buttons.getChildren().add(removeButton);
        buttons.getChildren().add(keyCode);
        this.add(buttons,2,0);
    }

    private boolean invalidEvent(Map<Class<?>, List<?>> myMap) {
        return !myMap.containsKey(Condition.class) || !myMap.containsKey(Action.class) ||
                myMap.get(Condition.class).size() == 0 || myMap.get(Action.class).size() == 0;
    }

    private void setUpKeyCodes(ChoiceBox<String> myKeyCodesListing){
        EventFactory.setUpKeyCode(myKeyCodes,myKeyCodesListing);
        if (myEvent.getImmutableKeyCodes().size()!= 0){
            myKeyCodesListing.setValue(KEY_DISPLAY.getString(myEvent.getImmutableKeyCodes().get(0).toString()));
        }
        myKeyCodesListing.getSelectionModel().selectedItemProperty().addListener((observableValue, s, newValue) -> {
            myKeyCodesListing.setAccessibleText(newValue);
            myKeyCodeAdder.refresh(myEvent,KeyCode.getKeyCode(myKeyCodesDisplay.getString(newValue)));
        });


    }
}
