package runner.internal;

import data.external.DatabaseEngine;

/**
 * Class used to test runner
 * @author Louis Jensen
 */
public class DataTestRunner {

    /**
     * Connects to database and creates new GameRunner
     */
    public static void main(String[] args){
        if (! DatabaseEngine.getInstance().open()){
            System.exit(1);
        };
        RunnerTester.main(args);
    }
}
