package ui.panes;

import javafx.application.Platform;
import javafx.beans.property.ObjectProperty;
import javafx.beans.value.ChangeListener;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TitledPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import ui.EntityField;
import ui.Propertable;
import ui.PropertableType;
import ui.UIException;
import ui.Utility;
import ui.control.ControlProperty;
import ui.manager.ObjectManager;
import ui.windows.PropertySelector;

import java.lang.reflect.Constructor;
import java.util.ArrayList;
import java.util.Collections;
import java.util.ResourceBundle;

/**
 * Class to display editable properties corresponding to fields tied to enums in a Propertable type
 * @author Harry Ross
 */
public class PropertiesPane extends TitledPane {

    private ObjectProperty<Propertable> myProp;
    private PropertableType myPropType;
    private String myPropFile;
    private ObjectManager myObjectManager;
    private GridPane myPropertyGrid;

    private static final String PROP_TYPE_EXT = " Properties";

    /**
     * Creates new PropertiesPane with given parameters
     * @param manager ObjectManager to access for setting actions
     * @param propType Type of Propertable to associate with this pane, for labeling purposes
     * @param prop Propertable object to edit/update displayed fields from
     * @throws UIException if an error occurs in creating or displaying properties
     */
    public PropertiesPane(ObjectManager manager, PropertableType propType, ObjectProperty<Propertable> prop) throws UIException {
        myProp = prop;
        myPropType = propType;
        myPropFile = propType.getPropFile();
        myObjectManager = manager;
        this.setText(propType.toString() + PROP_TYPE_EXT);
        this.setContent(createPropertiesGrid());
        this.getStyleClass().add("prop-pane");
        addListener();
    }

    private void addListener() {
        myProp.addListener((ChangeListener<? super Propertable>) (change, oldVal, newVal) -> {
            try {
                this.setContent(createPropertiesGrid());
            } catch (UIException e) {
                e.displayUIException();
            }
        });
    }

    private Node createPropertiesGrid() throws UIException {
        Platform.runLater(this::requestFocus);
        myPropertyGrid = new GridPane();
        myPropertyGrid.getStyleClass().add("prop-grid");
        VBox gridContainer = new VBox(myPropertyGrid);
        ScrollPane scrollpane = new ScrollPane(gridContainer);
        if (myProp.getValue() != null)
            extractNewProperties();
        else {
            return new Label("Create an Entity Type to Start");
        }
        addNewPropButton(gridContainer);
        return scrollpane;
    }

    private void extractNewProperties() throws UIException {
        ResourceBundle bundle = ResourceBundle.getBundle(myPropFile);
        ArrayList<String> types = Collections.list(bundle.getKeys());
        Collections.sort(types);
        for (String type : types) {
            try {
                String name = type.split("\\.")[1];
                String value = bundle.getString(type);
                myPropertyGrid.add(createProperty(name, value), 0, myPropertyGrid.getRowCount());
            } catch (IndexOutOfBoundsException e) {
                throw new UIException("Invalid properties file");
            }
        }
        addExtraProperties();
    }

    private void addExtraProperties() throws UIException {
        if (myPropType.equals(PropertableType.OBJECT)) {
            for (Enum field : myProp.getValue().getPropertyMap().keySet()) {
                if (!((EntityField) field).isDefault() && ((EntityField) field).isTextable()) {
                    myPropertyGrid.add(createProperty(((EntityField) field).getLabel(), "ui.control.TextFieldProperty:none:none"),
                            0, myPropertyGrid.getRowCount());
                }
            }
        }
    }

    private VBox createProperty(String name, String info) throws UIException {
        VBox newProp = new VBox();
        Class<? extends Enum> enumClass = myProp.getValue().getEnumClass();
        newProp.getStyleClass().add("prop-cell");
        Label propName = new Label(name);
        propName.getStyleClass().add("sub-label");
        String[] sep = info.split(":");
        try {
            Class<?> clazz = Class.forName(sep[0]);
            Constructor<?> constructor = (sep[1].equals("none")) ?
                    clazz.getConstructor() : clazz.getConstructor(String.class);
            ControlProperty instance = (sep[1].equals("none")) ?
                    (ControlProperty) constructor.newInstance() : (ControlProperty) constructor.newInstance(sep[1]);
            newProp.getChildren().addAll(propName, (Node) instance);
            instance.populateValue(myProp.getValue(), Enum.valueOf(enumClass, name.toUpperCase()),
                    myProp.getValue().getPropertyMap().get(Enum.valueOf(enumClass, name.toUpperCase())), myObjectManager.getLabelManager());
            instance.setAction(myObjectManager, myProp.getValue(), Enum.valueOf(enumClass, name.toUpperCase()), sep[2]);
        } catch (Exception e) {
            throw new UIException("Error creating properties controls");
        }
        return newProp;
    }

    private void addNewPropButton(VBox container) {
        if (myPropType.equals(PropertableType.OBJECT)) {
            Button addButton = Utility.makeButton(this, "selectProperty", "Add Property");
            HBox addButtonBox = new HBox(addButton);
            container.getChildren().add(addButtonBox);
            addButtonBox.getStyleClass().add("prop-grid");
        }
    }

    @SuppressWarnings("unused")
    private void selectProperty() throws UIException {
        PropertySelector propertySelector = new PropertySelector(myProp.getValue());
        propertySelector.showAndWait();
        if (propertySelector.getAddedField() != null) {
            myPropertyGrid.add(createProperty(propertySelector.getAddedField().getLabel(), "ui.control.TextFieldProperty:none:none"),
                    0, myPropertyGrid.getRowCount());
        }
    }
}
