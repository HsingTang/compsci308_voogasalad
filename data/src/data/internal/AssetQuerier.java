package data.internal;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * A Querier that is used to manage saving and loading assets (sounds and images)
 */
public class AssetQuerier extends Querier {

    private static final String IMAGES_TABLE_NAME = "Images";
    private static final String IMAGE_NAME_COLUMN = "ImageName";
    private static final String IMAGE_DATA_COLUMN = "ImageData";

    private static final String SOUNDS_TABLE_NAME = "Sounds";
    private static final String SOUND_NAME_COLUMN = "SoundName";
    private static final String SOUND_DATA_COLUMN = "SoundData";

    private static final String SQL_WILDCARD = "%";

    private static final String LOAD_ALL_ASSETS = "SELECT %s, %s FROM %s WHERE %s LIKE ?";
    private static final String IMAGES_INSERT = String.format(INSERT_TWO_VALUES, IMAGES_TABLE_NAME, IMAGE_NAME_COLUMN, IMAGE_DATA_COLUMN);
    private static final String SOUNDS_INSERT = String.format(INSERT_TWO_VALUES, SOUNDS_TABLE_NAME, SOUND_NAME_COLUMN, SOUND_DATA_COLUMN);
    private static final String UPDATE_IMAGES = String.format(UPDATE_ONE_COLUMN, IMAGES_INSERT, ON_DUPLICATE_UPDATE, IMAGE_DATA_COLUMN);
    private static final String UPDATE_SOUNDS = String.format(UPDATE_ONE_COLUMN, SOUNDS_INSERT, ON_DUPLICATE_UPDATE, SOUND_DATA_COLUMN);
    private static final String LOAD_SOUND = String.format(SELECT_ONE_COLUMN_ONE_CONDITION, SOUND_DATA_COLUMN, SOUNDS_TABLE_NAME, SOUND_NAME_COLUMN);
    private static final String LOAD_IMAGE = String.format(SELECT_ONE_COLUMN_ONE_CONDITION, IMAGE_DATA_COLUMN, IMAGES_TABLE_NAME, IMAGE_NAME_COLUMN);
    private static final String REMOVE_IMAGE = String.format(DELETE_ONE_CONDITION, IMAGES_TABLE_NAME, IMAGE_NAME_COLUMN);
    private static final String REMOVE_SOUND = String.format(DELETE_ONE_CONDITION, SOUNDS_TABLE_NAME, SOUND_NAME_COLUMN);
    private static final String LOAD_ALL_IMAGES = String.format(LOAD_ALL_ASSETS, IMAGE_NAME_COLUMN, IMAGE_DATA_COLUMN, IMAGES_TABLE_NAME, IMAGE_NAME_COLUMN);
    private static final String LOAD_ALL_SOUNDS = String.format(LOAD_ALL_ASSETS, SOUND_NAME_COLUMN, SOUND_DATA_COLUMN, SOUNDS_TABLE_NAME, SOUND_NAME_COLUMN);

    private static final String COULD_NOT_LOAD_ASSET = "Could not load asset: ";
    private static final String COULD_NOT_SAVE_THE_ASSET = "Could not save the asset: ";
    private static final String COULD_NOT_FIND_THE_FILE = "Could not find the file: ";


    private PreparedStatement myUpdateImagesStatement;
    private PreparedStatement myUpdateSoundsStatement;
    private PreparedStatement myLoadImageStatement;
    private PreparedStatement myLoadSoundStatement;
    private PreparedStatement myRemoveImageStatement;
    private PreparedStatement myRemoveSoundStatement;
    private PreparedStatement myLoadAllImagesStatement;
    private PreparedStatement myLoadAllSoundsStatement;

    /**
     * AssetQuerier constructor calls super constructor to initialize prepared statements
     *
     * @param connection connection to the database provided by database engine
     * @throws SQLException if cannot connect or prepare the statements
     */
    public AssetQuerier(Connection connection) throws SQLException {
        super(connection);
    }

    @Override
    protected void prepareStatements() throws SQLException {
        myUpdateImagesStatement = myConnection.prepareStatement(UPDATE_IMAGES);
        myUpdateSoundsStatement = myConnection.prepareStatement(UPDATE_SOUNDS);
        myLoadImageStatement = myConnection.prepareStatement(LOAD_IMAGE);
        myLoadSoundStatement = myConnection.prepareStatement(LOAD_SOUND);
        myRemoveImageStatement = myConnection.prepareStatement(REMOVE_IMAGE);
        myRemoveSoundStatement = myConnection.prepareStatement(REMOVE_SOUND);
        myLoadAllImagesStatement = myConnection.prepareStatement(LOAD_ALL_IMAGES);
        myLoadAllSoundsStatement = myConnection.prepareStatement(LOAD_ALL_SOUNDS);
        myPreparedStatements = List.of(myUpdateImagesStatement, myUpdateSoundsStatement, myLoadImageStatement,
                myLoadSoundStatement, myRemoveImageStatement, myRemoveSoundStatement, myLoadAllSoundsStatement,
                myLoadAllImagesStatement);
    }

