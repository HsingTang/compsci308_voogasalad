package engine.external.component;

/**
 * @author engine
 * This component stores the group of an entity so that group events can affect all entities of a group.
 */
public class GroupComponent extends Component<String> {
    private final static String DEFAULT = "NoGroup";

    public GroupComponent(String name) {
        super(name);
    }
    public GroupComponent(){
        super(DEFAULT);
    }
}
