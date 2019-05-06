package data.external;

import java.util.Arrays;

/**
 * Packages up all the information about a game rating to communicate with game center about game ratings
 */
public class GameRating {
    private String myUsername;
    private String myGameName;
    private String myAuthorName;
    private int myNumberOfStars;
    private String myComment;

    /**
     * GameRating constructor
     * @param username username
     * @param gameName game name
     * @param authorName author name
     * @param numberOfStars rating
     * @param comment comment
     */
    public GameRating(String username, String gameName, String authorName, int numberOfStars, String comment) {
        myUsername = username;
        myGameName = gameName;
        myAuthorName = authorName;
        myNumberOfStars = numberOfStars;
        myComment = comment;
    }

    /**
     * Getter for the user name
     * @return user name
     */
    public String getUsername() {
        return myUsername;
    }

    /**
     * Getter for the game name
     * @return game name
     */
    public String getGameName() {
        return myGameName;
    }

    /**
     * Getter for the rating
     * @return rating
     */
    public int getNumberOfStars() {
        return myNumberOfStars;
    }

    /**
     * Getter for the comment
     * @return comment
     */
    public String getComment() {
        return myComment;
    }

    /**
     * Getter for the author name
     * @return author name
     */
    public String getAuthorName() {
        return myAuthorName;
    }

    /**
     * Equals method just used for testing purposes
     * @param obj other object to compare to
     * @return true if all instance variables match
     */
    @Override
    public boolean equals(Object obj) {
        if (obj.getClass() == this.getClass()) {
            GameRating other = (GameRating) obj;
            Boolean[] conditions = {myUsername.equals(other.myUsername), myAuthorName.equals(other.myAuthorName), myComment.equals(other.myComment), myGameName.equals(other.myGameName), myNumberOfStars == other.myNumberOfStars};
            return Arrays.stream(conditions).allMatch(Boolean.TRUE::equals);
        }
        return false;
    }
}
