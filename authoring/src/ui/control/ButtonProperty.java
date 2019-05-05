package ui.control;

import javafx.scene.control.Button;
import javafx.stage.Stage;
import ui.Propertable;
import ui.UIException;
import ui.manager.LabelManager;
import ui.manager.ObjectManager;

import java.lang.reflect.Constructor;

/**
 * Class that extends Button for use in a PropertiesPane in the ByteMe Authoring Environment UI.
 * @author Harry Ross
 */
public class ButtonProperty extends Button implements ControlProperty {

    public ButtonProperty(String label) {
        super(label);
    }

    @Override
    public void populateValue(Propertable prop, Enum type, String newVal, LabelManager labels) {
        // Do nothing, action does not apply to this implementor of ControlProperty as there is
        // no value associated with a Button to set
    }

    /**
     * ButtonProperty action will always open another window of a certain type, passing the propertable object along with it
     * @param prop Propertable object to pass
     * @param action Class name of new stage to open
     */
    @Override
    public void setAction(ObjectManager manager, Propertable prop, Enum propLabel, String action) throws UIException {
        try {
            Class<?> clazz = Class.forName(action);
            Constructor<?> constructor = clazz.getConstructor(Propertable.class, ObjectManager.class);
            Stage instance = (Stage) constructor.newInstance(prop, manager);
            this.setOnAction(e -> instance.show());
        } catch (Exception e1) {
            try {
                Class<?> clazz = Class.forName(action);
                Constructor<?> constructor = clazz.getConstructor(Propertable.class);
                Stage instance = (Stage) constructor.newInstance(prop);
                this.setOnAction(e -> instance.show());
            } catch (Exception e2) {
                throw new UIException("Error binding property control actions");
            }
        }
    }
}
