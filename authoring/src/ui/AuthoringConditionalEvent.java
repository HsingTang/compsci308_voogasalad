package ui;
import engine.external.component.NameComponent;
import engine.external.conditions.Condition;
import engine.external.conditions.StringEqualToCondition;
import engine.external.events.Event;
import events.EventBuilder;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.ObservableList;
import javafx.scene.control.Button;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import ui.manager.RefreshEvents;

/**
 * This is meant to model a general event that simply prompts the user for a single condition and a single action.
 * When saving, it has to have slightly more robust error checking than other AuthoringEvent subclasses do, in the
 * case that the user inputs a combination of values we don't accept for conditions.
 * @see AuthoringEvent
 * @author Anna Darwish
 */
public class AuthoringConditionalEvent extends AuthoringEvent {
    private static final String USER_PROMPT = "Condition:";
    private static final String CONDITION_RESOURCE = "conditions";

    private static final String SAVE = "Save";
    private static final String STYLE = "default.css";
    private static final String STYLE_SIZING = "event-editor";
    private String myEntityName;
    private StringProperty componentName = new SimpleStringProperty(); //Name of the component for the conditional
    private StringProperty conditionOperator = new SimpleStringProperty(); //type of condition, such as a LessThanCondition
    private StringProperty triggerValue = new SimpleStringProperty();  //value bound to trigger the actions associated with this event

    private RefreshEvents myRefresher;
    private ObservableList<Event> myEntityEvents;
    public AuthoringConditionalEvent(String entityName){
        myEntityName = entityName;
    }
    /**
     * This VBox will display a broad range of options for conditions based upon the components that an entity may have
     * such as its x-position or its current image
     */
    @Override
    public VBox generateEventOptions(){
        VBox eventOptions = new VBox();
        eventOptions.getChildren().add(generateConditionOptions());
        eventOptions.getChildren().add(super.createActionOptions());
        eventOptions.getChildren().add(createToolBar());
        eventOptions.getStylesheets().add(STYLE);
        eventOptions.getStyleClass().add(STYLE_SIZING);
        return eventOptions;
    }


    @Override
    public void addSaveComponents(RefreshEvents refresher, ObservableList<Event> entityEvents) {
        myRefresher = refresher;
        myEntityEvents = entityEvents;
    }

    private HBox generateConditionOptions() {
        return super.createEventComponentOptions(USER_PROMPT,CONDITION_RESOURCE,componentName,conditionOperator,triggerValue);
    }

    private HBox createToolBar(){
        Button saveButton = new Button(SAVE);
        saveButton.setOnMouseClicked(mouseEvent -> saveConditionalEvent());
        return super.createToolBar(saveButton);
    }

    private void saveConditionalEvent(){
        Event conditionalEvent = new Event();
        conditionalEvent.addConditions(new StringEqualToCondition(NameComponent.class,myEntityName));
        EventBuilder myBuilder = new EventBuilder();
        try {
            Condition generatedCondition = myBuilder.createGeneralCondition(componentName.getValue(), conditionOperator.getValue(), triggerValue.getValue());
            conditionalEvent.addConditions(generatedCondition);
            super.saveAction(conditionalEvent);
            super.saveEvent(conditionalEvent, myRefresher, myEntityEvents);
        }
        catch(UIException exception){
            exception.displayUIException();
        }
        super.closeWindow();
    }
}
