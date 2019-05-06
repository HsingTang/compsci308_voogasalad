package engine.external.component;

import javafx.scene.media.Media;

/**
 * @author Hsingchih Tang
 * Stores the audio files to be played in the game
 * requires Audio file to be stored so that Runner can access the media and play the sound
 */
public class AudioComponent extends Component<Media> {
    public AudioComponent(Media audioFile) {
        super(audioFile);
    }
}
