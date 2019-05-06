package ui.main;

import data.external.DataManager;
import data.external.DatabaseEngine;
import data.external.GameCenterData;
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
import ui.AuthoringLevel;
import ui.ErrorBox;
import ui.Propertable;
import ui.PropertableType;
import ui.UIException;
import ui.windows.GroupEditor;
import ui.windows.InfoEditor;
import ui.manager.ObjectManager;
import ui.panes.DefaultTypesPane;
import ui.panes.LevelsPane;
import ui.panes.PropertiesPane;
import ui.panes.UserCreatedTypesPane;
import ui.panes.Viewer;
import ui.windows.AudioManager;
import ui.windows.ImageManager;
import ui.windows.LoadGameSelector;
import voogasalad.util.reflection.Reflection;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;

/**
 * Main class for ByteMe Authoring Environment, loads GUI and respective backend for a loaded Game
 * @author Harry Ross
 * @author Carrie Hunner
 */
public class MainGUI {

    private Game myLoadedGame;
    private GameCenterData myGameData;
    private Stage myStage;
    private DataManager myDataManager;
    private HBox myViewerBox;
    private UserCreatedTypesPane myCreatedTypesPane;
    private ObjectManager myObjectManager;
    private Map<Propertable, Viewer> myViewers;
    private ObservableStringValue myCurrentStyle;
    private ObjectProperty<Propertable> mySelectedEntity;
    private ObjectProperty<Propertable> myCurrentLevel;
    private Scene myScene;

    private static final double STAGE_MIN_HEIGHT = 600;
    private static final double PROP_PANE_HEIGHT = 210;
    private static final String DEFAULT_FIRST_LEVEL = "New_Level_1";
    private static final String DEFAULT_STYLESHEET = "default.css";
    private static final String MENU_ITEMS_FILE = "main_menu_items";
    private static final String STAGE_TITLE = "ByteMe Authoring Environment";
    private static final ResourceBundle GENERAL_RESOURCES = ResourceBundle.getBundle("authoring_general");
    private static final ResourceBundle SAVING_ASSETS_RESOURCES = ResourceBundle.getBundle("mainGUI_assets");

    /**
     * Creates new MainGUI for a game being started from scratch with given metadata
     * @param data Metadata for new game
     */
    public MainGUI(GameCenterData data) { // Default constructor for creating a new game from scratch
        myLoadedGame = new Game();
        myGameData = data;
        defaultGameData();
        myStage = new Stage();
        myDataManager = new DataManager();
        myViewers = new HashMap<>();

        myCurrentLevel = new SimpleObjectProperty<>();
        mySelectedEntity = new SimpleObjectProperty<>();
        myObjectManager = new ObjectManager(myCurrentLevel);
        loadAllAssets();
        AuthoringLevel blankLevel = new AuthoringLevel(DEFAULT_FIRST_LEVEL, myObjectManager);
        myObjectManager.addLevel(blankLevel);
        myCurrentLevel.setValue(blankLevel);

        myCurrentStyle = new SimpleStringProperty(DEFAULT_STYLESHEET);
        myCurrentStyle.addListener((change, oldVal, newVal) -> swapStylesheet(oldVal, newVal));
        myCurrentLevel.addListener((change, oldVal, newVal) -> swapViewer(oldVal, newVal));
        myObjectManager.setGameCenterData(myGameData);
    }

    /**
     * Creates new MainGUI for an already existing Game
     * @param game Game to load
     * @param gameData Corresponding metadata for loaded Game
     */
    public MainGUI(Game game, GameCenterData gameData) {
        this(gameData);
        myLoadedGame = game;
        loadDatabaseGame();
    }

    public void launch(boolean load) {
        createMainGUI(load);
        myStage.setTitle(STAGE_TITLE);
        myStage.setScene(myScene);
        myStage.setMinHeight(STAGE_MIN_HEIGHT);
        myStage.show();
        myStage.setMinWidth(myStage.getWidth());
    }

    private void createMainGUI(boolean load) {
        BorderPane mainBorderPane = new BorderPane();
        Scene mainScene = new Scene(mainBorderPane);
        HBox propPaneBox = new HBox();
        propPaneBox.getStyleClass().add("prop-pane-box");
        HBox entityPaneBox = new HBox();
        entityPaneBox.getStyleClass().add("entity-pane-box");
        myCreatedTypesPane = createTypePanes(entityPaneBox, mainScene, load);
        createViewersForExistingLevels();
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
        myScene = mainScene;
    }

