package data.internal;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * A Querier used to access the GameInformation table to save and load game information
 */
public class GameInformationQuerier extends Querier {

    private static final String GAME_INFORMATION_TABLE_NAME = "GameInformation";
    private static final String GAME_NAME_COLUMN = "GameName";
    private static final String GAME_DATA_COLUMN = "GameData";
    private static final String GAME_INFO_COLUMN = "GameInfo";
    private static final String AUTHOR_NAME_COLUMN = "AuthorName";

    private static final String SELECT_TWO_CONDITIONS_NOT_NULL = "SELECT %s FROM %s WHERE %s = ? AND %s = ? AND %s IS" + " NOT NULL;";
    private static final String GAME_DATA_INSERT = String.format(INSERT_THREE_VALUES, GAME_INFORMATION_TABLE_NAME, GAME_NAME_COLUMN, AUTHOR_NAME_COLUMN, GAME_DATA_COLUMN);
    private static final String GAME_INFO_INSERT = String.format(INSERT_THREE_VALUES, GAME_INFORMATION_TABLE_NAME, GAME_NAME_COLUMN, AUTHOR_NAME_COLUMN, GAME_INFO_COLUMN);
    private static final String UPDATE_GAME_DATA = String.format(UPDATE_ONE_COLUMN, GAME_DATA_INSERT, ON_DUPLICATE_UPDATE, GAME_DATA_COLUMN);
    private static final String UPDATE_GAME_INFO = String.format(UPDATE_ONE_COLUMN, GAME_INFO_INSERT, ON_DUPLICATE_UPDATE, GAME_INFO_COLUMN);
    private static final String LOAD_GAME_DATA = String.format(SELECT_TWO_CONDITIONS_NOT_NULL, GAME_DATA_COLUMN, GAME_INFORMATION_TABLE_NAME, GAME_NAME_COLUMN, AUTHOR_NAME_COLUMN, GAME_DATA_COLUMN);
    private static final String LOAD_GAME_INFORMATION = String.format(SELECT_TWO_CONDITIONS_NOT_NULL, GAME_INFO_COLUMN, GAME_INFORMATION_TABLE_NAME, GAME_NAME_COLUMN, AUTHOR_NAME_COLUMN, GAME_INFO_COLUMN);
    private static final String FIND_ALL_GAMES = String.format(SELECT_TWO_WHOLE_COLUMNS, GAME_NAME_COLUMN, AUTHOR_NAME_COLUMN, GAME_INFORMATION_TABLE_NAME);
    private static final String REMOVE_GAME = String.format(DELETE_TWO_CONDITIONS, GAME_INFORMATION_TABLE_NAME, GAME_NAME_COLUMN, AUTHOR_NAME_COLUMN);
    private static final String LOAD_GAME_NAMES = String.format(SELECT_ONE_COLUMN_ONE_CONDITION, GAME_NAME_COLUMN, GAME_INFORMATION_TABLE_NAME, AUTHOR_NAME_COLUMN);

    private PreparedStatement myUpdateGameEntryDataStatement;
    private PreparedStatement myUpdateGameEntryInfoStatement;
    private PreparedStatement myLoadGameDataStatement;
    private PreparedStatement myLoadGameInformationStatement;
    private PreparedStatement myFindAllGameNamesStatement;
    private PreparedStatement myRemoveGameStatement;
    private PreparedStatement myLoadGameNamesStatement;

    /**
     * GameInformationQuerier constructor
     *
     * @param connection connection to the database
     * @throws SQLException if statements cannot be prepared
     */
    public GameInformationQuerier(Connection connection) throws SQLException {
        super(connection);
    }

    @Override
    protected void prepareStatements() throws SQLException {
        myUpdateGameEntryDataStatement = myConnection.prepareStatement(UPDATE_GAME_DATA);
        myUpdateGameEntryInfoStatement = myConnection.prepareStatement(UPDATE_GAME_INFO);
        myLoadGameDataStatement = myConnection.prepareStatement(LOAD_GAME_DATA);
        myLoadGameInformationStatement = myConnection.prepareStatement(LOAD_GAME_INFORMATION);
        myFindAllGameNamesStatement = myConnection.prepareStatement(FIND_ALL_GAMES);
        myRemoveGameStatement = myConnection.prepareStatement(REMOVE_GAME);
        myLoadGameNamesStatement = myConnection.prepareStatement(LOAD_GAME_NAMES);
        myPreparedStatements = List.of(myUpdateGameEntryDataStatement, myUpdateGameEntryInfoStatement,
                myLoadGameDataStatement, myLoadGameInformationStatement, myFindAllGameNamesStatement,
                myRemoveGameStatement, myLoadGameNamesStatement);
    }

    /**
     * Deserializes the xml file stored at created_games/gameName/game_data.xml into an object
     *
     * @param gameName the game whose data is to be loaded
     * @return the deserialized game data that should then be cast to a game object
     */
    public String loadGameData(String gameName, String authorName) throws SQLException {
        return loadXML(gameName, authorName, myLoadGameDataStatement, GAME_DATA_COLUMN);
    }

