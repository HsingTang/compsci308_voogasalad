import data.external.DataManager;
import data.external.DatabaseEngine;
import data.external.GameRating;
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
    private DataManager myDataManager;

    @BeforeAll
    protected static void openDatabaseConnection() {
        // Must open the connection to the database before it can be used
        // DatabaseEngine uses the singleton design pattern
        DatabaseEngine.getInstance().open();
    }

    @BeforeEach
    protected void clearData() {
        myDataManager = new DataManager();
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
            myDataManager.removeGame(myFakeGameName1, myUserName1);
            myDataManager.removeGame(myFakeGameName1, myUserName2);
            myDataManager.removeGame(myFakeGameName2, myUserName1);
            myDataManager.removeGame(myFakeGameName2, myUserName2);
            myDataManager.removeUser(myUserName1);
            myDataManager.removeUser(myUserName2);
            myDataManager.removeRating(myFakeGameName1, myUserName1);
            myDataManager.deleteCheckpoints(myUserName1, myFakeGameName1, myUserName1);
            myDataManager.deleteCheckpoints(myUserName2, myFakeGameName1, myUserName1);
            myDataManager.removeScores(myUserName1, myFakeGameName1, myUserName1);
            myDataManager.removeScores(myUserName2, myFakeGameName1, myUserName1);
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
        assertTrue(myDataManager.createUser(myUserName1, myCorrectPassword));
        assertFalse(myDataManager.createUser(myUserName1, myIncorrectPassword));
    }

    @Test
    public void testValidateUser() {
        // User is only validated if they enter the correct username and password
        assertTrue(myDataManager.createUser(myUserName1, myCorrectPassword));
        assertTrue(myDataManager.validateUser(myUserName1, myCorrectPassword));
        assertFalse(myDataManager.validateUser(myUserName1, myIncorrectPassword));
        assertFalse(myDataManager.validateUser(myUserName2, myCorrectPassword));
    }

    @Test
    public void testSaveAndLoadGameData() {
        // Data can save any object as "game data" and reload it as long as it is casted properly when loaded
        try {
            myDataManager.saveGameData(myFakeGameName1, myUserName1, myFakeGameData1);
            String loadedData = (String) myDataManager.loadGameData(myFakeGameName1, myUserName1);
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
        assertThrows(SQLException.class, () -> myDataManager.removeUser(myUserName1));
        DatabaseEngine.getInstance().open();
    }

    @Test
    public void testSaveOverride() {
        try {
            myDataManager.saveGameData(myFakeGameName1, myUserName1, myFakeGameData1);
            String loadedData = (String) myDataManager.loadGameData(myFakeGameName1, myUserName1);
            assertEquals(loadedData, myFakeGameData1);
            myDataManager.saveGameData(myFakeGameName1, myUserName1, myFakeGameData2);
            loadedData = (String) myDataManager.loadGameData(myFakeGameName1, myUserName1);
            assertEquals(loadedData, myFakeGameData2);
        } catch (SQLException exception) {
            exception.printStackTrace(); // for debugging info in the tests
            fail();
        }
    }

    @Test
    public void testGameNameAuthorNameUniqueness() {
        try {
            myDataManager.saveGameData(myFakeGameName1, myUserName1, myFakeGameData1);
            myDataManager.saveGameData(myFakeGameName1, myUserName2, myFakeGameData2);
            myDataManager.saveGameData(myFakeGameName2, myUserName1, myFakeGameData3);
            String loadedData1 = (String) myDataManager.loadGameData(myFakeGameName1, myUserName1);
            String loadedData2 = (String) myDataManager.loadGameData(myFakeGameName1, myUserName2);
            String loadedData3 = (String) myDataManager.loadGameData(myFakeGameName2, myUserName1);
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
            myDataManager.createUser(myUserName1, myCorrectPassword);
            assertTrue(myDataManager.validateUser(myUserName1, myCorrectPassword));
            assertTrue(myDataManager.updatePassword(myUserName1, myIncorrectPassword));
            assertTrue(myDataManager.validateUser(myUserName1, myIncorrectPassword));
            assertFalse(myDataManager.validateUser(myUserName1, myCorrectPassword));
        } catch (SQLException e) {
            e.printStackTrace(); // Just used for debugging purposes in tests
            fail();
        }
    }

    @Test
    public void testLoadAllGameNames() {
        try {
            Map<String, InputStream> map = myDataManager.loadAllImages("center/");
            for (String imageName : map.keySet()) {
                System.out.println(imageName);
            }
            List<String> expected = new ArrayList<>(Arrays.asList(myFakeGameName1, myFakeGameName2));
            Collections.sort(expected);
            myDataManager.saveGameData(myFakeGameName1, myUserName1, myFakeGameData1);
            myDataManager.saveGameData(myFakeGameName2, myUserName1, myFakeGameData2);
            List<String> gameNames = myDataManager.loadUserGameNames(myUserName1);
            Collections.sort(gameNames);
            assertEquals(gameNames, expected);

        } catch (SQLException e) {
            e.printStackTrace(); // Just for debugging purposes in tests
            fail();
        }
    }

    public void getYeet3() {
        myDataManager.saveGameData("903", "Ryan", "Testing string");

    }

    @Test
    public void testAddRating() {
        try {
            myDataManager.addRating(myGameRating1);
            assertEquals(myGameRating1, myDataManager.getAllRatings(myFakeGameName1).get(0));
        } catch (SQLException e) {
            e.printStackTrace(); //Just used for debugging purposes in tests
            fail();
        }
    }

    @Test
    void testGetAllRatings() {
        try {
            myDataManager.addRating(myGameRating1);
            myDataManager.addRating(myGameRating2);
            List<GameRating> loadedRatings = myDataManager.getAllRatings(myFakeGameName1);
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
            myDataManager.addRating(myGameRating1);
            myDataManager.addRating(myGameRating2);
            assertEquals(4.5, myDataManager.getAverageRating(myFakeGameName1), 1e-8);
        } catch (SQLException e) {
            e.printStackTrace(); // just for debugging purposes in tests
            fail();
        }
    }

    @Test
    void testSaveAndLoadCheckpoints() {
        try {
            myDataManager.saveCheckpoint(myUserName1, myFakeGameName1, myUserName1, myFakeGameData1);
            myDataManager.saveCheckpoint(myUserName2, myFakeGameName1, myUserName1, myFakeGameData1);
            assertEquals(1, myDataManager.getCheckpoints(myUserName1, myFakeGameName1, myUserName1).keySet().size());
            assertEquals(1, myDataManager.getCheckpoints(myUserName2, myFakeGameName1, myUserName1).keySet().size());
        } catch (SQLException e) {
            // Just debugging in tests so print stack trace
            e.printStackTrace();
            fail();
        }
    }

    @Test
    public void testUserBios() {
        myDataManager.createUser(myUserName1, myCorrectPassword);
        try {
            myDataManager.setBio(myUserName1, "Expected");
            assertEquals("Expected", myDataManager.getBio(myUserName1));
        } catch (SQLException e) {
            e.printStackTrace(); // Just for testing/debugging purposes
            fail();
        }

    }

    @Test
    public void testUserScores() {
        try {
            myDataManager.saveScore(myUserName1, myFakeGameName1, myUserName1, 10.0D);
            myDataManager.saveScore(myUserName1, myFakeGameName1, myUserName1, 12.0D);
            myDataManager.saveScore(myUserName2, myFakeGameName1, myUserName1, 15.0D);
            List<UserScore> userScores = myDataManager.loadScores(myFakeGameName1, myUserName1);
            assertTrue(userScores.contains(new UserScore(myUserName2, 15.0D)));
            assertTrue(userScores.contains(new UserScore(myUserName1, 10.0D)));
            assertTrue(userScores.contains(new UserScore(myUserName1, 12.0D)));
            assertEquals(3, userScores.size());
        } catch (SQLException e){
            e.printStackTrace(); // Just for testing/debugging purposes

        }
    }
    

}