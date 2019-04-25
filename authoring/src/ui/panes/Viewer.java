package ui.panes;

import engine.external.Entity;
import engine.external.component.SpriteComponent;
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
import ui.LevelField;
import ui.Propertable;
import ui.Utility;
import ui.manager.ObjectManager;

import java.io.FileInputStream;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Comparator;
import java.util.List;
import java.util.ResourceBundle;


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
     * @param authoringLevel
     * @param userCreatedTypesPane
     * @param objectProperty
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
    }

    private void addAllExistingEntities(AuthoringLevel authoringLevel) {
        List<AuthoringEntity> authoringEntityList = authoringLevel.getEntities();
        authoringEntityList.sort(new Comparator<AuthoringEntity>() {
            @Override
            public int compare(AuthoringEntity o1, AuthoringEntity o2) {
                int firstZ = Integer.parseInt(o1.getPropertyMap().get(EntityField.Z));
                int secondZ = Integer.parseInt(o2.getPropertyMap().get(EntityField.Z));
                return firstZ - secondZ;
            }
        });

        for(AuthoringEntity authoringEntity : authoringEntityList){
            String imagePath = GENERAL_RESOURCES.getString("images_filepath/") + authoringEntity.getPropertyMap().get(EntityField.IMAGE);
            FileInputStream fileInputStream = Utility.makeFileInputStream(imagePath);
            ImageWithEntity imageWithEntity = new ImageWithEntity(fileInputStream, authoringEntity);
            myStackPane.getChildren().add(imageWithEntity);
        }
    }

    private void initializeAndFormatVariables() {
        myStackPane = new StackPane();
        myLinesPane = new Pane();
        myBackgroundFileName = null;
        myStackPane.getChildren().addListener((ListChangeListener<Node>) change -> updateZField());
        myStackPane.getChildren().add(myLinesPane);
        System.out.println("List size with just lines: " + myStackPane.getChildren().size());
        myStackPane.setAlignment(Pos.TOP_LEFT);
        this.setContent(myStackPane);
        this.getStyleClass().add(SHEET);
    }

    private void updateZField() {
        int objectCount = 0;
        for(Node node : myStackPane.getChildren()){
            if(node instanceof ImageWithEntity){
                System.out.println("*************");
                AuthoringEntity authoringEntity = ((ImageWithEntity) node).getAuthoringEntity();
                authoringEntity.getPropertyMap().put(EntityField.Z, Integer.toString(objectCount));
                System.out.println("Label: " + authoringEntity.getPropertyMap().get(EntityField.LABEL) + "\t Index: " + authoringEntity.getPropertyMap().get(EntityField.Z));
                System.out.println("****************");
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
            String filepath = GENERAL_RESOURCES.getString("images_filepath") + filename;
            FileInputStream fileInputStream = Utility.makeFileInputStream(filepath);
            Image image = new Image(fileInputStream, myRoomWidth, myRoomHeight, false, false);
            BackgroundImage backgroundImage = new BackgroundImage(image, BackgroundRepeat.NO_REPEAT, BackgroundRepeat.NO_REPEAT, null, null);
            myStackPane.setBackground(new Background(backgroundImage));
            myBackgroundFileName = filename;
        }
    }

    private void setupAcceptDragEvents() {
        myStackPane.setOnDragOver(dragEvent -> dragEvent.acceptTransferModes(TransferMode.ANY));
    }

    private void setupDragDropped() {
        myStackPane.setOnDragDropped(dragEvent -> {
            AuthoringEntity authoringEntity;
            if(isDragOnView){
                authoringEntity = myDraggedAuthoringEntity;
                isDragOnView = false;
            }
            else{
                authoringEntity = myUserCreatedPane.getDraggedAuthoringEntity();
                String imageName = authoringEntity.getPropertyMap().get(EntityField.IMAGE);
                authoringEntity = new AuthoringEntity(authoringEntity);
                authoringEntity.getPropertyMap().put(EntityField.IMAGE, imageName);
                addImage(Utility.createImageWithEntity(authoringEntity));
            }
            authoringEntity.getPropertyMap().put(EntityField.X, "" + snapToGrid(dragEvent.getX()));
            authoringEntity.getPropertyMap().put(EntityField.Y, "" + snapToGrid(dragEvent.getY()));
            mySelectedEntityProperty.setValue(authoringEntity);
        });
    }

    /**
     * Snaps the point to the closest gridline
     * @param value int to be snapped
     * @return int that is snapped
     */
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
                    } catch (IllegalAccessException e) {
                        e.printStackTrace();
                    } catch (InvocationTargetException e) {
                        e.printStackTrace();
                    }
                });
            } catch (NoSuchMethodException e) {
                e.printStackTrace();
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

    private void applyDragHandler(ImageWithEntity imageView) {
        imageView.setOnDragDetected(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                Utility.setupDragAndDropImage(imageView);
                isDragOnView = true;
                myDraggedAuthoringEntity = imageView.getAuthoringEntity();
            }
        });
    }

    private void applyLeftClickHandler(ImageWithEntity imageView) {
        imageView.setOnMouseClicked(mouseEvent -> mySelectedEntityProperty.setValue(imageView.getAuthoringEntity()));
    }

    private void setRoomSize(){
        myRoomHeight = Double.parseDouble(myAuthoringLevel.getPropertyMap().get(LevelField.HEIGHT));
        myRoomWidth = Double.parseDouble(myAuthoringLevel.getPropertyMap().get(LevelField.WIDTH));
        this.setPrefHeight(myRoomHeight);
        this.setPrefWidth(myRoomWidth);
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
