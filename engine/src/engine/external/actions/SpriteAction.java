package engine.external.actions;

import engine.external.component.SpriteComponent;

/**
 * @author Engine
 * Used to change the sprite of an entity
 */
public class SpriteAction extends StringAction {
    public SpriteAction(String pathname) {
        setAction(pathname, SpriteComponent.class);
    }
}
