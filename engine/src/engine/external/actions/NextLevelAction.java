package engine.external.actions;


import engine.external.component.NextLevelComponent;

public class NextLevelAction extends AddComponentAction {
    private static final String MESSAGE = "Go To Level ";
    private double myLevel;
    public NextLevelAction(Double level){
        myLevel = level;
        setAbsoluteAction(new NextLevelComponent(level));
        myComponentClass = NextLevelComponent.class;
    }
    public String toString(){
        return MESSAGE + myLevel;
    }



}