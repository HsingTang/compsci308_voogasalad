package data.internal;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;

/**
 * Querier class to access and query the User table to perform user authentication
 */
public class UserQuerier extends Querier {

    private static final String USERS_TABLE_NAME = "Users";
    private static final String USERNAME_COLUMN = "UserName";
    private static final String PASSWORD_COLUMN = "Password";
    private static final String PROFILE_PIC_COLUMN = "ProfilePicture";
    private static final String BIO_COLUMN = "Bio";

    private static final String UPDATE_USERS_TABLE = "UPDATE %s SET %s = ? WHERE %s = ?";
    private static final String SELECT_ONE_COLUMN = "SELECT %s FROM %s WHERE %s = ?";

    private static final String GET_HASHED_PASSWORD = String.format(SELECT_ONE_COLUMN_ONE_CONDITION, PASSWORD_COLUMN, USERS_TABLE_NAME, USERNAME_COLUMN);
    private static final String CREATE_USER = String.format(INSERT_TWO_VALUES, USERS_TABLE_NAME, USERNAME_COLUMN, PASSWORD_COLUMN);
    private static final String DELETE_USER = String.format(DELETE_ONE_CONDITION, USERS_TABLE_NAME, USERNAME_COLUMN);
    private static final String SET_PROFILE_PIC = String.format(UPDATE_USERS_TABLE, USERS_TABLE_NAME, PROFILE_PIC_COLUMN, USERNAME_COLUMN);
    private static final String SET_BIO = String.format(UPDATE_USERS_TABLE, USERS_TABLE_NAME, BIO_COLUMN, USERNAME_COLUMN);
    private static final String GET_PROFILE_PIC = String.format(SELECT_ONE_COLUMN, PROFILE_PIC_COLUMN, USERS_TABLE_NAME, USERNAME_COLUMN);
    private static final String GET_BIO = String.format(SELECT_ONE_COLUMN, BIO_COLUMN, USERS_TABLE_NAME, USERNAME_COLUMN);

    private static final String HASH_ALGORITHM = "SHA-256";

    private static final String COULD_NOT_VALIDATE_USER = "Could not validate user: ";
    private static final String COULD_NOT_GENERATE_HASH = "Could not generate hash: ";
    private static final String COULD_NOT_LOAD_IMAGE = "COULD NOT LOAD IMAGE ";

    private PreparedStatement myGetPasswordStatement;
    private PreparedStatement myCreateUserStatement;
    private PreparedStatement myDeleteUserStatement;
    private PreparedStatement mySetUserBioStatement;
    private PreparedStatement myGetUserBioStatement;
    private PreparedStatement mySetUserProfilePicStatement;
    private PreparedStatement myGetUserProfilePicStatement;


    /**
     * UserQuerier constructor
     *
     * @param connection connection to the database
     * @throws SQLException if cannot prepare statements
     */
    public UserQuerier(Connection connection) throws SQLException {
        super(connection);
    }

    @Override
    protected void prepareStatements() throws SQLException {
        myGetPasswordStatement = myConnection.prepareStatement(GET_HASHED_PASSWORD);
        myCreateUserStatement = myConnection.prepareStatement(CREATE_USER);
        myDeleteUserStatement = myConnection.prepareStatement(DELETE_USER);
        mySetUserBioStatement = myConnection.prepareStatement(SET_BIO);
        myGetUserBioStatement = myConnection.prepareStatement(GET_BIO);
        mySetUserProfilePicStatement = myConnection.prepareStatement(SET_PROFILE_PIC);
        myGetUserProfilePicStatement = myConnection.prepareStatement(GET_PROFILE_PIC);
        myPreparedStatements = List.of(myGetPasswordStatement, myCreateUserStatement, myDeleteUserStatement,
                mySetUserBioStatement, mySetUserProfilePicStatement, myGetUserBioStatement, myGetUserProfilePicStatement);
    }

    /**
     * Authenticates a user's login attempt
     *
     * @param userName entered user name
     * @param password entered password
     * @return true if valid user name and password combination
     */
    public boolean authenticateUser(String userName, String password) {
        try {
            String hashedPassword = generateHashedPassword(password);
            String storedHash = retrieveStoredHash(userName);
            return hashedPassword.equals(storedHash);
        } catch (NoSuchAlgorithmException e) {
            // do nothing, should never fail
        } catch (SQLException e) {
            System.out.println(COULD_NOT_VALIDATE_USER + e.getMessage());
        }
        return false;
    }

