package engine.external;

import engine.external.component.Component;
import engine.internal.systems.CollisionSystem;
import engine.internal.systems.SaveGameSystem;
import engine.internal.systems.VoogaSystem;
import javafx.scene.input.KeyCode;
import voogasalad.util.reflection.Reflection;
import voogasalad.util.reflection.ReflectionException;

import java.util.ResourceBundle;
import java.util.HashMap;
import java.util.Collection;
import java.util.Enumeration;
import java.util.ArrayList;

/**
 * @author Hsingchih Tang
 * Game Engine class which interacts with Game Runner to maintain an ongoing game. Manages and updates Component values
 * of all Entities, and invokes pre-defined Events for checking engine.external.conditions and triggering engine.external.actions on each game loop.
 */
public class Engine {
    private final ResourceBundle SYSTEM_COMPONENTS_RESOURCES = ResourceBundle.getBundle("SystemRequiredComponents");
    private final ResourceBundle SYSTEM_ORDER_RESOURCES = ResourceBundle.getBundle("SystemUpdateOrder");
    private final ResourceBundle REMOVABLE_COMPONENTS_RESOURCES = ResourceBundle.getBundle("SystemRemoveComponents");

    public static final String SYSTEMS_PACKAGE_PATH = "engine.internal.systems.";
    public static final String COMPONENTS_PACKAGE_PATH = "engine.external.component.";
    private static final String EVENT_HANDLER_SYSTEM = "EventHandlerSystem";
    private static final String COLLISION_SYSTEM = "CollisionSystem";
    private static final String SAVE_GAME_SYSTEM = "SaveGameSystem";

    private HashMap<Integer,VoogaSystem> mySystems;
    private CollisionSystem myCollisionSystem;
    private SaveGameSystem mySaveGameSystem;
    private Collection<Entity> myEntities;
    private Collection<IEventEngine> myEvents;
    private Double myLevelHeight;
    private Double myLevelWidth;

    /**
     * An Engine is expected be initialized by a GameRunner and accepts a Level object containing all data (Entities and
     * Events) defined for a specific game level. Engine retrieves the collection of Entities and Events from Level, and
     * sets up concrete Systems for handling the Entities and Events in the game process
     * @param level Level object of the current game to run
     */
    public Engine(Level level){
        myEntities = level.getEntities();
        myEvents = level.getEvents();
        myLevelHeight = level.getHeight();
        myLevelWidth = level.getWidth();
        initSystemMap();
    }

    /**
     * Call expected to be made by GameRunner. Accepts a Collection of KeyCode inputs received from GameRunner on
     * the front end, and invokes all Systems one by one to update Entities' status and execute Events
     * @param inputs collection of user Keycode inputs received on this game loop
     * @return all game Entities after being updated by Systems in current game loop
     */
    public synchronized Collection<Entity> updateState(Collection<KeyCode> inputs){
        for(int i = 0; i<SYSTEM_ORDER_RESOURCES.keySet().size(); i++){
            if(mySystems.get(i)!=null){

                mySystems.get(i).update(myEntities,inputs);
            }
        }
        myCollisionSystem.adjustCollidedEntities();
        return this.getEntities();
    }

    /**
     * @return an unmodifiable collection of Entities within the currently running game
     */
    public Collection<Entity> getEntities(){
        return myEntities;
    }

    /**
     * Expected to be called by CleanupSystem for permanently removing an Entity from the running game
     * @param e Entity to be removed
     */
    public void removeEntity(Entity e){
        myEntities.remove(e);
    }

    /**
     * Expected to be called by AddEntitySystem for adding an Entity to the running game
     * @param e Entity to be added
     */
    public void addEntity(Entity e){
        myEntities.add(e);
    }

    /**
     * Clean up all Components that have been created in Engine for Runner to save the status of game
     * @return a copy of all currently existing Entities that have had Components cleaned up
     */
    public Collection<Entity> saveGame(){
        ArrayList<Entity> entityCopy = new ArrayList<>(myEntities);
        Collection<Class<? extends Component>> componentsToRemove = retrieveComponentClazz(REMOVABLE_COMPONENTS_RESOURCES,mySaveGameSystem.getClass().getSimpleName());
        return mySaveGameSystem.getSavedEntities(entityCopy,componentsToRemove);
    }

    /**
     * @return room height of the current game level
     */
    public double getRoomHeight(){
        return myLevelHeight;
    }

    /**
     * @return room width of the current game level
     */
    public double getRoomWidth(){
        return myLevelWidth;
    }

    // Loop over the System class names in the properties file, instantiate the concrete Systems, and store the Systems
    // mapped by their corresponding updating order on each game loop
    private void initSystemMap() {
        mySystems = new HashMap<>();
        Enumeration<String> enumSystems= SYSTEM_ORDER_RESOURCES.getKeys(); //
        while (enumSystems.hasMoreElements()){
            Integer order = Integer.valueOf(enumSystems.nextElement());
            initSystem(order,SYSTEM_ORDER_RESOURCES.getString(String.valueOf(order)));
        }
    }

    // Use the reflection utility module's wrapper methods to instantiate the concrete System classes with the
    // corresponding set of required Components. EventHandlerSystem has a different constructor from the other Systems
    // and thus is instantiated differently, while SaveGameSystem and CollisionSystem have special methods (aside from
    // the generic update() call) in order to serve special purposes, so they need to be stored as instance fields
    private void initSystem(Integer order, String systemName) throws ReflectionException{
        try {
            Collection<Class<? extends Component>> systemComponents = retrieveComponentClazz(SYSTEM_COMPONENTS_RESOURCES,systemName);
            if (systemName.contains(EVENT_HANDLER_SYSTEM)) {
                mySystems.put(order, (VoogaSystem) Reflection.createInstance(SYSTEMS_PACKAGE_PATH+systemName,systemComponents, this, myEvents));
            } else {
                mySystems.put(order, (VoogaSystem) Reflection.createInstance(SYSTEMS_PACKAGE_PATH+systemName,systemComponents, this));
                if(systemName.contains(COLLISION_SYSTEM)){
                    myCollisionSystem = (CollisionSystem) mySystems.get(order);
                }
                if(systemName.contains(SAVE_GAME_SYSTEM)){
                    mySaveGameSystem = (SaveGameSystem) mySystems.get(order);
                    mySystems.remove(order);
                }
            }
        }catch(ReflectionException e){
            e.printStackTrace();
            throw new ReflectionException(e,"Cannot create System "+systemName+" for Engine");
        }
    }

    // Read the class names of required Components for a System from the Resource Bundle, and uses reflection to
    // instantiate the collection of Component classes that would be passed into the System's constructor.
    private Collection<Class<? extends Component>> retrieveComponentClazz(ResourceBundle resource, String systemName) throws ReflectionException {
        String[] componentArr = resource.getString(systemName).split(",");
        ArrayList<Class<? extends Component>> componentList = new ArrayList<>();
        for(String component:componentArr){
            try {
                componentList.add((Class) Class.forName(COMPONENTS_PACKAGE_PATH + component));
            }catch (ClassNotFoundException e){
                throw new ReflectionException(e,"Required Component "+COMPONENTS_PACKAGE_PATH+component+" nor found");
            }
        }
        return componentList;
    }

}
