package engine.internal.systems;

import engine.external.Entity;
import engine.external.Engine;
import engine.external.component.*;

import javafx.scene.input.KeyCode;
import voogasalad.util.reflection.Reflection;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;

/**
 * @author Hsingchih Tang
 * Abstract super class of all internal Systems of Engine
 * Every concrete System stores a different set of Component classes required from an Entity
 * such that its relevant Component values could be managed by the System
 */
public abstract class VoogaSystem {
    // Define the Component classes as static fields in this super class to make it more convenient for sub-classes to access the Component values
    static final Class<? extends Component> X_POSITION_COMPONENT_CLASS = XPositionComponent.class;
    static final Class<? extends Component> Y_POSITION_COMPONENT_CLASS = YPositionComponent.class;
    static final Class<? extends Component> X_VELOCITY_COMPONENT_CLASS = XVelocityComponent.class;
    static final Class<? extends Component> Y_VELOCITY_COMPONENT_CLASS = YVelocityComponent.class;
    static final Class<? extends Component> X_ACCELERATION_COMPONENT_CLASS = XAccelerationComponent.class;
    static final Class<? extends Component> Y_ACCELERATION_COMPONENT_CLASS = YAccelerationComponent.class;
    static final Class<? extends Component> BOTTOM_COLLIDED_COMPONENT_CLASS = BottomCollidedComponent.class;
    static final Class<? extends Component> TOP_COLLIDED_COMPONENT_CLASS = TopCollidedComponent.class;
    static final Class<? extends Component> RIGHT_COLLIDED_COMPONENT_CLASS = RightCollidedComponent.class;
    static final Class<? extends Component> LEFT_COLLIDED_COMPONENT_CLASS = LeftCollidedComponent.class;
    static final Class<? extends Component> ANY_COLLIDED_COMPONENT_CLASS = AnyCollidedComponent.class;
    static final Class<? extends Component> COLLISION_COMPONENT_CLASS = CollisionComponent.class;
    static final Class<? extends Component> DESTROY_COMPONENT_CLASS = DestroyComponent.class;
    static final Class<? extends Component> HEALTH_COMPONENT_CLASS = HealthComponent.class;
    static final Class<? extends Component> IMAGEVIEW_COMPONENT_CLASS = ImageViewComponent.class;
    static final Class<? extends Component> OPACITY_COMPONENT_CLASS = OpacityComponent.class;
    static final Class<? extends Component> NAME_COMPONENT_CLASS = NameComponent.class;
    static final Class<? extends Component> WIDTH_COMPONENT_CLASS = WidthComponent.class;
    static final Class<? extends Component> HEIGHT_COMPONENT_CLASS = HeightComponent.class;
    static final Class<? extends Component> AUDIO_COMPONENT_CLASS = AudioComponent.class;
    static final Class<? extends Component> SOUND_COMPONENT_CLASS = SoundComponent.class;
    static final Class<? extends Component> SPRITE_COMPONENT_CLASS = SpriteComponent.class;
    static final Class<? extends Component> SPAWN_ENTITY_COMPONENT_CLASS = SpawnEntityComponent.class;
    static final Class<? extends Component> TIMER_COMPONENT_CLASS = TimerComponent.class;
    static final Class<? extends Component> PLAY_AUDIO_COMPONENT_CLASS = PlayAudioComponent.class;
    static final Class<? extends Component> LIVES_COMPONENT_CLASS = LivesComponent.class;
    static final Class<? extends Component> SCORE_COMPONENT_CLASS = ScoreComponent.class;

    static final String GET_OLD_VALUE = "getOldValue";


    private Collection<Class<? extends Component>> myRequiredComponents;
    private Collection<Entity> myEntities;
    private Collection<KeyCode> myInputs;
    Engine myEngine;


    /**
     * Every System (regardless of its functionality) stores the same main Engine in its instance field,
     * and requires different sets of Component classes from an Entity so that it could be processed by the System
     * @param requiredComponents collection of Component classes required for an Entity to be processed by this System
     * @param engine the main Engine which initializes all Systems for a game and makes update() calls on each game loop
     */
    public VoogaSystem(Collection<Class<? extends Component>> requiredComponents, Engine engine) {
        myInputs = new ArrayList<>();
        myRequiredComponents = requiredComponents;
        myEngine = engine;
    }

    /**
     * Generic call expected to be made from Engine on every game loop
     * Receives the most up-to-date collection of Entities currently existing in the Game and user input KeyCodes
     * received on the frontend, filters the Entities to only interact with those equipped with required Components
     * to prepare for next-step processing. Call run() to execute own special operations on the Entities and user
     * input KeyCodes. Clear up the input KeyCodes after this System is done within current game loop.
     * @param entities Collection of Entities passed in from Engine
     * @param inputs Collection of keyCodes received by Runner and then passed in by Engine
     */
    public void update(Collection<Entity> entities, Collection<KeyCode> inputs) {
        myEntities = new ArrayList<>();
        myInputs = new ArrayList<>(inputs);

        for (Entity e: entities) {
            if (filter(e)) {
                myEntities.add(e);
            }
        }
        run();
    }

    /**
     * Verify whether an Entity is equipped with all the required Components in order to be handled by a System
     * @param e Entity to verify
     * @return boolean value indicating whether the Entity is a match to the System
     */
    private boolean filter(Entity e) {
        return e.hasComponents(myRequiredComponents);
    }

    /**
     * Abstract method in which every concrete System interacts with a matching Entity in its own way
     */
    protected abstract void run();

    /**
     * Allow concrete Systems to retrieve the private Collection of Entities stored in the super System
     * @return Collection of Entities held in the System
     */
    protected Collection<Entity> getEntities() {
        return myEntities;
    }

    /**
     * Allow concrete Systems to retrieve the private Collection of KeyCodes (user inputs) stored in the super System
     * @return Collection of Keycodes held in the System
     */
    Collection<KeyCode> getKeyCodes(){
        return Collections.unmodifiableCollection(myInputs);
    }

    /**
     * Wrapper method for retrieving the value stored in a Component's myValue field using the default getValue method
     * @param componentClazz class type of the target Component
     * @param entity the target Entity whose Component value we want to retrieve
     * @return the value stored at myValue field of the Component
     */
    protected Object getComponentValue(Class<? extends Component> componentClazz,Entity entity){
        return entity.getComponent(componentClazz).getValue();
    }

    /**
     * Wrapper method for retrieving the value stored in a Component by calling the specified method
     * @param componentClazz class type of the target Component
     * @param entity the target Entity whose Component value we want to retrieve
     * @param method name of the method that should be called to retrieve the Component value
     *               e.g. "getOldValue" on XPositionComponent for retrieving the previous XPosition value
     * @return the value stored at a desired field of the Component accessible via the method specified by the String argument
     */
    protected Object getComponentValue(Class<? extends Component> componentClazz,Entity entity, String method){
        return Reflection.callMethod(entity.getComponent(componentClazz),method);
    }
    

}
