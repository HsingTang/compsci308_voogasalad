import data.external.AssetDataManager;
import data.external.DatabaseCleaner;
import data.external.DatabaseEngine;
import data.external.GameDataManager;
import data.external.GameRating;
import data.external.UserDataManager;
import data.external.UserScore;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.InputStream;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Suite of tests for the data module that show the basics of how the module works
 */
public class DataTest {

    private String myUserName1;
    private String myUserName2;
    private String myCorrectPassword;
    private String myIncorrectPassword;
    private String myFakeGameName1;
    private String myFakeGameName2;
    private String myFakeGameData1;
    private String myFakeGameData2;
    private String myFakeGameData3;
    private GameRating myGameRating1;
    private GameRating myGameRating2;
    private UserDataManager myUserDataManager;
    private GameDataManager myGameDataManager;
    private AssetDataManager myAssetDataManager;

    @BeforeAll
    protected static void openDatabaseConnection() {
        // Must open the connection to the database before it can be used
        // DatabaseEngine uses the singleton design pattern
        DatabaseEngine.getInstance().open();
    }

    @BeforeEach
    protected void clearData() {
        myUserDataManager = new UserDataManager();
        myGameDataManager = new GameDataManager();
        myAssetDataManager = new AssetDataManager();
        instantiateVariables();
        clearDatabase();
    }

    @AfterEach
    protected void resetDatabase() {
        clearDatabase();
    }

    @AfterAll
    protected static void closeResources() {
        DatabaseEngine.getInstance().close();
    }

    private void clearDatabase() {
        try {
            DatabaseCleaner databaseCleaner = new DatabaseCleaner();
            databaseCleaner.removeGame(myFakeGameName1, myUserName1);
            databaseCleaner.removeGame(myFakeGameName1, myUserName2);
            databaseCleaner.removeGame(myFakeGameName2, myUserName1);
            databaseCleaner.removeGame(myFakeGameName2, myUserName2);
            databaseCleaner.removeUser(myUserName1);
            databaseCleaner.removeUser(myUserName2);
            databaseCleaner.removeRating(myFakeGameName1, myUserName1);
            databaseCleaner.deleteCheckpoints(myUserName1, myFakeGameName1, myUserName1);
            databaseCleaner.deleteCheckpoints(myUserName2, myFakeGameName1, myUserName1);
            databaseCleaner.removeScores(myUserName1, myFakeGameName1, myUserName1);
            databaseCleaner.removeScores(myUserName2, myFakeGameName1, myUserName1);
        } catch (SQLException exception) {
            // just debugging the test cases, does not get included
            exception.printStackTrace();
        }

    }

    private void instantiateVariables() {
        myUserName1 = "userName1";
        myUserName2 = "userName2";
        myCorrectPassword = "correctPassword";
        myIncorrectPassword = "incorrectPassword";
        myFakeGameName1 = "fakeGameName1";
        myFakeGameName2 = "fakeGameName2";
        myFakeGameData1 = "fakeGameData1";
        myFakeGameData2 = "fakeGameData2";
        myFakeGameData3 = "fakeGameData3";
        myGameRating1 = new GameRating(myUserName1, myFakeGameName1, myUserName1, 4, "User1 comment");
        myGameRating2 = new GameRating(myUserName2, myFakeGameName1, myUserName1, 5, "User2 comment");
    }

    @Test
    public void testCreateUser() {
        // Can only create one user for each user name
        assertTrue(myUserDataManager.createUser(myUserName1, myCorrectPassword));
        assertFalse(myUserDataManager.createUser(myUserName1, myIncorrectPassword));
    }

    @Test
    public void testValidateUser() {
        // User is only validated if they enter the correct username and password
        assertTrue(myUserDataManager.createUser(myUserName1, myCorrectPassword));
        assertTrue(myUserDataManager.validateUser(myUserName1, myCorrectPassword));
        assertFalse(myUserDataManager.validateUser(myUserName1, myIncorrectPassword));
        assertFalse(myUserDataManager.validateUser(myUserName2, myCorrectPassword));
    }

    @Test
    public void testSaveAndLoadGameData() {
        // Data can save any object as "game data" and reload it as long as it is casted properly when loaded
        try {
            myGameDataManager.saveGameData(myFakeGameName1, myUserName1, myFakeGameData1);
            String loadedData = (String) myGameDataManager.loadGameData(myFakeGameName1, myUserName1);
            assertEquals(loadedData, myFakeGameData1);
        } catch (SQLException exception) {
            exception.printStackTrace(); // for debugging purposes in the test
            fail();
        }
    }

    @Test
    public void testInvalidConnection() {
        // If the connection is closed, SQLExceptions will be thrown
        DatabaseEngine.getInstance().close();
        DatabaseCleaner databaseCleaner = new DatabaseCleaner();
        assertThrows(SQLException.class, () -> databaseCleaner.removeUser(myUserName1));
        DatabaseEngine.getInstance().open();
    }

    @Test
    public void testSaveOverride() {
        try {
            myGameDataManager.saveGameData(myFakeGameName1, myUserName1, myFakeGameData1);
            String loadedData = (String) myGameDataManager.loadGameData(myFakeGameName1, myUserName1);
            assertEquals(loadedData, myFakeGameData1);
            myGameDataManager.saveGameData(myFakeGameName1, myUserName1, myFakeGameData2);
            loadedData = (String) myGameDataManager.loadGameData(myFakeGameName1, myUserName1);
            assertEquals(loadedData, myFakeGameData2);
        } catch (SQLException exception) {
            exception.printStackTrace(); // for debugging info in the tests
            fail();
        }
    }

