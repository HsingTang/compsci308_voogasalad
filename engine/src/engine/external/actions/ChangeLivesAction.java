package engine.external.actions;

import engine.external.component.LivesComponent;

/**
 * @author Feroze
 *
 * Edits the lives component of an entity
 */
public class ChangeLivesAction extends NumericAction {
    public ChangeLivesAction(ModifyType type, Double lives) {
        setAction(type, lives, LivesComponent.class);
    }
}
