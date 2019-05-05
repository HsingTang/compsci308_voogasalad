package ui;

import engine.external.actions.Action;
import engine.external.actions.ProgressionAction;
import engine.external.events.Event;
import events.EventBuilder;
import events.EventFactory;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.ObservableList;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ToggleButton;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import ui.manager.RefreshEvents;
import java.util.*;
/**
 * This superclass helps to handle a lot of the similarities of display and saving among the differing authoring events.
 * Because they all significantly differ in the information they may require and the way they are saved, a hierarchy
 * structure seemed to fit best for handling the different forms an event a user may wish to create. It is important to note
 * that this hierarchy does not just duplicate the hierarchy of our engine's event hierarchy, nor does it require creating a
 * new subclass for every type of event created. Instead, this allows the event prompts to receive exactly the information they
 * need but still help in removing duplicate code by handling aspects common to all events, such as an action.
 *
 * @see AuthoringEvent
 * @author Anna Darwish
 */
public abstract class AuthoringEvent extends Stage {
    private static final String USER_ACTION_PROMPT = "Action:";
    private static final String ACTION_RESOURCE = "actions_display";
    private static final String CANCEL = "Cancel";
    private static final String CHECKPOINT = "Click to Insert CheckPoint";
    private static final String DEFAULT_STYLESHEET = "default.css";
    private static final String BUTTONS_DISPLAY = "buttons-bar";
    private static final String BUILDER_STYLE = "event-builder";

    private StringProperty componentName = new SimpleStringProperty(); //Name of the component for the conditional
    private StringProperty modifierOperator = new SimpleStringProperty(); //type of modifier such as set or scale
    private StringProperty triggerValue = new SimpleStringProperty();  //value associated with action
    private BooleanProperty checkPoint = new SimpleBooleanProperty();

    /**
     * The subclasses differ primarily in regards to what conditions options they offer to users, so this vbox is meant
     * to allow the user to specific what they wish to prompt to the user for the conditions
     */
    abstract VBox generateEventOptions();
    /**
     * These save components are necessary for any event to add in the event generated when the user saves the event they
     * created from the prompt box, and then to refresh the event display pane to show the changes the user just saved. Since
     * saving happens differently because of the different conditions involved in the subclasses, they handle using these
     * differently
     */
    public abstract void addSaveComponents(RefreshEvents myRefresher, ObservableList<Event> myEntityEvents);

    void saveEvent(Event createdEvent, RefreshEvents myRefresher, ObservableList<Event> myEntityEvents){
        myEntityEvents.add(createdEvent);
        myRefresher.refreshEventDisplay(createdEvent);
        this.close();
    }
    /**
     * This method is invoked when a user chooses add a new event and displays the pop-up associated with the event they
     * selected. It invokes the subclass's generateEventOptions method, which handles displaying the conditions for that
     * particular event
     */
    public void render(){
        VBox eventOptions = generateEventOptions();
        Scene myScene = new Scene(eventOptions);
        myScene.getStylesheets().add(DEFAULT_STYLESHEET);
        eventOptions.getStyleClass().add(BUILDER_STYLE);
        this.setScene(myScene);
        this.sizeToScene();
        this.show();
    }

    VBox createActionOptions(){
        VBox actionOptions = new VBox();
        HBox actions = createEventComponentOptions(USER_ACTION_PROMPT,ACTION_RESOURCE,componentName,modifierOperator,triggerValue);
        ToggleButton myButton = new ToggleButton(CHECKPOINT);
        myButton.setOnMouseClicked(mouseEvent -> {
            if (myButton.isSelected())
                myButton.setTextFill(Color.AQUAMARINE);
        });
        checkPoint.bindBidirectional(myButton.selectedProperty());
        actionOptions.getChildren().add(actions);
        actionOptions.getChildren().add(myButton);
        return actionOptions;
    }

    void saveAction(Event buildingEvent){
        EventBuilder myBuilder = new EventBuilder();
        try {
            Action createdAction = myBuilder.createGeneralAction(componentName.getValue(), modifierOperator.getValue(), triggerValue.getValue());
            buildingEvent.addActions(createdAction);
            if (checkPoint.getValue())
                buildingEvent.addActions(new ProgressionAction(true));
        }
        catch (UIException e){
            e.displayUIException();
        }
    }

    HBox createEventComponentOptions(String prompt, String resourceName, StringProperty componentName,
                                               StringProperty modifierOperator, StringProperty triggerValue){
        return EventFactory.createEventComponentOptions(prompt,resourceName,componentName,modifierOperator,triggerValue);

    }

    void setUpPairedChoiceBoxes(Map<String,ObservableList<String>> actionOperatorOptions,
                                                     StringProperty controller, StringProperty dependent,HBox parent){
        EventFactory.setUpPairedChoiceBoxes(actionOperatorOptions,controller,dependent,parent);
    }

    HBox createToolBar(Button saveButton){
        HBox toolBar = new HBox();
        toolBar.getStyleClass().add(BUTTONS_DISPLAY);
        Button cancelButton = new Button(CANCEL);
        cancelButton.setOnMouseClicked(mouseEvent -> closeWindow());
        toolBar.getChildren().add(saveButton);
        toolBar.getChildren().add(cancelButton);
        return toolBar;
    }

    void closeWindow(){
        this.close();
    }

}
