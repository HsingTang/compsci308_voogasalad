package engine.external.component;

/**
 * @author Hsingchih Tang
 * Stores a double value indicating the current acceleration of an Entity in Y direction
 */
public class YAccelerationComponent extends Component<Double> {

    private final static double DEFAULT = 0.0;

    public YAccelerationComponent(Double value) {
        super(value);
    }

    public YAccelerationComponent() {
        super(DEFAULT);
    }
}
