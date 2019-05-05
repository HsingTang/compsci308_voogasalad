/**
 * @Author Megan Phibbons
 * @Date April 2019
 * @Purpose Displays individual ratings complete with user profile picture, number of stars, and their comment
 * @Dependencies javafx, GameCenterData, and Data.DataManager
 * @Uses: This class is called repeatedly by RatingList to avoid redundant code in RatingList
 */

package frontend.ratings;

import data.external.DataManager;
import data.external.GameCenterData;
import data.external.GameRating;
import frontend.popups.GamePage;
import javafx.geometry.Pos;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.text.Text;

import java.io.FileInputStream;
import java.io.FileNotFoundException;

public class SingleRating {
    private static final String BODY_SELECTOR = "bodyfont";
    private static final String PADDING_SELECTOR = "contentpadding";
    private static final String RATING_SELECTOR = "gamepageratingbody";
    private static final double WRAP_OFFSET = 100;
    private static final int PADDING = 5;
    private GameRating myRating;
    private DataManager myManager;
    private GameCenterData myData;
    private BorderPane myDisplay;
    private String myUsername;

    private static final int IMAGE_WIDTH = 40;
    private static final String DEFAULT_IMAGE_LOCATION = "center/data/profile_information/images/default.png";

    /**
     * @purpose constructor that initializes all values and the display
     * @param rating the rating information that needs to be displayed
     * @param manager the DataManager that allows getting more information when necessary
     * @param data the GameCenterData corresponding to the rated object
     */
    public SingleRating(GameRating rating, DataManager manager, GameCenterData data) {
        myRating = rating;
        myManager = manager;
        myData = data;
        myUsername = rating.getUsername();
        initializeDisplay();
    }

    /**
     * @purpose create a display that can be added into any other component of the project
     * @return the pane corresponding to the current display
     */
    public Pane getDisplay() {
        return myDisplay;
    }

    private void initializeDisplay() {
        myDisplay = new BorderPane();
        addUserImage();
        addRatingBody();
    }

    private void addUserImage() {
        ImageView userImage;
        try {
            userImage = new ImageView(new Image(myManager.getProfilePic(myUsername)));
        } catch (Exception e) {
            try {
                userImage = new ImageView(new Image(new FileInputStream(DEFAULT_IMAGE_LOCATION)));
            } catch (FileNotFoundException e1) {
                // do nothing, there isn't really anything else we can do
                return;
            }
        }
        userImage.setPreserveRatio(true);
        userImage.setFitWidth(IMAGE_WIDTH);
        BorderPane.setAlignment(userImage, Pos.CENTER);
        myDisplay.setLeft(userImage);
    }

    private void addRatingBody() {
        BorderPane ratingBody = new BorderPane();
        addHeader(ratingBody);
        addText(ratingBody);
        ratingBody.getStyleClass().add(PADDING_SELECTOR);
        myDisplay.setCenter(ratingBody);
    }

    private void addHeader(BorderPane pane) {
        HBox box = new HBox();
        Text username = new Text(myUsername);
        username.getStyleClass().add(BODY_SELECTOR);
        box.getChildren().add(username);
        box.setSpacing(PADDING);
        StarBox stars = new StarBox();
        stars.setStars(myRating.getNumberOfStars());
        box.getChildren().add(stars.getDisplay());
        pane.setTop(box);
    }

    private void addText(BorderPane pane) {
        Text comment = new Text(myRating.getComment());
        comment.setWrappingWidth(GamePage.POPUP_WIDTH - WRAP_OFFSET);
        comment.getStyleClass().add(RATING_SELECTOR);
        pane.setBottom(comment);
    }

}
