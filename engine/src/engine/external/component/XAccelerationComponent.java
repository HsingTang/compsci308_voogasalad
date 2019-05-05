package engine.external.component;

/**
 * @author Hsingchih Tang
 * Stores a double value indicating the current acceleration of an Entity in X direction
 */
public class XAccelerationComponent extends Component<Double> {

    private final static double DEFAULT = 0.0;

    public XAccelerationComponent(Double value) {
        super(value);
    }
    public XAccelerationComponent() {
        super(DEFAULT);
    }


}
