package page;

import controls.LogOutButton;
import javafx.scene.layout.VBox;
import manager.SwitchToUserOptions;
import manager.SwitchToUserPage;
import pane.UserOptionsDisplay;
import pane.WelcomeDisplay;

public class WelcomeUserPage extends VBox {
    private static final String MY_STYLE = "tight-vbox";
    private static final String WELCOME_LABEL_KEY = "specific_welcome";
    /**
     * This page will prompt the user either enter the authoring environment to create games or go to the game center so
     * they can play games
     * @author Anna Darwish
     */
    public WelcomeUserPage(SwitchToUserOptions switchToPageBeforeAuthoring, SwitchToUserOptions switchToLauncher, String userName, SwitchToUserOptions logout){
        VBox internal = new VBox();
        internal.getStyleClass().add(MY_STYLE);
        WelcomeDisplay myDisplay = new WelcomeDisplay(WELCOME_LABEL_KEY,userName);
        internal.getChildren().add(myDisplay);
        internal.getChildren().add(new UserOptionsDisplay(switchToPageBeforeAuthoring, switchToLauncher, logout));
        this.getChildren().add(internal);
        LogOutButton logOutButton = new LogOutButton(logout);
        this.getChildren().add(logOutButton);
    }


}
