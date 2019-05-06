package data.external;

import data.internal.AssetQuerier;
import data.internal.CheckpointQuerier;
import data.internal.GameInformationQuerier;
import data.internal.Querier;
import data.internal.RatingsQuerier;
import data.internal.StatisticsQuerier;
import data.internal.UserQuerier;

import java.io.File;
import java.io.InputStream;
import java.sql.*;
import java.util.List;
import java.util.Map;

/**
 * The DatabaseEngine establishes the connection to the database and has several Querier objects that it delegates
 * the responsibilities of querying the database to.  The DatabaseEngine is designed to be used as a singleton so a
 * connection to the database can be established at the beginning of the program and closed at the end of the project
 * to avoid resource leaks and constantly having to open and close the connection.
 */
public class DatabaseEngine {

    private static final String JDBC_DRIVER = "jdbc:mysql://";
    private static final String IP_ADDRESS = "67.159.94.60";
    private static final String PORT_NUMBER = "3306";
    private static final String DATABASE_NAME = "vooga_byteme";
    private static final String SERVER_TIMEZONE = "serverTimezone=UTC";
    private static final String DATABASE_URL = JDBC_DRIVER + IP_ADDRESS + ":" + PORT_NUMBER + "/" + DATABASE_NAME +
            "?" + SERVER_TIMEZONE;
    private static final String USERNAME = "vooga";
    private static final String PASSWORD = "byteMe!";

    private Connection myConnection;
    private GameInformationQuerier myGameInformationQuerier;
    private AssetQuerier myAssetQuerier;
    private UserQuerier myUserQuerier;
    private RatingsQuerier myRatingsQuerier;
    private CheckpointQuerier myCheckpointQuerier;
    private StatisticsQuerier myStatisticsQuerier;
    private List<Querier> myQueriers;

    private static DatabaseEngine myInstance = new DatabaseEngine();

    private DatabaseEngine() {

    }

    /**
     * The DatabaseEngine class is supposed to be used as a singleton so the connection is only opened once,
     * The getInstance method allows other parts of the program to access the instance to open the connection
     *
     * @return DatabaseEngine
     */
    public static DatabaseEngine getInstance() {
        return myInstance;
    }

    /**
     * Opens the connection to the database, should be called at the very beginning of the program
     *
     * @return true if successfully opened connection to the database
     */
    public boolean open() {
        try {
            myConnection = DriverManager.getConnection(DATABASE_URL, USERNAME, PASSWORD);
            initializeQueriers();
            return true;
        } catch (SQLException exception) {
            System.out.println("Couldn't connect to database" + exception.getMessage());
            return false;
        }
    }

    private void initializeQueriers() throws SQLException {
        myAssetQuerier = new AssetQuerier(myConnection);
        myGameInformationQuerier = new GameInformationQuerier(myConnection);
        myUserQuerier = new UserQuerier(myConnection);
        myRatingsQuerier = new RatingsQuerier(myConnection);
        myCheckpointQuerier = new CheckpointQuerier(myConnection);
        myStatisticsQuerier = new StatisticsQuerier(myConnection);
        myQueriers = List.of(myAssetQuerier, myGameInformationQuerier, myUserQuerier, myRatingsQuerier,
                myCheckpointQuerier, myStatisticsQuerier);
    }

    /**
     * Closes the resources associated with the database, should be called from the stop method
     */
    public void close() {
        try {
            for (Querier querier : myQueriers) {
                querier.closeStatements();
            }
            if (myConnection != null) {
                myConnection.close();
            }
        } catch (SQLException exception) {
            System.out.println("Couldn't close the connection");
        }
    }

    void updateGameEntryData(String gameName, String authorName, String myRawXML) throws SQLException {
        myGameInformationQuerier.updateGameEntryData(gameName, authorName, myRawXML);
    }

    List<String> loadAllGameInformationXMLs() throws SQLException {
        return myGameInformationQuerier.loadAllSerializedGameInformationObjects();
    }

    void updateGameEntryInfo(String gameName, String authorName, String myRawXML) throws SQLException {
        myGameInformationQuerier.updateGameEntryInfo(gameName, authorName, myRawXML);
    }

