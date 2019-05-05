package data.external;

import java.io.FileNotFoundException;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.List;
import java.util.Map;

public interface ExternalGameData {
    /**
     * Saves an object passed to the method to an xml file at the specified path
     *
     * @param path            to the file to be saved
     * @param objectToBeSaved the object that should be saved to xml
     */
    void saveSerializedObjectToFile(String path, Object objectToBeSaved);

    /**
     * Loads an object at the specified path from xml
     *
     * @param path path to the xml file of the serialized object you wish to deserialize
     * @return a deserialized xml object at path
     * @throws FileNotFoundException when the specified path doesn't point to a valid file
     */
    Object loadSerializedObjectFromFile(String path) throws FileNotFoundException;

    /**
     * Saves game data to the database in the form of serialized xml of a game object
     *
     * @param gameName   name of the game -> folder to be created
     * @param authorName name of the author of the game
     * @param gameObject the object containing all game information except for assets
     */
    void saveGameData(String gameName, String authorName, Object gameObject);

    /**
     * Loads and deserializes all the game info objects from the database to pass to the game center
     *
     * @return deserialized game center data objects
     */
    List<GameCenterData> loadAllGameCenterDataObjects();

    /**
     * Saves game information (game center data) to the data base
     *
     * @param gameName       name of the game
     * @param authorName     name of the author of the game
     * @param gameInfoObject the game center data object to be serialized and saved
     */
    void saveGameInfo(String gameName, String authorName, GameCenterData gameInfoObject);

    /**
     * Returns a GameCenterData object for the specified game
     * @param gameName name of the game of the GameCenterData object to load
     * @param authorName author name that wrote the game
     * @return GameCenterData object for the specified game
     * @throws SQLException if statement fails
     */
    GameCenterData loadGameCenterData(String gameName, String authorName) throws SQLException;

    /**
     * Removes a game from the database
     *
     * @param gameName   name of the game to remove
     * @param authorName author of the game to remove
     * @throws SQLException if operation fails
     */
    void removeGame(String gameName, String authorName) throws SQLException;

    /**
     * Loads the deserialized game object from the database
     * @param gameName   name of the game
     * @param authorName name of the author that wrote the game
     * @return deserialized game object that needs to be cast
     * @throws SQLException if operation fails
     */
    Object loadGameData(String gameName, String authorName) throws SQLException;

    /**
     * Adds a rating to the database for a specific game
     * @param rating GameRating object that contains the rating information
     * @throws SQLException if statement fails
     */
    void addRating(GameRating rating) throws SQLException;

    /**
     * Returns the average rating for a game
     * @param gameName name to retrieve the average rating for
     * @return the average rating for the game gameName
     * @throws SQLException if statement fails
     */
    double getAverageRating(String gameName) throws SQLException;

    /**
     * Returns a list of all the ratings for a specific game
     * @param gameName name of the game to get the ratings for
     * @return a list of all the ratings for a specific game
     * @throws SQLException if statement fails
     */
    List<GameRating> getAllRatings(String gameName) throws SQLException;

    /**
     * Loads a list of all GameCenterData objects for the games authored by a specific user
     * @param userName user whose games to retrieve
     * @return a list of all GameCenterData objects for the games authored by a specific user
     */
    List<GameCenterData> loadAllGameCenterDataObjects(String userName);

    /**
     * Method just used for testing purposes
     * @param gameName name of the game to remove
     * @param authorName author of the game to remove
     * @throws SQLException if statement fails
     */
    void removeRating(String gameName, String authorName) throws SQLException;

    /**
     * Returns a map from the Timestamp to the deserialized checkpoint object
     * @param userName of the person playing the game
     * @param gameName of the game that's checkpoint should be loaded
     * @param authorName author of the game that is being played
     * @return a map from the Timestamp to the deserialized chekcpoint object
     * @throws SQLException if statement fails
     */
    Map<Timestamp, Object> getCheckpoints(String userName, String gameName, String authorName) throws SQLException;

    /**
     * Saves a checkpoint to the database
     * @param userName of the person playing the game
     * @param gameName of the game that's checkpoint should be loaded
     * @param authorName author of the game that is being played
     * @param checkpoint the object that should be serialized as a checkpoint
     * @throws SQLException if statement fails
     */
    void saveCheckpoint(String userName, String gameName, String authorName, Object checkpoint) throws SQLException;

    /**
     * Just for testing purposes
     * @param userName name of the user whose chekckpoint should be removed
     * @param gameName game name
     * @param authorName author name
     * @throws SQLException if statement fails
     */
    void deleteCheckpoints(String userName, String gameName, String authorName) throws SQLException;

    /**
     * Saves the score of a game and a user to the database
     * @param userName person playing the game
     * @param gameName name of the game
     * @param authorName author of the game
     * @param score score for the game
     */
    void saveScore(String userName, String gameName, String authorName, Double score);

    /**
     * Loads all the scores for a given game
     * @param gameName name of the game to get scores for
     * @param authorName name of the author of the game
     * @return list of the scores for a give game
     */
    List<UserScore> loadScores(String gameName, String authorName) throws SQLException;

    /**
     * Just used for testing purposes to remove score
     * @param userName name of the user
     * @param gameName game name
     * @param authorName author name
     * @throws SQLException if statement fails
     */
    void removeScores(String userName, String gameName, String authorName) throws SQLException;
}
