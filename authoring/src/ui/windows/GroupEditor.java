package ui.windows;

import javafx.beans.property.StringProperty;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.TextFieldListCell;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;
import ui.EntityField;
import ui.Utility;
import ui.manager.ObjectManager;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

/**
 * Class for displaying, adding, removing, and modifying Group types
 * @author Harry Ross
 */
public class GroupEditor extends Stage {

    private ObjectManager myObjectManager;
    private ListView<String> myListView;

    /**
     * Creates new GroupEditor from given ObjectManager
     * @param objectManager ObjectManager to populate Group names from
     */
    public GroupEditor(ObjectManager objectManager) {
        this.setResizable(false);
        this.setTitle("Group Manager");
        myObjectManager = objectManager;
        myListView = new ListView<>();
        ScrollPane scrollpane = new ScrollPane(createListContent());

        Map<String, List<String>> instructions = new TreeMap<>();
        instructions.put("label", new ArrayList<>(Collections.singletonList("Add or Remove a Group")));
        instructions.put("sub-label", new ArrayList<>(Collections.singletonList("Double-click a Group to rename")));

        Button addButton = new Button("Add");
        addButton.setOnAction(event -> newGroupPrompt());
        Button removeButton = new Button("Remove");
        removeButton.setOnAction(event -> removeLabel());
        Button okButton = new Button("Close");
        okButton.setOnAction(event -> this.close());

        this.setScene(Utility.createDialogPane(Utility.createLabelsGroup(instructions), new HBox(scrollpane),
                new ArrayList<>(Arrays.asList(addButton, removeButton, okButton))));
    }

    private HBox createListContent() {
        HBox contentBox = new HBox();
        contentBox.getChildren().add(myListView);
        myListView.setEditable(true);
        myListView.setCellFactory(TextFieldListCell.forListView());
        myListView.setItems(myObjectManager.getLabelManager().getLabels(EntityField.GROUP));
        myListView.setOnEditCommit(event -> editLabel(event));
        return contentBox;
    }

    @SuppressWarnings("unused")
    private void addLabel(StringProperty newLabelProp, Stage stage) {
        myObjectManager.getLabelManager().addLabel(EntityField.GROUP, newLabelProp.getValue());
        if (stage != null)
            stage.close();
    }

    private void editLabel(ListView.EditEvent<String> event) {
        String newVal = event.getNewValue();
        myListView.getItems().set(event.getIndex(), newVal);
    }

    private void removeLabel() {
        if (!myListView.getSelectionModel().getSelectedItems().isEmpty()) {
            String badLabel = myListView.getSelectionModel().getSelectedItems().get(0);
            myObjectManager.getLabelManager().removeLabel(EntityField.GROUP, badLabel);
        }
    }

    private void newGroupPrompt() {
        Stage prompt = new Stage();
        prompt.setResizable(false);
        prompt.setTitle("New Group");

        TextField newGroupField = new TextField();
        newGroupField.setPromptText("New Group Name...");
        HBox centerContent = new HBox(newGroupField);

        Button addButton = Utility.makeButton(this, "addLabel", "Add Group", newGroupField.textProperty(), prompt);
        Button cancelButton = Utility.makeButton(prompt, "close", "Cancel");

        prompt.setScene(Utility.createDialogPane(null, centerContent, new ArrayList<>(Arrays.asList(addButton, cancelButton))));
        prompt.showAndWait();
    }
}
