package controls;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import manager.SwitchToUserOptions;
import manager.SwitchToUserPage;

import java.util.ResourceBundle;

public class LauncherSymbol extends ImageView {
    private static final String WELCOME_RESOURCE = "launcher_display";
    private static final String DEFAULT_KEY = "default_";
    private static final String ACTIVE_KEY = "active_";
    private static final double DEFAULT_SCALE = 1.5;
    private static final ResourceBundle myResources = ResourceBundle.getBundle(WELCOME_RESOURCE);
    private Image myDefaultImage;
    private Image myPressedImage;
    /**
     * When pressed, these LaunchSymbols switch their image to appear as active
     * @author Anna Darwish
     */
    public LauncherSymbol(String action){
       basicSetUp(action);
       this.setOnMousePressed(mouseEvent -> switchImage(myPressedImage));
        this.setOnMouseReleased(mouseEvent -> switchImage(myDefaultImage));
    }

    public LauncherSymbol(String action, SwitchToUserOptions switchPage) {
        this(action);
        this.setOnMouseClicked(mouseEvent -> switchPage.switchPage());
    }

    private void basicSetUp(String action){
        myDefaultImage = new Image(myResources.getString(DEFAULT_KEY + action));
        myPressedImage = new Image(myResources.getString(ACTIVE_KEY + action));
        this.setImage(myDefaultImage);
        this.setScaleX(DEFAULT_SCALE);
        this.setScaleY(DEFAULT_SCALE);
    }

    private void switchImage(Image switchedImage){
        this.setImage(switchedImage);
    }
}
