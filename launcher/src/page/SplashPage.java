package page;
import controls.LauncherSymbol;
import javafx.geometry.Pos;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import manager.SwitchToUserOptions;
import manager.SwitchToUserPage;
import pane.UserLoginDisplay;
import pane.WelcomeDisplay;

public class SplashPage extends VBox {
    private static final String MY_STYLE = "default_launcher.css";
    private static final String HBOX_STYLE = "login-hbox";
    private static final String VBOX_STYLE = "login-vbox";
    private static final String WELCOME_LABEL_KEY = "general_welcome";
    private static final String START_ACTION = "new_player";
    private UserLoginDisplay myLogin;

    /**
     * This is the first page that is displayed when the user loads in the launcher. It displays prompts to either
     * enter credentials so they can login, or they may select to create a new account
     * @author Anna Darwish
     */

    public SplashPage(SwitchToUserOptions switchToNewUser, SwitchToUserPage switchToLoggedIn) {
        this.getStylesheets().add(MY_STYLE);
        this.getChildren().add(new WelcomeDisplay(WELCOME_LABEL_KEY));
        this.getStyleClass().add(VBOX_STYLE);
        makeLoginOptions(switchToNewUser, switchToLoggedIn);
    }

    private void makeLoginOptions(SwitchToUserOptions switchToNewUser, SwitchToUserPage switchToLoggedIn) {
        HBox loginOptions = new HBox();
        LauncherSymbol mySymbol = new LauncherSymbol(START_ACTION, switchToNewUser);
        loginOptions.getChildren().add(mySymbol);
        myLogin = new UserLoginDisplay(switchToLoggedIn);
        loginOptions.getChildren().add(myLogin);
        loginOptions.getStyleClass().add(HBOX_STYLE);
        this.getChildren().add(loginOptions);
    }

    public void refresh(){
        myLogin.refresh();
    }
}
