package ui.control;

import javafx.scene.control.ComboBox;
import ui.Propertable;
import ui.manager.LabelManager;
import ui.manager.ObjectManager;

/**
 * Class that extends ComboBox for use in a PropertiesPane in the ByteMe Authoring Environment UI. This class
 * detects changes in its own user input to propagate elsewhere.
 * @author Harry Ross
 */
public class ComboBoxProperty extends ComboBox<String> implements ControlProperty {

    /**
     * Instantiates new ComboBoxProperty with specified prompt text
     * @param prompt Prompt text to display in instance without a selected value.
     */
    public ComboBoxProperty(String prompt) {
        this.setPromptText(prompt);
    }

    @Override
    public void populateValue(Propertable prop, Enum type, String newVal, LabelManager labels) {
        this.itemsProperty().set(labels.getLabels(type));
        this.setValue(newVal);
    }

    @Override
    public void setAction(ObjectManager manager, Propertable propertable, Enum propLabel, String action) {
        this.setOnAction(e -> updateProperty(propertable, propLabel, this.getValue()));
    }

    private void updateProperty(Propertable prop, Enum propLabel, String value) {
        prop.getPropertyMap().put(propLabel, value);
    }
}
