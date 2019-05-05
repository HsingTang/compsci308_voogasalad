package engine.external.component;

/**
 * @author Hsingchih Tang
 * Stores a double value indicating the current velocity of an Entity in X direction
 */
public class XVelocityComponent extends Component<Double> {

    private final static double DEFAULT = 1.0;

    public XVelocityComponent(Double value) {
        super(value);
    }
    public XVelocityComponent() {
        super(DEFAULT);
    }


}
