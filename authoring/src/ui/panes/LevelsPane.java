package ui.panes;

import javafx.beans.property.ObjectProperty;
import javafx.beans.value.ChangeListener;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.ListView;
import javafx.scene.control.MenuItem;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TitledPane;
import ui.AuthoringLevel;
import ui.ErrorBox;
import ui.LevelField;
import ui.Propertable;
import ui.manager.ObjectManager;

/**
 * Pane that displays list of created levels and allows user to click to transition between them
 * @author Harry Ross
 */
public class LevelsPane extends TitledPane {

    private ObjectManager myObjectManager;
    private ObjectProperty<Propertable> myCurrentLevel;
    private ListView<String> myListView;

    private static final String LEVELS_PANE_TITLE = "Levels";
    private static final String NEW_LEVEL_PREFIX = "New_Level_";

    /**
     * Creates new LevelsPane with given ObjectManager and CurrentLevel property
     * @param manager ObjectManager to base levels list off of
     * @param currentLevel Property to represent current level
     */
    public LevelsPane(ObjectManager manager, ObjectProperty<Propertable> currentLevel) {
        myObjectManager = manager;
        myCurrentLevel = currentLevel;
        myListView = new ListView<>();
        myListView.setContextMenu(createContextMenu());
        ScrollPane scrollPane = new ScrollPane(myListView);
        scrollPane.setFitToWidth(true);

        this.setText(LEVELS_PANE_TITLE);
        this.setContent(scrollPane);
        myListView.setItems(myObjectManager.getLabelManager().getLabels(LevelField.LABEL));
        this.getStyleClass().add("prop-pane");
        myListView.getSelectionModel().selectedItemProperty()
                .addListener((ChangeListener<? super String>) (change, oldVal, newVal) -> changeLevel(newVal));
    }

    private void changeLevel(String newVal) {
        if (!newVal.equals((myCurrentLevel.getValue()).getPropertyMap().get(LevelField.LABEL))) {
            for (AuthoringLevel level : myObjectManager.getLevels()) {
                if (level.getPropertyMap().get(LevelField.LABEL).equals(newVal))
                    myCurrentLevel.setValue(level);
            }
        }
    }

    private void addLevel() {
        String newLevelLabel;
        int count = 1;
        newLevelLabel = NEW_LEVEL_PREFIX + count;

        while (myObjectManager.getLabelManager().getLabels(LevelField.LABEL).contains(newLevelLabel)) {
            count++;
            newLevelLabel = NEW_LEVEL_PREFIX + count;
        }

        AuthoringLevel newLevel = new AuthoringLevel(newLevelLabel, myObjectManager);
        myObjectManager.addLevel(newLevel);
        myCurrentLevel.setValue(newLevel);
        myListView.getSelectionModel().select(newLevelLabel);
    }

    private void removeLevel() {
        if (myListView.getItems().size() > 1)
            myObjectManager.removeLevel(myListView.getSelectionModel().getSelectedItems().get(0));
        else {
            ErrorBox error = new ErrorBox("Level Error", "Cannot remove only level");
            error.display();
        }
    }

    private ContextMenu createContextMenu() {
        MenuItem addLevelItem = new MenuItem("Add Level");
        MenuItem removeLevelItem = new MenuItem("Remove Level");

        addLevelItem.setOnAction(event -> addLevel());
        removeLevelItem.setOnAction(event -> removeLevel());

        return new ContextMenu(addLevelItem, removeLevelItem);
    }
}
