package runner.internal.runnerSystems;

import data.external.DataManager;
import engine.external.Engine;
import engine.external.Entity;
import engine.external.Level;
import engine.external.component.Component;
import engine.external.component.SaveComponent;
import runner.external.Game;
import runner.internal.LevelRunner;
import java.sql.SQLException;
import java.util.Collection;

/**
 *
 * @author Louis Jensen
 */
public class SavingSystem extends RunnerSystem {
    private Engine myEngine;
    private Level myLevel;
    private Game myGame;
    private String myUsername;
    private String myGamename;
    private String myAuthorname;
    private final String CANNOT_SAVE = "Unable to save game";

    /**
     * Constructor for SoundSystem
     * @param requiredComponents - list of all components necessary for system
     * @param levelRunner - LevelRunner object so that system can modify the level
     */
    public SavingSystem(Collection<Class<? extends Component>> requiredComponents, LevelRunner levelRunner, Engine engine, Level level, Game game,
                        String username, String gameName, String authorName) {
        super(requiredComponents, levelRunner);
        myEngine = engine;
        myGame = game;
        myAuthorname = authorName;
        myGamename = gameName;
        myUsername = username;
    }

    /**
     *
     */
    @Override
    public void run() {
        for (Entity entity : this.getEntities()) {
            if (entity.hasComponents(SaveComponent.class)) {
                save(entity);
            }
        }
    }

    private void save(Entity entity) {
        Collection<Entity> entitiesToSave = myEngine.saveGame();
        myLevel.clearEntities();
        for(Entity e : entitiesToSave){
            myLevel.addEntity(e);
        }
        DataManager dataManager = new DataManager();
        try {
            dataManager.saveCheckpoint(myUsername, myGamename, myAuthorname, myGame);
        } catch (SQLException e){
            System.out.println(CANNOT_SAVE);
        }
    }
}

