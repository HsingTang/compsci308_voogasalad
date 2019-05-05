package runner.external;

import data.external.DataManager;
import engine.external.Entity;
import engine.external.Level;
import engine.external.component.LivesComponent;
import engine.external.component.ScoreComponent;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import runner.internal.LevelRunner;
import java.io.*;
import java.sql.SQLException;
import java.util.*;
import java.util.function.Consumer;

/**
 * Primary class that runs the game
 * Gets game object from database and plays it
 * @author Louis Jensen
 */
public class GameRunner {
    private int mySceneWidth;
    private int mySceneHeight;
    private Game myGame;
    private Stage myGameStage;
    private String myGameName;
    private String myAuthorName;
    private DataManager myDataManager;
    private Level myCurrentLevel;
    private Double myScore;
    private String myUsername;
    private Double myLives;

    /**
     * Constructor for GameRunner
     * @param gameName - name of the game
     * @param authorName - name of the game's author
     * @throws FileNotFoundException if game is not found
     */
    public GameRunner(String gameName, String authorName, String username) throws FileNotFoundException {
        myGame = loadGameObject(gameName, authorName);
        myGameName = gameName;
        myUsername = username;
        myAuthorName = authorName;
        myGameStage = new Stage();
        int firstLevel = 1;
        runLevel(firstLevel);
    }

    private Game loadGameObject(String gameName, String authorName){
        myDataManager = new DataManager();
        try {
            return (Game) myDataManager.loadGameData(gameName, authorName);
        } catch (SQLException e) {
            System.out.println(e.getMessage());
            return null;
        }
    }

    private void runLevel(int currentLevelNumber){
        DataManager dm = new DataManager();
        Game gameToPlay;
        try {
            gameToPlay = (Game) dm.loadGameData(myGameName, myAuthorName);
        } catch(SQLException e){
            gameToPlay = myGame;
        }
        myCurrentLevel = gameToPlay.getLevels().get(currentLevelNumber - 1);
        mySceneWidth = myGame.getWidth();
        mySceneHeight = myGame.getHeight();
        Consumer<Double> goToNext = (level) -> { nextLevel(level); };
        Image background;
        try {
           background = new Image(myDataManager.loadImage(myCurrentLevel.getBackground()), myCurrentLevel.getWidth(), myCurrentLevel.getHeight(), false, false);
        } catch (Exception e){
            background = new Image(myDataManager.loadImage("byteme_default_runnerBackground"), myCurrentLevel.getWidth(), myCurrentLevel.getHeight(), false, false);
        }
        new LevelRunner(myCurrentLevel, mySceneWidth, mySceneHeight, myGameStage, goToNext, gameToPlay.getLevels().size(), background, myScore, myLives, myAuthorName, myGameName, myUsername, myGame);
    }

    private void nextLevel(Double level) {
        int levelToPlay;
        try{
            levelToPlay = level.intValue();
        } catch (Exception e){
            levelToPlay = 10;
        }
        for(Entity entity : myCurrentLevel.getEntities()){
            if (entity.hasComponents(ScoreComponent.class)){
                myScore = (Double) entity.getComponent(ScoreComponent.class).getValue();
            }
            if (entity.hasComponents(LivesComponent.class)){
                myScore = (Double) entity.getComponent(LivesComponent.class).getValue();
            }
        }
        this.runLevel(levelToPlay);
    }

}