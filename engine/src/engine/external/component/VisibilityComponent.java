package engine.external.component;
@Deprecated
public class  VisibilityComponent extends Component<Boolean> {

    private final static boolean DEFAULT = true;

    public VisibilityComponent() {
        super(DEFAULT);
    }
    public VisibilityComponent(Boolean visible) {
        super(visible);
    }

}
