package events;
import engine.external.actions.Action;
import engine.external.actions.NumericAction;
import engine.external.conditions.Condition;
import engine.external.conditions.ConditionType;
import ui.UIException;
import voogasalad.util.reflection.Reflection;
import java.util.ResourceBundle;
/**
 * This class handles managing user input in order to create either conditions and actions associated with events. It uses
 * the voogasalad_util module as these event components are created using reflection. Having one class that is invoked for saving
 * has helped with debugging and centralizes any issues that may arise from saving actions and conditions
 * @author Anna Darwish
 */
public class EventBuilder {

    private static final String ERROR_PACKAGE_NAME = "error_messages";
    private static final String ACTION_PACKAGE_PREFIX = "engine.external.actions.";
    private static final String COMPONENT_PACKAGE_PREFIX = "engine.external.component.";

    private static final String ACTION_KEY = "Action";
    private static final String COMPONENT_KEY = "Component";

    private static final ResourceBundle myErrors = ResourceBundle.getBundle(ERROR_PACKAGE_NAME);
    private static final String ERROR_ONE_KEY = "InvalidCondition";
    private static final String ERROR_TWO_KEY = "InvalidAction";

    /**
     * Typically when saving a condition, the values bound to the controls where the user input information vary according
     * to each other's values. Since this makes it difficult to assert what type of condition is being saved at a given
     * instance, this method helps to handle the different cases agreed upon between the Engine and Authoring for creating
     * conditions for general events
     */
    public Condition createGeneralCondition(String componentName, String comparatorDisplayName, String conditionValue) throws UIException{
        ConditionType myConditionType = ConditionType.valueOf(comparatorDisplayName.replaceAll(" ",""));
        String conditionClassName = myConditionType.getClassName();
        try{
            Class componentClass = Class.forName(COMPONENT_PACKAGE_PREFIX + componentName + COMPONENT_KEY);
            Double value = Double.parseDouble(conditionValue);
            return  (Condition)Reflection.createInstance(conditionClassName,componentClass,value);
        }
        catch(Exception nonNumericCondition){
            try {
                Class componentClass = Class.forName(COMPONENT_PACKAGE_PREFIX + componentName + COMPONENT_KEY);
                return (Condition)Reflection.createInstance(conditionClassName,componentClass,conditionValue) ;

            }
            catch(Exception e2) {
                UIException myException = new UIException(myErrors.getString(ERROR_ONE_KEY));
                myException.displayUIException();
            }
        }
        throw new UIException(myErrors.getString(ERROR_ONE_KEY));
    }

    /**
     * Similarly when saving an action, the values bound to the controls where the user input information vary according
     * to each other's values. Since this makes it difficult to assert what type of action is being saved at a given
     * instance, this method helps to handle the different cases agreed upon between the Engine and Authoring for creating
     * actions for general events.
     */
    public Action createGeneralAction(String componentName, String modifierName, String conditionValue) throws UIException{
        String actionClassName = ACTION_PACKAGE_PREFIX + componentName + ACTION_KEY;
        try {
            return (Action) Reflection.createInstance(actionClassName, NumericAction.ModifyType.valueOf(modifierName),
                    Double.parseDouble(conditionValue));
        }
        catch(Exception nonNumericAction){
            try {
                return (Action) Reflection.createInstance(actionClassName,
                        Double.parseDouble(conditionValue));
              }
            catch(Exception nonComponentAction) {
                try {
                    return (Action) Reflection.createInstance(actionClassName, conditionValue);
                }
                catch (Exception e){
                    UIException myException = new UIException(myErrors.getString(ERROR_TWO_KEY));
                    myException.displayUIException();
                }
            }
        }
        throw new UIException(myErrors.getString(ERROR_TWO_KEY));

    }

}
