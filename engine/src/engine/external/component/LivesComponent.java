package engine.external.component;

/**
 * @author Hsingchih Tang
 * Stores the number of lives left only for the main game character Entity of a Level
 * Player loses the game by default once the number of lives fall below zero
 */
public class LivesComponent extends Component<Double> {
    private final static double DEFAULT = 2.0;

    public LivesComponent(Double value) {
        super(value);
    }
    public LivesComponent(){
        super(DEFAULT);
    }
}
