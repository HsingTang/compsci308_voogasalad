package data.internal;

/**
 * Packages a primary key for a game as the game name and the author name
 */
public class GamePrimaryKey {

    private String myGameName;
    private String myAuthorName;

    /**
     * Constructor for a game primary key (in database gameName + authorName is unique)
     * @param gameName name of the game
     * @param authorName name of the author
     */
    GamePrimaryKey(String gameName, String authorName){
        myGameName = gameName;
        myAuthorName = authorName;
    }

    /**
     * Getter for game name
     * @return game name
     */
    public String getGameName() {
        return myGameName;
    }

    /**
     * Getter for author name
     * @return author name
     */
    public String getAuthorName() {
        return myAuthorName;
    }

    @Override
    public boolean equals(Object obj) {
        return obj.getClass() == this.getClass() &&
                this.myGameName.equals(((GamePrimaryKey) obj).myGameName) &&
                this.myAuthorName.equals(((GamePrimaryKey) obj).myAuthorName);
    }
}
