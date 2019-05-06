package ui.panes;

import javafx.beans.property.ObjectProperty;
import javafx.collections.ListChangeListener;
import javafx.collections.MapChangeListener;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.MenuItem;
import javafx.scene.control.ScrollPane;
import javafx.scene.image.Image;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.TransferMode;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundImage;
import javafx.scene.layout.BackgroundRepeat;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.shape.Line;
import ui.AuthoringEntity;
import ui.AuthoringLevel;
import ui.EntityField;
import ui.ErrorBox;
import ui.LevelField;
import ui.Propertable;
import ui.Utility;
import ui.manager.ObjectManager;

import java.awt.*;
import java.io.FileInputStream;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;


/**
 * @author Carrie Hunner
 * This class creates a pane where the user can build their level. It accepts drag and drops from the
 * UsercreatedTypesPane and will add those imageWithEntities to the Viewer. It also has a grid lining
 * the back of the stackpane and snaps drag and drops to that grid.
 */
public class Viewer extends ScrollPane {
    private StackPane myStackPane;
    private static final int CELL_SIZE = 50;
    private Double myRoomHeight;
    private Double myRoomWidth;
    private boolean isDragOnView;
    private ObjectManager myObjectManager;
    private AuthoringLevel myAuthoringLevel;
    private AuthoringEntity myDraggedAuthoringEntity;
    private ObjectProperty<Propertable> mySelectedEntityProperty;
    private UserCreatedTypesPane myUserCreatedPane;
    private static final ResourceBundle GENERAL_RESOURCES = ResourceBundle.getBundle("authoring_general");
    private Pane myLinesPane;
    private String myBackgroundFileName;
    private static final ResourceBundle RESOURCES = ResourceBundle.getBundle("viewer");
    private static final String SHEET = "viewer-scroll-pane";


    /**
     *
     * @param authoringLevel  This allows the Viewer to be a listener to the level size and to
     *                        respond to the backend size updating
     * @param userCreatedTypesPane This is where the Viewer can get dragged items from when a drop is received
     * @param objectProperty This allows for when an entity is selected on screen, the properties panes can
     *                       be updated with the correct information
     * @param objectManager This is used to add and remove types from the screen (if a user created type
     *                      is deleted, that effect will ripple through the Viewer)
     */
    public Viewer(AuthoringLevel authoringLevel, UserCreatedTypesPane userCreatedTypesPane,
                  ObjectProperty objectProperty, ObjectManager objectManager){
        myObjectManager = objectManager;
        myUserCreatedPane = userCreatedTypesPane;
        mySelectedEntityProperty = objectProperty;
        myAuthoringLevel = authoringLevel;
        initializeAndFormatVariables();
        addAllExistingEntities(authoringLevel);
        myAuthoringLevel.getPropertyMap().addListener((MapChangeListener<? super Enum, ? super String>) change ->
                handleChange(change));
        setupAcceptDragEvents();
        setupDragDropped();
        setRoomSize();
        updateGridLines();
        if (authoringLevel.getPropertyMap().get(LevelField.BACKGROUND) != null)
            updateBackground(authoringLevel.getPropertyMap().get(LevelField.BACKGROUND));
    }

    private void addAllExistingEntities(AuthoringLevel authoringLevel) {
        List<AuthoringEntity> authoringEntityList = authoringLevel.getEntities();
        authoringEntityList.sort((o1, o2) -> {
            int firstZ = (int) Double.parseDouble(o1.getPropertyMap().get(EntityField.Z));
            int secondZ = (int) Double.parseDouble(o2.getPropertyMap().get(EntityField.Z));
            return firstZ - secondZ;
        });

        for(AuthoringEntity authoringEntity : authoringEntityList){
            String imagePath = authoringEntity.getPropertyMap().get(EntityField.IMAGE);
            FileInputStream fileInputStream = Utility.makeImageAssetInputStream(imagePath); //closed
            ImageWithEntity imageWithEntity = new ImageWithEntity(fileInputStream, authoringEntity); //closed
            Utility.closeInputStream(fileInputStream); //closed
            addImage(imageWithEntity);
        }
    }

