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
public class GameDataManager extends DataManager implements ExternalGameData {


    private static final String COULD_NOT_UPDATE_GAME_ENTRY_DATA = "Couldn't update game entry data: ";
    private static final String CANT_LOAD_GAME_INFORMATION_XMLS = "Couldn't load game information xmls: ";
    private static final String CANT_UPDATE_GAME_ENTRY_INFO = "Couldn't update game entry information: ";

    private Serializer mySerializer;
    private FileManager myFileManager;
    private DatabaseEngine myDatabaseEngine;

    /**
     * DataManager constructor creates a new serializer and connects to the the Database
     */
    public GameDataManager() {
        mySerializer = new XStreamSerializer();
        myFileManager = new FileManager();
        myDatabaseEngine = DatabaseEngine.getInstance();
    }

    /**
     * Saves an object passed to the method to an xml file at the specified path
     *
     * @param path            to the file to be saved
     * @param objectToBeSaved the object that should be saved to xml
     */
    @Override
    public void saveSerializedObjectToFile(String path, Object objectToBeSaved) {
        String myRawXML = mySerializer.serialize(objectToBeSaved);
        myFileManager.writeToFile(path, myRawXML);
    }

    /**
     * Loads an object at the specified path from xml
     *
     * @param path path to the xml file of the serialized object you wish to deserialize
     * @return a deserialized xml object at path
     * @throws FileNotFoundException when the specified path doesn't point to a valid file
     */
    @Override
    public Object loadSerializedObjectFromFile(String path) throws FileNotFoundException {
        String rawXML = myFileManager.readFromFile(path);
        return mySerializer.deserialize(rawXML);
    }

    /**
     * Saves game data to the database in the form of serialized xml of a game object
     *
     * @param gameName   name of the game -> folder to be created
     * @param authorName name of the author of the game
     * @param gameObject the object containing all game information except for assets
     */
    @Override
    public void saveGameData(String gameName, String authorName, Object gameObject) {
        String myRawXML = mySerializer.serialize(gameObject);
        try {
            myDatabaseEngine.updateGameEntryData(gameName, authorName, myRawXML);
        } catch (SQLException e) {
            System.out.println(COULD_NOT_UPDATE_GAME_ENTRY_DATA + e.getMessage());
        }
    }

    /**
     * Loads and deserializes all the game info objects from the database to pass to the game center
     *
     * @return deserialized game center data objects
     */
    @Override
    public List<GameCenterData> loadAllGameCenterDataObjects() {
        List<String> gameInfoObjectXMLs = new ArrayList<>();
        try {
            gameInfoObjectXMLs = myDatabaseEngine.loadAllGameInformationXMLs();
        } catch (SQLException e) {
            System.out.println(CANT_LOAD_GAME_INFORMATION_XMLS + e.getMessage());
        }
        return deserializeGameInfoObjects(gameInfoObjectXMLs);
    }

    private List<GameCenterData> deserializeGameInfoObjects(List<String> serializedGameInfoObjects) {
        List<GameCenterData> gameInfoObjects = new ArrayList<>();
        for (String serializedObject : serializedGameInfoObjects) {
            try {
                GameCenterData gameCenterDataToAdd = (GameCenterData) mySerializer.deserialize(serializedObject);
                gameInfoObjects.add(gameCenterDataToAdd);
            } catch (CannotResolveClassException exception){
                // do nothing, invalid objects should not be added to the list sent to game center
            }
        }
        return gameInfoObjects;
    }

    /**
     * Saves game information (game center data) to the data base
     *
     * @param gameName       name of the game
     * @param authorName     name of the author of the game
     * @param gameInfoObject the game center data object to be serialized and saved
     */
    @Override
    public void saveGameInfo(String gameName, String authorName, GameCenterData gameInfoObject) {
        String myRawXML = mySerializer.serialize(gameInfoObject);
        try {
            myDatabaseEngine.updateGameEntryInfo(gameName, authorName, myRawXML);
        } catch (SQLException e) {
            System.out.println(CANT_UPDATE_GAME_ENTRY_INFO + e.getMessage());
        }
    }

