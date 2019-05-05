package data.external;

/**
 * Class that packages together the relevant information about a game to be displayed by the game center
 */
public class GameCenterData {
    private String myTitle;
    private String myImageLocation;
    private String myDescription;
    private String myAuthorName;

    /**
     * GameCenterData constructor
     * @param title title of the game
     * @param description game description
     * @param imageLocation name of the image in the database
     * @param authorName name of the author of the game
     */
    public GameCenterData(String title, String description, String imageLocation, String authorName){
        myTitle = title;
        myDescription = description;
        myImageLocation = imageLocation;
        myAuthorName = authorName;
    }

    public GameCenterData() {
        // empty constructor used for two step constructors in combination with setters
    }

    /**
     * Getter for the title
     * @return title
     */
    public String getTitle() {
        return myTitle;
    }

    /**
     * Setter for the title
     * @param title title
     */
    public void setTitle(String title) {
        myTitle = title;
    }

    /**
     * Getter for the image name
     * @return image name
     */
    public String getImageLocation() {
        return myImageLocation;
    }

    /**
     * Setter for the image location
     * @param imageLocation image location
     */
    public void setImageLocation(String imageLocation) {
        myImageLocation = imageLocation;
    }

    /**
     * Getter for the game description
     * @return game description
     */
    public String getDescription() {
        return myDescription;
    }

    /**
     * Setter for the game description
     * @param description game description
     */
    public void setDescription(String description) {
        myDescription = description;
    }

    /**
     * Getter for the author name
     * @return author name
     */
    public String getAuthorName() {
        return myAuthorName;
    }

    /**
     * Setter for the author name
     * @param authorName author name
     */
    public void setAuthorName(String authorName) {
        myAuthorName = authorName;
    }

}
