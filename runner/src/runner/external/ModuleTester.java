package runner.external;

import data.external.GameDataManager;

/**
 * Class used to make sure modules dependencies
 * were set up correctly
 * @author Anna Darwish
 */
public class ModuleTester {
    public static void main(String[] args) {
        GameDataManager dm = new GameDataManager();
    }
}
