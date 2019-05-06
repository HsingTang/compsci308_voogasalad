package engine.external.component;

/**
 * @author Hsingchih Tang
 * Stores a double value indicating the current velocity of an Entity in Y direction
 */
public class YVelocityComponent extends Component<Double> {

    private final static double DEFAULT = 1.0;

    public YVelocityComponent(Double value) {
        super(value);
    }
    public YVelocityComponent() {
        super(DEFAULT);
    }
}
