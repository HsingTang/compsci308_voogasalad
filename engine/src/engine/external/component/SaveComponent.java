package engine.external.component;

/**
 * @author Hsingchih Tang
 * Notify Runner to save the current game status by calling Engine.saveGame() usually when reaching checkpoints
 * Could be attached to an Entity by some Action created in Authoring
 */
public class SaveComponent extends Component<Boolean> {

    private final static boolean DEFAULT = true;

    public SaveComponent(Boolean save) {
        super(save);
    }
    public SaveComponent() {
        super(DEFAULT);
    }
}
