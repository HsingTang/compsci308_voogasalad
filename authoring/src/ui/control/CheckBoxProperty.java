package ui.control;

import javafx.scene.control.CheckBox;
import ui.EntityField;
import ui.Propertable;
import ui.manager.LabelManager;
import ui.manager.ObjectManager;

/**
 * Class that extends CheckBox for use in a PropertiesPane in the ByteMe Authoring Environment UI. This class
 * detects changes in its own user input to propagate elsewhere.
 * @author Harry Ross
 */
public class CheckBoxProperty extends CheckBox implements ControlProperty {

    @Override
    public void populateValue(Propertable prop, Enum type, String newValue, LabelManager labels) {
        this.setSelected(Boolean.parseBoolean(prop.getPropertyMap().get(type)));
    }

    @Override
    public void setAction(ObjectManager manager, Propertable propertable, Enum label, String action) {
        this.setOnAction(event -> {
            if (label.equals(EntityField.FOCUS))
                manager.flushFocusAssignment(propertable);
            propertable.getPropertyMap().put(label,
                    Boolean.toString(!Boolean.parseBoolean(propertable.getPropertyMap().get(label))));
        });
    }
}
