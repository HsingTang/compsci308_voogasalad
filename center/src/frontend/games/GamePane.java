/**
 * @Author Megan Phibbons
 * @Date April 2019
 * @Purpose This is the pane that encompasses all of the game data. For now, it only includes a title and list of games.
 * Moving forward, this will also have a sort button and an add new game button.
 * @Dependencies javafx and Utilities.
 * @Uses: Used in CenterMain to display the games
 */

package frontend.games;

import center.external.CenterView;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import javafx.scene.text.Text;
import frontend.Utilities;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.ResourceBundle;

public class GamePane {
    public static final int GAME_PANE_TOP_PADDING = 10;
    public static final int GAME_PANE_LEFT_PADDING = 120;
    public static final int GAME_PANE_BOTTOM_PADDING = 150;
    public static final int GAME_PANE_RIGHT_PADDING = 50;
    private static final int IMAGE_OFFSET = 50;
    private static final String SUBTITLE_SELECTOR = "subtitlefont";
    private static final String REFRESH_BUTTON_PATH = "center/data/refresh.png";
    private static final String SPACING_SELECTOR = "refreshspacing";
    private BorderPane myDisplay;
    private ResourceBundle myResources;
    private String myCurrentUser;

    /**
     * @purpose initialize the languages resource bundle and set up the game display.
     */
    public GamePane(String username) {
        myResources = ResourceBundle.getBundle("languages/English");
        myCurrentUser = username;
        myDisplay = new BorderPane();
        initializeDisplay();
    }

    /**
     * @purpose give the display to CenterMain so that it can show everything relating to games.
     * @return the current display of the GamePane
     */
    public Pane getDisplay() {
        return myDisplay;
    }

    private void initializeDisplay() {
        BorderPane gamePane = new BorderPane();
        gamePane.setPadding(new Insets(GAME_PANE_TOP_PADDING, GAME_PANE_RIGHT_PADDING, GAME_PANE_BOTTOM_PADDING, GAME_PANE_LEFT_PADDING));
        Text subtitle = new Text(Utilities.getValue(myResources, "gamePaneTitle"));
        subtitle.getStyleClass().add(SUBTITLE_SELECTOR);
        BorderPane.setAlignment(subtitle, Pos.CENTER);
        gamePane.setTop(subtitle);
        gamePane.setCenter((new GameList(myCurrentUser, CenterView.STAGE_HEIGHT)).getDisplay());
        myDisplay.setCenter(gamePane);
        myDisplay.setRight(getRefreshButtonPane());
    }

    private Pane getRefreshButtonPane() {
        BorderPane refreshButtonPane = new BorderPane();
        try {
            ImageView refreshImage = new ImageView(new Image(new FileInputStream(REFRESH_BUTTON_PATH)));
            refreshImage.setOnMouseClicked(e -> refreshPage());
            refreshImage.setPreserveRatio(true);
            refreshImage.setFitWidth(IMAGE_OFFSET);
            refreshButtonPane.setRight(refreshImage);
            refreshButtonPane.getStyleClass().add(SPACING_SELECTOR);
            return refreshButtonPane;
        } catch (FileNotFoundException e) {
            return refreshButtonPane; // empty pane, because we don't need anything
        }
    }

    private void refreshPage() {
        myDisplay.getChildren().clear();
        initializeDisplay();
    }
}
