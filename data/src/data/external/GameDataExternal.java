package data.external;

import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.List;
import java.util.Map;

public interface GameDataExternal {
    /**
     * Saves game data to the database in the form of serialized xml of a game object
     * @param gameName   name of the game -> folder to be created
     * @param authorName name of the author of the game
     * @param gameObject the object containing all game information except for assets
     */
    void saveGameData(String gameName, String authorName, Object gameObject) throws SQLException;

    /**
     * Loads and deserializes all the game info objects from the database to pass to the game center
     * @return deserialized game center data objects
     */
    List<GameCenterData> loadAllGameCenterDataObjects() throws SQLException;

    /**
     * Saves game information (game center data) to the data base
     * @param gameName       name of the game
     * @param authorName     name of the author of the game
     * @param gameInfoObject the game center data object to be serialized and saved
     */
    void saveGameCenterData(String gameName, String authorName, GameCenterData gameInfoObject) throws SQLException;

    /**
     * Returns a GameCenterData object for the specified game
     * @param gameName   name of the game of the GameCenterData object to load
     * @param authorName author name that wrote the game
     * @return GameCenterData object for the specified game
     * @throws SQLException if statement fails
     */
    GameCenterData loadGameCenterData(String gameName, String authorName) throws SQLException;

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
    List<GameCenterData> loadAllGameCenterDataObjects(String userName) throws SQLException;

    /**
     * Returns a map from the Timestamp to the deserialized checkpoint object
     * @param userName   of the person playing the game
     * @param gameName   of the game that's checkpoint should be loaded
     * @param authorName author of the game that is being played
     * @return a map from the Timestamp to the deserialized chekcpoint object
     * @throws SQLException if statement fails
     */
    Map<Timestamp, Object> getCheckpoints(String userName, String gameName, String authorName) throws SQLException;

    /**
     * Saves a checkpoint to the database
     * @param userName   of the person playing the game
     * @param gameName   of the game that's checkpoint should be loaded
     * @param authorName author of the game that is being played
     * @param checkpoint the object that should be serialized as a checkpoint
     * @throws SQLException if statement fails
     */
    void saveCheckpoint(String userName, String gameName, String authorName, Object checkpoint) throws SQLException;

    /**
     * Saves the score of a game and a user to the database
     * @param userName   person playing the game
     * @param gameName   name of the game
     * @param authorName author of the game
     * @param score      score for the game
     */
    void saveScore(String userName, String gameName, String authorName, Double score) throws SQLException;

    /**
     * Loads all the scores for a given game
     * @param gameName   name of the game to get scores for
     * @param authorName name of the author of the game
     * @return list of the scores for a give game
     */
    List<UserScore> loadScores(String gameName, String authorName) throws SQLException;
}
