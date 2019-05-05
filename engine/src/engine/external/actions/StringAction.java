package engine.external.actions;

import engine.external.component.Component;

/**
 * The StringAction applies to Components whose getValue() methods return Strings.
 * As a result, there is no scaling or addition that can be done on Strings, just replacement
 *
 * @author Feroze
 */
public abstract class StringAction extends Action<String> {
    private String myNewValue;
    private static final String CHANGE = "Switch ";
    private static final String TO = " to ";
    private static final String COMPONENT = "Component";
    /**
     * This method used by subclass objects when they are constructed specifies the lambda, which
     * performs a set operation on this new value.
     *
     * @param newValue       new String to change the component value to
     * @param componentClass the class of the specified component
     */
    public void setAction(String newValue, Class<? extends Component<String>> componentClass) {
        super.setAbsoluteAction(newValue, componentClass);
        myNewValue = newValue;
        myComponentClass = componentClass;
    }
    protected void setMyNewValue(String value){
        myNewValue = value;
    }
    public String toString(){
        return CHANGE + myComponentClass.getSimpleName().replaceAll(COMPONENT,"") + TO + myNewValue;
    }
}
