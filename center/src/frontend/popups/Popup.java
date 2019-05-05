/**
 * @Author Megan Phibbons
 * @Date April 2019
 * @Purpose Superclass for all Popups contained in GameCenter
 * @Dependencies javafx, Data.DataManager
 * @Uses: This class is extended by GamePage, RatingScreen, and UserProfileDisplay
 */

package frontend.popups;

import data.external.DataManager;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.util.ResourceBundle;

public abstract class Popup {
    private static final String PANE_SELECTOR = "pane";
    private static final Color BACKGROUND_COLOR = Color.rgb(46, 43, 51);
    private static final String TITLE_SELECTOR = "titlefont";
    private static final String SUBTITLE_SELECTOR = "subtitlefont";
    private static final String GAME_PAGE_SELECTOR = "gamepage";
    private static final String DEFAULT_LANGUAGE = "languages/English";
    private static final String STYLESHEET = "center.css";

    protected BorderPane myDisplay;
    protected DataManager myManager;
    protected ResourceBundle myLanguageBundle;


    /**
     * @purpose the constructor, which sets the language bundle and myManager (both of which are used by all subclasses)
     * @param manager the GameCenterData associated with the popup
     */
    public Popup(DataManager manager) {
        myManager = manager;
        myLanguageBundle = ResourceBundle.getBundle(DEFAULT_LANGUAGE);
    }

    /**
     * This method calls on all of the required display methods so that the popups can be displayed on-screen.
     */
    protected void initializeDisplay() {
        myDisplay = new BorderPane();
        myDisplay.getStylesheets().add(STYLESHEET);
        myDisplay.getStyleClass().add(GAME_PAGE_SELECTOR);
        addHeader();
        addBody();
        addButtons();
    }

    /**
     * @purpose this sets the Popup to show and wait for some sort of input from the user
     * @param pane the pane to be displayed
     * @param width the width of the display
     * @param height the height of the display
     */
    protected void showScene(Pane pane, double width, double height) {
        pane.getStylesheets().add(STYLESHEET);
        pane.getStyleClass().add(PANE_SELECTOR);
        Scene scene = new Scene(pane, width, height, BACKGROUND_COLOR);
        Stage stage = new Stage();
        stage.setScene(scene);
        stage.setResizable(false);
        stage.showAndWait();
    }

    /**
     * @purpose add the title and subtitle to the top of a given Pane so that it can be used as an actual title
     * @param pane The pane to add to
     * @param title A string representation of the title
     * @param subtitle A string representation of the subtitle
     * @param subtitleWrapSize the wrapping length of the subtitle
     */
    protected void addTitleAndSubtitle(BorderPane pane, String title, String subtitle, double subtitleWrapSize) {
        pane.setTop(getTitleAndSubtitle(title, subtitle, subtitleWrapSize));
    }

    /**
     * @purpose Create a pane with the title and subtitle as the center. The purpose of this is so that the pane itself can be reached, not just added to something else.
     * @param title the string representation of the title
     * @param subtitle the string representation of the subtitle
     * @param subtitleWrapSize the wrapping length of the subtitle
     * @return a BorderPane containing the title and subtitle
     */
    protected Pane getTitleAndSubtitle(String title, String subtitle, double subtitleWrapSize) {
        BorderPane pane = new BorderPane();
        Text titleText = new Text(title);
        titleText.getStyleClass().add(TITLE_SELECTOR);
        Text subtitleText = new Text(subtitle);
        subtitleText.getStyleClass().add(SUBTITLE_SELECTOR);
        subtitleText.setWrappingWidth(subtitleWrapSize);
        VBox text = new VBox(titleText, subtitleText);
        text.setAlignment(Pos.CENTER);
        BorderPane.setAlignment(text, Pos.CENTER);
        pane.setCenter(text);
        return pane;
    }

    /**
     * @purpose add the header to myDisplay. This allows subclasses to customize what goes into the header of the popup
     */
    protected abstract void addHeader();

    /**
     * @purpose add the body to myDisplay. This allows subclasses to customize what goes into the body of the popup
     */
    protected abstract void addBody();

    /**
     * @purpose add the buttons to myDisplay. This allows subclasses to customize what buttons go into the popup
     */
    protected abstract void addButtons();

    /**
     * @purpose tell the popup to display. Sometimes some intermediate steps happen between this and showScene.
     */
    protected abstract void display();

}
