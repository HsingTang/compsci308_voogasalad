package engine.external.component;

/**
 * @author engine
 *
 * This component holds a timer value for each entity, which is decremented every refresh by the Timer System
 */
public class TimerComponent extends Component<Double> {
    private final static double DEFAULT = 10.0;

    public TimerComponent(Double value) {
        super(value);
    }
    public TimerComponent() {
        super(DEFAULT);
    }
}
