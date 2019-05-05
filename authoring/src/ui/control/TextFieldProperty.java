package ui.control;

import javafx.collections.MapChangeListener;
import javafx.scene.control.TextField;
import ui.EntityField;
import ui.Propertable;
import ui.Utility;
import ui.manager.LabelManager;
import ui.manager.ObjectManager;

/**
 * Class that extends TextField for use in a PropertiesPane in the ByteMe Authoring Environment UI. This class
 * both detects changes in its own user input to propagate elsewhere and detects changes in its bound property from
 * other areas of the UI in order to update its displayed value
 * @author Harry Ross
 */
public class TextFieldProperty extends TextField implements ControlProperty {

    private LabelManager myLabelManager;
    private String myCurrentValue;
    private Enum myLabelGroup;

    private static final String PROP_SYNTAX = "property_syntax";

    /**
     * Instantiates a new TextFieldProperty with default LabelManager and empty current value
     */
    public TextFieldProperty() {
        myLabelManager = new LabelManager();
        myCurrentValue = "";
    }

    /**
     * Only used when creating a new TextField
     */
    @Override
    public void populateValue(Propertable prop, Enum labelGroup, String newVal, LabelManager labels) {
        this.setText(newVal);
        myCurrentValue = newVal;
        myLabelManager = labels;
        myLabelGroup = labelGroup;
        prop.getPropertyMap().addListener((MapChangeListener<? super Enum, ? super String>) change -> updateDisplayFromProp(change));
    }

    @Override
    public void setAction(ObjectManager manager, Propertable propertable, Enum label, String action) {
        this.focusedProperty().addListener(e -> updatePropFromDisplay(propertable, label, this.getText()));
    }

    private void updateDisplayFromProp(MapChangeListener.Change change) {
        if (change.getKey().equals(myLabelGroup)) {
            this.setText((String) change.getValueAdded());
        }
    }

    private void updatePropFromDisplay(Propertable prop, Enum propLabel, String value) {
        if (isValidValue(propLabel, value)) {
            prop.getPropertyMap().put(propLabel, value); // Ask immediately if new value if valid, not later
            myCurrentValue = value;
        }
        else {
            this.setText(myCurrentValue); // Revert to old value if validity check fails
        }
    }

    /**
     * Check validity of new value from based on regex syntax from properties file, whether or not label exists in LabelManager already
     */
    private boolean isValidValue(Enum key, String newVal) {
        if (key.equals(EntityField.LABEL) && myLabelManager.getLabels(EntityField.LABEL).contains(newVal))
            return false;
        return Utility.isValidValue(key, newVal, PROP_SYNTAX);
    }
}
