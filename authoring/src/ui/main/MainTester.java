package ui.main;

import data.external.DatabaseEngine;
import data.external.GameCenterData;
import javafx.application.Application;
import javafx.stage.Stage;

import java.io.File;
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
        clearFolder(GENERAL_RESOURCES.getString("images_filepath"));
        clearFolder(GENERAL_RESOURCES.getString("audio_filepath"));
    }


    /**
     * This method takes in the path to the directory wished to be cleared and then
     * iterates through each file and deletes it
     * @param outerDirectoryPath
     */
    public void clearFolder(String outerDirectoryPath){
        DatabaseEngine.getInstance().close();
        File outerDirectory = new File(outerDirectoryPath);
        for(File file : outerDirectory.listFiles()){
            file.delete();
        }
        DatabaseEngine.getInstance().open();
    }

}
