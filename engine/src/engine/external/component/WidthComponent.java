package engine.external.component;

/**
 * @author Hsingchih Tang
 * Stores the width value of the ImageView of this Entity if it also has a SpriteComponent
 * ImageViewSystem adjusts the ImageView for Entities on each game loop using the value stored in this Component
 */
public class WidthComponent extends Component<Double> {

    private final static double DEFAULT = 10.0;

    public WidthComponent(Double value) {
        super(value);
    }
    public WidthComponent() {
        super(DEFAULT);
    }
}
