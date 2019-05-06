package engine.external.component;

/**
 * @author Hsingchih Tang
 * @author
 * Stores a String indicating the name of a sound file
 * Used by Engine to retrieve the sound file from database and create AudioComponents for Entities with sound effects
 */
public class SoundComponent extends Component<String> {

    private final static String DEFAULT = "coin";

    public SoundComponent() {
        super(DEFAULT);
    }

    public SoundComponent(String pathname) {
        super(pathname);
    }
}
