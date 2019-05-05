package engine.external.actions;

import engine.external.component.ProgressionComponent;

public class ProgressionAction extends AddComponentAction<Boolean> {
    private static final String MESSAGE = "Add Checkpoint";
    public ProgressionAction(Boolean progress){
        setAbsoluteAction(new ProgressionComponent(progress));
        myComponentClass = ProgressionComponent.class;
    }
    @Override
    public String toString(){
        return MESSAGE;
    }


}