    void saveImage(String imageName, File imageToSave) {
        myAssetQuerier.saveImage(imageName, imageToSave);
    }

    void saveSound(String soundName, File soundToSave) {
        myAssetQuerier.saveSound(soundName, soundToSave);
    }

    InputStream loadSound(String soundName) {
        return myAssetQuerier.loadSound(soundName);
    }

    InputStream loadImage(String imageName) {
        return myAssetQuerier.loadImage(imageName);
    }

    boolean createUser(String userName, String password) throws SQLException {
        return myUserQuerier.createUser(userName, password);
    }

    boolean authenticateUser(String userName, String password) {
        return myUserQuerier.authenticateUser(userName, password);
    }

    boolean removeUser(String userName) throws SQLException {
        return myUserQuerier.removeUser(userName);
    }

    boolean removeGame(String gameName, String authorName) throws SQLException {
        return myGameInformationQuerier.removeGame(gameName, authorName);
    }

    boolean removeImage(String imageName) throws SQLException {
        return myAssetQuerier.removeImage(imageName);
    }

    boolean removeSound(String soundName) throws SQLException {
        return myAssetQuerier.removeSound(soundName);
    }

    String loadGameData(String gameName, String authorName) throws SQLException {
        return myGameInformationQuerier.loadGameData(gameName, authorName);
    }

    String loadGameInfo(String gameName, String authorName) throws SQLException {
        return myGameInformationQuerier.loadGameInformation(gameName, authorName);
    }

    Map<String, InputStream> loadAllImages(String prefix) throws SQLException {
        return myAssetQuerier.loadAllImages(prefix);
    }

    Map<String, InputStream> loadAllSounds(String prefix) throws SQLException {
        return myAssetQuerier.loadAllSounds(prefix);
    }

    List<String> loadAllGameNames(String userName) throws SQLException {
        return myGameInformationQuerier.loadAllGameNames(userName);
    }

    boolean updatePassword(String userName, String newPassword) throws SQLException {
        return myUserQuerier.updatePassword(userName, newPassword);
    }

    void addGameRating(GameRating rating) throws SQLException {
        myRatingsQuerier.addGameRating(rating);
    }

    double getAverageRating(String gameName) throws SQLException {
        return myRatingsQuerier.getAverageRating(gameName);
    }

    List<GameRating> getAllRatings(String gameName) throws SQLException {
        return myRatingsQuerier.getAllRatings(gameName);
    }

    List<String> loadAllGameInformationXMLs(String userName) throws SQLException {
        return myGameInformationQuerier.loadAllSerializedGameInformationObjects(userName);
    }

    void removeRating(String gameName, String authorName) throws SQLException{
        myRatingsQuerier.removeAllGameRatings(gameName, authorName);
    }

    Map<Timestamp, String> getCheckpoints(String userName, String gameName, String authorName) throws SQLException {
        return myCheckpointQuerier.getCheckpoints(userName, gameName, authorName);
    }

    void saveCheckpoint(String userName, String gameName, String authorName, String rawXML) throws SQLException {
        myCheckpointQuerier.saveCheckpoint(userName,gameName, authorName, rawXML);
    }

    void deleteCheckpoint(String userName, String gameName, String authorName) throws SQLException {
        myCheckpointQuerier.deleteCheckpoints(userName, gameName, authorName);
    }

    void setProfilePic(String userName, File profilePic) throws SQLException {
        myUserQuerier.setProfilePic(userName, profilePic);
    }

    void setBio(String userName, String bio) throws SQLException {
        myUserQuerier.setBio(userName, bio);
    }

    InputStream getProfilePic(String userName) throws SQLException {
        return myUserQuerier.getProfilePic(userName);
    }

    String getBio(String userName) throws SQLException {
        return myUserQuerier.getBio(userName);
    }

    void saveScore(String userName, String gameName, String authorName, Double score) throws SQLException {
        myStatisticsQuerier.saveScore(userName, gameName, authorName, score);
    }

    List<UserScore> loadScores(String gameName, String authorName) throws SQLException {
        return myStatisticsQuerier.loadScores(gameName, authorName);
    }

    void removeScores(String userName, String gameName, String authorName) throws SQLException {
        myStatisticsQuerier.removeScores(userName, gameName, authorName);
    }
}
