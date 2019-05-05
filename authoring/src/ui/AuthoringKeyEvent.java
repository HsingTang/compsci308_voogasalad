package ui;

import engine.external.component.NameComponent;
import engine.external.conditions.StringEqualToCondition;
import engine.external.events.Event;
import events.EventFactory;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.ObservableList;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import ui.manager.RefreshEvents;

import java.util.ResourceBundle;
/**
 * This is sub-class of @AuthoringEvent acts to represent key events, as they require prompting the user for a
 * key input that they want to associate with an action. This was necessary to add as an option for user-appeal, as
 * it was quite annoying to create an event with a dummy condition so a key input could be added later on. This class
 * utilizes the EventFactory to create a listing of key codes for the user
 * @see AuthoringEvent
 * @see events.EventFactory
 * @author Anna Darwish
 */
public class AuthoringKeyEvent extends AuthoringEvent {
    private static final String USER_PROMPT = "Key:";
    private ResourceBundle myKeyCodes = ResourceBundle.getBundle("keycode");
    private ResourceBundle generateKeyCodes = ResourceBundle.getBundle("concrete_keycode");

    private static final String SAVE = "Save";
    private static final String STYLE = "default.css";
    private static final String STYLE_SIZING = "event-editor";
    private String myEntityName;
    private StringProperty keyName = new SimpleStringProperty(); //Name of the component for the conditional
    

    private RefreshEvents myRefresher;
    private ObservableList<Event> myEntityEvents;
    public AuthoringKeyEvent(String entityName){
        myEntityName = entityName;
    }
    /**
     * This VBox will display a listing of optional keycodes that we offer to the user to associate with a particular
     * set of actions
     */
    @Override
    public VBox generateEventOptions(){
        VBox eventOptions = new VBox();
        eventOptions.getStylesheets().add(STYLE);
        eventOptions.getStyleClass().add(STYLE_SIZING);
        eventOptions.getChildren().add(generateKeyOptions());
        eventOptions.getChildren().add(super.createActionOptions());
        eventOptions.getChildren().add(createToolBar());
        return eventOptions;
    }


    @Override
    public void addSaveComponents(RefreshEvents refresher, ObservableList<Event> entityEvents) {
        myRefresher = refresher;
        myEntityEvents = entityEvents;
    }

    private HBox generateKeyOptions() {
        Label userPrompt = EventFactory.createLabel(USER_PROMPT);
        ChoiceBox<String> keyCodeOptions = new ChoiceBox<>();
        EventFactory.setUpKeyCode(myKeyCodes,keyCodeOptions);
        keyName.bindBidirectional(keyCodeOptions.accessibleTextProperty());
        HBox keyOptions = new HBox();
        keyOptions.getChildren().add(userPrompt);
        keyOptions.getChildren().add(keyCodeOptions);
        return keyOptions;
    }

    private HBox createToolBar(){
        Button saveButton = new Button(SAVE);
        saveButton.setOnMouseClicked(mouseEvent -> saveKeyEvent());
        return super.createToolBar(saveButton);
    }

    private void saveKeyEvent(){
        Event conditionalEvent = new Event();
        conditionalEvent.addConditions(new StringEqualToCondition(NameComponent.class,myEntityName));
        conditionalEvent.addInputs(KeyCode.valueOf(generateKeyCodes.getString(keyName.getValue())));
        super.saveAction(conditionalEvent);
        super.saveEvent(conditionalEvent, myRefresher, myEntityEvents);
        super.closeWindow();
    }
}