    /**
     * Loads the GameCenterData object associated with the specific gameName and authorName of the game
     * @param gameName name of the game of the GameCenterData object to load
     * @param authorName author name that wrote the game
     * @return rawXML of a serialized Game Center Data object for the game specified by gameName and authorName
     */
    public String loadGameInformation(String gameName, String authorName) throws SQLException{
        return loadXML(gameName, authorName, myLoadGameInformationStatement, GAME_INFO_COLUMN);
    }

    /**
     * Loads the raw xml for all the game info objects from the database to pass to the serializer
     *
     * @return raw xml of game info objects
     */
    public List<String> loadAllSerializedGameInformationObjects() throws SQLException {
        List<String> gameInformations = new ArrayList<>();
        List<GamePrimaryKey> games = getGameNames();
        for (GamePrimaryKey game : games) {
            String gameInfoXML = loadGameInformation(game.getGameName(), game.getAuthorName());
            if (gameInfoXML != null) {
                gameInformations.add(gameInfoXML);
            }
        }
        return gameInformations;
    }

    private List<GamePrimaryKey> getGameNames() throws SQLException {
        List<GamePrimaryKey> games = new ArrayList<>();
        ResultSet resultSet = myFindAllGameNamesStatement.executeQuery();
        while (resultSet.next()) {
            String gameName = resultSet.getString(GAME_NAME_COLUMN);
            String authorName = resultSet.getString(AUTHOR_NAME_COLUMN);
            games.add(new GamePrimaryKey(gameName, authorName));
        }
        return games;
    }

    private String loadXML(String gameName, String authorName, PreparedStatement preparedStatement,
                           String columnName) throws SQLException {
        preparedStatement.setString(1, gameName);
        preparedStatement.setString(2, authorName);
        ResultSet resultSet = preparedStatement.executeQuery();
        if (resultSet.next()) {
            return resultSet.getString(columnName);
        }
        return null;
    }

    /**
     * Updates data entry for a game if it already exists, or creates new game if it doesn't
     *
     * @param gameName   game name
     * @param authorName author name
     * @param myRawXML   serialized game object
     * @throws SQLException if statement fails
     */
    public void updateGameEntryData(String gameName, String authorName, String myRawXML) throws SQLException {
        prepareAndExecuteUpdate(myUpdateGameEntryDataStatement, gameName, authorName, myRawXML);
    }

    private void prepareAndExecuteUpdate(PreparedStatement statement, String gameName, String authorName,
                                         String myRawXML) throws SQLException {
        statement.setString(1, gameName);
        statement.setString(2, authorName);
        statement.setString(3, myRawXML);
        statement.setString(4, myRawXML);
        statement.executeUpdate();
    }

    /**
     * Updates game info entry for a game if it already exists, or creates new game if it doesn't
     *
     * @param gameName   game name
     * @param authorName author name
     * @param myRawXML   serialized game object
     * @throws SQLException if statement fails
     */
    public void updateGameEntryInfo(String gameName, String authorName, String myRawXML) throws SQLException {
        prepareAndExecuteUpdate(myUpdateGameEntryInfoStatement, gameName, authorName, myRawXML);
    }

    /**
     * Removes the game called gameName written by authorName
     *
     * @param gameName   name of the game to remove
     * @param authorName name of the author that wrote the game to remove
     * @return true if game successfully removed
     * @throws SQLException if statement fails
     */
    public boolean removeGame(String gameName, String authorName) throws SQLException {
        myRemoveGameStatement.setString(1, gameName);
        myRemoveGameStatement.setString(2, authorName);
        return myRemoveGameStatement.executeUpdate() > 0;
    }

    /**
     * Loads all the game names that a user has authored
     * @param userName user in question
     * @return list of all the names of the games that user has authored
     * @throws SQLException if statement fails
     */
    public List<String> loadAllGameNames(String userName) throws SQLException {
        List<String> gameNames = new ArrayList<>();
        myLoadGameNamesStatement.setString(1, userName);
        ResultSet resultSet = myLoadGameNamesStatement.executeQuery();
        while (resultSet.next()) {
            gameNames.add(resultSet.getString(GAME_NAME_COLUMN));
        }
        return gameNames;
    }

    /**
     * Loads the raw xml for all the game info objects from the database to pass to the serializer
     * @return raw xml of game info objects
     */
    public List<String> loadAllSerializedGameInformationObjects(String userName) throws SQLException {
        List<String> serializedGameInfoObjects = new ArrayList<>();
        List<GamePrimaryKey> games = getGameNames();
        for (GamePrimaryKey game : games) {
            if (game.getAuthorName().equals(userName)) {
                String gameInfoXML = loadGameInformation(game.getGameName(), userName);
                if (gameInfoXML != null) {
                    serializedGameInfoObjects.add(gameInfoXML);
                }
            }
        }
        return serializedGameInfoObjects;
    }
}
