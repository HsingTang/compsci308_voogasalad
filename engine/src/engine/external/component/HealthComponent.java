package engine.external.component;

/**
 * @author Hsingchih Tang
 * Stores the health value of an Entity
 * An Entity should by default be destroyed once its health falls below zero
 */
public class HealthComponent extends Component<Double> {
    private final static double DEFAULT = 2.0;

    public HealthComponent(Double value) {
        super(value);
    }
    public HealthComponent(){
        super(DEFAULT);
    }
}
