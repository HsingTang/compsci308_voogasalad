package engine.external.component;

/**
 * @author Hsingchih Tang
 * Stores a double value indicating the "layer" an Entity's ImageView is on
 * Used by Runner to organize front-end display of Entities
 */
public class ZPositionComponent extends Component<Double> {

    private final static double DEFAULT = 0.0;

    public ZPositionComponent(Double value) {
        super(value);
    }
    public ZPositionComponent() {
        super(DEFAULT);
    }
}
