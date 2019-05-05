package page;

import controls.BackButton;
import controls.LogOutButton;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import manager.SwitchToAuthoring;
import manager.SwitchToNewGameAuthoring;
import manager.SwitchToUserOptions;
import manager.SwitchToUserPage;
import pane.CreateGameDisplay;
import pane.WelcomeDisplay;

public class CreateNewGamePage extends BorderPane {
    private static final String MY_STYLE = "default_launcher.css";
    private static final String CREATE_KEY = "create_game";
    /**
     * This page will prompt the user to enter information about the new game they wish to create, such as its cover image,
     * title, and description, along with a way to access the authoring environment or the game center if they choose to
     * go there instead
     * @author Anna Darwish
     */
    public CreateNewGamePage(SwitchToUserOptions backToPlayOrCreate, SwitchToAuthoring goToOldAuthoring, SwitchToNewGameAuthoring goToNewAuthoring, String userName, SwitchToUserOptions logout){
        this.setTop(new BackButton(backToPlayOrCreate));
        this.setCenter(new CreateGameDisplay(goToOldAuthoring, goToNewAuthoring, userName));
        this.setBottom(new LogOutButton(logout));
    }
}