    private void createViewersForExistingLevels() {
        for (AuthoringLevel level : myObjectManager.getLevels()) {
            myViewers.put(level, createViewer(level));
        }
    }

    private UserCreatedTypesPane createTypePanes(HBox entityPaneBox, Scene mainScene, boolean load) {
        UserCreatedTypesPane userCreatedTypesPane;
        if (load)
            userCreatedTypesPane = new UserCreatedTypesPane(myObjectManager, myLoadedGame.getUserCreatedTypes());
        else {
            userCreatedTypesPane = new UserCreatedTypesPane(myObjectManager);
        }
        DefaultTypesPane defaultTypesPane = new DefaultTypesPane(userCreatedTypesPane, myObjectManager);
        entityPaneBox.getChildren().addAll(defaultTypesPane, userCreatedTypesPane);
        entityPaneBox.prefHeightProperty().bind(mainScene.heightProperty().subtract(PROP_PANE_HEIGHT));
        userCreatedTypesPane.prefHeightProperty().bind(entityPaneBox.heightProperty());
        defaultTypesPane.prefHeightProperty().bind(entityPaneBox.heightProperty());
        return userCreatedTypesPane;
    }

    private Viewer createViewer(AuthoringLevel levelBasis) {
        return new Viewer(levelBasis, myCreatedTypesPane, mySelectedEntity, myObjectManager);
    }