    @Test
    public void testGameNameAuthorNameUniqueness() {
        try {
            myGameDataManager.saveGameData(myFakeGameName1, myUserName1, myFakeGameData1);
            myGameDataManager.saveGameData(myFakeGameName1, myUserName2, myFakeGameData2);
            myGameDataManager.saveGameData(myFakeGameName2, myUserName1, myFakeGameData3);
            String loadedData1 = (String) myGameDataManager.loadGameData(myFakeGameName1, myUserName1);
            String loadedData2 = (String) myGameDataManager.loadGameData(myFakeGameName1, myUserName2);
            String loadedData3 = (String) myGameDataManager.loadGameData(myFakeGameName2, myUserName1);
            assertNotEquals(loadedData1, loadedData2);
            assertNotEquals(loadedData1, loadedData3);
            assertEquals(loadedData1, myFakeGameData1);
            assertEquals(loadedData2, myFakeGameData2);
            assertEquals(loadedData3, myFakeGameData3);
        } catch (SQLException exception) {
            exception.printStackTrace(); // for debugging info in tests
            fail();
        }
    }

    @Test
    public void testUpdatePassword() {
        try {
            myUserDataManager.createUser(myUserName1, myCorrectPassword);
            assertTrue(myUserDataManager.validateUser(myUserName1, myCorrectPassword));
            assertTrue(myUserDataManager.updatePassword(myUserName1, myIncorrectPassword));
            assertTrue(myUserDataManager.validateUser(myUserName1, myIncorrectPassword));
            assertFalse(myUserDataManager.validateUser(myUserName1, myCorrectPassword));
        } catch (SQLException e) {
            e.printStackTrace(); // Just used for debugging purposes in tests
            fail();
        }
    }

    @Test
    public void testLoadAllGameNames() {
        try {
            List<String> expected = new ArrayList<>(Arrays.asList(myFakeGameName1, myFakeGameName2));
            Collections.sort(expected);
            myGameDataManager.saveGameData(myFakeGameName1, myUserName1, myFakeGameData1);
            myGameDataManager.saveGameData(myFakeGameName2, myUserName1, myFakeGameData2);
            List<String> gameNames = myUserDataManager.loadUserGameNames(myUserName1);
            Collections.sort(gameNames);
            assertEquals(gameNames, expected);

        } catch (SQLException e) {
            e.printStackTrace(); // Just for debugging purposes in tests
            fail();
        }
    }

    @Test
    public void testAddRating() {
        try {
            myGameDataManager.addRating(myGameRating1);
            assertEquals(myGameRating1, myGameDataManager.getAllRatings(myFakeGameName1).get(0));
        } catch (SQLException e) {
            e.printStackTrace(); //Just used for debugging purposes in tests
            fail();
        }
    }

    @Test
    void testGetAllRatings() {
        try {
            myGameDataManager.addRating(myGameRating1);
            myGameDataManager.addRating(myGameRating2);
            List<GameRating> loadedRatings = myGameDataManager.getAllRatings(myFakeGameName1);
            assertTrue(loadedRatings.contains(myGameRating1));
            assertTrue(loadedRatings.contains(myGameRating2));
            assertEquals(loadedRatings.size(), 2);
        } catch (SQLException e) {
            e.printStackTrace(); //Just used for debugging purposes in tests
            fail();
        }
    }

    @Test
    void testAverageRating() {
        try {
            myGameDataManager.addRating(myGameRating1);
            myGameDataManager.addRating(myGameRating2);
            assertEquals(4.5, myGameDataManager.getAverageRating(myFakeGameName1), 1e-8);
        } catch (SQLException e) {
            e.printStackTrace(); // just for debugging purposes in tests
            fail();
        }
    }

    @Test
    void testSaveAndLoadCheckpoints() {
        try {
            myGameDataManager.saveCheckpoint(myUserName1, myFakeGameName1, myUserName1, myFakeGameData1);
            myGameDataManager.saveCheckpoint(myUserName2, myFakeGameName1, myUserName1, myFakeGameData1);
            assertEquals(1, myGameDataManager.getCheckpoints(myUserName1, myFakeGameName1, myUserName1).keySet().size());
            assertEquals(1, myGameDataManager.getCheckpoints(myUserName2, myFakeGameName1, myUserName1).keySet().size());
        } catch (SQLException e) {
            // Just debugging in tests so print stack trace
            e.printStackTrace();
            fail();
        }
    }

    @Test
    public void testUserBios() {
        myUserDataManager.createUser(myUserName1, myCorrectPassword);
        try {
            myUserDataManager.setBio(myUserName1, "Expected");
            assertEquals("Expected", myUserDataManager.getBio(myUserName1));
        } catch (SQLException e) {
            e.printStackTrace(); // Just for testing/debugging purposes
            fail();
        }

    }

    @Test
    public void testUserScores() {
        try {
            myGameDataManager.saveScore(myUserName1, myFakeGameName1, myUserName1, 10.0D);
            myGameDataManager.saveScore(myUserName1, myFakeGameName1, myUserName1, 12.0D);
            myGameDataManager.saveScore(myUserName2, myFakeGameName1, myUserName1, 15.0D);
            List<UserScore> userScores = myGameDataManager.loadScores(myFakeGameName1, myUserName1);
            assertTrue(userScores.contains(new UserScore(myUserName2, 15.0D)));
            assertTrue(userScores.contains(new UserScore(myUserName1, 10.0D)));
            assertTrue(userScores.contains(new UserScore(myUserName1, 12.0D)));
            assertEquals(3, userScores.size());
        } catch (SQLException e){
            e.printStackTrace(); // Just for testing/debugging purposes

        }
    }
    

}