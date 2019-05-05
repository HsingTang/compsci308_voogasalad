package pane;

import controls.InformativeField;
import controls.LauncherSymbol;
import controls.TitleLabel;
import data.external.DataManager;
import data.external.GameCenterData;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.geometry.Pos;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import manager.SwitchToAuthoring;
import manager.SwitchToNewGameAuthoring;
import manager.SwitchToUserOptions;
import popup.ErrorPopUp;
import runner.external.Game;

import java.io.File;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class CreateGameDisplay extends AnchorPane {
    private static final String FOLDER_KEY = "user_files";
    private static final String CREATE_LAUNCHER = "create";
    private static final String GAME_FIELD = "Enter Game Name";
    private static final String GAME_DESCRIPITON = "Enter Game Description";
    private static final String NEW = "new";
    private static final String MODIFY = "modify";


    private File myFile;
    private InformativeField gameName = new InformativeField(GAME_FIELD);
    private InformativeField gameDescription = new InformativeField(GAME_DESCRIPITON);
    private static final String DATABASE_ERROR = "database_error";

    private VBox emptyBox = new VBox();
    private VBox newGamePreferences = new VBox();
    private VBox modifyGamePreferences = new VBox();
    private static final String AUTHORING_STYLE = "authoring-page";
    private static final String CHOICEBOX_DISPLAY = "game-display";
    private static final String INNER_BOX_STYLE = "inner-hbox";
    private static final String LARGER_STYLE = "bigger-title";
    private static final String IMAGE_PREFIX = "byteme_default_launcher_gameIcon_";
    private static final String CORRUPT = "corrupt_game";
    private static final String FILE_QUERY_PATH = "user.home";
    private static final String DATABASE_DELIMITER = "_";

    private String myUserName;
    private SwitchToNewGameAuthoring openNewGame;
    private SwitchToAuthoring openOldGame;
    private ChoiceBox<String> gameNames = new ChoiceBox<>();

    private static final double TOP_ANCHOR = 20;
    private static final double LEFT_ANCHOR = 30;
    private static final double BOTTOM_ANCHOR = 70;
    private static final double RIGHT_ANCHOR = 100;
    private static final double ANCHOR_OFFSET = 270;
    /**
     * This page switches between prompting the user to enter in information for a new game and then entering the authoring
     * environment to design a game from scratch, or selecting from a drop down menu of games they have previously
     * created to edit in the authoring environment
     * of their game
     * @author Anna Darwish
     */
    public CreateGameDisplay(SwitchToAuthoring goToOldAuthoring, SwitchToNewGameAuthoring goToNewGame, String userName){
        this.getStyleClass().add(AUTHORING_STYLE);
        this.getStyleClass().add(CHOICEBOX_DISPLAY);
        openOldGame = goToOldAuthoring;
        openNewGame = goToNewGame;
        myUserName = userName;
        setUpImages(userName);


    }
    private void setUpImages(String userName){
        HBox labels = new HBox();
        labels.getStyleClass().add(LARGER_STYLE);
        Label newGame = new TitleLabel(NEW);
        Label modifyGame = new TitleLabel(MODIFY);
        labels.getChildren().add(newGame);
        labels.getChildren().add(modifyGame);

        AnchorPane.setTopAnchor(labels,TOP_ANCHOR);
        AnchorPane.setLeftAnchor(labels,LEFT_ANCHOR);
        this.getChildren().add(labels);
        this.getChildren().add(emptyBox);
        makeNewGamePreferences();
        makeModifyGamePreferences(userName);
        newGame.setOnMouseClicked(mouseEvent -> setMiddlePreferences(newGamePreferences));
        modifyGame.setOnMouseClicked(mouseEvent -> setMiddlePreferences(modifyGamePreferences));

    }

    private void setMiddlePreferences(VBox gameCreationType){
        setBottomAnchor(gameCreationType,BOTTOM_ANCHOR);
        this.getChildren().remove(1);
        this.getChildren().add(1,gameCreationType);
    }

    private void makeNewGamePreferences(){
        LauncherControlDisplay myCreator = new LauncherControlDisplay(FOLDER_KEY);
        HBox myGameInfo = new HBox();

        myGameInfo.getStyleClass().add(INNER_BOX_STYLE);
        myCreator.setOnMouseClicked(mouseEvent -> {
            FileChooser fileChooser = new FileChooser();
            fileChooser.setInitialDirectory(new File(System.getProperty(FILE_QUERY_PATH)));
            myFile =  fileChooser.showOpenDialog(new Stage());
        });
        LauncherSymbol mySymbol = new LauncherSymbol(CREATE_LAUNCHER);
        mySymbol.setOnMouseClicked(mouseEvent -> enterAuthoringToMakeNewGame());
        myGameInfo.getChildren().add(myCreator);
        myGameInfo.getChildren().add(gameName);
        myGameInfo.getChildren().add(gameDescription);
        newGamePreferences.getChildren().add(myGameInfo);
        newGamePreferences.getChildren().add(mySymbol);
        newGamePreferences.getStyleClass().add(INNER_BOX_STYLE);
        AnchorPane.setLeftAnchor(newGamePreferences,RIGHT_ANCHOR - myGameInfo.getWidth()/2.0);

    }
    //game name, game description, image,
    private void enterAuthoringToMakeNewGame(){
        DataManager dataManager = new DataManager();
        String imageFileName = IMAGE_PREFIX + gameName.getTextEntered() + DATABASE_DELIMITER + myUserName +DATABASE_DELIMITER+ myFile.getName();
        dataManager.saveImage(imageFileName,myFile);
        GameCenterData myData = new GameCenterData(gameName.getTextEntered(),gameDescription.getTextEntered(),imageFileName,myUserName);
        openNewGame.switchScene(myData);

    }

    private void makeModifyGamePreferences(String userName){
        List<String> gameNamesList = new ArrayList<>();
        try {
            DataManager myManager = new DataManager();
            gameNamesList = myManager.loadUserGameNames(userName);
        }
        catch (SQLException e){
            ErrorPopUp dataBaseError = new ErrorPopUp(DATABASE_ERROR);
            dataBaseError.display();
        }
        gameNames.setItems(FXCollections.observableList(gameNamesList));
        gameNames.getSelectionModel().selectedItemProperty().addListener((observableValue, s, t1) -> gameNames.setAccessibleText(t1));
        LauncherSymbol mySymbol = new LauncherSymbol(CREATE_LAUNCHER);
        mySymbol.setOnMouseClicked(mouseEvent -> enterAuthoringToModifyOldGame());
        modifyGamePreferences.getStyleClass().add(CHOICEBOX_DISPLAY);
        modifyGamePreferences.getChildren().add(gameNames);
        modifyGamePreferences.getChildren().add(mySymbol);
        AnchorPane.setLeftAnchor(modifyGamePreferences,ANCHOR_OFFSET - modifyGamePreferences.getWidth()/2.0);
        setTopAnchor(gameNames,TOP_ANCHOR);
        setLeftAnchor(gameNames,ANCHOR_OFFSET-modifyGamePreferences.getWidth()/2.0);
        modifyGamePreferences.getStyleClass().add(INNER_BOX_STYLE);
    }

    private void enterAuthoringToModifyOldGame(){
        DataManager dataManager = new DataManager();
        try {
            String gameName = gameNames.getAccessibleText();
            Game gameObject = (Game)dataManager.loadGameData(gameName, myUserName);
            GameCenterData myData = dataManager.loadGameInfo(gameName,myUserName);
            openOldGame.switchScene(gameObject, myData);
        }
        catch (Exception e){
            ErrorPopUp invalidGameSelection = new ErrorPopUp(CORRUPT);
            invalidGameSelection.display();
        }
    }


}
