package data.external;

import java.sql.SQLException;

public interface IDatabaseCleaner {
    /**
     * Just used for testing purposes to remove score
     * @param userName name of the user
     * @param gameName game name
     * @param authorName author name
     * @throws SQLException if statement fails
     */
    void removeScores(String userName, String gameName, String authorName) throws SQLException;

    /**
     * Just for testing purposes
     * @param userName name of the user whose checkpoint should be removed
     * @param gameName game name
     * @param authorName author name
     * @throws SQLException if statement fails
     */
    void deleteCheckpoints(String userName, String gameName, String authorName) throws SQLException;

    /**
     * Method just used for testing purposes
     * @param gameName name of the game to remove
     * @param authorName author of the game to remove
     * @throws SQLException if statement fails
     */
    void removeRating(String gameName, String authorName) throws SQLException;

    /**
     * Removes a game from the database
     *
     * @param gameName   name of the game to remove
     * @param authorName author of the game to remove
     * @throws SQLException if operation fails
     */
    void removeGame(String gameName, String authorName) throws SQLException;

    /**
     * Removes a user account
     *
     * @param userName user name of the user to remove
     * @throws SQLException if operation fails
     */
    void removeUser(String userName) throws SQLException;

    /**
     * Removes an image from the database
     *
     * @param imageName name of the image to remove
     * @return true if the image was successfully removed
     * @throws SQLException if operation fails
     */
    boolean removeImage(String imageName) throws SQLException;

    /**
     * Removes a sound from the database
     *
     * @param soundName name of the sound to remove
     * @return true if the sound was successfully removed
     * @throws SQLException if operation fails
     */
    boolean removeSound(String soundName) throws SQLException;
}
