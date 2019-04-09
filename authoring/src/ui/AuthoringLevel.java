package ui;

import javafx.collections.FXCollections;
import javafx.collections.ObservableMap;
import ui.manager.ObjectManager;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

/**
 * @author Harry Ross
 */
public class AuthoringLevel implements Propertable {

    private ObservableMap<Enum, String> myPropertyMap;
    private ObjectManager myObjectManager;

    private static final List<? extends Enum> PROP_VAR_NAMES = Arrays.asList(LevelField.values());
    private static final Integer DEFAULT_ROOM_SIZE = 500;

    public AuthoringLevel(String label, ObjectManager manager) {
        myObjectManager = manager;
        myPropertyMap = FXCollections.observableHashMap();
        for (Enum name : PROP_VAR_NAMES)
            myPropertyMap.put(name, null);
        myPropertyMap.put(LevelField.LABEL, label);
        myPropertyMap.put(LevelField.HEIGHT, DEFAULT_ROOM_SIZE.toString());
        myPropertyMap.put(LevelField.WIDTH, DEFAULT_ROOM_SIZE.toString());
        addPropertyListener();
    }

    public Map<Enum, String> getPropertyMap() {
        return myPropertyMap;
    }

    @Override
    public Class<? extends Enum> getEnumClass() {
        return LevelField.class;
    }

    private void addPropertyListener() {
        // This may be useful someday but not yet
    }
}
