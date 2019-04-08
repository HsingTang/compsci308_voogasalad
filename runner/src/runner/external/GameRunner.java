package runner.external;

import data.external.DataManager;
import engine.external.Entity;
import engine.external.Level;
import engine.external.component.PositionComponent;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.geometry.Point3D;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.util.Duration;
import runner.internal.TestEngine;

import java.util.*;

public class GameRunner {
    /**
     * This will be the primary class that creates a new game engine
     * and displays sprites on a stage
     */
    private Collection<Entity> myEntities;
    private int mySceneWidth;
    private int mySceneHeight;
    private Stage myStage;
    private Group myGroup;
    private Scene myScene;
    private TestEngine myTestEngine;
    private Timeline myAnimation;
    private static final int FRAMES_PER_SECOND = 60;
    private static final int MILLISECOND_DELAY = 1000 / FRAMES_PER_SECOND;
    private static final double SECOND_DELAY = 1.0 / FRAMES_PER_SECOND;
    private Map myEntitiesAndNodes;
    private List<Level> myLevels;
    private Game myGame;
    private Set<KeyCode> myCurrentKeys;


    public GameRunner(Game game) {
       /* Actual way to get game object
        GameRunner will have parameter String name, not Game game
        code below

        DataManager dm = new DataManager();
        Game myGame = (Game) dm.loadGameData(name); */

        myGame = game;
        myLevels = myGame.getLevels();
        myEntities = myLevels.get(0).getEntities();
        mySceneWidth = myGame.getWidth();
        mySceneHeight = myGame.getHeight();
        myStage = new Stage();
        myGroup = new Group();
        myScene = new Scene(myGroup, mySceneWidth, mySceneHeight);
        myScene.setFill(Color.BEIGE);
        myScene.setOnKeyPressed(e -> handleKeyPress(e.getCode()));
        myScene.setOnKeyReleased(e -> handleKeyRelease(e.getCode()));
        myEntitiesAndNodes = initializeMap();
        showEntities();
        myCurrentKeys = new HashSet<KeyCode>();
        myStage.setScene(myScene);
        myTestEngine = new TestEngine(myLevels.get(0));
        var frame = new KeyFrame(Duration.millis(MILLISECOND_DELAY), e -> step(SECOND_DELAY));
        myAnimation = new Timeline();
        myAnimation.setCycleCount(Timeline.INDEFINITE);
        myAnimation.getKeyFrames().add(frame);
        myAnimation.play();
        myStage.show();
    }

    private void handleKeyPress(KeyCode code) {
        myCurrentKeys.add(code);
    }

    private void handleKeyRelease(KeyCode code){
        myCurrentKeys.remove(code);
    }

    private HashMap<Entity, Node> initializeMap() {
        HashMap<Entity, Node> map = new HashMap<Entity, Node>();
        for(Entity entity : myEntities){
            PositionComponent positionComponent = (PositionComponent) entity.getComponent(PositionComponent.class);
            Point3D position = (Point3D) positionComponent.getValue();
            ImageView image = new ImageView("basketball.png");
            image.setFitWidth(50);
            image.setPreserveRatio(true);
            image.setSmooth(false);
            image.setLayoutX(position.getX());
            image.setLayoutY(position.getY());
            map.put(entity, image);
        }
        return map;
    }

    private void step (double elapsedTime) {
        myEntities = myTestEngine.updateState();
        updateMap();
        showEntities();
        printKeys();
    }

    private void printKeys() {
        System.out.println(myCurrentKeys);
    }

    private void updateMap(){
        for(Entity entity : myEntities){
            myEntitiesAndNodes.put(entity, updateNode(entity));
        }
    }

    private Node updateNode(Entity entity) {
        Node toUpdate = (Node) myEntitiesAndNodes.get(entity);
        PositionComponent positionComponent = (PositionComponent) entity.getComponent(PositionComponent.class);
        Point3D position = (Point3D) positionComponent.getValue();
        toUpdate.setLayoutX(position.getX());
        toUpdate.setLayoutY(position.getY());
        return toUpdate;
    }

    private void printEntityLocations(){
        for(Entity entity : myEntities){
            PositionComponent positionComponent = (PositionComponent) entity.getComponent(PositionComponent.class);
            Point3D position = (Point3D) positionComponent.getValue();
            System.out.println(position.getX());
        }
    }

    private void showEntities(){
        myGroup.getChildren().clear();
        for(Entity entity : myEntities){
            Node toAdd = (Node) myEntitiesAndNodes.get(entity);
            myGroup.getChildren().add((Node) myEntitiesAndNodes.get(entity));
        }
    }

}