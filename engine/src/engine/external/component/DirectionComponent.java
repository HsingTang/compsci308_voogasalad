package engine.external.component;

/**
 * @author engine
 *
 * This component stores the direction of an entity, which we have encoded as an integer.
 */
public class DirectionComponent extends Component<Double> {
    private final static double DEFAULT = 2.0;

    public DirectionComponent(Double value) {
        super(value);
    }
    public DirectionComponent(){
        super(DEFAULT);
    }

}
