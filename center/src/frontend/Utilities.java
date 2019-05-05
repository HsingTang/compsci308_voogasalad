/**
 * @Author Megan Phibbons
 * @Date April 2019
 * @Purpose This class wraps up methods that are used in many places into one class. The methods are static because
 * they do not rely on the instantiation of an object to work.
 * @Dependencies none
 * @Uses: Used in almost every class so that there is no duplicated code.
 */

package frontend;

import data.external.DataManager;
import data.external.GameCenterData;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import runner.external.GameRunner;
import voogasalad.util.reflection.Reflection;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.MissingResourceException;
import java.util.ResourceBundle;

public class Utilities {
    private static final String ERROR_MESSAGE = "error";
    private static final String DEFAULT_IMAGE_LOCATION = "center/data/game_information/images/default_game.png";
    private static final String INDIVIDUAL_BUTTON_SELECTOR = "button";
    private static final String BUTTON_LIST_SELECTOR = "buttons";

    /**
     * @purpose given a resource bundle and a key, get the value. This was pulled out for the sole purpose of adding in
     * error checking, because checking for missing keys makes the process longer than just a line.
     * @param bundle the resource bundle which we access
     * @param key the key that we are looking for in the bundle
     * @return the string that is associated with that key (unless the key is not in the bundle, then return the error message
     */
    public static String getValue(ResourceBundle bundle, String key) {
        try {
            return bundle.getString(key);
        }
        catch (MissingResourceException e) {
            return ERROR_MESSAGE;
        }
    }

    /**
     * @purpose load in an image to be displayed, then give it in the form of a BorderPane so that it can be easily used
     * @param manager the DataManager loading in images
     * @param imageLocation the title of the image to be loaded
     * @param gameSize the size of the image to be loaded
     * @param maxHeight the maximum height of the image
     * @return the pane that holds the ImageView
     * @throws FileNotFoundException if neither the searched for image nor the "no image found" image cannot be found
     */
    public static Pane getImagePane(DataManager manager, String imageLocation, double gameSize, double maxHeight) throws FileNotFoundException {
        ImageView gameImage;
        try {
            gameImage = new ImageView(new Image(manager.loadImage(imageLocation)));
        } catch (Exception e) { // if any exceptions come from this, it should just become a default image.
            gameImage = new ImageView(new Image(new FileInputStream(DEFAULT_IMAGE_LOCATION)));
        }
        double originalWidth = gameImage.getFitWidth();
        gameImage.setPreserveRatio(true);
        gameImage.setFitWidth(gameSize);
        if(gameImage.getFitWidth() > maxHeight) {
            gameImage.setFitWidth(originalWidth);
            gameImage.setFitHeight(maxHeight);
        }
        BorderPane imagePane = new BorderPane();
        imagePane.setCenter(gameImage);
        return imagePane;
    }

    /**
     * @purpose launch the game runner. This is in utilities because it is used in many different places.
     * @param gameName the name of which the game should be launched
     * @param authorName the name of the author who created the game
     * @param username the name of the current user logged in
     */
    public static void launchGameRunner(String gameName, String authorName, String username) {
        try {
            new GameRunner(gameName, authorName, username);
        } catch (FileNotFoundException e) {
            // do nothing, the GameRunner will not launch in this case.
        }
    }

    /**
     * @purpose create an HBox of buttons using reflection so that buttons don't have to be repeatedly created
     * @param object an instance of the object that needs the buttons (for reflection)
     * @param data the GameCenterData that the buttons are being made for
     * @return an HBox that holds all of the desired buttons
     */
    public static HBox makeButtons(Object object, GameCenterData data) {
        HBox buttonList = new HBox();
        buttonList.getStyleClass().add(BUTTON_LIST_SELECTOR);
        ResourceBundle buttonResources = ResourceBundle.getBundle(object.getClass().getSimpleName());
        for(String buttonName : buttonResources.keySet()) {
            Button button = new Button(getValue(buttonResources, buttonName));
            button.getStyleClass().add(INDIVIDUAL_BUTTON_SELECTOR);
            button.setOnAction(e -> Reflection.callMethod(object, buttonName, data));
            buttonList.getChildren().add(button);
        }
        BorderPane.setAlignment(buttonList, Pos.CENTER);
        return buttonList;
    }

}
