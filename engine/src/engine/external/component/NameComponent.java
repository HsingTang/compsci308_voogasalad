package engine.external.component;

/**
 * @author Hsingchih Tang
 * Stores the individual name String of an Entity
 * Every Entity should have a unique name
 */
public class NameComponent extends Component<String> {
    private final static String DEFAULT = "NoName";
    public NameComponent(String name) {
        super(name);
    }
    public NameComponent(){
        super(DEFAULT);
    }

}
