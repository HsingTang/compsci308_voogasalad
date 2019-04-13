package events;

import java.util.Arrays;
import java.util.List;

public enum EventType {

    BOTTOMCOLLISION ("Bottom Collision", BottomCollisionEvent.class, new Class<?>[]{String.class,String.class},"INTERACTIVE"),
    LEFTCOLLISION ("Left Collision", LeftCollisionEvent.class, new Class<?>[]{String.class,String.class}, "INTERACTIVE"),
    RIGHTCOLLISION ("Right Collision", RightCollisionEvent.class, new Class<?>[]{String.class,String.class}, "INTERACTIVE"),
    TOPCOLLISION ("Top Collision", TopCollisionEvent.class, new Class<?>[]{String.class,String.class}, "INTERACTIVE"),
    TIMER ("Timer", TimerEvent.class, new Class<?>[]{String.class,Double.class},"INTERACTIVE"),
    CONDITIONALEVENT ("Conditional", Event.class, new Class<?>[]{String.class},"CONDITIONAL");
    public static final List<String> allDisplayNames = Arrays.asList(BOTTOMCOLLISION.displayName,LEFTCOLLISION.displayName,
            RIGHTCOLLISION.displayName,TOPCOLLISION.displayName,TIMER.displayName, CONDITIONALEVENT.displayName);
    private final String displayName;
    private final Class<?> className;
    private final Class<?>[] classConstructorTypes;
    private final String eventClassifier;

    EventType(String displayName, Class<?> className, Class<?>[] constructorTypes, String eventClassifier) {
        this.displayName = displayName;
        this.className = className;
        this.classConstructorTypes = constructorTypes;
        this.eventClassifier = eventClassifier;
    }


    public Class<?>[] getConstructorTypes(){
        return this.classConstructorTypes;
    }

    public Class<?> getClassName(){
        return this.className;
    }

    public String getEventClassifier(){ return this.eventClassifier;}

    public String getDisplayName(){return this.displayName;}


}
