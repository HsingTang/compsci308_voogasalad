package data.external;

import data.external.DataManager;

import java.sql.SQLException;

public class DatabaseCleaner extends DataManager implements IDatabaseCleaner {

    public DatabaseCleaner() {
        super();
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

    /**
     * Just for testing purposes
     * @param userName name of the user whose checkpoint should be removed
     * @param gameName game name
     * @param authorName author name
     * @throws SQLException if statement fails
     */
    @Override
    public void deleteCheckpoints(String userName, String gameName, String authorName) throws SQLException {
        myDatabaseEngine.deleteCheckpoint(userName, gameName, authorName);
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
     * Removes a user account
     *
     * @param userName user name of the user to remove
     * @throws SQLException if operation fails
     */
    @Override
    public void removeUser(String userName) throws SQLException {
        myDatabaseEngine.removeUser(userName);
    }


    /**
     * Removes an image from the database
     *
     * @param imageName name of the image to remove
     * @return true if the image was successfully removed
     * @throws SQLException if operation fails
     */
    @Override
    public boolean removeImage(String imageName) throws SQLException {
        return myDatabaseEngine.removeImage(imageName);
    }

    /**
     * Removes a sound from the database
     *
     * @param soundName name of the sound to remove
     * @return true if the sound was successfully removed
     * @throws SQLException if operation fails
     */
    @Override
    public boolean removeSound(String soundName) throws SQLException {
        return myDatabaseEngine.removeSound(soundName);
    }
}
