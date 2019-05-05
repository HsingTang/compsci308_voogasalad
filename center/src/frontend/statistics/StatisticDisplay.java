/**
 * @Author Megan Phibbons
 * @Date April 2019
 * @Purpose Display the statistics for the specific game
 * @Dependencies javafx
 * @Uses: Shown when the user presses the Read More button
 */

package frontend.statistics;

import data.external.DataManager;
import data.external.GameCenterData;
import data.external.UserScore;
import frontend.Utilities;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.input.ScrollEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.text.Text;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

public class StatisticDisplay {
    private static final String SUBTITLE_SELECTOR = "subtitlefont";
    private static final String BODY_SELECTOR = "bodyfont";
    private static final String DEFAULT_LANGUAGE = "languages/English";
    private static final int HORIZONTAL_GAP = 100;
    private static final int VERTICAL_GAP = 20;
    private static final int PADDING = 10;
    private static final int MAX_SCORE = 10;
    private BorderPane myDisplay;
    private List<UserScore> myScores;
    private ResourceBundle myLanguageBundle;

    private static final int COLUMN_NUMBER = 3;

    /**
     * @purpose constructor, which sets the language bundle and gets all scores from DataManager to display
     * @param data the GameCenterData for which we are looking at ratings
     * @param manager the DataManager used to read in ratings
     */
    public StatisticDisplay(GameCenterData data, DataManager manager) {
        myLanguageBundle = ResourceBundle.getBundle(DEFAULT_LANGUAGE);
        try {
            myScores = manager.loadScores(data.getTitle(), data.getAuthorName());
        } catch (SQLException e) {
            myScores = new ArrayList<>();
        }
        initializeDisplay();
    }

    /**
     * @purpose allow the statistics to be displayed elsewhere
     * @return the current display of the statistics
     */
    public Pane getDisplay() {
        return myDisplay;
    }

    private void initializeDisplay() {
        myDisplay = new BorderPane();
        GridPane scores = new GridPane();
        scores.setAlignment(Pos.CENTER);
        scores.setHgap(HORIZONTAL_GAP);
        scores.setVgap(VERTICAL_GAP);
        scores.setPadding(new Insets(PADDING));
        setUpScoresHeader(scores);
        setUpScoresBody(scores);
        myDisplay.setCenter(scores);
    }

    private void setUpScoresHeader(GridPane scores) {
        for(int i = 0; i < COLUMN_NUMBER; i++) {
            Text header = new Text(Utilities.getValue(myLanguageBundle, "scoreHeader" + i));
            header.getStyleClass().add(SUBTITLE_SELECTOR);
            scores.add(header, i, 0);
        }
    }

    private void setUpScoresBody(GridPane scores) {
        int i = 1;
        for(UserScore score : myScores) {
            if(i > MAX_SCORE) {
                break;
            }
            Text rank = new Text(i + "");
            rank.getStyleClass().add(BODY_SELECTOR);
            scores.add(rank, 0, i);
            Text username = new Text(score.getUserName());
            username.getStyleClass().add(BODY_SELECTOR);
            scores.add(username, 1, i);
            Text scoreValue = new Text("" + Math.round(score.getScore()));
            scoreValue.getStyleClass().add(BODY_SELECTOR);
            scores.add(scoreValue, 2, i);
            i++;
        }
    }


}
