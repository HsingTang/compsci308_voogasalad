package engine.external.actions;

import engine.external.component.ScoreComponent;

/**
 * @author Feroze
 * The Score Action acts on the associated entity of the entity, assuming that the associated entity possesses a
 * ScoreComponent. This action is used under the assumption that the associated entity is some type of global game
 * object that can keep track of score and/or lives across levels.
 */
public class ScoreAction extends AssociatedEntityAction {
    public ScoreAction(ModifyType type, Double value) {
        setAction(type, value, ScoreComponent.class);
    }
}
