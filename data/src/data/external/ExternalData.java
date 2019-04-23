package data.external;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.sql.SQLException;
import java.util.List;

public interface ExternalData {

    /**
     * Saves a an object to xml at the path specified by path
     * @param path to the file to be saved
     * @param objectToBeSaved the object that should be saved to xml
     */
    void saveObjectToXML(String path, Object objectToBeSaved);

    /**
     * Load a deserialized version of the object represented by the xml file specified by path
     * @param path path to the xml file of the serialized object you wish to deserialize
     * @return deserialized version of the object that should be cast
     */
    Object loadObjectFromXML(String path) throws FileNotFoundException;

    /**
     * Save a Game of name gameName to the path created_games/gameName/game_data.xml
     * @param gameName name of the game -> folder to be created
     * @param authorName name of the author of the game
     * @param gameObject the object containing all game information except for assets
     */
    void saveGameData(String gameName, String authorName, Object gameObject);

    /**
     * Deserializes the xml file stored at created_games/gameName/game_data.xml into an object
     * @param gameName the game whose data is to be loaded
     * @return the deserialized game data that should then be cast to a game object
     */
    Object loadGameData(String gameName) throws FileNotFoundException;

    /**
     * Saves game information (game center data) to the data base
     * @param gameName name of the game
     * @param authorName name of the author of the game
     * @param gameInfoObject the game center data object to be serialized and saved
     */
    void saveGameInfo(String gameName, String authorName, Object gameInfoObject);

    /**
     * Loads and deserializes all the game info objects from the database to pass to the game center
     * @return deserialized game center data objects
     */
    List<Object> loadAllGameInfoObjects();

    /**
     * Saves an image to the database
     * @param imageName the name of the image to save
     * @param imageToSave the image file that should be saved
     */
    void saveImage(String imageName, File imageToSave);

    /**
     * Saves a sound to the database
     * @param soundName name of the sound to be saved
     * @param soundToSave sound file to be saved
     */
    void saveSound(String soundName, File soundToSave);

    /**
     * Loads a sound from the database
     * @param soundName name of the sound to be loaded
     * @return an input stream of sound data to be converted to a media object
     */
    InputStream loadSound(String soundName);

    /**
     * Loads an image from the database
     * @param imageName name of the image to be loaded
     * @return an input stream of image data to be converted to an image object
     */
    InputStream loadImage(String imageName);

    /**
     * Creates a new user in the database
     * @param userName name of the user
     * @param password user's password
     * @return true if the user was successfully created
     */
    boolean createUser (String userName, String password);

    /**
     * Validates a user's login attempt
     * @param userName entered user name
     * @param password entered password
     * @return true if a valid user name and password combination
     */
    boolean validateUser (String userName, String password);

    /**
     * Removes a user from the database
     * @param userName user name of the user to remove
     * @return true if the user was successfully removed
     */
    boolean removeUser (String userName) throws SQLException;

    /**
     * Removes a game from the database
     * @param gameName name of the game to remove
     * @param authorName author of the game to remove
     * @return true if game was successfully removed
     */
    boolean removeGame (String gameName, String authorName) throws SQLException;

    /**
     * Remove an image from the database
     * @param imageName name of the image to remove
     * @return true if image was successfully removed
     */
    boolean removeImage (String imageName) throws SQLException;

    /**
     * Remove a sound from the database
     * @param soundName name of the sound to remove
     * @return true if the image was successfully removed
     */
    boolean removeSound (String soundName) throws SQLException;

}