    private void initializeAndFormatVariables() {
        myStackPane = new StackPane();
        myLinesPane = new Pane();
        myBackgroundFileName = null;
        myStackPane.getChildren().addListener((ListChangeListener<Node>) change -> updateZField());
        myStackPane.getChildren().add(myLinesPane);
        myStackPane.setAlignment(Pos.TOP_LEFT);
        this.setContent(myStackPane);
        this.getStyleClass().add(SHEET);
    }

    private void updateZField() {
        int objectCount = 0;
        for(Node node : myStackPane.getChildren()){
            if(node instanceof ImageWithEntity){
                AuthoringEntity authoringEntity = ((ImageWithEntity) node).getAuthoringEntity();
                authoringEntity.getPropertyMap().put(EntityField.Z, Integer.toString(objectCount));
                objectCount++;
            }
        }
    }


    private void handleChange(MapChangeListener.Change<? extends Enum,? extends String> change) {
        if(change.wasAdded() && RESOURCES.containsKey(change.getKey().toString())){
            Utility.makeAndCallMethod(RESOURCES, change, this);
        }
    }

    private void updateWidth(String width){
        myRoomWidth = Double.parseDouble(width);
        updateGridLines();
        updateBackground(myBackgroundFileName);
        myStackPane.setMinWidth(myRoomWidth);
        myStackPane.setMaxWidth(myRoomWidth);
    }

    private void updateHeight(String height){
        myRoomHeight = Double.parseDouble(height);
        updateGridLines();
        updateBackground(myBackgroundFileName);
        myStackPane.setMinHeight(myRoomHeight);
        myStackPane.setMaxHeight(myRoomHeight);
    }

    private void updateBackground(String filename){
        if(filename != null){
            FileInputStream fileInputStream = Utility.makeImageAssetInputStream(filename);  //closed
            Image image = new Image(fileInputStream, myRoomWidth, myRoomHeight, false, false); //closed
            BackgroundImage backgroundImage = new BackgroundImage(image, BackgroundRepeat.NO_REPEAT, BackgroundRepeat.NO_REPEAT, null, null);
            myStackPane.setBackground(new Background(backgroundImage));
            myBackgroundFileName = filename;
            Utility.closeInputStream(fileInputStream); //closed
        }
    }

    private void setupAcceptDragEvents() {
        myStackPane.setOnDragOver(dragEvent -> dragEvent.acceptTransferModes(TransferMode.ANY));
    }

    private void setupDragDropped() {
        myStackPane.setOnDragDropped(dragEvent -> {
            AuthoringEntity authoringEntity;
            ImageWithEntity imageWithEntity;
            if(isDragOnView){
                authoringEntity = myDraggedAuthoringEntity;
                imageWithEntity = Utility.createImageWithEntity(authoringEntity);
                isDragOnView = false;
            }
            else{
                authoringEntity = myUserCreatedPane.getDraggedAuthoringEntity();
                String imageName = authoringEntity.getPropertyMap().get(EntityField.IMAGE);
                authoringEntity = new AuthoringEntity(authoringEntity);
                myObjectManager.addEntityInstance(authoringEntity);
                authoringEntity.getPropertyMap().put(EntityField.IMAGE, imageName);
                imageWithEntity = Utility.createImageWithEntity(authoringEntity);
                addImage(imageWithEntity);
            }
            Image image = imageWithEntity.getImage();
            authoringEntity.getPropertyMap().put(EntityField.X, "" + snapToGrid(dragEvent.getX() - image.getWidth()/2));
            authoringEntity.getPropertyMap().put(EntityField.Y, "" + snapToGrid(dragEvent.getY() - image.getHeight()/2));
            mySelectedEntityProperty.setValue(authoringEntity);
        });
    }

    private double snapToGrid(double value){
        double valueRemainder = value % CELL_SIZE;
        double result;
        if(valueRemainder >= CELL_SIZE/2){
            result = value + CELL_SIZE - valueRemainder;
        }
        else{
            result = value - valueRemainder;
        }
        return result;
    }


