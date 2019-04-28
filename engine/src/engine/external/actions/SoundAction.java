package engine.external.actions;

import engine.external.Entity;
import engine.external.component.Component;
import engine.external.component.PlayAudioComponent;
import engine.external.component.SoundComponent;

import java.io.Serializable;
import java.util.function.Consumer;

public class SoundAction extends StringAction {
    public SoundAction(String pathname) {

        setAbsoluteAction(pathname, SoundComponent.class);

    }
    protected void setAbsoluteAction(String newValue, Class<? extends Component<String>> componentClass) {
        setAction((Consumer<Entity> & Serializable) (entity) -> {
            Component soundComponent = entity.getComponent(SoundComponent.class);
            soundComponent.setValue(newValue);

            entity.addComponent(new PlayAudioComponent(true));
        });
    }
}
