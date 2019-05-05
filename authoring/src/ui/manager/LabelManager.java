package ui.manager;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import ui.EntityField;
import ui.LevelField;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * This class keeps track of valid labels for Object Type, Group, etc. in Authoring Environment
 * @author Harry Ross
 */
public class LabelManager {

    private Map<Enum, ObservableList<String>> myLabelGroups;

    /**
     * Creates new LabelManager with empty label lists for various groups
     */
    public LabelManager() {
        myLabelGroups = new HashMap<>();
        myLabelGroups.put(EntityField.LABEL, FXCollections.observableArrayList(new ArrayList<>()));
        myLabelGroups.put(EntityField.GROUP, FXCollections.observableArrayList(new ArrayList<>()));
        myLabelGroups.put(LevelField.LABEL, FXCollections.observableArrayList(new ArrayList<>()));
    }

    /**
     * Adds a label to the label list with specified enum key
     * @param labelGroup Key of where to place new label
     * @param labelName New label String
     */
    public void addLabel(Enum labelGroup, String labelName) {
        if (myLabelGroups.containsKey(labelGroup) &&
                !myLabelGroups.get(labelGroup).contains(labelName) && labelName != null) //Checks to make sure it isn't already here
            myLabelGroups.get(labelGroup).add(labelName);
    }

    /**
     * Removes a given label from specified enum group
     * @param labelGroup Key of where to remove label
     * @param labelName Label to remove as String
     */
    public void removeLabel(Enum labelGroup, String labelName) {
        if (myLabelGroups.containsKey(labelGroup))
            myLabelGroups.get(labelGroup).remove(labelName);
    }

    /**
     * Returns all labels of a given enum group as an ObservableList
     * @param labelGroup Key of label list to return
     * @return ObservableList of labels corresponding with enum key
     */
    public ObservableList<String> getLabels(Enum labelGroup) {
        return myLabelGroups.getOrDefault(labelGroup, null);
    }
}
