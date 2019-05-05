package engine.external.component;

/**
 * @author Hsingchih Tang
 * Stores the current score earned by a player
 * Transferrable across multiple levels in the same game
 */
public class ScoreComponent extends Component<Double> {
    private final static double DEFAULT = 1.0;

    public ScoreComponent() {
        super(DEFAULT);
    }

    public ScoreComponent(Double value) {
        super(value);
    }
}
