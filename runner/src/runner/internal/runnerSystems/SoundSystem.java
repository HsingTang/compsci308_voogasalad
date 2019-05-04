package runner.internal.runnerSystems;

import engine.external.Entity;
import engine.external.component.AudioComponent;
import engine.external.component.Component;
import engine.external.component.PlayAudioComponent;
import runner.internal.AudioManager;
import runner.internal.LevelRunner;
import java.util.Collection;

/**
 * System that plays sounds at appropriate times
 * @author Louis Jensen
 */
public class SoundSystem extends RunnerSystem {
    private AudioManager myAudioManager;

    /**
     * Constructor for SoundSystem
     * @param requiredComponents - list of all components necessary for system
     * @param levelRunner - LevelRunner object so that system can modify the level
     * @param audioManager - AudioManager that can play sounds
     */
    public SoundSystem(Collection<Class<? extends Component>> requiredComponents, LevelRunner levelRunner, AudioManager audioManager) {
        super(requiredComponents, levelRunner);
        myAudioManager = audioManager;
    }

    /**
     * Plays sound if PlayAudioComponent exists
     * Calls the audio manager to handle threading
     * and plays sound while game continues to run
     */
    @Override
    public void run() {
        for (Entity entity : this.getEntities()) {
            if (entity.hasComponents(PlayAudioComponent.class) && (Boolean) getComponentValue(PlayAudioComponent.class, entity) && entity.hasComponents(AudioComponent.class)) {
                playSound(entity);
            }
        }
    }

    private void playSound(Entity entity) {
        myAudioManager.playSound(entity);
    }
}
