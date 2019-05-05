package engine.external.actions;


import engine.external.component.OpacityComponent;

/**
 * @author engine
 * Modifies the opacity of a component
 */
public class OpacityAction extends NumericAction {
    public OpacityAction(ModifyType type, Double opacity){
        setAction(type, opacity, OpacityComponent.class);
    }

}