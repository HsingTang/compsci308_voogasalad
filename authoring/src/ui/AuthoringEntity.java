package ui;

import engine.external.Entity;
import engine.external.component.OpacityComponent;
import engine.external.events.Event;
import javafx.collections.FXCollections;
import javafx.collections.MapChangeListener;
import javafx.collections.ObservableList;
import javafx.collections.ObservableMap;
import ui.manager.ObjectManager;

/**
 * Authoring Environment version of Entity that contains property map to be observed by other classes
 * @author Harry Ross
 */
public class AuthoringEntity implements Propertable {

    private ObservableMap<Enum, String> myPropertyMap;
    private ObjectManager myObjectManager;

    private AuthoringEntity() { // Initialize default property map
        myPropertyMap = FXCollections.observableHashMap();
        for (EntityField defaultField : EntityField.getDefaultFields()) {
            myPropertyMap.put(defaultField, defaultField.getDefaultValue());
        }
    }

    /**
     * Creates new AuthoringEntity type from scratch
     * @param label Label of new AuthoringEntity
     * @param manager ObjectManager to associate with new AuthoringEntity
     */
    public AuthoringEntity(String label, ObjectManager manager) {
        this();
        myObjectManager = manager;
        myPropertyMap.put(EntityField.LABEL, label);
        addPropertyListeners();
    }

    /**
     * Creates new AuthoringEntity type from an Entity used elsewhere in project
     * @param basis Entity to base translated AuthoringEntity on
     * @param manager ObjectManager to associate new AuthoringEntity with
     */
    public AuthoringEntity(Entity basis, ObjectManager manager) { // Create new AuthoringEntity type from Entity
        this();
        myObjectManager = manager;
        for (EntityField field : EntityField.values()) {
            if (!field.equals(EntityField.EVENTS) && !field.equals(EntityField.VISIBLE) && basis.hasComponents(field.getComponentClass()))
                myPropertyMap.put(field, String.valueOf(basis.getComponent(field.getComponentClass()).getValue()));
        }
        if (basis.hasComponents(OpacityComponent.class)) {
            if (((OpacityComponent) basis.getComponent(OpacityComponent.class)).getValue() == 1.0)
                myPropertyMap.put(EntityField.VISIBLE, String.valueOf(true));
            else
                myPropertyMap.put(EntityField.VISIBLE, String.valueOf(false));
        }
        addPropertyListeners();
    }

    /**
     * Creates a new AuthoringEntity instance from pre-existing instance
     * @param copyBasis AuthoringEntity to base new copy on
     */
    public AuthoringEntity(AuthoringEntity copyBasis) { // Create new AuthoringEntity instance from pre-existing type
        this();
        myObjectManager = copyBasis.myObjectManager;
        for (EntityField commonField : EntityField.getCommonFields()) {
            if (copyBasis.myPropertyMap.containsKey(commonField))
                myPropertyMap.put(commonField, copyBasis.myPropertyMap.get(commonField));
        }
        addPropertyListeners();
    }

    private void addPropertyListeners() {
        myPropertyMap.addListener((MapChangeListener<Enum, String>) change ->
                propagateChanges(change.getKey(),  change.getValueRemoved(), change.getValueAdded()));
    }

    private void propagateChanges(Enum key, String oldVal, String newVal) { // At this point value is assumed valid or untaken label
        if (key.equals(EntityField.LABEL)) // If we're changing the label, preserve old label for propagation purposes
            myObjectManager.propagate(oldVal, key, newVal);
        else if (key.equals(EntityField.IMAGE) || key.equals(EntityField.GROUP)) // If we're changing the Image or Group, just do it
            myObjectManager.propagate(myPropertyMap.get(EntityField.LABEL), key, newVal);
        else if (key.equals(EntityField.FOCUS))
            myObjectManager.flushFocusAssignment(this);
        else if (!((EntityField) key).isDefault()) {
            myObjectManager.propagate(myPropertyMap.get(EntityField.LABEL), key, newVal);
        }
    }

    @Override
    public ObservableMap<Enum, String> getPropertyMap() {
        return myPropertyMap;
    }

    @Override
    public Class<? extends Enum> getEnumClass() {
        return EntityField.class;
    }

    /**
     * Returns list of Events associated with this AuthoringEntity type
     * @return List of Events for AuthoringEntities matching the label of this AuthoringEntity
     */
    public ObservableList<Event> getEvents() {
        return myObjectManager.getEvents(this.myPropertyMap.get(EntityField.LABEL));
    }
}
