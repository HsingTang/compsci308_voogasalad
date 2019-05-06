package data.external;

import com.thoughtworks.xstream.mapper.CannotResolveClassException;
import data.Serializers.Serializer;
import data.Serializers.XStreamSerializer;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * GameDataManager is part of what used to be the DataManager class (before refactoring). The GameDataManager
 * encapsulates the implementation decision of how game data gets serialized and stored away from the rest of the
 * program. The DataManager class used to have too many responsibilities and as part of my code masterpiece I have
 * refactored it into four different classes, the most substantial of which being the GameDataManager. The
 * GameDataManager accesses the database, and delegates a lot of its responsibility to the database because that is
 * our primary method of storing information, but if we wanted to switch how we stored our data, or any part of our
 * data, the GameDataManager would make that very easy and would hide that decision from the rest of the program.
 * Additionally, if we wanted to change how we serialized our games (using json instead of xml for example) the
 * GameDataManager would be unaffected besides changing one line in the constructor to initialize mySerializer to a
 * different object that implements the Serializer interface. Another part of the refactoring was to remove the xml
 * specifics from this class to make the serialization process more flexible and easily changed.
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
        String serializedGameData = mySerializer.serialize(gameObject);
        myDatabaseEngine.updateGameEntryData(gameName, authorName, serializedGameData);
    }

    /**
     * Loads and deserializes all the game info objects from the database to pass to the game center
     * @return deserialized game center data objects
     */
    @Override
    public List<GameCenterData> loadAllGameCenterDataObjects() throws SQLException {
        List<String> serializedGameCenterDataObjects = myDatabaseEngine.loadSerializedGameCenterDataObjects();
        return deserializeGameCenterDataObjects(serializedGameCenterDataObjects);
    }

    // Takes in a list of serialized GameCenterData objects and returns a list of successfully deserialized
    // GameCenterData objects
    private List<GameCenterData> deserializeGameCenterDataObjects(List<String> serializedGameCenterDataObjects) {
        List<GameCenterData> gameInfoObjects = new ArrayList<>();
        for (String serializedObject : serializedGameCenterDataObjects) {
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
        String serializedGameCenterData = mySerializer.serialize(gameInfoObject);
        myDatabaseEngine.updateGameEntryInfo(gameName, authorName, serializedGameCenterData);
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
        List<String> serializedGameCenterDataObjects = myDatabaseEngine.loadSerializedGameCenterDataObjects(userName);
        return deserializeGameCenterDataObjects(serializedGameCenterDataObjects);
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
