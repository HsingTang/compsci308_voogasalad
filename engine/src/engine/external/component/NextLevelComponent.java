package engine.external.component;

/**
 * @author Hsingchih Tang
 * Stores an integer indicating the next game level to run once player wins the current level
 * Could be attached to an Entity by some Action created in Authoring
 */
public class NextLevelComponent extends Component<Double> {
    private final static double DEFAULT = 1.0;
    public NextLevelComponent(Double level){
        super(level);
    }
    public NextLevelComponent(){
        super(DEFAULT);
    }
}