    /**
     * Creates a new user entry in the database
     *
     * @param userName user name of new user
     * @param password password of new user
     * @return true if successfully creates a new user entry
     * @throws SQLException if statement fails
     */
    public boolean createUser(String userName, String password) throws SQLException {
        myCreateUserStatement.setString(1, userName);
        try {
            myCreateUserStatement.setString(2, generateHashedPassword(password));
        } catch (NoSuchAlgorithmException e) {
            System.out.println(COULD_NOT_GENERATE_HASH + e.getMessage());
        }
        int updates = myCreateUserStatement.executeUpdate();
        return updates == 1;
    }

    /**
     * Updates the specified user's password in the database
     * @param userName user whose password should be updated
     * @param password the new password it should be updated to
     * @return true if successful, false else
     * @throws SQLException if statement fails
     */
    public boolean updatePassword(String userName, String password) throws SQLException {
        if (removeUser(userName)) {
            return createUser(userName, password);
        }
        return false;
    }

    private String retrieveStoredHash(String userName) throws SQLException {
        myGetPasswordStatement.setString(1, userName);
        ResultSet resultSet = myGetPasswordStatement.executeQuery();
        if (resultSet.next()) {
            return resultSet.getString(PASSWORD_COLUMN);
        } else {
            return "";
        }
    }

    // Hashing process adapted from: https://howtodoinjava.com/security/how-to-generate-secure-password-hash-md5-sha-pbkdf2-bcrypt-examples/
    // Author: Lokesh Gupta
    private String generateHashedPassword(String password) throws NoSuchAlgorithmException {
        MessageDigest messageDigest = MessageDigest.getInstance(HASH_ALGORITHM);
        byte[] bytes = messageDigest.digest(password.getBytes());
        StringBuilder stringBuilder = new StringBuilder();
        for (byte bite : bytes) {
            stringBuilder.append(Integer.toString((bite & 0xff) + 0x100, 16).substring(1));
        }
        return stringBuilder.toString();
    }

    /**
     * Removes a user from the database
     *
     * @param userName name of the user to delete
     * @return true if any users were deleted
     * @throws SQLException if statement fails
     */
    public boolean removeUser(String userName) throws SQLException {
        myDeleteUserStatement.setString(1, userName);
        int affectedRows = myDeleteUserStatement.executeUpdate();
        return affectedRows > 0;
    }

    /**
     * Sets the profile pic for a user in the database
     * @param userName user's username
     * @param profilePic profile pic to set
     * @throws SQLException if statement fails
     */
    public void setProfilePic(String userName, File profilePic) throws SQLException {
        System.out.println("Called set profile pic");
        try (BufferedInputStream bufferedInputStream = new BufferedInputStream(new FileInputStream(profilePic))) {
            mySetUserProfilePicStatement.setBinaryStream(1, bufferedInputStream);
            mySetUserProfilePicStatement.setString(2, userName);
            mySetUserProfilePicStatement.execute();
            System.out.println("Statement executed");
        } catch (IOException e) {
            System.out.println(COULD_NOT_LOAD_IMAGE + e.getMessage());
        }
    }

    /**
     * Sets the bio for a user in the database
     * @param userName user's username
     * @throws SQLException if statement fails
     */
    public void setBio(String userName, String bio) throws SQLException {
        mySetUserBioStatement.setString(1, bio);
        mySetUserBioStatement.setString(2, userName);
        mySetUserBioStatement.execute();
    }

    /**
     * Retrieves the profile picture of a user as an InputStream from the database
     * @param userName user name of the user whose profile pic should be retrieved
     * @return an InputStream of the profile picture for that user
     * @throws SQLException if statement fails
     */
    public InputStream getProfilePic(String userName) throws SQLException {
        myGetUserProfilePicStatement.setString(1, userName);
        ResultSet resultSet = myGetUserProfilePicStatement.executeQuery();
        if (resultSet.next()){
            return resultSet.getBinaryStream(PROFILE_PIC_COLUMN);
        } else {
            throw new SQLException();
        }
    }

    /**
     * Retrieves the bio of a user in the database
     * @param userName user whose bio should be retrieved
     * @return bio
     * @throws SQLException if statement fails
     */
    public String getBio(String userName) throws SQLException {
        myGetUserBioStatement.setString(1, userName);
        ResultSet resultSet = myGetUserBioStatement.executeQuery();
        if (resultSet.next()){
            return resultSet.getString(BIO_COLUMN);
        } else {
            throw new SQLException();
        }
    }

}
