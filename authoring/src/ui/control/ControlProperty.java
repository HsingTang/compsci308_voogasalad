package ui.control;

import ui.Propertable;
import ui.UIException;
import ui.manager.LabelManager;
import ui.manager.ObjectManager;

/**
 * Interface to be implemented by graphical properties displays in ByteMe Authoring Environment
 * @author Harry Ross
 */
public interface ControlProperty {

    /**
     * Populates value of property to be displayed
     * @param prop Propertable instance that owns property
     * @param type Propertable type enum
     * @param newValue Value to populate display with
     * @param labels LabelManager for referencing valid labels
     */
    void populateValue(Propertable prop, Enum type, String newValue, LabelManager labels);

    /**
     * Sets action for ControlProperty, implemented slightly differently in concrete implementations
     * @param manager ObjectManager of Propertable
     * @param propertable Propertable to set action for (generally same as Propertable used in populateValue)
     * @param label Enum corresponding with field type
     * @param action Action to be invoked on interaction with property
     * @throws UIException if issues occur in setting action or within action method
     */
    void setAction(ObjectManager manager, Propertable propertable, Enum label, String action) throws UIException;
}
