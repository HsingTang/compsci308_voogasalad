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
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;

import java.util.MissingResourceException;
import java.util.ResourceBundle;

public class Utilities {
    private static final String ERROR_MESSAGE = "error";
    private static final String DEFAULT_IMAGE_LOCATION = "default_game";

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

    public static Pane getImagePane(DataManager manager, String imageLocation, double gameSize) {
        ImageView gameImage;
        try {
            gameImage = new ImageView(new Image(manager.loadImage(imageLocation)));
        } catch (Exception e) { // if any exceptions come from this, it should just become a default image.
            gameImage = new ImageView(new Image(manager.loadImage(DEFAULT_IMAGE_LOCATION)));
        }
        gameImage.setPreserveRatio(true);
        gameImage.setFitWidth(gameSize);
        BorderPane imagePane = new BorderPane();
        imagePane.setCenter(gameImage);
        return imagePane;
    }

}