    private void addImage(ImageWithEntity imageView){
        applyLeftClickHandler(imageView);
        applyDragHandler(imageView);
        applyRightClickHandler(imageView);
        myStackPane.getChildren().add(imageView);
        myObjectManager.getTypeMap().addListener(new MapChangeListener<AuthoringEntity, String>() {
            @Override
            public void onChanged(Change<? extends AuthoringEntity, ? extends String> change) {
                if(change.wasRemoved()){
                    if(imageView.getAuthoringEntity().getPropertyMap().get(EntityField.LABEL).equals(change.getKey().getPropertyMap().get(EntityField.LABEL))){
                        myStackPane.getChildren().remove(imageView);
                    }

                }
            }
        });
    }

    private void applyRightClickHandler(ImageWithEntity imageView) {
        ContextMenu contextMenu = new ContextMenu();
        String[] reflectionInfo = RESOURCES.getString("ContextMenu").split(";");
        for(String itemInfo : reflectionInfo){
            String text = itemInfo.split(",")[0];
            String methodName = itemInfo.split(",")[1];
            MenuItem menuItem = new MenuItem();
            menuItem.setText(text);
            //TODO: replace with Duvall's Utility reflection
            try {
                Method method = this.getClass().getDeclaredMethod(methodName, ImageWithEntity.class);
                menuItem.setOnAction(actionEvent -> {
                    try {
                        method.invoke(this, imageView);
                    } catch (IllegalAccessException | InvocationTargetException e) {
                        ErrorBox error = new ErrorBox("Viewer Error", "Error applying right click handler");
                        error.display();
                    }
                });
            } catch (NoSuchMethodException e) {
                //TODO
            }
            contextMenu.getItems().add(menuItem);
        }
        imageView.setOnContextMenuRequested(contextMenuEvent -> contextMenu.show(imageView, contextMenuEvent.getScreenX(), contextMenuEvent.getScreenY()));

    }

    private void handleToFront(ImageWithEntity imageWithEntity){
        imageWithEntity.toFront();
    }

    private void handleToBack(ImageWithEntity imageWithEntity){
        imageWithEntity.toBack();
        updateGridLines();
    }

    private void handleDelete(ImageWithEntity imageWithEntity){
        myStackPane.getChildren().remove(imageWithEntity);
        myObjectManager.removeEntityInstance(imageWithEntity.getAuthoringEntity());
    }

    private void applyDragHandler(ImageWithEntity imageWithEntity) {
        imageWithEntity.setOnDragDetected(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                Utility.setupDragAndDropImage(imageWithEntity);
                isDragOnView = true;
                myDraggedAuthoringEntity = imageWithEntity.getAuthoringEntity();
            }
        });
    }

    private void applyLeftClickHandler(ImageWithEntity imageView) {
        imageView.setOnMouseClicked(mouseEvent -> mySelectedEntityProperty.setValue(imageView.getAuthoringEntity()));
    }

    private void setRoomSize(){
        myRoomHeight = Double.parseDouble(myAuthoringLevel.getPropertyMap().get(LevelField.HEIGHT));
        myRoomWidth = Double.parseDouble(myAuthoringLevel.getPropertyMap().get(LevelField.WIDTH));
        this.setPrefHeight(Integer.MAX_VALUE);
        this.setPrefWidth(Integer.MAX_VALUE);
        myStackPane.setMinWidth(myRoomWidth);
        myStackPane.setMinHeight(myRoomHeight);
    }

    private void updateGridLines(){
        myStackPane.getChildren().remove(myLinesPane);
        myLinesPane.getChildren().clear();
        myLinesPane.setMaxSize(myRoomWidth, myRoomHeight);
        myLinesPane.setMinSize(myRoomWidth, myRoomHeight);
        addHorizontalLines();
        addVerticalLines();
        myStackPane.getChildren().add(myLinesPane);
        myLinesPane.toBack();
    }

    private void addHorizontalLines() {
        int x1 = 0;
        for(int k = 0; k < myRoomHeight/CELL_SIZE; k++){
            int y = k * CELL_SIZE;
            Line tempLine = new Line(x1, y, myRoomWidth, y);
            myLinesPane.getChildren().add(tempLine);
        }
    }

    private void addVerticalLines(){
        int y1 = 0;
        for(int k = 0; k < myRoomWidth/CELL_SIZE; k++){
            int x = k * CELL_SIZE;
            Line tempLine = new Line(x, y1, x, myRoomHeight);
            myLinesPane.getChildren().add(tempLine);
        }
    }

}
