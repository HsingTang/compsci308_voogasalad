package data.external;

public abstract class DataManager {

    protected DatabaseEngine myDatabaseEngine;

    public DataManager() {
        myDatabaseEngine = DatabaseEngine.getInstance();
    }
}
