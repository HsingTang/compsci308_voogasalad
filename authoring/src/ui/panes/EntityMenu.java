package ui.panes;

import javafx.geometry.Pos;
import javafx.scene.control.ListView;
import javafx.scene.control.TitledPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author Carrie Hunner
 * This class extends TitledPane and creates a general format for displaying information
 * It is used by both the UserCreatedTypesPane and the DefaultTypesPane
 */
public class EntityMenu extends TitledPane {
    private VBox myVBox;
    private Map<String, ListView> myMap;

    /**
     * creates a new EntityMenu
     * @param title Text to be displayed at the top of the TitledPane
     */
    public EntityMenu(String title){
        this.setText(title);
        this.setCollapsible(false);
        myVBox = new VBox();
        myMap = new HashMap<>();
        this.setContent(myVBox);
        this.getStyleClass().add("entity-menu");
    }

    /**
     * Creates a new sub Titled-Pane
     * @param title Text name of the new Titled Pane
     */
    public void addDropDown(String title){
        TitledPane newTitled = new TitledPane();
        newTitled.setAlignment(Pos.CENTER);
        newTitled.setText(title);
        ListView listView = new ListView();
        newTitled.setContent(listView);
        myMap.put(title, listView);
        myVBox.getChildren().add(newTitled);
    }

    /**
     * Adds the contents passed in to a DropDown previously created using the addDropDown
     * @param category String name of the category/title of the dropdown
     * @param contentToAdd a List of Panes to add to the dropdown
     */
    public void setDropDown(String category, List<Pane> contentToAdd){
        ListView vBox = myMap.get(category);
        vBox.getItems().clear();
        vBox.getItems().addAll(contentToAdd);
    }
}
