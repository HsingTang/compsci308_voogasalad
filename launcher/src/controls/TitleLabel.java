package controls;

import javafx.scene.control.Label;

import java.util.ResourceBundle;

public class TitleLabel extends Label {
    private static final String WELCOME_RESOURCE = "launcher_display";
    private static final String STYLE = "welcome.css";
    private static final String SMALL_STYLE = "small-label";

    /**
     * This is the larger title display users see among the various pages of the launcher environment
     * @author Anna Darwish
     */
    public TitleLabel(String resourceKey){
        ResourceBundle myResources = ResourceBundle.getBundle(WELCOME_RESOURCE);
        String myWelcomeLabel = myResources.getString(resourceKey);
        this.setText(myWelcomeLabel);
        this.getStylesheets().add(STYLE);
    }
    public TitleLabel(String resourceKey, String modifyText){
        this(resourceKey);
        this.getStyleClass().add(SMALL_STYLE);
        addToLabel(modifyText);
    }
    /**
     * This allows for toggling the display, as it may dependent upon the user who is currently logged in
     */
    private void addToLabel(String addition){
        this.setText(this.getText() + " " + addition);
    }


}
