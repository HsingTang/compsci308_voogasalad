package ui.panes;

import engine.external.Entity;
import engine.external.component.NameComponent;
import engine.external.component.SpriteComponent;
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

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;

/**
 * @author Carrie Hunner
 * This is a page that will display all of the types created by the user
 */
public class UserCreatedTypesPane extends VBox {
    private EntityMenu myEntityMenu;
    private static final ResourceBundle myResources = ResourceBundle.getBundle("default_entity_type");
    private static final ResourceBundle myGeneralResources = ResourceBundle.getBundle("authoring_general");
    private ObjectManager myObjectManager;
    private DefaultTypeXMLReaderFactory myDefaultTypesFactory;
    private Entity myDraggedEntity;
    private AuthoringEntity myDraggedAuthoringEntity;
    private Map<String, List<Pane>> myCategoryToList;


    /**
     * Creates a pane that displayes the user created types
     * @param objectManager
     */
    public UserCreatedTypesPane(ObjectManager objectManager){
        myObjectManager = objectManager;
        String title = myResources.getString("UserCreatedTitle");
        myEntityMenu = new EntityMenu(title);
        myDefaultTypesFactory = new DefaultTypeXMLReaderFactory();
        myCategoryToList = new HashMap<>();
        populateCategories();
        this.getChildren().add(myEntityMenu);
    }

    /**
     * Used by Viewer to get the dragged Entity
     * @return Entity
     */
    public Entity getDraggedEntity(){
        return myDraggedEntity;
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

    public void addUserDefinedType(Entity originalEntity,String defaultName){
        String label = (String) originalEntity.getComponent(NameComponent.class).getValue();
        String category = myDefaultTypesFactory.getCategory(defaultName);
        myCategoryToList.putIfAbsent(category, new ArrayList<>());
        String imageName = (String) originalEntity.getComponent(SpriteComponent.class).getValue();
        try {
            AuthoringEntity originalAuthoringEntity = new AuthoringEntity(originalEntity, myObjectManager);
            originalAuthoringEntity.getPropertyMap().put(EntityField.LABEL, label);
            String assetImagesFilePath = myGeneralResources.getString("images_filepath");
            ImageWithEntity imageWithEntity = new ImageWithEntity(new FileInputStream(assetImagesFilePath + "/" + imageName), originalAuthoringEntity);
            UserDefinedTypeSubPane subPane = new UserDefinedTypeSubPane(imageWithEntity, label, originalAuthoringEntity);
            myCategoryToList.get(category).add(subPane);
            myEntityMenu.setDropDown(category, myCategoryToList.get(category));
            subPane.setOnDragDetected(mouseEvent -> {
                myDraggedEntity = myDefaultTypesFactory.createEntity(defaultName);
                myDraggedEntity.addComponent(new NameComponent(originalAuthoringEntity.getPropertyMap().get(EntityField.LABEL)));
                myDraggedEntity.addComponent(new SpriteComponent(originalAuthoringEntity.getPropertyMap().get(EntityField.IMAGE)));
                myDraggedAuthoringEntity = originalAuthoringEntity;
                Utility.setupDragAndDropImage(imageWithEntity);
            });
            subPane.setOnContextMenuRequested(contextMenuEvent -> handleRightClick(contextMenuEvent, subPane, category));
        } catch (FileNotFoundException e) {
            //TODO: deal with this
            e.printStackTrace();
        }
    }

    private void handleRightClick(ContextMenuEvent contextMenuEvent, UserDefinedTypeSubPane userDefinedTypeSubPane, String category){
        ContextMenu contextMenu = new ContextMenu();
        MenuItem menuItem = new MenuItem();
        menuItem.setText("Delete");
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
