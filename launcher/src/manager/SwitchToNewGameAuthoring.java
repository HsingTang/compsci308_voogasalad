package manager;

import data.external.GameCenterData;

@FunctionalInterface

public interface SwitchToNewGameAuthoring {
    /**
     * This functional interface takes in a GameCenter object as a parameter that contains information about the
     * username, the image they chose to associate with the new game they are planning to author, and a description
     * of the game that will be displayed in the game center. This functional interface is invoked when someone requests
     * to create a new game
     * @author Anna Darwish
     */
    void switchScene(GameCenterData myGameData);
}
