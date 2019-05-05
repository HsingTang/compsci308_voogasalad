package engine.external.component;

/**
 * @author Anna
 * @author Hsingchih Tang
 * Stores the Double indicating opacity of an Entity's ImageView if it has a SpriteComponent
 * ImageViewSystem adjusts the ImageView for Entities on each game loop using the value stored in this Component
 */
public class OpacityComponent extends Component<Double> {

    private final static double DEFAULT = 1.0;

    public OpacityComponent(Double value){
        super(value);
    }
    public OpacityComponent() {
        super(DEFAULT);
    }
}

