package ui.panes;

import engine.external.Entity;
import javafx.scene.control.Label;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import ui.DefaultTypeXMLReaderFactory;
import ui.manager.ObjectManager;
import ui.windows.CreateNewTypeWindow;

import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

/**
 * @author Carrie Hunner
 * This class creates the page displaying the default types
 * provided using an EntityMenu as a layout template.
 */
public class DefaultTypesPane extends VBox{
    private EntityMenu myEntityMenu;
    private ResourceBundle myResources;
    private UserCreatedTypesPane myUserCreatedTypesPane;
    private DefaultTypeXMLReaderFactory myDefaultTypesFactory;
    private ObjectManager myObjectManager;

    private static final String RESOURCE = "default_entity_type";
    private static final String TITLE_KEY = "DefaultTitle";


    /**
     * Creates a new page to display the default types
     * @param userCreatedTypesPane Takes in a UserCreatedTypes pane such that it can call the addUserCreateType
     *                             when a new type is created
     * @param objectManager Takes in an ObjectManager because that is needed when creating a new type and calling
     *                      that window.
     */
    public DefaultTypesPane(UserCreatedTypesPane userCreatedTypesPane, ObjectManager objectManager)     {
        myResources = ResourceBundle.getBundle(RESOURCE);
        myEntityMenu = new EntityMenu(myResources.getString(TITLE_KEY));
        myUserCreatedTypesPane = userCreatedTypesPane;
        myDefaultTypesFactory = new DefaultTypeXMLReaderFactory();
        myObjectManager = objectManager;
        populateEntityMenu();
        this.getChildren().add(myEntityMenu);
    }

    private void populateEntityMenu(){
        List<String> tabNames = myDefaultTypesFactory.getCategories();
        for(String category : tabNames){
            ArrayList<Pane> labelsList = new ArrayList<>();
            List<String> specificTypes = myDefaultTypesFactory.getDefaultNames(category);
            for(String name : specificTypes){
                Label label = new Label(name);
                VBox pane = createAndFormatVBox(name, label);
                labelsList.add(pane);
            }
            myEntityMenu.addDropDown(category);
            myEntityMenu.setDropDown(category, labelsList);
        }
    }


    private VBox createAndFormatVBox(String defaultName, Label label) {
        VBox pane = new VBox(label);
        pane.setFillWidth(true);
        pane.setOnMouseClicked(mouseEvent -> {
            CreateNewTypeWindow createNewTypeWindow = new CreateNewTypeWindow(defaultName, myObjectManager);
            createNewTypeWindow.showAndWait();
            Entity entity = createNewTypeWindow.getUserCreatedEntity();
            if(entity != null){
                myUserCreatedTypesPane.addUserDefinedType(entity, defaultName);
            }
        });
        return pane;
    }

}
