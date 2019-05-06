package data.external;

import java.io.File;
import java.io.InputStream;
import java.sql.SQLException;
import java.util.List;

public interface UserDataExternal {
    /**
     * Creates a user in the data base
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
     * @return true if valid user name and password combination
     */
    boolean validateUser(String userName, String password);

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
}
