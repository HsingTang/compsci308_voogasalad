package events;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import java.util.*;

/**
 * This is essentially a Utilities class that EventPane uses in order to display particular options and properties associated
 * with engine.external.events and engine.external.actions. This helps with reflection in creating engine.external.events, as different engine.external.events need to provide different
 * controls to the user to input information necessary to instantiate different engine.external.events and engine.external.actions
 * @author Anna Darwish
 */
public class EventFactory {
    private static final String ASSOCIATED_OPTIONS_DELIMITER = "::";
    private static final String KEYCODE_DELIMITER = ".";
    private static final String STYLE = "default.css";
    private static final String STYLE_EVENTS = "event-options";
    private static List<String> getIndependentOptionsListing(ResourceBundle associatedOptionsResource){
        List<String> independentOptions = new ArrayList<>(associatedOptionsResource.keySet());
        Collections.sort(independentOptions);
        return independentOptions;
    }
    /**
     * This method returns a map of associated options with a particular key value from a properties file. This is useful for
     * toggling drop down menu's whose values change depending on the value currently selected in the first drop-down menu.
     */
    private static Map<String, ObservableList<String>> createAssociatedOptions(String bundleName){
        Map<String, ObservableList<String>> associatedOptions = new HashMap<>();
        ResourceBundle associatedOptionsResource = ResourceBundle.getBundle(bundleName);

        for (String independentOption: getIndependentOptionsListing(associatedOptionsResource)){
            String[] dependentOptionsArray = associatedOptionsResource.getString(independentOption).split(ASSOCIATED_OPTIONS_DELIMITER);
            List<String> dependentOptionsList = Arrays.asList(dependentOptionsArray);
            associatedOptions.put(independentOption, FXCollections.observableArrayList(dependentOptionsList));
        }

        return associatedOptions;
    }
    /**
     * This method handles setting up a keycode choice-box from a resource file that has indexed keycodes. Since there are
     * multiple areas that a user may toggle an event's associated key codes, it was useful to keep this in the utilities class
     * as it is then easier to ensure any key code changes we would ever need to make
     */
    public static void setUpKeyCode(ResourceBundle myKeyCodes, ChoiceBox<String> myKeyCodesListing){
        Set<String> keyCodes = myKeyCodes.keySet();
        List<String> keyCodesList = new ArrayList<>(keyCodes);
        Collections.sort(keyCodesList);
        List<String> removedIndex = new ArrayList<>();
        for (String key: keyCodesList){
            removedIndex.add(key.substring(key.indexOf(KEYCODE_DELIMITER) + 1));
        }
        myKeyCodesListing.setItems(FXCollections.observableList(removedIndex));
        myKeyCodesListing.setOnAction(actionEvent -> myKeyCodesListing.setAccessibleText(myKeyCodesListing.getValue()));
    }
    /**
     * This method handles setting up event component display as this prompt appears in multiple settings. It also binds
     * the values of the controls to the StringProperty values that are passed to it
     * @see ValueFieldProperty
     * @return HBox that prompts the user to enter information about the component whose value they are setting a conditional on
     */
    public static HBox createEventComponentOptions(String prompt, String resourceName, StringProperty componentName,
                                               StringProperty modifierOperator, StringProperty triggerValue){
        HBox eventComponentOptions = new HBox();
        Label userPrompt = EventFactory.createLabel(prompt);
        eventComponentOptions.getChildren().add(userPrompt);
        Map<String,ObservableList<String>> eventOperatorOptions = createAssociatedOptions(resourceName);
        ChoiceBox<String> componentChoiceBox = setUpPairedChoiceBoxes(eventOperatorOptions,componentName,modifierOperator,eventComponentOptions);


        ValueFieldProperty myTriggerControl = new ValueFieldProperty();
        triggerValue.bindBidirectional(myTriggerControl.textProperty());
        createDependencyForValueField(componentChoiceBox,myTriggerControl);
        eventComponentOptions.getChildren().add(myTriggerControl);
        eventComponentOptions.getStyleClass().add(STYLE_EVENTS);
        return eventComponentOptions;

    }
    /**
     * This method binds the value of an independent choice box to a dependent one so that the latter's options change
     * depending on the independent's current value.
     * @param actionOperatorOptions maps a possible independent value to the associated options that should appear in the dependent
     *                              choice-box
     * @see ValueFieldProperty
     * @return HBox that prompts the user to enter information about the component whose value they are setting a conditional on
     */
    public static ChoiceBox<String> setUpPairedChoiceBoxes(Map<String,ObservableList<String>> actionOperatorOptions,
                                                       StringProperty controller, StringProperty dependent,HBox parent){
        List<String> componentOptions = new ArrayList<>(actionOperatorOptions.keySet());
        Collections.sort(componentOptions);
        ChoiceBox<String> componentChoiceBox = setUpBoundChoiceBox(componentOptions,controller,parent);
        ChoiceBox<String> comparatorChoiceBox = setUpBoundChoiceBox(new ArrayList<>(), dependent,parent);
        createDependencyBetweenChoiceBoxes(componentChoiceBox,comparatorChoiceBox,actionOperatorOptions);
        return componentChoiceBox;
    }

    private static void createDependencyBetweenChoiceBoxes(ChoiceBox<String> controller, ChoiceBox<String> controllee,
                                                      Map<String,ObservableList<String>> conditionOperatorOptions){
        controller.getSelectionModel().selectedItemProperty().addListener((observableEvent, oldComponent, newComponent) -> {
            controllee.setItems(FXCollections.observableList(conditionOperatorOptions.get(newComponent)));
            if (conditionOperatorOptions.get(newComponent).size() == 1){
                controllee.setValue(conditionOperatorOptions.get(newComponent).get(0));
            }
        });
    }

    private static ChoiceBox<String> setUpBoundChoiceBox(List<String> controllerOptions, StringProperty binder, HBox parent){
        ChoiceBox<String> choice = new ChoiceBox<>(FXCollections.observableArrayList(controllerOptions));
        choice.setOnAction(e -> choice.setAccessibleText(choice.getValue()));
        binder.bindBidirectional(choice.accessibleTextProperty());
        parent.getChildren().add(choice);
        return choice;
    }

    private static void createDependencyForValueField(ChoiceBox<String> controller,ValueFieldProperty valueField){
        controller.getSelectionModel().selectedItemProperty().addListener((observableEvent, oldComponent, newComponent) -> {
            valueField.clearListeners();
            valueField.addListeners(newComponent);
        });
    }

    /**
     * This method returns a label to be displayed in an event-specific prompt box
     */
    public static Label createLabel(String labelText){
        Label myLabel = new Label(labelText);
        myLabel.getStylesheets().add(STYLE);
        return myLabel;
    }


}
