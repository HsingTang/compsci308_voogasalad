package engine.external.actions;

import engine.external.component.ScoreComponent;

public class ScoreAction extends AssociatedEntityAction {
    public ScoreAction(ModifyType type, Double value) {
        setAction(type, value, ScoreComponent.class);
    }
}
