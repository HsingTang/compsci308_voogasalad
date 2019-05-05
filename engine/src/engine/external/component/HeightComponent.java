package engine.external.component;

/**
 * @author Hsingchih Tang
 * Stores the height value of the ImageView of this Entity if it also has a SpriteComponent
 * ImageViewSystem adjusts the ImageView for Entities on each game loop using the value stored in this Component
 */
public class HeightComponent extends Component<Double> {

    private final static double DEFAULT = 10.0;

    public HeightComponent(Double value) {
        super(value);
    }
    public HeightComponent() {
        super(DEFAULT);
    }
}
