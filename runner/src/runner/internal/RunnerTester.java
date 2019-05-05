package runner.internal;

import data.external.DatabaseEngine;
import javafx.application.Application;
import javafx.stage.Stage;
import runner.external.GameRunner;

/**
 * Creates new GameRunner object for testing game
 * @author Louis Jensen
 */
public class RunnerTester extends Application {
    private final String DEFAULT_GAME_TO_LOAD = "YeetRevised3";
    private final String DEFAULT_AUTHOR = "defaultAuthor";
    private final String DEFAULT_USER = "defaultUserName";

    /**
     * Runs application start method
     */
    public static void main(String[] args){
        launch(args);
    }

    /**
     * Starts application
     * @param primaryStage - Stage to create game in
     * @throws Exception if application cannot start
     */
    @Override
    public void start (Stage primaryStage) throws Exception{
        GameRunner runner = new GameRunner(DEFAULT_GAME_TO_LOAD, DEFAULT_AUTHOR, DEFAULT_USER);
    }

    /**
     * Closes application
     * @throws Exception upon failure
     */
    @Override
    public void stop() throws Exception {
        super.stop();
        DatabaseEngine.getInstance().close();
    }

}


