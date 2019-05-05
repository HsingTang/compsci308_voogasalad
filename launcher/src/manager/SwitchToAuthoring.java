package manager;

import data.external.GameCenterData;
import runner.external.Game;

@FunctionalInterface

public interface SwitchToAuthoring {
    /**
     * This functional interface takes in a loaded Game and GameCenterData object from the database in order to load in authoring
     * when we are editing an old game, as there needed to be a distinguished constructor from creating a new game
     * @author Anna Darwish
     */
    void switchScene(Game oldGame, GameCenterData myGameData);
}