    /**
     * Saves an image to the database
     *
     * @param imageName   the name of the image to save
     * @param imageToSave the image file that should be saved
     */
    public void saveImage(String imageName, File imageToSave) {
        saveAsset(imageName, imageToSave, myUpdateImagesStatement);
    }

    /**
     * Saves a sound to the database
     *
     * @param soundName   name of the sound to be saved
     * @param soundToSave sound file to be saved
     */
    public void saveSound(String soundName, File soundToSave) {
        saveAsset(soundName, soundToSave, myUpdateSoundsStatement);
    }

    /**
     * Loads an image from the database
     *
     * @param imageName name of the image to be loaded
     * @return an input stream of image data to be converted to an image object
     */
    public InputStream loadImage(String imageName) {
        return loadAsset(imageName, IMAGE_DATA_COLUMN, myLoadImageStatement);
    }

    /**
     * Loads a sound from the database
     *
     * @param soundName name of the sound to be loaded
     * @return an input stream of sound data to be converted to a media object
     */
    public InputStream loadSound(String soundName) {
        return loadAsset(soundName, SOUND_DATA_COLUMN, myLoadSoundStatement);
    }

    private InputStream loadAsset(String assetName, String columnName, PreparedStatement statement) {
        try {
            statement.setString(1, assetName);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                InputStream imageInputStream = resultSet.getBinaryStream(columnName);
                resultSet.close();
                return imageInputStream;
            }
        } catch (SQLException e) {
            System.out.println(COULD_NOT_LOAD_ASSET + e.getMessage());
        }
        return null;
    }


    private void saveAsset(String assetName, File assetToSave, PreparedStatement statement) {
        try (FileInputStream fileInputStream = new FileInputStream(assetToSave);
            BufferedInputStream assetData = new BufferedInputStream(fileInputStream);

            FileInputStream fileInputStream1 = new FileInputStream(assetToSave);
            FileInputStream fileInputStream2 = new FileInputStream(assetToSave);

            BufferedInputStream bufferedInputStream = new BufferedInputStream(fileInputStream1);
            BufferedInputStream bufferedInputStream1 = new BufferedInputStream(fileInputStream2)) {

            System.out.println(assetData);
            statement.setString(1, assetName);
            statement.setBinaryStream(2, bufferedInputStream);
            statement.setBinaryStream(3, bufferedInputStream1);
            statement.executeUpdate();

        } catch (SQLException e) {
            System.out.println(COULD_NOT_SAVE_THE_ASSET + e.getMessage());
        } catch (FileNotFoundException e) {
            System.out.println(COULD_NOT_FIND_THE_FILE + assetToSave.toString());
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }

    /**
     * Loads all the images involved in a game specified by prefix
     *
     * @param prefix the gameName + the authorName
     * @return a map of the sound names to the input stream data
     */
    public Map<String, InputStream> loadAllSounds(String prefix) throws SQLException {
        return loadAllAssets(prefix, myLoadAllSoundsStatement, SOUND_NAME_COLUMN, SOUND_DATA_COLUMN);
    }

    /**
     * Loads all the images involved in a game specified by prefix
     *
     * @param prefix the gameName + the authorName
     * @return a map of the image names to the input stream data
     */
    public Map<String, InputStream> loadAllImages(String prefix) throws SQLException {
        return loadAllAssets(prefix, myLoadAllImagesStatement, IMAGE_NAME_COLUMN, IMAGE_DATA_COLUMN);
    }

    private Map<String, InputStream> loadAllAssets(String prefix, PreparedStatement statement,
                                                   String assetNameColumn, String assetDataColumn) throws SQLException {
        statement.setString(1, prefix + SQL_WILDCARD);
        ResultSet resultSet = statement.executeQuery();
        Map<String, InputStream> allAssets = new HashMap<>();
        while (resultSet.next()) {
            allAssets.put(resultSet.getString(assetNameColumn), resultSet.getBinaryStream(assetDataColumn));
        }
        return allAssets;
    }

    /**
     * Removes an image from the database for testing purposes
     * @param imageName image to remove
     * @return true if successful
     * @throws SQLException if statement fails
     */
    public boolean removeImage(String imageName) throws SQLException {
        myRemoveImageStatement.setString(1, imageName);
        return myRemoveImageStatement.executeUpdate() > 0;
    }

    /**
     * Removes a sound from the database for testing purposes
     * @param soundName sound to remove
     * @return true if successful
     * @throws SQLException if statement fails
     */
    public boolean removeSound(String soundName) throws SQLException {
        myRemoveSoundStatement.setString(1, soundName);
        return myRemoveSoundStatement.executeUpdate() > 0;
    }
}
