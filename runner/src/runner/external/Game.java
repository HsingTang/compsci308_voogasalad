package runner.external;

import engine.external.Entity;
import engine.external.Level;
import java.util.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Game Object contains all information to play a game
 * @author Louis Jensen
 */
public class Game {
    private List<Level> myLevels;
    private Map<Entity, String> myUserCreatedTypes;
    private int myWidth;
    private int myHeight;

    /**
     * Constructor to create a game object with initial size
     */
    public Game(){
        myLevels = new ArrayList<>();

        //Temporary defaults
        myHeight = 550;
        myWidth = 700;
    }

    /**
     * Adds a level to the game object
     * @param level - built level to add
     */
    public void addLevel(Level level){
        myLevels.add(level);
    }

    /**
     * Gets list of levels
     * @return Unmodifiable list of levels
     */
    public List<Level> getLevels(){
        return Collections.unmodifiableList(myLevels);
    }

    /**
     * Gets scene width
     * @return int scene width
     */
    public int getWidth(){
        return myWidth;
    }

    /**
     * Gets scene height
     * @return int scene height
     */
    public int getHeight(){
        return myHeight;
    }

    /**
     * Adds types created by user
     * @param userCreatedTypes - map of user defined type
     */
    public void addUserCreatedTypes(Map<Entity, String> userCreatedTypes){
        myUserCreatedTypes = userCreatedTypes;
    }

    /**
     * Gets the map of the types that the user authors
     * @return Map containing entities and their types
     */
    public Map<Entity, String> getUserCreatedTypes() {
        return myUserCreatedTypes;
    }
}

