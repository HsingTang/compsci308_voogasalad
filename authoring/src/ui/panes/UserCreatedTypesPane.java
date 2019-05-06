package ui.panes;

import engine.external.Entity;
import engine.external.component.NameComponent;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.MenuItem;
import javafx.scene.input.ContextMenuEvent;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import ui.AuthoringEntity;
import ui.DefaultTypeXMLReaderFactory;
import ui.EntityField;
import ui.Utility;
import ui.manager.ObjectManager;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;

/**
 * @author Carrie Hunner
 * This is a page that will display all of the types created by the user.
 * Each of these will be set up to be draggable such that the user can drag and when dropped, a duplicate
 * ImageWithEntity will be created such that it can be used in the game
 */
public class UserCreatedTypesPane extends VBox {
    private EntityMenu myEntityMenu;
    private static final ResourceBundle DEFAULT_RESOURCES = ResourceBundle.getBundle("default_entity_type");
    private static final ResourceBundle RESOURCES = ResourceBundle.getBundle("user_created_types");
    private ObjectManager myObjectManager;
    private DefaultTypeXMLReaderFactory myDefaultTypesFactory;
    private AuthoringEntity myDraggedAuthoringEntity;
    private Map<String, List<Pane>> myCategoryToList;


    /**
     * Creates a pane that displayes the user created types
     * @param objectManager used to create new AuthoringEntities and to keep track of them
     */
    public UserCreatedTypesPane(ObjectManager objectManager){
        myObjectManager = objectManager;
        String title = DEFAULT_RESOURCES.getString("UserCreatedTitle");
        myEntityMenu = new EntityMenu(title);
        myDefaultTypesFactory = new DefaultTypeXMLReaderFactory();
        myCategoryToList = new HashMap<>();
        populateCategories();
        this.getChildren().add(myEntityMenu);
    }

    /**
     * This creates the same pane but allows for a map of previously defined user-created types to be
     * passed in. Used for loading in a past game
     * @param objectManager
     * @param previouslyDefinedTypesMap the key is the String name of the default type it is associates with
     *                                  and the value is the original entity it is associated with
     */
    public UserCreatedTypesPane(ObjectManager objectManager, Map<Entity, String> previouslyDefinedTypesMap){
        this(objectManager);
        for(Map.Entry<Entity, String> entry : previouslyDefinedTypesMap.entrySet()){
            addUserDefinedType(entry.getKey(), entry.getValue());
        }
    }

    /**
     * Used by Viewer to get the dragged AuthoringEntity
     * @return AuthoringEntity
     */
    public AuthoringEntity getDraggedAuthoringEntity(){
        return  myDraggedAuthoringEntity;
    }

    private void populateCategories() {
        for(String s : myDefaultTypesFactory.getCategories()){
            myEntityMenu.addDropDown(s);
        }
    }

    /**
     * Adds a new user created type to the window
     * @param originalEntity The entity whose properties will want to be duplicated when a drag event occurs.
     * @param defaultName The name of the default type such that it can be used with the Default
     *                    TypeXMLParser to make another entity
     */
    public void addUserDefinedType(Entity originalEntity, String defaultName){
        String label = (String) originalEntity.getComponent(NameComponent.class).getValue();
        String category = myDefaultTypesFactory.getCategory(defaultName);
        myCategoryToList.putIfAbsent(category, new ArrayList<>());
        AuthoringEntity originalAuthoringEntity = new AuthoringEntity(originalEntity, myObjectManager);
        if(shouldAddEntity(originalAuthoringEntity)){
            myObjectManager.addEntityType(originalAuthoringEntity, defaultName);
        }
        originalAuthoringEntity.getPropertyMap().put(EntityField.LABEL, label);
        UserDefinedTypeSubPane subPane = createAndSetupSubPane(category, originalAuthoringEntity);
        myEntityMenu.setDropDown(category, myCategoryToList.get(category));

        subPane.setOnContextMenuRequested(contextMenuEvent -> handleRightClick(contextMenuEvent, subPane, category));
    }

    private UserDefinedTypeSubPane createAndSetupSubPane(String category, AuthoringEntity originalAuthoringEntity) {
        ImageWithEntity imageWithEntity = new ImageWithEntity(Utility.makeImageAssetInputStream(originalAuthoringEntity.getPropertyMap().get(EntityField.IMAGE)), originalAuthoringEntity); //closed
        UserDefinedTypeSubPane subPane = new UserDefinedTypeSubPane(imageWithEntity, originalAuthoringEntity.getPropertyMap().get(EntityField.LABEL), originalAuthoringEntity);
        myCategoryToList.get(category).add(subPane);
        subPane.setOnDragDetected(mouseEvent -> {
            myDraggedAuthoringEntity = originalAuthoringEntity;
            Utility.setupDragAndDropImage(imageWithEntity);
        });
        return subPane;
    }

    private boolean shouldAddEntity(AuthoringEntity authoringEntity){
        for(Map.Entry<AuthoringEntity, String> entry : myObjectManager.getTypeMap().entrySet()){
            if(authoringEntity.getPropertyMap().get(EntityField.LABEL).equals(entry.getKey().getPropertyMap().get(EntityField.LABEL))){
                return false;
            }
        }
        return true;
    }


    private void handleRightClick(ContextMenuEvent contextMenuEvent, UserDefinedTypeSubPane userDefinedTypeSubPane, String category){
        ContextMenu contextMenu = new ContextMenu();
        MenuItem menuItem = new MenuItem();
        menuItem.setText(RESOURCES.getString("deleteText"));
        menuItem.setOnAction(actionEvent -> {
            myCategoryToList.get(category).remove(userDefinedTypeSubPane);
            myObjectManager.removeEntityType(userDefinedTypeSubPane.getAuthoringEntity());
            updateEntityMenu();
        });
        contextMenu.getItems().add(menuItem);
        contextMenu.show(userDefinedTypeSubPane, contextMenuEvent.getScreenX(), contextMenuEvent.getScreenY());
    }

    private void updateEntityMenu(){
        for(Map.Entry<String, List<Pane>> entry : myCategoryToList.entrySet()){
            myEntityMenu.setDropDown(entry.getKey(), entry.getValue());
        }
    }
}
