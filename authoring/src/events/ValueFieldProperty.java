package events;

import javafx.beans.value.ChangeListener;
import javafx.event.EventHandler;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.stage.WindowEvent;
import ui.windows.AudioManager;
import ui.windows.ImageManager;
import voogasalad.util.reflection.Reflection;


import java.util.ResourceBundle;

/**
 * This class is meant to hold the "trigger-value", for a condition, or the "action-value", for an action, in order
 * to adjust to the user's input on what type of condition or action they hope to make. Many of the methods had to
 * be public in order to use the Reflection utility module, but they are not used elsewhere to hardcode specific options
 * for different components
 * @author Anna Darwish
 */
public class ValueFieldProperty extends TextField {
    private ChangeListener<String> myListener;
    private EventHandler showFileOptions;

    private static final String listenerResourceName = "component_value_listener";
    private ResourceBundle listenerResource = ResourceBundle.getBundle(listenerResourceName);

    private static final String PROMPT = "Value";
    private static final String SOUND = "Sound";
    private static final String IMAGE = "Image";
    private static final String NUMBER = "Number";

    private static final String DOUBLE_REGEX = "^-?\\d+(?:\\.\\d+)?";
    private static final String DECIMAL_ENDING = "^-?\\d+(?:\\.)?";

    private static final ResourceBundle VALUE_PROMPTS_RESOURCE = ResourceBundle.getBundle("value_field_prompts");

    public ValueFieldProperty(){
        this.setPromptText(PROMPT);
    }

    public void clearListeners(){
        showFileOptions = event -> doNothing();
        this.setOnMouseClicked(showFileOptions);
        if (myListener != null)
            this.textProperty().removeListener(myListener);

    }
    /**
     * This method handles setting up the appropriate listeners and handlers on this textfield based on the current component
     * selected by the user, as there is a properties file that associated particular components with the proper listeners
     * and handlers
     * @see EventFactory
     */
    public void addListeners(String componentName){
        Reflection.callMethod(this,listenerResource.getString(componentName));
    }

    /**
     * This method restricts users to only enter double values, as many components expect numerical input
     */
    public void addNumericRestriction(){
        resetValueField(NUMBER);
        myListener = (observableValue, s, newValue) -> {
            if (!newValue.matches(DOUBLE_REGEX)) {
                if (newValue.matches(DECIMAL_ENDING))
                    setText(newValue + "0");
            }
        };
        this.textProperty().addListener(myListener);
    }
    /**
     * This method sets an on-click event that pops up an available list of audio files for the user to associate
     * with an action or condition
     * @see AudioManager
     */
    public void addSoundFilePrompt(){
        resetValueField(SOUND);
        showFileOptions = (EventHandler<MouseEvent>) mouseEvent -> {
            AudioManager myManager = new AudioManager();
            myManager.showAndWait();
            setText(myManager.getAssetName());
            setAccessibleText(myManager.getAssetName());
        };
        this.setOnMouseClicked(showFileOptions);
    }
    /**
     * This method sets an on-click event that pops up an available list of image files for the user to associate
     * with an action or condition
     * @see ImageManager
     */
    public void addImageFilePrompt(){
        resetValueField(IMAGE);
        showFileOptions = (EventHandler<MouseEvent>) mouseEvent -> {
            ImageManager myManager = new ImageManager();
            myManager.showAndWait();
            setText(myManager.getAssetName());
        };
        this.setOnMouseClicked(showFileOptions);
    }

    private void resetValueField(String promptKey){
        this.setText("");
        this.setPromptText(VALUE_PROMPTS_RESOURCE.getString(promptKey));
    }
    /**
     * This method was necessary in order to temporarily overwrite the current handler associated with pop-up display for image
     * and sound actions, as the process of removing a handler was quite convoluted
     */
    private void doNothing(){

    }
}
