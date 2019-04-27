package engine.external.events;

import engine.external.actions.Action;
import engine.external.conditions.CollisionCondition;
import engine.external.conditions.Condition;

public abstract class CollisionEvent extends Event {
    private String myCollisionWithEntity;
    private boolean myGrouped;

    public boolean getGrouped() {
        return myGrouped;
    }

    protected void setGrouped(boolean grouped) {
        myGrouped = grouped;
    }

    public String getCollisionWithEntity() {
        return myCollisionWithEntity;
    }

    protected void setCollisionWithEntity(String collidedWithEntity) {
        myCollisionWithEntity = collidedWithEntity;
    }

    public void removeActions(Action actionToRemove){
        super.removeActions(actionToRemove);
    }

    public void addActions(Action actionToAdd){
        super.addActions(actionToAdd);
    }

    public void addConditions(Condition addCondition){
        if (addCondition.getClass().equals(CollisionCondition.class)){
            return;
        }
        super.addConditions(addCondition);
    }

    public void removeConditions(Condition removeCondition){
        if (removeCondition.getClass().equals(CollisionCondition.class)){
            return;
        }
        super.removeConditions(removeCondition);
    }
}
