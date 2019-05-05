/**
 * @Author Megan Phibbons
 * @Date April 2019
 * @Purpose Display the list of all ratings that users have received
 * @Dependencies javafx, GameCenterData, and Data.DataManager
 * @Uses: This class is called on in the GameCard when a user clicks on Read More
 */

package frontend.ratings;

import data.external.DataManager;
import data.external.GameCenterData;
import data.external.GameRating;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class RatingList {
    private GameCenterData myData;
    private BorderPane myDisplay;
    private List<GameRating> myRatings;
    private DataManager myManager;
    private static final int PADDING = 10;

    /**
     * @purpose constructor so that the RatingList can be initialized for being displayed later.
     * @param data the GameCenterData that the ratings are for
     * @param manager the DataManager that allows us to get existing ratings
     */
    public RatingList(GameCenterData data, DataManager manager) {
        myData = data;
        myManager = manager;
        try {
            myRatings = myManager.getAllRatings(data.getTitle());
        } catch (SQLException e) {
            myRatings = new ArrayList<>(); // worst case, the ratings list is just empty.
        }
        initializeDisplay();
    }

    /**
     * @purpose provide a method to get the display so that the GameList can be added into the rest of the project without causing issues
     * @return the current display that the RatingList has.
     */
    public Pane getDisplay() {
        return myDisplay;
    }

    private void initializeDisplay() {
        myDisplay = new BorderPane();
        VBox allRatings = new VBox();
        for(GameRating rating : myRatings) {
            SingleRating ratingDisplay = new SingleRating(rating, myManager, myData);
            allRatings.getChildren().add(ratingDisplay.getDisplay());
        }
        allRatings.setSpacing(PADDING);
        myDisplay.setCenter(allRatings);
    }

}