    private void createPropertiesPanes(HBox propPaneBox, Scene mainScene) {
        try {
            LevelsPane levelsPane = new LevelsPane(myObjectManager, myCurrentLevel);
            PropertiesPane objectProperties =
                    new PropertiesPane(myObjectManager, PropertableType.OBJECT, mySelectedEntity);
            PropertiesPane levelProperties =
                    new PropertiesPane(myObjectManager, PropertableType.LEVEL, myCurrentLevel);
            PropertiesPane instanceProperties =
                    new PropertiesPane(myObjectManager, PropertableType.INSTANCE, mySelectedEntity);
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
        menuBar.getMenus().addAll(createMenu("File", "New", "Open", "Save"),
                createMenu("Edit", "Info", "Groups", "Images", "Audio"), createMenu("View", "Fullscreen"));
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
                    ErrorBox error = new ErrorBox("Menu Reflection Error", "Error in reflected Menu method");
                    error.display();
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
        MainGUI newWorkspace = new MainGUI(new GameCenterData());
        newWorkspace.launch(false);
    }

    @SuppressWarnings("unused")
    private void openGame() {
        DataManager dataManager = new DataManager();
        try {
            List<String> gameNames = dataManager.loadUserGameNames(myGameData.getAuthorName());
            LoadGameSelector selector = new LoadGameSelector(gameNames);
            selector.showAndWait();

            if(selector.getSelectedGame() != null) {
                String selectedGameTitle = selector.getSelectedGame();
                Game loadedGame = (Game) dataManager.loadGameData(selectedGameTitle, myGameData.getAuthorName());
                GameCenterData loadedGameData = dataManager.loadGameInfo(selectedGameTitle, myGameData.getAuthorName());
                MainGUI newWorkspace = new MainGUI(loadedGame, loadedGameData);
                newWorkspace.launch(true);
            }
        } catch (SQLException e) {
            ErrorBox error = new ErrorBox("Load", "Error loading from database");
        }
    }

    private void loadDatabaseGame() {
        GameTranslator translator = new GameTranslator(myObjectManager);
        myObjectManager.removeAllLevels();
        try {
            translator.loadGame(myLoadedGame, myCurrentLevel);
        } catch (UIException e) {
            ErrorBox error = new ErrorBox("Load Error", e.getMessage());
            error.display();
        }
        myCurrentLevel.setValue(myObjectManager.getLevels().get(0));
        mySelectedEntity.setValue(myObjectManager.getLevels().get(0).getEntities().get(0));
        createMainGUI(true);
    }

    @SuppressWarnings("unused")
    private void saveGame() {
        GameTranslator translator = new GameTranslator(myObjectManager);
        try {
            Game exportableGame = translator.saveGame();
            myDataManager = new DataManager();
            myDataManager.saveGameData(myGameData.getTitle(), myGameData.getAuthorName(), exportableGame);
            myDataManager.saveGameInfo(myGameData.getTitle(), myGameData.getAuthorName(), myGameData);

            saveFolderToDataBase(GENERAL_RESOURCES.getString("images_filepath"));
            saveFolderToDataBase(GENERAL_RESOURCES.getString("audio_filepath"));

            DatabaseEngine.getInstance().open();
        } catch (UIException e) {
            ErrorBox errorBox = new ErrorBox("Save Error", e.getMessage());
            errorBox.showAndWait();
        }
    }

    @SuppressWarnings("unused")
    private void openGroupManager() {
        GroupEditor groupEditor = new GroupEditor(myObjectManager);
        groupEditor.showAndWait();
    }

    @SuppressWarnings("unused")
    private void openGameInfo() {
        InfoEditor infoEditor = new InfoEditor(myGameData);
        infoEditor.showAndWait();
    }

    @SuppressWarnings("unused")
    private void openImageAssets() {
        ImageManager manager = new ImageManager(myObjectManager);
        manager.show();
    }

    @SuppressWarnings("unused")
    private void openAudioAssets() {
        AudioManager manager = new AudioManager(myObjectManager);
        manager.show();
    }

    @SuppressWarnings("unused")
    private void toggleFullscreen() {
        myStage.setFullScreen(!myStage.isFullScreen());
    }

    private void swapViewer(Propertable oldLevel, Propertable newLevel) {
        if (!myViewers.isEmpty()) {
            myViewerBox.getChildren().remove(myViewers.get(oldLevel));
            if (!myViewers.containsKey(newLevel))
                myViewers.put(newLevel, createViewer((AuthoringLevel) newLevel));

            myViewerBox.getChildren().add(myViewers.get(newLevel));
        }
    }

    private void swapStylesheet(String oldVal, String newVal) {
        myStage.getScene().getStylesheets().remove(oldVal);
        myStage.getScene().getStylesheets().add(newVal);
    }

    private void defaultGameData() {
        if (myGameData.getTitle() == null)
            myGameData.setTitle("New Game");
        if (myGameData.getImageLocation() == null)
            myGameData.setImageLocation("");
        if (myGameData.getDescription() == null)
           myGameData.setDescription("A fun new game");
        if (myGameData.getAuthorName() == null)
           myGameData.setAuthorName("carrie");
    }

    private void saveFolderToDataBase(String outerDirectoryPath){
        File outerDirectory = new File(outerDirectoryPath);
        String methodName = SAVING_ASSETS_RESOURCES.getString(outerDirectory.getName());
        for(File file : outerDirectory.listFiles()){
            Reflection.callMethod(myDataManager, methodName, file.getName(), file);
        }
    }

    private void loadAllAssets(){
        String prefix = SAVING_ASSETS_RESOURCES.getString("userUploaded") + myGameData.getTitle() + myGameData.getAuthorName();
        try {
            Map<String, InputStream> defaultImages = myDataManager.loadAllImages(SAVING_ASSETS_RESOURCES.getString("defaults"));
            Map<String, InputStream> userUploadedImages = myDataManager.loadAllImages(prefix);
            Map<String, InputStream> defaultAudio = myDataManager.loadAllSounds(SAVING_ASSETS_RESOURCES.getString("defaults"));
            Map<String, InputStream> userUploadedAudio = myDataManager.loadAllSounds(prefix);
            loadAssets(GENERAL_RESOURCES.getString("images_filepath"), defaultImages);
            loadAssets(GENERAL_RESOURCES.getString("images_filepath"), userUploadedImages);
            loadAssets(GENERAL_RESOURCES.getString("audio_filepath"), defaultAudio);
            loadAssets((GENERAL_RESOURCES.getString("audio_filepath")), userUploadedAudio);
        } catch (SQLException e) {
            //TODO deal with this
        }
    }

    private void loadAssets(String folderFilePath, Map<String, InputStream> databaseInfo){
        try {
            for(Map.Entry<String, InputStream> entry : databaseInfo.entrySet()){
                InputStream inputStream = entry.getValue();
                File destination = new File(folderFilePath + entry.getKey());
                Files.copy(inputStream, destination.toPath(), StandardCopyOption.REPLACE_EXISTING);
                inputStream.close();
            }
        } catch (IOException e) {
            //TODO: handle error
        }
    }

    /**
     * This method takes in the path to the directory wished to be cleared and then
     * iterates through each file and deletes it
     * @param outerDirectoryPath
     */
    public void clearFolder(String outerDirectoryPath){
        DatabaseEngine.getInstance().close();
        File outerDirectory = new File(outerDirectoryPath);
        for(File file : outerDirectory.listFiles()){
            file.delete();
        }
        DatabaseEngine.getInstance().open();
    }
}
