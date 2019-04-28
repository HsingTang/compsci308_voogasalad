package frontend.popups;

import data.external.DataManager;
import data.external.GameCenterData;
import frontend.Utilities;
import frontend.games.GameList;
import javafx.geometry.Pos;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.StackPane;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.sql.SQLException;

public class UserProfileDisplay extends Popup {
    private String myUsername;
    private String myCurrentUser;
    private GameCenterData myData;

    private static final double USER_WIDTH = 750;
    private static final double USER_HEIGHT = 500;
    private static final double IMAGE_WIDTH = 125;
    private static final double SUBTITLE_WRAP_LENGTH = USER_WIDTH - 2 * IMAGE_WIDTH - 20;
    private static final String BODY_SELECTOR = "bodyfont";
    private static final String TITLE_SELECTOR = "titlefont";
    private static final String SUBTITLE_SELECTOR = "subtitlefont";

    public UserProfileDisplay(GameCenterData data, DataManager manager, String currentUser, String username) {
        super(manager);
        myData = data;
        myUsername = username;
        myCurrentUser = currentUser;
        initializeDisplay();
        display();
    }

    public void closeButton(GameCenterData data) {
        ((Stage) myDisplay.getScene().getWindow()).close();
    }

    @Override
    protected void addHeader() {
        StackPane header = new StackPane();
        try {
            BorderPane titleAndSubtitle = new BorderPane();
            Text username = new Text(myUsername);
            username.getStyleClass().add(TITLE_SELECTOR);
            BorderPane.setAlignment(username, Pos.CENTER);
            Text bio = new Text(myManager.getBio(myUsername));
            bio.setWrappingWidth(SUBTITLE_WRAP_LENGTH);
            bio.getStyleClass().add(BODY_SELECTOR);
            titleAndSubtitle.setTop(username);
            titleAndSubtitle.setCenter(bio);
            ImageView userImage = new ImageView(new Image(myManager.getProfilePic(myUsername)));
            userImage.setPreserveRatio(true);
            userImage.setFitWidth(IMAGE_WIDTH);
            BorderPane imagePane = new BorderPane();
            imagePane.setLeft(userImage);
            header.getChildren().addAll(titleAndSubtitle, imagePane);
            myDisplay.setTop(header);
        } catch (SQLException e) {
            //todo: handle this
        }
    }

    @Override
    protected void addBody() {
        BorderPane gameListPane = new BorderPane();
        Text title = new Text(myUsername + Utilities.getValue(myLanguageBundle, "userPageGameList"));
        title.getStyleClass().add(SUBTITLE_SELECTOR);
        BorderPane.setAlignment(title, Pos.CENTER);
        gameListPane.setTop(title);
        gameListPane.setCenter(new GameList(myCurrentUser, myUsername).getDisplay());
        myDisplay.setCenter(gameListPane);
    }

    @Override
    protected void addButtons() {
        myDisplay.setBottom(Utilities.makeButtons(this, myData));
    }

    @Override
    protected void display() {
        showScene(myDisplay, USER_WIDTH, USER_HEIGHT);
    }
}
