package ui;

import engine.external.component.NameComponent;
import engine.external.conditions.StringEqualToCondition;
import engine.external.events.Event;
import events.EventType;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.ObservableList;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import ui.manager.LabelManager;
import ui.manager.RefreshEvents;
import voogasalad.util.reflection.Reflection;

import java.util.HashMap;
import java.util.Map;
/**
 * This is sub-class of @AuthoringEvent acts to represent collision events, as they require prompting the user for an
 * interactee entity or group. This is extensible to other interactive events that may be generated in the future as
 * Collision Events handle setting up the collision conditions specific to them. This class also uses the bound control
 * options from @EventFactory to either display a listing of the user's current entities and current groups
 * @see AuthoringEvent
 * @see events.EventFactory
 * @author Anna Darwish
 */
public class AuthoringInteractiveEvent extends AuthoringEvent {
    private static final String SAVE = "Save";

    private static final String GROUP = "Group";
    private static final String ENTITY = "Entity";


    private static final String STYLE = "default.css";
    private static final String STYLE_SIZING = "event-editor";
    private static final String STYLE_OPTIONS = "event-options";
    private static final String COLLISION_PROMPT = "Collision With...";
    private Map<String,ObservableList<String>> myInteractees = new HashMap<>();

    private StringProperty interactionType = new SimpleStringProperty(); //Whether this will be an interaction with a group or entity
    private StringProperty interacteeName =  new SimpleStringProperty(); //Name of group or entity this interactive event would occur with

    private String myEntityName;
    private String myEventName;

    private RefreshEvents myRefresher;
    private ObservableList<Event> myEntityEvents;

    public AuthoringInteractiveEvent(LabelManager myLabelManager, String eventName, String entityName){
        ObservableList<String> myEntityNames = myLabelManager.getLabels(EntityField.LABEL);
        ObservableList<String> myGroupNames = myLabelManager.getLabels(EntityField.GROUP);
        myInteractees.put(GROUP,myGroupNames);
        myInteractees.put(ENTITY,myEntityNames);
        myEventName = eventName;
        myEntityName = entityName;
    }

    @Override
    public VBox generateEventOptions(){
        VBox eventOptions = new VBox();
        eventOptions.getStylesheets().add(STYLE);
        eventOptions.getStyleClass().add(STYLE_SIZING);

        HBox myInteractionOptions = new HBox();
        super.setUpPairedChoiceBoxes(myInteractees,interactionType,interacteeName,myInteractionOptions);

        eventOptions.getChildren().add(createInteractionOptions(myInteractionOptions));
        eventOptions.getChildren().add(super.createActionOptions());
        eventOptions.getChildren().add(createToolBar());
        myInteractionOptions.getStyleClass().add(STYLE_OPTIONS);

        return eventOptions;
    }

    private HBox createInteractionOptions(HBox myInteractionOptions) {
        HBox optionsWithLabel = new HBox();
        optionsWithLabel.getStyleClass().add(STYLE_OPTIONS);
        optionsWithLabel.getChildren().add(new Label(COLLISION_PROMPT));
        optionsWithLabel.getChildren().add(myInteractionOptions);
        return optionsWithLabel;
    }

    @Override
    public void addSaveComponents(RefreshEvents refresher, ObservableList<Event> entityEvents) {
        myRefresher = refresher;
        myEntityEvents = entityEvents;
    }


    private HBox createToolBar(){
        Button saveButton = new Button(SAVE);
        saveButton.setOnMouseClicked(mouseEvent -> saveConditionalEvent());
        return super.createToolBar(saveButton);
    }

    private void saveConditionalEvent(){
        boolean isGrouped = interactionType.getValue().equals(GROUP);
        Event interactiveEvent = (Event)Reflection.createInstance(EventType.valueOf(myEventName).getClassName(),interacteeName.getValue(),isGrouped);
        interactiveEvent.addConditions(new StringEqualToCondition(NameComponent.class,myEntityName));
        super.saveAction(interactiveEvent);
        super.saveEvent(interactiveEvent,myRefresher,myEntityEvents);
        super.closeWindow();
    }

}
