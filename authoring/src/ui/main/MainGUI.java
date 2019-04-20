package ui.main;

import data.external.DataManager;
import factory.GameTranslator;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ObservableStringValue;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;
import runner.external.Game;
import runner.external.GameCenterData;
import ui.AuthoringLevel;
import ui.ErrorBox;
import ui.Propertable;
import ui.PropertableType;
import ui.UIException;
import ui.manager.GroupManager;
import ui.manager.InfoEditor;
import ui.manager.ObjectManager;
import ui.panes.DefaultTypesPane;
import ui.panes.LevelsPane;
import ui.panes.PropertiesPane;
import ui.panes.UserCreatedTypesPane;
import ui.panes.Viewer;

import java.util.HashMap;
import java.util.Map;
import java.util.ResourceBundle;

/**
 * @author Harry Ross
 */
public class MainGUI {

    private Game myGame;
    private GameCenterData myGameData;
    private Stage myStage;
    private HBox myViewerBox;
    private ObjectManager myObjectManager;
    private Map<Propertable, Viewer> myViewers;
    private ObservableStringValue myCurrentStyle;
    private ObjectProperty<Propertable> mySelectedEntity;
    private ObjectProperty<Propertable> myCurrentLevel;

    private static final double STAGE_MIN_HEIGHT = 600;
    private static final double PROP_PANE_HEIGHT = 210;
    private static final String DEFAULT_STYLESHEET = "default.css";
    private static final String MENU_ITEMS_FILE = "main_menu_items";
    private static final String STAGE_TITLE = "ByteMe Authoring Environment";

    public MainGUI() { // Default constructor for creating a new game from scratch
        myGame = new Game();
        myGameData = new GameCenterData();
        myStage = new Stage();
        myViewers = new HashMap<>();
        defaultGameData();

        AuthoringLevel blankLevel = new AuthoringLevel("Level_1");
        AuthoringLevel blankLevel2 = new AuthoringLevel("Level 2"); //TODO
        myCurrentLevel = new SimpleObjectProperty<>(blankLevel);
        myObjectManager = new ObjectManager(myCurrentLevel);
        myObjectManager.addLevel(blankLevel);
        myObjectManager.addLevel(blankLevel2); //TODO
        myViewers.put(blankLevel, null);
        myViewers.put(blankLevel2, null);
        mySelectedEntity = new SimpleObjectProperty<>(null);
        myCurrentStyle = new SimpleStringProperty(DEFAULT_STYLESHEET);
        myCurrentStyle.addListener((change, oldVal, newVal) -> swapStylesheet(oldVal, newVal));
        myCurrentLevel.addListener((change, oldVal, newVal) -> swapViewer(oldVal, newVal));
    }

    public MainGUI(Game game, GameCenterData gameData) {
        this();
        myGame = game;
        myGameData = gameData;
    }

    public void launch() {
        myStage.setTitle(STAGE_TITLE);
        myStage.setScene(createMainGUI());
        myStage.setMinHeight(STAGE_MIN_HEIGHT);
        myStage.show();
        myStage.setMinWidth(myStage.getWidth());
    }

    private Scene createMainGUI() { //TODO clean up
        BorderPane mainBorderPane = new BorderPane();
        Scene mainScene = new Scene(mainBorderPane);
        HBox propPaneBox = new HBox();
        propPaneBox.getStyleClass().add("prop-pane-box");
        HBox entityPaneBox = new HBox();
        entityPaneBox.getStyleClass().add("entity-pane-box");

        UserCreatedTypesPane userCreatedTypesPane = createTypePanes(entityPaneBox, mainScene);
        createViewersForExistingLevels(userCreatedTypesPane);

        createPropertiesPanes(propPaneBox, mainScene);
        myViewerBox = new HBox(myViewers.get(myCurrentLevel.getValue()));
        myViewerBox.prefHeightProperty().bind(mainScene.heightProperty());
        myViewerBox.prefWidthProperty().bind(mainScene.widthProperty());

        mainBorderPane.setCenter(myViewerBox);
        mainBorderPane.setRight(entityPaneBox);
        mainBorderPane.setTop(addMenu());
        mainBorderPane.setBottom(propPaneBox);

        mainScene.getStylesheets().add(myCurrentStyle.getValue());
        mainBorderPane.getCenter().getStyleClass().add("main-center-pane");
        return mainScene;
    }

    private void createViewersForExistingLevels(UserCreatedTypesPane userCreatedTypesPane) {
        for (AuthoringLevel level : myObjectManager.getLevels()) {
            myViewers.put(level, createViewer(level, userCreatedTypesPane));
        }
    }

    private UserCreatedTypesPane createTypePanes(HBox entityPaneBox, Scene mainScene) {
        UserCreatedTypesPane userCreatedTypesPane = new UserCreatedTypesPane(myObjectManager);
        DefaultTypesPane defaultTypesPane = new DefaultTypesPane(userCreatedTypesPane);
        entityPaneBox.getChildren().addAll(defaultTypesPane, userCreatedTypesPane);
        entityPaneBox.prefHeightProperty().bind(mainScene.heightProperty().subtract(PROP_PANE_HEIGHT));
        userCreatedTypesPane.prefHeightProperty().bind(entityPaneBox.heightProperty());
        defaultTypesPane.prefHeightProperty().bind(entityPaneBox.heightProperty());
        return userCreatedTypesPane;
    }

