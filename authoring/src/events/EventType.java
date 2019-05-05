package events;

import java.util.Arrays;
import java.util.List;
/**
 * This enum was helpful for reflection and translating between displaying options for the users to create events and
 * instantiating them according to the user's request
 */
public enum EventType {

    BottomCollision ("Bottom Collision","Interactive", "engine.external.events.BottomCollisionEvent"),
    LeftCollision("Left Collision", "Interactive", "engine.external.events.LeftCollisionEvent"),
    RightCollision ("Right Collision",  "Interactive", "engine.external.events.RightCollisionEvent"),
    TopCollision ("Top Collision", "Interactive", "engine.external.events.TopCollisionEvent"),
    General ("General", "Conditional", "engine.external.events.Event"),
    KeyInput ( "Key Input", "Key", "engine.external.events.Event");
    
    public static final List<String> allDisplayNames = Arrays.asList(BottomCollision.displayName,LeftCollision.displayName,
            RightCollision.displayName,TopCollision.displayName, General.displayName, KeyInput.displayName);
    private final String displayName;
    private final String interactive;
    private final String className;

    EventType(String displayName, String eventClassifier, String className) {
        this.displayName = displayName;
        this.interactive = eventClassifier;
        this.className = className;
    }


    public String isInteractive(){return this.interactive;}

    public String getClassName(){return this.className;}


}
