package data.internal;

import data.external.GameRating;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * A Querier that is used to manage saving and loading ratings
 */
public class RatingsQuerier extends Querier {

    private static final String GAME_RATINGS_TABLE_NAME = "GameRatings";
    private static final String GAME_NAME_COLUMN = "GameName";
    private static final String AUTHOR_NAME_COLUMN = "AuthorName";
    private static final String USER_NAME_COLUMN = "UserName";
    private static final String RATING_COLUMN = "Rating";
    private static final String COMMENTS_COLUMN = "Comments";
    private static final String AVERAGE = "average";

    private static final String UPDATE_GAME_RATINGS = String.format(" %s = ?, %s = ?", RATING_COLUMN, COMMENTS_COLUMN);
    private static final String INSERT_GAME_RATINGS = String.format("INSERT INTO %s (%s, %s, %s, %s, %s) VALUES (?, ?, ?, ?, ?)", GAME_RATINGS_TABLE_NAME, GAME_NAME_COLUMN, AUTHOR_NAME_COLUMN, USER_NAME_COLUMN, RATING_COLUMN, COMMENTS_COLUMN);

    private static final String AVERAGE_RATINGS_STATEMENT = String.format("SELECT AVG(%s) AS %s FROM %s WHERE %s = ?",
            RATING_COLUMN, AVERAGE, GAME_RATINGS_TABLE_NAME, GAME_NAME_COLUMN);
    private static final String ALL_RATINGS_STATEMENT = String.format("SELECT * FROM %s WHERE %s = ?", GAME_RATINGS_TABLE_NAME, GAME_NAME_COLUMN);
    private static final String UPDATE_RATING_STATEMENT = String.format("%s %s %s", INSERT_GAME_RATINGS, ON_DUPLICATE_UPDATE, UPDATE_GAME_RATINGS);
    private static final String REMOVE_RATING_STATEMENT = String.format(DELETE_TWO_CONDITIONS, GAME_RATINGS_TABLE_NAME, GAME_NAME_COLUMN, AUTHOR_NAME_COLUMN);

    private PreparedStatement myAverageRatingsStatement;
    private PreparedStatement myAllRatingsStatement;
    private PreparedStatement myUpdateRatingStatement;
    private PreparedStatement myRemoveRatingsStatement;

    public RatingsQuerier(Connection connection) throws SQLException {
        super(connection);
    }

    @Override
    protected void prepareStatements() throws SQLException {
        myAverageRatingsStatement = myConnection.prepareStatement(AVERAGE_RATINGS_STATEMENT);
        myAllRatingsStatement = myConnection.prepareStatement(ALL_RATINGS_STATEMENT);
        myUpdateRatingStatement = myConnection.prepareStatement(UPDATE_RATING_STATEMENT);
        myRemoveRatingsStatement = myConnection.prepareStatement(REMOVE_RATING_STATEMENT);
        myPreparedStatements = List.of(myAverageRatingsStatement, myAllRatingsStatement, myUpdateRatingStatement, myRemoveRatingsStatement);
    }

    /**
     * Adds a rating to the database for a specific game
     * @param rating GameRating object that contains the rating information
     * @throws SQLException if statement fails
     */
    public void addGameRating(GameRating rating) throws SQLException {
        myUpdateRatingStatement.setString(1, rating.getGameName());
        myUpdateRatingStatement.setString(2, rating.getAuthorName());
        myUpdateRatingStatement.setString(3, rating.getUsername());
        myUpdateRatingStatement.setInt(4, rating.getNumberOfStars());
        myUpdateRatingStatement.setString(5, rating.getComment());
        myUpdateRatingStatement.setInt(6, rating.getNumberOfStars());
        myUpdateRatingStatement.setString(7, rating.getComment());
        myUpdateRatingStatement.execute();
    }

    /**
     * Returns a list of all the ratings for a specific game
     * @param gameName name of the game to get the ratings for
     * @return a list of all the ratings for a specific game
     * @throws SQLException if statement fails
     */
    public List<GameRating> getAllRatings(String gameName) throws SQLException {
        List<GameRating> gameRatings = new LinkedList<>();
        myAllRatingsStatement.setString(1, gameName);
        ResultSet resultSet = myAllRatingsStatement.executeQuery();
        while (resultSet.next()) {
            gameRatings.add(new GameRating(resultSet.getString(USER_NAME_COLUMN),
                    resultSet.getString(GAME_NAME_COLUMN), resultSet.getString(AUTHOR_NAME_COLUMN),
                    resultSet.getInt(RATING_COLUMN), resultSet.getString(COMMENTS_COLUMN)));
        }
        return gameRatings;
    }

    /**
     * Returns the average rating for a game
     * @param gameName name to retrieve the average rating for
     * @return the average rating for the game gameName
     * @throws SQLException if statement fails
     */
    public double getAverageRating(String gameName) throws SQLException {
        myAverageRatingsStatement.setString(1, gameName);
        ResultSet resultSet = myAverageRatingsStatement.executeQuery();
        if (resultSet.next()) {
            return resultSet.getDouble(AVERAGE);
        }
        return -1.0D;
    }

    /**
     * Used for testing purposes
     * @param gameName name of the game
     * @param authorName name of the author
     * @throws SQLException if statement fails
     */
    public void removeAllGameRatings(String gameName, String authorName) throws SQLException {
        myRemoveRatingsStatement.setString(1, gameName);
        myRemoveRatingsStatement.setString(2, authorName);
        myRemoveRatingsStatement.executeUpdate();
    }
}
