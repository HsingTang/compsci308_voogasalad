package data.external;

import com.thoughtworks.xstream.mapper.CannotResolveClassException;
import data.Serializers.Serializer;
import data.Serializers.XStreamSerializer;
import data.internal.FileManager;

import java.io.*;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * DataManager is the wrapper class that allows other modules to interact with the database, saving and loading
 * games, images, and user information.  Whenever another module wants to access the database, it should use a
 * DataManager object and call the appropriate method
 */
public class GameDataManager extends DataManager implements GameDataExternal {

    private Serializer mySerializer;

    /**
     * DataManager constructor creates a new serializer and connects to the the Database
     */
    public GameDataManager() {
        super();
        mySerializer = new XStreamSerializer();
    }

    /**
     * Saves game data to the database in the form of serialized xml of a game object
     * @param gameName   name of the game -> folder to be created
     * @param authorName name of the author of the game
     * @param gameObject the object containing all game information except for assets
     */
    @Override
    public void saveGameData(String gameName, String authorName, Object gameObject) throws SQLException {
        String myRawXML = mySerializer.serialize(gameObject);
        myDatabaseEngine.updateGameEntryData(gameName, authorName, myRawXML);
    }

    /**
     * Loads and deserializes all the game info objects from the database to pass to the game center
     * @return deserialized game center data objects
     */
    @Override
    public List<GameCenterData> loadAllGameCenterDataObjects() throws SQLException {
        List<String> gameInfoObjectXMLs = new ArrayList<>();
        gameInfoObjectXMLs = myDatabaseEngine.loadAllGameInformationXMLs();
        return deserializeGameInfoObjects(gameInfoObjectXMLs);
    }

    private List<GameCenterData> deserializeGameInfoObjects(List<String> serializedGameInfoObjects) {
        List<GameCenterData> gameInfoObjects = new ArrayList<>();
        for (String serializedObject : serializedGameInfoObjects) {
            try {
                GameCenterData gameCenterDataToAdd = (GameCenterData) mySerializer.deserialize(serializedObject);
                gameInfoObjects.add(gameCenterDataToAdd);
            } catch (CannotResolveClassException exception) {
                // do nothing, invalid objects should not be added to the list sent to game center
            }
        }
        return gameInfoObjects;
    }

    /**
     * Saves game information (game center data) to the data base
     * @param gameName       name of the game
     * @param authorName     name of the author of the game
     * @param gameInfoObject the game center data object to be serialized and saved
     */
    @Override
    public void saveGameCenterData(String gameName, String authorName, GameCenterData gameInfoObject) throws SQLException {
        String myRawXML = mySerializer.serialize(gameInfoObject);
        myDatabaseEngine.updateGameEntryInfo(gameName, authorName, myRawXML);
    }

    /**
     * Returns a GameCenterData object for the specified game
     * @param gameName   name of the game of the GameCenterData object to load
     * @param authorName author name that wrote the game
     * @return GameCenterData object for the specified game
     * @throws SQLException if statement fails
     */
    @Override
    public GameCenterData loadGameCenterData(String gameName, String authorName) throws SQLException {
        return (GameCenterData) mySerializer.deserialize(myDatabaseEngine.loadGameInfo(gameName, authorName));
    }

    /**
     * Loads the deserialized game object from the database
     * @param gameName   name of the game
     * @param authorName name of the author that wrote the game
     * @return deserialized game object that needs to be cast
     * @throws SQLException if operation fails
     */
    @Override
    public Object loadGameData(String gameName, String authorName) throws SQLException {
        return mySerializer.deserialize(myDatabaseEngine.loadGameData(gameName, authorName));
    }

    /**
     * Adds a rating to the database for a specific game
     * @param rating GameRating object that contains the rating information
     * @throws SQLException if statement fails
     */
    @Override
    public void addRating(GameRating rating) throws SQLException {
        myDatabaseEngine.addGameRating(rating);
    }

    /**
     * Returns the average rating for a game
     * @param gameName name to retrieve the average rating for
     * @return the average rating for the game gameName
     * @throws SQLException if statement fails
     */
    @Override
    public double getAverageRating(String gameName) throws SQLException {
        return myDatabaseEngine.getAverageRating(gameName);
    }

    /**
     * Returns a list of all the ratings for a specific game
     * @param gameName name of the game to get the ratings for
     * @return a list of all the ratings for a specific game
     * @throws SQLException if statement fails
     */
    @Override
    public List<GameRating> getAllRatings(String gameName) throws SQLException {
        return myDatabaseEngine.getAllRatings(gameName);
    }

    /**
     * Loads a list of all GameCenterData objects for the games authored by a specific user
     * @param userName user whose games to retrieve
     * @return a list of all GameCenterData objects for the games authored by a specific user
     */
    @Override
    public List<GameCenterData> loadAllGameCenterDataObjects(String userName) throws SQLException {
        List<String> gameInfoObjectXMLs = new ArrayList<>();
        gameInfoObjectXMLs = myDatabaseEngine.loadAllGameInformationXMLs(userName);
        return deserializeGameInfoObjects(gameInfoObjectXMLs);
    }

    /**
     * Returns a map from the Timestamp to the deserialized checkpoint object
     * @param userName   of the person playing the game
     * @param gameName   of the game that's checkpoint should be loaded
     * @param authorName author of the game that is being played
     * @return a map from the Timestamp to the deserialized chekcpoint object
     * @throws SQLException if statement fails
     */
    @Override
    public Map<Timestamp, Object> getCheckpoints(String userName, String gameName, String authorName) throws SQLException {
        Map<Timestamp, Object> deserializedCheckpoints = new HashMap<>();
        Map<Timestamp, String> serializedCheckpoints = myDatabaseEngine.getCheckpoints(userName, gameName, authorName);
        for (Timestamp time : serializedCheckpoints.keySet()) {
            deserializedCheckpoints.put(time, mySerializer.deserialize(serializedCheckpoints.get(time)));
        }
        return deserializedCheckpoints;
    }

    /**
     * Saves a checkpoint to the database
     * @param userName   of the person playing the game
     * @param gameName   of the game that's checkpoint should be loaded
     * @param authorName author of the game that is being played
     * @param checkpoint the object that should be serialized as a checkpoint
     * @throws SQLException if statement fails
     */
    @Override
    public void saveCheckpoint(String userName, String gameName, String authorName, Object checkpoint) throws SQLException {
        myDatabaseEngine.saveCheckpoint(userName, gameName, authorName, mySerializer.serialize(checkpoint));
    }

    /**
     * Saves the score of a game and a user to the database
     * @param userName   person playing the game
     * @param gameName   name of the game
     * @param authorName author of the game
     * @param score      score for the game
     */
    @Override
    public void saveScore(String userName, String gameName, String authorName, Double score) throws SQLException {
        myDatabaseEngine.saveScore(userName, gameName, authorName, score);
    }

    /**
     * Loads all the scores for a given game
     * @param gameName   name of the game to get scores for
     * @param authorName name of the author of the game
     * @return list of the scores for a give game
     */
    @Override
    public List<UserScore> loadScores(String gameName, String authorName) throws SQLException {
        return myDatabaseEngine.loadScores(gameName, authorName);
    }

}
