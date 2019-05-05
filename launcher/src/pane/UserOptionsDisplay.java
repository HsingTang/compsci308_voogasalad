package pane;
import javafx.geometry.Pos;
import javafx.scene.layout.HBox;
import manager.SwitchToUserOptions;

public class UserOptionsDisplay extends HBox {
    private static final String CREATE_LAUNCHER = "create";
    private static final String PLAY_LAUNCHER = "play";
    private static final String CSS_STYLE_NAME = "default_launcher.css";
    private static final int OFFSET_VALUE = 100;
    /**
     * These are the pathways the user may take upon logging in - either navigating to the authoring environment or going
     * into the game center
     * @author Anna Darwish
     */

   public UserOptionsDisplay(SwitchToUserOptions switchToPageBeforeAuthoring, SwitchToUserOptions switchToLauncher,SwitchToUserOptions logout){

        this.getStyleClass().add(CSS_STYLE_NAME);
        this.setTranslateY(OFFSET_VALUE);
        setUpImages(switchToPageBeforeAuthoring,switchToLauncher);
        this.setAlignment(Pos.CENTER);
        this.setSpacing(OFFSET_VALUE);

    }
    private void setUpImages(SwitchToUserOptions switchDisplay, SwitchToUserOptions switchToLauncher){
        LauncherControlDisplay myCreator = new LauncherControlDisplay(CREATE_LAUNCHER);
        myCreator.setOnMouseClicked(mouseEvent -> switchDisplay.switchPage());
        LauncherControlDisplay myPlayer = new LauncherControlDisplay(PLAY_LAUNCHER);
        myPlayer.setOnMouseClicked(mouseEvent -> switchToLauncher.switchPage());
        this.getChildren().add(myCreator);
        this.getChildren().add(myPlayer);
    }
}
