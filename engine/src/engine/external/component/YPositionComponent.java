package engine.external.component;

/**
 * @author Lucas Liu
 * @author Hsingchih Tang
 * <p>
 * Component with one-step history
 * Stores double values indicating the current and previous positions of an Entity in Y direction
 * Automatically updates oldValue field whenever value is changed
 */
public class YPositionComponent extends Component<Double> {
    private final static double DEFAULT = 0.0;
    private Double oldValue;


    public YPositionComponent() {
        super(DEFAULT);
    }

    public YPositionComponent(Double value) {
        super(value);
        oldValue = value;
    }

    @Override
    public void setValue(Double value) {
        oldValue = Double.valueOf(myValue);
        myValue = value;
    }

    public Double getOldValue() {
        return oldValue;
    }

    public void revertValue(Double value) {
        myValue = Double.valueOf(value);
        oldValue = Double.valueOf(value);
    }

}