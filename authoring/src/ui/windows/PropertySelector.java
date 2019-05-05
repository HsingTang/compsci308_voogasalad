package ui.windows;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.ScrollPane;
import javafx.stage.Stage;
import ui.EntityField;
import ui.Propertable;
import ui.Utility;

import java.util.Arrays;

/**
 * Window that displays additional properties for the user to select from for an AuthoringEntity
 * @author Harry Ross
 */
public class PropertySelector extends Stage {

    private Propertable myAuthEntity;
    private ListView<String> myProptions;
    private EntityField myAddedField;

    /**
     * Creates a new PropertySelector for given Propertable
     * @param prop Propertable to add selected field to
     */
    public PropertySelector(Propertable prop) {
        myAuthEntity = prop;
        myProptions = new ListView<>();
        this.setResizable(false);
        createDialog();
    }

    private void createDialog() {
        Label header = new Label("Add Additional Property");

        Button addButton = Utility.makeButton(this, "addAndClose", "Add");
        Button cancelButton = Utility.makeButton(this, "cancel", "Cancel");

        this.setScene(Utility.createDialogPane(header, createContent(), Arrays.asList(addButton, cancelButton)));
    }

    private Node createContent() {
        ObservableList<String> listContents = FXCollections.observableList(EntityField.getTextFieldList());
        listContents.removeIf(label -> myAuthEntity.getPropertyMap().containsKey(EntityField.valueOf(label)));
        myProptions.setItems(listContents);
        return new ScrollPane(myProptions);
    }

    @SuppressWarnings("unused")
    private void addAndClose() {
        if (!myProptions.getSelectionModel().getSelectedItems().isEmpty()) {
            EntityField addedField = EntityField.valueOf(myProptions.getSelectionModel().getSelectedItems().get(0));
            myAuthEntity.getPropertyMap().put(addedField, addedField.getDefaultValue());
            myAddedField = addedField;
        }
        this.close();
    }

    @SuppressWarnings("unused")
    private void cancel() {
        this.close();
    }

    /**
     * Returns selected field
     * @return EntityField selected by user
     */
    public EntityField getAddedField() {
        return myAddedField;
    }

}
