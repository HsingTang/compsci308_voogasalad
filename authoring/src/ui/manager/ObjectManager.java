package ui.manager;

import events.Event;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import ui.AuthoringEntity;
import ui.AuthoringLevel;
import ui.EntityField;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * @author Harry Ross
 */
public class ObjectManager {

    private Set<AuthoringEntity> myEntities;
    private Map<String, ObservableList<Event>> myEventMap;
    private LabelManager myLabelManager;
    private ObservableList<AuthoringLevel> myLevels;

    /**
     * Class that keeps track of every single instance of an Entity, across Levels, for the purposes of authoring environment
     */
    public ObjectManager() {
        myEntities = new HashSet<>();
        myLabelManager = new LabelManager();
        myEventMap = new HashMap<>();
        myLevels = FXCollections.observableArrayList(new ArrayList<>());
        myLabelManager.getLabels(EntityField.GROUP).addListener((ListChangeListener<? super String>) change -> groupRemoveAction(change));
    }

    public void addLevel(AuthoringLevel level) {
        myLevels.add(level);
    }

    public void removeLevel(AuthoringLevel level) {
        myLevels.remove(level);
    }

    /**
     * Use this for adding a new general type
     * @param entity
     */
    public void addEntityType(AuthoringEntity entity) {
        myEntities.add(entity);
        myLabelManager.addLabel(EntityField.LABEL, entity.getPropertyMap().get(EntityField.LABEL));
        myEventMap.put(entity.getPropertyMap().get(EntityField.LABEL), FXCollections.observableArrayList(new ArrayList<>()));
    }

    /**
     * Use this for instances that are added to a specific level
     * @param entity
     * @param level
     */
    public void addEntityInstance(AuthoringEntity entity, AuthoringLevel level) {
        myEntities.add(entity);
        level.addEntity(entity);
    }

    //TODO remove entity??

    public void propagate(String objectLabel, Enum property, String newValue) {
        for (AuthoringEntity entity : myEntities) {
            if (entity.getPropertyMap().get(EntityField.LABEL).equals(objectLabel)) { // Match found
                entity.getPropertyMap().put(property, newValue);
                myLabelManager.addLabel(EntityField.LABEL, newValue);
            }
        } //TODO propagate changed label into Event Map
        if (property.equals(EntityField.LABEL))
            myLabelManager.removeLabel(EntityField.LABEL, objectLabel); // Remove old label from LabelManager if a label was just propagated
    }

    private void groupRemoveAction(ListChangeListener.Change<? extends String> change) {
        change.next();
        String str = null;

        if (change.wasReplaced())
            str = change.getAddedSubList().get(0);

        if (change.wasRemoved())
            System.out.println("REMOVED");

        if (change.wasReplaced() || change.wasRemoved()) {
            for (AuthoringEntity entity : myEntities) {
                if (entity.getPropertyMap().get(EntityField.GROUP) != null &&
                        entity.getPropertyMap().get(EntityField.GROUP).equals(change.getRemoved().get(0)))
                    entity.getPropertyMap().put(EntityField.GROUP, str);
            }
        }
    }

    public LabelManager getLabelManager() {
        return myLabelManager;
    }

    public ObservableList<Event> getEvents(String objectType) {
        return myEventMap.get(objectType);
    }

    public List<AuthoringLevel> getLevels() {
        return myLevels;
    }
}
