package data.external;

import java.io.File;
import java.io.InputStream;
import java.sql.SQLException;
import java.util.Map;

public class AssetDataManager extends DataManager implements AssetDataExternal {

    /**
     * DataManager constructor creates a new serializer and connects to the the Database
     */
    public AssetDataManager() {
        super();
    }

    /**
     * Saves an image to the database
     *
     * @param imageName   the name of the image to save
     * @param imageToSave the image file that should be saved
     */
    @Override
    public void saveImage(String imageName, File imageToSave) {
        myDatabaseEngine.saveImage(imageName, imageToSave);
    }

    /**
     * Saves a sound to the database
     *
     * @param soundName   name of the sound to be saved
     * @param soundToSave sound file to be saved
     */
    @Override
    public void saveSound(String soundName, File soundToSave) {
        myDatabaseEngine.saveSound(soundName, soundToSave);
    }

    /**
     * Loads a sound from the database
     *
     * @param soundName name of the sound to be loaded
     * @return an input stream of sound data to be converted to a media object
     */
    @Override
    public InputStream loadSound(String soundName) {
        return myDatabaseEngine.loadSound(soundName);
    }

    /**
     * Loads an image from the database
     *
     * @param imageName name of the image to be loaded
     * @return an input stream of image data to be converted to an image object
     */
    @Override
    public InputStream loadImage(String imageName) {
        return myDatabaseEngine.loadImage(imageName);
    }

    /**
     * Loads all the images involved in a game specified by prefix
     *
     * @param prefix the gameName + the authorName
     * @return a map of the image names to the input stream data
     */
    @Override
    public Map<String, InputStream> loadAllImages(String prefix) throws SQLException {
        return myDatabaseEngine.loadAllImages(prefix);
    }

    /**
     * Loads all the images involved in a game specified by prefix
     *
     * @param prefix the gameName + the authorName
     * @return a map of the sound names to the input stream data
     */
    @Override
    public Map<String, InputStream> loadAllSounds(String prefix) throws SQLException {
        return myDatabaseEngine.loadAllSounds(prefix);
    }

}
