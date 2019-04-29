package pane;

import controls.InformativeField;
import controls.LauncherSymbol;
import controls.TitleLabel;
import data.external.DataManager;
import data.external.GameCenterData;
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
import popup.ErrorPopUp;
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
    private static final String INNER_BOX_STYLE = "inner-hbox";
    private static final String LARGER_STYLE = "bigger-title";
    private static final String IMAGE_PREFIX = "byteme_default_launcher_gameIcon_";
    private String myUserName;
    /**
     * This page will prompt the user to enter information about the new game they wish to create, such as its cover image,
     * title, and description. Then, it will create a new GameDisplay object to be passed to the authoring environment so they
     * can create a folder for the user to put other necessary files in, such as images they want to use in the creation
     * of their game
     * @author Anna Darwish
     */
    public CreateGameDisplay(SwitchToAuthoring goToAuthoring, String userName){
        this.getStyleClass().add(AUTHORING_STYLE);
        myUserName = userName;
        setUpImages(goToAuthoring,userName);


    }
    private void setUpImages(SwitchToAuthoring sceneSwitch,String userName){
        HBox labels = new HBox();
        labels.getStyleClass().add(LARGER_STYLE);
        Label newGame = new TitleLabel(NEW);
        Label modifyGame = new TitleLabel(MODIFY);
        labels.getChildren().add(newGame);
        labels.getChildren().add(modifyGame);

        AnchorPane.setTopAnchor(labels,20.0);
        AnchorPane.setLeftAnchor(labels,30.0);
        this.getChildren().add(labels);
        this.getChildren().add(emptyBox);
        makeNewGamePreferences();
        makeModifyGamePreferences(userName);
        newGame.setOnMouseClicked(mouseEvent -> setMiddlePreferences(newGamePreferences));
        modifyGame.setOnMouseClicked(mouseEvent -> setMiddlePreferences(modifyGamePreferences));

    }

    private void setMiddlePreferences(VBox gameCreationType){
        setBottomAnchor(gameCreationType,70.0);
        this.getChildren().remove(1);
        this.getChildren().add(1,gameCreationType);
    }

    private void makeNewGamePreferences(){
        LauncherControlDisplay myCreator = new LauncherControlDisplay(FOLDER_KEY);
        HBox myGameInfo = new HBox();
        myGameInfo.getStyleClass().add(INNER_BOX_STYLE);
        myCreator.setOnMouseClicked(mouseEvent -> {
            FileChooser fileChooser = new FileChooser();
            fileChooser.setInitialDirectory(new File(System.getProperty("user.home")));
            myFile =  fileChooser.showOpenDialog(new Stage());
        });
        LauncherSymbol mySymbol = new LauncherSymbol(CREATE_LAUNCHER);
        mySymbol.setOnMouseClicked(mouseEvent -> enterAuthoringToMakeNewGame());
        myGameInfo.getChildren().add(myCreator);
        myGameInfo.getChildren().add(gameName);
        myGameInfo.getChildren().add(gameDescription);
        newGamePreferences.getChildren().add(myGameInfo);
        AnchorPane.setLeftAnchor(newGamePreferences,100.0 - myGameInfo.getWidth()/2.0);
        newGamePreferences.getChildren().add(mySymbol);

    }
    //game name, game description, image,
    private void enterAuthoringToMakeNewGame(){
        DataManager dataManager = new DataManager();
        String imageFileName = IMAGE_PREFIX + "_" + gameName.getTextEntered() + "_" + myUserName +"_"+ myFile.getName();
        dataManager.saveImage(imageFileName,myFile);
        //GameCenterData myData = new GameCenterData(gameName.getTextEntered(),gameDescription.getTextEntered(),imageFileName,myUserName);

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
        ChoiceBox<String> gameNames = new ChoiceBox<>();
        gameNames.setItems(FXCollections.observableList(gameNamesList));
        LauncherSymbol mySymbol = new LauncherSymbol(CREATE_LAUNCHER);
        mySymbol.setOnMouseClicked(mouseEvent -> enterAuthoringToModifyOldGame(gameNames.getValue()));
        modifyGamePreferences.getChildren().add(gameNames);
        modifyGamePreferences.getChildren().add(mySymbol);
        AnchorPane.setLeftAnchor(modifyGamePreferences,300.0 - modifyGamePreferences.getWidth()/2.0);
        modifyGamePreferences.getStyleClass().add(INNER_BOX_STYLE);
    }

    private void enterAuthoringToModifyOldGame(String gameName){

    }


    private void makeGameInfo(InformativeField field, int buttonWidth){
        field.setMaxWidth(buttonWidth);
        field.setAlignment(Pos.CENTER);
    }

}