    private Viewer createViewer(AuthoringLevel levelBasis, UserCreatedTypesPane userCreatedTypesPane) {
        return new Viewer(levelBasis, userCreatedTypesPane, mySelectedEntity, myObjectManager); //TODO change to take AuthoringLevel instead of prop
    }

    @SuppressWarnings("Duplicates")
    private void createPropertiesPanes(HBox propPaneBox, Scene mainScene) {
        try {
            LevelsPane levelsPane = new LevelsPane(myObjectManager, myCurrentLevel);
            PropertiesPane objectProperties =
                    new PropertiesPane(myObjectManager, PropertableType.OBJECT, mySelectedEntity, myObjectManager.getLabelManager());
            PropertiesPane levelProperties =
                    new PropertiesPane(myObjectManager, PropertableType.LEVEL, myCurrentLevel, myObjectManager.getLabelManager());
            PropertiesPane instanceProperties =
                    new PropertiesPane(myObjectManager, PropertableType.INSTANCE, mySelectedEntity, myObjectManager.getLabelManager());
            levelsPane.prefWidthProperty().bind(mainScene.widthProperty().divide(4));
            objectProperties.prefWidthProperty().bind(mainScene.widthProperty().divide(4));
            levelProperties.prefWidthProperty().bind(mainScene.widthProperty().divide(4));
            instanceProperties.prefWidthProperty().bind(mainScene.widthProperty().divide(4));
            propPaneBox.getChildren().addAll(levelsPane, levelProperties, instanceProperties, objectProperties);
        } catch (UIException e) {
            ErrorBox errorbox = new ErrorBox("Properties Error", e.getMessage());
            errorbox.display();
        }
    }

    private MenuBar addMenu() {
        MenuBar menuBar = new MenuBar();
        menuBar.getMenus().addAll(createMenu("File", "New", "Open", "Save"), //TODO make this better
                createMenu("Edit", "Info", "Groups", "Preferences"), createMenu("View", "Fullscreen"));
        return menuBar;
    }

    private Menu createMenu(String label, String... options) {
        Menu newMenu = new Menu(label);
        ResourceBundle bundle = ResourceBundle.getBundle(MENU_ITEMS_FILE);
        for (String option : options) {
            newMenu.getItems().add(makeMenuItem(option, event -> {
                try {
                    this.getClass().getDeclaredMethod((String) bundle.getObject(option)).invoke(this);
                } catch (Exception e) {
                    // catch FIX
                }
            }));
        }
        return newMenu;
    }

    private MenuItem makeMenuItem(String label, EventHandler<ActionEvent> handler) {
        MenuItem newItem = new MenuItem(label);
        newItem.setOnAction(handler);
        return newItem;
    }

    @SuppressWarnings("unused")
    private void newGame() {
        MainGUI newWorkspace = new MainGUI();
        newWorkspace.launch();
    }

    @SuppressWarnings("unused")
    private void openGame() {
        System.out.println("Open"); //TODO
    }

    @SuppressWarnings("unused")
    private void saveGame() {
        GameTranslator translator = new GameTranslator(myGame, myGameData, myObjectManager);
        Game exportableGame = translator.translate();
        GameCenterData gameData = translator.getNewGameData();

        DataManager dm = new DataManager();
        dm.createGameFolder(gameData.getFolderName());
        dm.saveGameData(gameData.getFolderName(), exportableGame);
        dm.saveGameInfo(gameData.getFolderName(), gameData);
    }

    @SuppressWarnings("unused")
    private void openGroupManager() {
        GroupManager groupManager = new GroupManager(myObjectManager);
        groupManager.showAndWait();
    }

    @SuppressWarnings("unused")
    private void openGameInfo() {
        InfoEditor infoEditor = new InfoEditor(myGameData);
        infoEditor.showAndWait();
    }

    @SuppressWarnings("unused")
    private void openPreferences() {
        System.out.println("Preferences"); //TODO
    }

    @SuppressWarnings("unused")
    private void toggleFullscreen() {
        myStage.setFullScreen(!myStage.isFullScreen());
    }

    private void swapViewer(Propertable oldLevel, Propertable newLevel) {
        myViewerBox.getChildren().remove(myViewers.get(oldLevel));
        myViewerBox.getChildren().add(myViewers.get(newLevel));
    }

    private void swapStylesheet(String oldVal, String newVal) {
        myStage.getScene().getStylesheets().remove(oldVal);
        myStage.getScene().getStylesheets().add(newVal);
    }

    private void defaultGameData() {
        myGameData.setFolderName("NewGame");
        myGameData.setImageLocation("");
        myGameData.setTitle("New Game");
        myGameData.setDescription("A fun new game");
    }

}