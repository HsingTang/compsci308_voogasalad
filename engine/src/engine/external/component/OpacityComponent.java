package engine.external.component;

public class OpacityComponent extends Component<Double> {

    private final static double DEFAULT = 1.0;

    public OpacityComponent() {
        super(DEFAULT);
    }

    public OpacityComponent(Double value){
        super(value);
    }
}

