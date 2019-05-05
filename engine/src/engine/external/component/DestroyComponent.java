package engine.external.component;


/**
 * @author Feroze Mohideen
 * @author Hsingchih Tang
 * Indicates that an Entity should be permanently removed from Engine by CleanupSystem
 * Can be attached to an Entity by Engine or some Action created in Authoring
 */

public class DestroyComponent extends Component<Boolean> {

    public DestroyComponent(Boolean destroy) {
        super(destroy);
    }

}
