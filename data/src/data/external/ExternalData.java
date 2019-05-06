package data.external;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.List;
import java.util.Map;

public interface ExternalData {

    /**
     * Saves a an object to xml at the path specified by path
     *
     * @param path            to the file to be saved
     * @param objectToBeSaved the object that should be saved to xml
     */
    void saveObjectToXML(String path, Object objectToBeSaved);

    /**
     * Load a deserialized version of the object represented by the xml file specified by path
     *
     * @param path path to the xml file of the serialized object you wish to deserialize
     * @return deserialized version of the object that should be cast
     */
    Object loadObjectFromXML(String path) throws FileNotFoundException;

    /**
     * Save a Game of name gameName to the path created_games/gameName/game_data.xml
     *
     * @param gameName   name of the game -> folder to be created
     * @param authorName name of the author of the game
     * @param gameObject the object containing all game information except for assets
     */
    void saveGameData(String gameName, String authorName, Object gameObject);

    /**
     * Saves game information (game center data) to the data base
     *
     * @param gameName       name of the game
     * @param authorName     name of the author of the game
     * @param gameInfoObject the game center data object to be serialized and saved
     */
    void saveGameInfo(String gameName, String authorName, GameCenterData gameInfoObject);

    /**
     * Loads the GameCenterData object associated with the specific gameName and authorName of the game
     * @param gameName name of the game of the GameCenterData object to load
     * @param authorName author name that wrote the game
     * @return a deserialized Game Center Data object for the game specified by gameName and authorName
     */
    GameCenterData loadGameInfo(String gameName, String authorName) throws SQLException;

    /**
     * Loads and deserializes all the game info objects from the database to pass to the game center
     *
     * @return deserialized game center data objects
     */
    List<GameCenterData> loadAllGameInfoObjects();

    /**
     * Saves an image to the database
     *
     * @param imageName   the name of the image to save
     * @param imageToSave the image file that should be saved
     */
    void saveImage(String imageName, File imageToSave);

    /**
     * Saves a sound to the database
     *
     * @param soundName   name of the sound to be saved
     * @param soundToSave sound file to be saved
     */
    void saveSound(String soundName, File soundToSave);

    /**
     * Loads a sound from the database
     *
     * @param soundName name of the sound to be loaded
     * @return an input stream of sound data to be converted to a media object
     */
    InputStream loadSound(String soundName);

    /**
     * Loads an image from the database
     *
     * @param imageName name of the image to be loaded
     * @return an input stream of image data to be converted to an image object
     */
    InputStream loadImage(String imageName);

    /**
     * Creates a new user in the database
     *
     * @param userName name of the user
     * @param password user's password
     * @return true if the user was successfully created
     */
    boolean createUser(String userName, String password);

    /**
     * Validates a user's login attempt
     *
     * @param userName entered user name
     * @param password entered password
     * @return true if a valid user name and password combination
     */
    boolean validateUser(String userName, String password);

    /**
     * Removes a user from the database
     *
     * @param userName user name of the user to remove
     */
    void removeUser(String userName) throws SQLException;

    /**
     * Removes a game from the database
     *
     * @param gameName   name of the game to remove
     * @param authorName author of the game to remove
     */
    void removeGame(String gameName, String authorName) throws SQLException;

    /**
     * Remove an image from the database
     *
     * @param imageName name of the image to remove
     * @return true if image was successfully removed
     */
    boolean removeImage(String imageName) throws SQLException;

    /**
     * Remove a sound from the database
     *
     * @param soundName name of the sound to remove
     * @return true if the image was successfully removed
     */
    boolean removeSound(String soundName) throws SQLException;

    /**
     * Loads the deserialized game object from the database
     *
     * @param gameName   name of the game
     * @param authorName name of the author that wrote the game
     * @return deserialized game object that needs to be cast
     * @throws SQLException if operation fails
     */
    Object loadGameData(String gameName, String authorName) throws SQLException;

    /**
     * Loads all the images involved in a game specified by prefix
     *
     * @param prefix the gameName + the authorName
     * @return a map of the image names to the input stream data
     */
    Map<String, InputStream> loadAllImages(String prefix) throws SQLException;

    /**
     * Loads all the images involved in a game specified by prefix
     *
     * @param prefix the gameName + the authorName
     * @return a map of the sound names to the input stream data
     */
    Map<String, InputStream> loadAllSounds(String prefix) throws SQLException;

    /**
     * Loads all the names of the games that a user has created
     *
     * @param userName user name of the user whose games are to be loaded
     * @return list of the names of all the games of the user has created
     * @throws SQLException if operation fails
     */
    List<String> loadUserGameNames(String userName) throws SQLException;

    /**
     * Updates the specified user's password in the database
     * @param userName user whose password should be updated
     * @param newPassword the new password it should be updated to
     * @return true if successful, false else
     * @throws SQLException if statement fails
     */
    boolean updatePassword(String userName, String newPassword) throws SQLException;

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
    List<GameCenterData> loadAllGameInfoObjects(String userName);

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
     * Sets the profile pic for a user in the database
     * @param userName user's username
     * @param profilePic profile pic to set
     * @throws SQLException if statement fails
     */
    void setProfilePic(String userName, File profilePic) throws SQLException;

    /**
     * Sets the bio for a user in the database
     * @param userName user's username
     * @throws SQLException if statement fails
     */
    void setBio(String userName, String bio) throws SQLException;

    /**
     * Retrieves the profile picture of a user as an InputStream from the database
     * @param userName user name of the user whose profile pic should be retrieved
     * @return an InputStream of the profile picture for that user
     * @throws SQLException if statement fails
     */
    InputStream getProfilePic(String userName) throws SQLException;

    /**
     * Retrieves the bio of a user in the database
     * @param userName user whose bio should be retrieved
     * @return bio
     * @throws SQLException if statement fails
     */
    String getBio(String userName) throws SQLException;

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

}
