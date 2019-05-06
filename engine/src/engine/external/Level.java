package engine.external;


import java.io.Serializable;
import java.util.Collection;
import java.util.HashSet;

/**
 * @author Hsingchih Tang
 * Stores Entity and Event data for a certain level in a Game
 * Distinct Levels of the same Game may store different data from each other
 */
public class Level implements Serializable {

    private String myLabel;
    private Double myWidth;
    private Double myHeight;
    private String myBackground;
    private String myMusic;

    private Collection<Entity> myEntities;
    private Collection<IEventEngine> myEvents;
    private Level myCheckPoint;


    /**
     * Initialized fields for storing the Events and Entities tied to this game Level
     * No duplicate Entity objects or Event objects allowed in the same Level
     */
    public Level() {
        myEntities = new HashSet<>();
        myEvents = new HashSet<>();
    }

    /**
     * Attaches a new Entity object to this Game Level
     * @param entity new Entity to be added
     */
    public void addEntity(Entity entity) {
        myEntities.add(entity);
    }

    public void clearEntities(){
        myEntities.clear();
    }

    /**
     * Attaches a new Event object to this Game Level
     * @param event new Event to be added
     */
    public void addEvent(IEventEngine event){
        myEvents.add(event);
    }

    /**
     * Attaches a Collection of Event object to this Game Level
     * @param events new Events to be added
     */
    public void addEvent(Collection<IEventEngine> events){
        myEvents.addAll(events);
    }

    /**
     * Retrieves all Entities attached to a Game Level
     * @return HashSet of Entities stored for this Level
     */
    public Collection<Entity> getEntities() {
        return myEntities;
    }

    /**
     * Retrieves all Events attached to a Game Level
     * @return HashSet of Events stored for this Level
     */
    public Collection<IEventEngine> getEvents() {
        return myEvents;
    }

    /**
     * Sets label to be associated with a Level for the purposes of distinguishing in Authoring Environment on reload
     * @param label String level label
     */
    public void setLabel(String label) {
        myLabel = label;
    }

    /**
     * Sets background image file path to display during gameplay
     * @param imageResource String file path
     */
    public void setBackground(String imageResource) {
        myBackground = imageResource;
    }

    /**
     * Sets width of Level
     * @param width double width of Level
     */
    public void setWidth(double width) {
        myWidth = width;
    }

    /**
     * Sets height of Level
     * @param height height double height of Level
     */
    public void setHeight(double height) {
        myHeight = height;
    }

    /**
     * Sets the background music for the Game Level
     * @param musicResource String argument used to retrieve the music file from database
     */
    public void setMusic(String musicResource) {
        myMusic = musicResource;
    }

    /**
     * Returns Label associated with a Level for display
     * @return String Level label
     */
    public String getLabel() {
        return myLabel;
    }

    /**
     * Returns background image file path for Level
     * @return String image file path
     */
    public String getBackground() {
        return myBackground;
    }

    /**
     * @return String argument with which the background music could be retrieved from database
     */
    public String getMusic() {
        return myMusic;
    }

    /**
     * Returns width of Level
     * @return Double width of Level
     */
    public Double getWidth() {
        return myWidth;
    }

    /**
     * Returns height of Level
     * @return Double height of Level
     */
    public Double getHeight() {
        return myHeight;
    }

}