    /**
     * Returns a GameCenterData object for the specified game
     * @param gameName name of the game of the GameCenterData object to load
     * @param authorName author name that wrote the game
     * @return GameCenterData object for the specified game
     * @throws SQLException if statement fails
     */
    @Override
    public GameCenterData loadGameCenterData(String gameName, String authorName) throws SQLException {
        return (GameCenterData) mySerializer.deserialize(myDatabaseEngine.loadGameInfo(gameName, authorName));
    }

    /**
     * Removes a game from the database
     *
     * @param gameName   name of the game to remove
     * @param authorName author of the game to remove
     * @throws SQLException if operation fails
     */
    @Override
    public void removeGame(String gameName, String authorName) throws SQLException {
        myDatabaseEngine.removeGame(gameName, authorName);
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
    public List<GameCenterData> loadAllGameCenterDataObjects(String userName) {
        List<String> gameInfoObjectXMLs = new ArrayList<>();
        try {
            gameInfoObjectXMLs = myDatabaseEngine.loadAllGameInformationXMLs(userName);
        } catch (SQLException e) {
            System.out.println(CANT_LOAD_GAME_INFORMATION_XMLS + e.getMessage());
        }
        return deserializeGameInfoObjects(gameInfoObjectXMLs);
    }

    /**
     * Method just used for testing purposes
     * @param gameName name of the game to remove
     * @param authorName author of the game to remove
     * @throws SQLException if statement fails
     */
    @Override
    public void removeRating(String gameName, String authorName) throws SQLException {
        myDatabaseEngine.removeRating(gameName, authorName);
    }

    /**
     * Returns a map from the Timestamp to the deserialized checkpoint object
     * @param userName of the person playing the game
     * @param gameName of the game that's checkpoint should be loaded
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
     * @param userName of the person playing the game
     * @param gameName of the game that's checkpoint should be loaded
     * @param authorName author of the game that is being played
     * @param checkpoint the object that should be serialized as a checkpoint
     * @throws SQLException if statement fails
     */
    @Override
    public void saveCheckpoint(String userName, String gameName, String authorName, Object checkpoint) throws SQLException {
        myDatabaseEngine.saveCheckpoint(userName, gameName, authorName, mySerializer.serialize(checkpoint));
    }



    /**
     * Just for testing purposes
     * @param userName name of the user whose chekckpoint should be removed
     * @param gameName game name
     * @param authorName author name
     * @throws SQLException if statement fails
     */
    @Override
    public void deleteCheckpoints(String userName, String gameName, String authorName) throws SQLException {
        myDatabaseEngine.deleteCheckpoint(userName, gameName, authorName);
    }

    /**
     * Saves the score of a game and a user to the database
     * @param userName person playing the game
     * @param gameName name of the game
     * @param authorName author of the game
     * @param score score for the game
     */
    @Override
    public void saveScore(String userName, String gameName, String authorName, Double score) {
        try {
            myDatabaseEngine.saveScore(userName, gameName, authorName, score);
        } catch (SQLException e) {
            // do nothing, agreed upon by team
        }
    }

    /**
     * Loads all the scores for a given game
     * @param gameName name of the game to get scores for
     * @param authorName name of the author of the game
     * @return list of the scores for a give game
     */
    @Override
    public List<UserScore> loadScores(String gameName, String authorName) throws SQLException {
        return myDatabaseEngine.loadScores(gameName, authorName);
    }

    /**
     * Just used for testing purposes to remove score
     * @param userName name of the user
     * @param gameName game name
     * @param authorName author name
     * @throws SQLException if statement fails
     */
    @Override
    public void removeScores(String userName, String gameName, String authorName) throws SQLException {
        myDatabaseEngine.removeScores(userName, gameName, authorName);
    }

    public GameCenterData loadGameInfo(String selectedGameTitle, String authorName) throws SQLException {
        return (GameCenterData) mySerializer.deserialize(myDatabaseEngine.loadGameInfo(selectedGameTitle, authorName));
    }
}
