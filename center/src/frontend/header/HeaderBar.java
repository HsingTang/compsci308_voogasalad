/**
 * @Author Megan Phibbons
 * @Date April 2019
 * @Purpose This code encompasses the general aspects of the header bar so that it can be displayed. For now, this is
 * simply a title, but may eventually contain a settings button.
 * @Dependencies javafx and Utilities.
 * @Uses: Used in CenterMain to display the title
 */

package frontend.header;

import data.external.DataManager;
import data.external.ImageChooser;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import frontend.Utilities;
import javafx.scene.text.Text;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.ResourceBundle;

public class HeaderBar {
    private Pane myHeaderLayout;
    private ResourceBundle myLanguageBundle;
    private String myUsername;

    private static final String TITLE_SELECTOR = "titlefont";
    private static final String SUBTITLE_SELECTOR = "subtitlefont";
    private static final String DEFAULT_IMAGE_LOCATION = "center/data/profile_information/images/default.png";
    private static final int FIT_WIDTH = 50;


    /**
     * @purpose constructor that initializes the resource bundle and sets the layout of the pane.
     */
    public HeaderBar(String username) {
        myLanguageBundle = ResourceBundle.getBundle("languages/English");
        myHeaderLayout = new StackPane();
        myUsername = username;
        initializeLayouts();
    }

    /**
     * @purpose share the header layout with CenterMain
     * @return the current layout of the pane.
     */
    public Pane getHeaderLayout() {
        return myHeaderLayout;
    }

    private void initializeLayouts() {
        myHeaderLayout.getChildren().clear();
        Text title = new Text(Utilities.getValue(myLanguageBundle, "titleText"));
        title.getStyleClass().add(TITLE_SELECTOR);
        BorderPane titleLayout = new BorderPane();
        titleLayout.setCenter(title);
        Text welcome = new Text(Utilities.getValue(myLanguageBundle, "welcomeSubtitle") + myUsername);
        welcome.getStyleClass().add(SUBTITLE_SELECTOR);
        BorderPane.setAlignment(welcome, Pos.CENTER);
        titleLayout.setBottom(welcome);
        myHeaderLayout.getChildren().add(titleLayout);
        addSettingsPane();
    }

    private void addSettingsPane() {
        BorderPane settingsPane = new BorderPane();
        settingsPane.setPadding(new Insets(20));
        try {
            DataManager temporaryManager = new DataManager(); // todo: pass this in
            ImageView settings;
            try {
                settings = new ImageView(new Image(temporaryManager.getProfilePic(myUsername)));
            } catch(Exception e) {
                settings = new ImageView(new Image(new FileInputStream(DEFAULT_IMAGE_LOCATION)));
            }
            settings.setOnMouseClicked(e -> openSettings());
            settings.setPreserveRatio(true);
            settings.setFitWidth(FIT_WIDTH);
            settingsPane.setRight(settings);
            myHeaderLayout.getChildren().add(settingsPane);
        } catch (FileNotFoundException e) {
            // do nothing
        }
    }

    private void openSettings() {
        ImageChooser imageChooser = new ImageChooser(myUsername);
        imageChooser.uploadImage();
        initializeLayouts();
    }

}
