package ui.main;

import data.external.DatabaseEngine;
import data.external.GameCenterData;
import javafx.application.Application;
import javafx.stage.Stage;

import java.util.ResourceBundle;

public class MainTester extends Application {
    private static final ResourceBundle GENERAL_RESOURCES = ResourceBundle.getBundle("authoring_general");
    private MainGUI myMainGui;

    @Override
    public void start(Stage stage) {
        myMainGui = new MainGUI(new GameCenterData());
        myMainGui.launch(false);
    }

    public static void main(String[] args){
        launch(args);
    }

    @Override
    public void stop() throws Exception {
        super.stop();
        DatabaseEngine.getInstance().close();
        myMainGui.clearFolder(GENERAL_RESOURCES.getString("images_filepath"));
        myMainGui.clearFolder(GENERAL_RESOURCES.getString("audio_filepath"));
    }

    //outerDirectory - folder that needs sub-folders "defaults" and "user-uploaded"

}
