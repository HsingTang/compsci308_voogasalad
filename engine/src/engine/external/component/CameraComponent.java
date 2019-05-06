package engine.external.component;

/**
 * @author Hsingchih Tang
 * Only equipped by the main game character entity of a Level
 * Runner identifies the main entity with this Component and scrolls the view as the entity moves
 */
public class CameraComponent extends Component<Boolean> {
    private final static boolean DEFAULT = false;
    public CameraComponent(Boolean cameraOn) {
        super(cameraOn);
    }
    public CameraComponent(){
        super(DEFAULT);
    }
}
