package data.external;


import java.io.File;
import java.io.InputStream;
import java.sql.SQLException;
import java.util.List;

public class UserDataManager extends DataManager implements UserDataExternal {

    private static final String COULD_NOT_CREATE_USER = "Could not create user";

    /**
     * DataManager constructor creates a new serializer and connects to the the Database
     */
    public UserDataManager() {
        super();
    }

    /**
     * Creates a user in the data base
     *
     * @param userName name of the user
     * @param password user's password
     * @return true if the user was successfully created
     */
    @Override
    public boolean createUser(String userName, String password) {
        boolean success = false;
        try {
            success = myDatabaseEngine.createUser(userName, password);
        } catch (SQLException e) {
            System.out.println(COULD_NOT_CREATE_USER + e.getMessage());
        }
        return success;
    }

    /**
     * Validates a user's login attempt
     *
     * @param userName entered user name
     * @param password entered password
     * @return true if valid user name and password combination
     */
    @Override
    public boolean validateUser(String userName, String password) {
        return myDatabaseEngine.authenticateUser(userName, password);
    }

    /**
     * Loads all the names of the games that a user has created
     *
     * @param userName user name of the user whose games are to be loaded
     * @return list of the names of all the games of the user has created
     * @throws SQLException if operation fails
     */
    @Override
    public List<String> loadUserGameNames(String userName) throws SQLException {
        return myDatabaseEngine.loadAllGameNames(userName);
    }

    /**
     * Updates the specified user's password in the database
     * @param userName user whose password should be updated
     * @param newPassword the new password it should be updated to
     * @return true if successful, false else
     * @throws SQLException if statement fails
     */
    @Override
    public boolean updatePassword(String userName, String newPassword) throws SQLException {
        return myDatabaseEngine.updatePassword(userName, newPassword);
    }

    /**
     * Sets the profile pic for a user in the database
     * @param userName user's username
     * @param profilePic profile pic to set
     * @throws SQLException if statement fails
     */
    @Override
    public void setProfilePic(String userName, File profilePic) throws SQLException {
        myDatabaseEngine.setProfilePic(userName, profilePic);
    }

    /**
     * Sets the bio for a user in the database
     * @param userName user's username
     * @throws SQLException if statement fails
     */
    @Override
    public void setBio(String userName, String bio) throws SQLException {
        myDatabaseEngine.setBio(userName, bio);
    }

    /**
     * Retrieves the profile picture of a user as an InputStream from the database
     * @param userName user name of the user whose profile pic should be retrieved
     * @return an InputStream of the profile picture for that user
     * @throws SQLException if statement fails
     */
    @Override
    public InputStream getProfilePic(String userName) throws SQLException {
        return myDatabaseEngine.getProfilePic(userName);
    }

    /**
     * Retrieves the bio of a user in the database
     * @param userName user whose bio should be retrieved
     * @return bio
     * @throws SQLException if statement fails
     */
    @Override
    public String getBio(String userName) throws SQLException {
        return myDatabaseEngine.getBio(userName);
    }
}
