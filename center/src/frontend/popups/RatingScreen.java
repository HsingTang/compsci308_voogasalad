/**
 * @Author Megan Phibbons
 * @Date April 2019
 * @Purpose Screen that allows users to rate a game.
 * @Dependencies javafx, GamePane, Data.DataManager
 * @Uses: This is called when the Rate Me button is pressed
 */

package frontend.popups;

import data.external.DataManager;
import data.external.GameCenterData;
import data.external.GameRating;
import frontend.Utilities;
import frontend.ratings.StarBox;
import javafx.geometry.Pos;
import javafx.scene.control.TextArea;
import javafx.scene.layout.*;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.sql.SQLException;

public class RatingScreen extends Popup {
    private static final double RATING_WIDTH = 350;
    private static final double RATING_HEIGHT = 450;
    private static final String BODY_SELECTOR = "bodyfont";
    private static final String TEXT_FIELD_SELECTOR = "commentbox";
    private static final String PADDING_SELECTOR = "ratingpadding";

    private TextArea myText;
    private StarBox myStars;
    private String myCurrentUser;
    private GameCenterData myData;

    /**
     * @purpose constructor that initializes the display and launches the popup
     * @param data the GameCenterData that corresponds to the game being rated
     * @param manager the DataManager that will eventually save the ratings
     * @param user the current user logged into the Center
     */
    public RatingScreen(GameCenterData data, DataManager manager, String user) {
        super(manager);
        myData = data;
        myCurrentUser = user;
        initializeDisplay();
        display();
    }

    /**
     * @purpose allows for the reflection of the addRatingButton, which saves the GameRating to the DataManager
     * @param data the data that should be saved
     */
    public void addRatingButton(GameCenterData data) {
        try {
            myManager.addRating(new GameRating(myCurrentUser, data.getTitle(), data.getAuthorName(), myStars.getCurrentNumberOfStars(), myText.getText()));
        } catch (SQLException e) {
            // todo: handle this
            System.out.println("Adding rating was unsuccessful");
        }
        ((Stage) myDisplay.getScene().getWindow()).close();
    }

    /**
     * @purpose allows for the reflection of the cancelButton, which closes the RatingScreen window
     * @param data unused here, but necessary for reflection to work
     */
    public void cancelButton(GameCenterData data) {
        ((Stage) myDisplay.getScene().getWindow()).close();
    }

    @Override
    protected void addButtons() {
        myDisplay.setBottom(Utilities.makeButtons(this, myData));
    }

    @Override
    protected void addHeader() {
        addTitleAndSubtitle(myDisplay, Utilities.getValue(myLanguageBundle, "ratingTitle"), myData.getTitle(), RATING_WIDTH);
    }

    @Override
    protected void addBody() {
        BorderPane ratingOptions = new BorderPane();
        addStars(ratingOptions);
        addCommentBox(ratingOptions);
        ratingOptions.getStyleClass().add(PADDING_SELECTOR);
        myDisplay.setCenter(ratingOptions);
        myDisplay.getStyleClass().add(PADDING_SELECTOR);
    }

    @Override
    protected void display() {
        showScene(myDisplay, RATING_WIDTH, RATING_HEIGHT);
    }

    private void addStars(BorderPane pane) {
        BorderPane stars = new BorderPane();
        Text starTitle = new Text(Utilities.getValue(myLanguageBundle, "starTitle"));
        starTitle.getStyleClass().add(BODY_SELECTOR);
        BorderPane.setAlignment(starTitle, Pos.CENTER);
        stars.setTop(starTitle);
        myStars = new StarBox();
        BorderPane.setAlignment(myStars.getDisplay(), Pos.CENTER);
        stars.setCenter(myStars.getDisplay());
        stars.getStyleClass().add(PADDING_SELECTOR);
        pane.setTop(stars);
    }

    private void addCommentBox(BorderPane pane) {
        BorderPane comment = new BorderPane();
        Text commentTitle = new Text(Utilities.getValue(myLanguageBundle, "commentTitle"));
        commentTitle.getStyleClass().add(BODY_SELECTOR);
        BorderPane.setAlignment(commentTitle, Pos.CENTER);
        comment.setTop(commentTitle);
        myText = new TextArea();
        myText.getStyleClass().add(TEXT_FIELD_SELECTOR);
        BorderPane.setAlignment(myText, Pos.CENTER);
        comment.setCenter(myText);
        pane.setCenter(comment);
    }
}
