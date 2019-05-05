package engine.external.component;

/**
 * @author Hsingchih Tang
 * Stores a boolean value indicating player winning or losing a game
 * Could be attached to an Entity by Engine or some Action created in Authoring
 * Used by Runner to detect when a game should terminate
 */
public class ProgressionComponent extends Component<Boolean> {
    private final static boolean DEFAULT = false;
    public ProgressionComponent(Boolean progress) {
        super(progress);
    }
    public ProgressionComponent(){
        super(DEFAULT);
    }
}
