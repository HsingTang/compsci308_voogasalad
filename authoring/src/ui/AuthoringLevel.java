package ui;

import javafx.collections.FXCollections;
import javafx.collections.MapChangeListener;
import javafx.collections.ObservableMap;
import ui.manager.ObjectManager;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Authoring version of Level, holds property map to be observed by other classes in Authoring Environment
 * @author Harry Ross
 */
public class AuthoringLevel implements Propertable {

    private ObjectManager myObjectManager;
    private ObservableMap<Enum, String> myPropertyMap;
    private List<AuthoringEntity> myEntities;

    private static final List<? extends Enum> PROP_VAR_NAMES = Arrays.asList(LevelField.values());
    private static final Integer DEFAULT_ROOM_SIZE = 1200;

    /**
     * Creates a new AuthoringLevel with given label and ObjectManager
     * @param label String label of new level
     * @param manager ObjectManager to associate with new AuthoringLevel
     */
    public AuthoringLevel(String label, ObjectManager manager) {
        myObjectManager = manager;
        myEntities = new ArrayList<>();
        myPropertyMap = FXCollections.observableHashMap();
        for (Enum name : PROP_VAR_NAMES)
            myPropertyMap.put(name, null);
        myPropertyMap.put(LevelField.LABEL, label);
        myPropertyMap.put(LevelField.HEIGHT, DEFAULT_ROOM_SIZE.toString());
        myPropertyMap.put(LevelField.WIDTH, DEFAULT_ROOM_SIZE.toString());
        addPropertyListeners();
    }

    private void addPropertyListeners() {
        myPropertyMap.addListener((MapChangeListener<Enum, String>) change ->
                propagateChanges(change.getKey(),  change.getValueRemoved(), change.getValueAdded()));
    }

    private void propagateChanges(Enum key, String valueRemoved, String valueAdded) {
        if (key.equals(LevelField.LABEL)) // If we're changing the label, preserve old label for propagation purposes
            myObjectManager.updateLevelLabel(valueRemoved, valueAdded);
    }

    /**
     * Adds specified AuthoringEntity to level
     * @param newEntity AuthoringEntity to add to level
     */
    public void addEntity(AuthoringEntity newEntity) {
        myEntities.add(newEntity);
    }

    /**
     * Returns list of AuthoringEntities assigned to level
     * @return List of AuthoringEntities in level
     */
    public List<AuthoringEntity> getEntities() {
        return myEntities;
    }

    @Override
    public ObservableMap<Enum, String> getPropertyMap() {
        return myPropertyMap;
    }

    @Override
    public Class<? extends Enum> getEnumClass() {
        return LevelField.class;
    }
}
