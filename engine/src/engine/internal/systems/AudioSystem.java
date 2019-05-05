package engine.internal.systems;

import data.external.AssetDataManager;
import engine.external.Engine;
import engine.external.Entity;
import engine.external.component.AudioComponent;
import engine.external.component.Component;
import javafx.scene.media.Media;

import java.io.InputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.File;

import java.util.Collection;
import java.util.HashMap;

/**
 * @Author Hsingchih Tang
 * Reads the sound file path from SoundComponent of Entities, retrieves corresponding InputStream,
 * and converts them into Media to be stored in AudioComponents
 */
public class AudioSystem extends VoogaSystem {

    private static final Integer REAE_BYTE_NUM = 1024;

    AssetDataManager myDataManager;
    HashMap<Entity, String> myEntityPastSound;
    HashMap<String, Media> myMedia;

    /**
     * Accepts a reference to the Engine in charge of all Systems in current game, and a Collection of Component classes
     * that this System would require from an Entity in order to interact with its relevant Components
     *
     * @param requiredComponents collection of Component classes required for an Entity to be processed by this System
     * @param engine             the main Engine which initializes all Systems for a game and makes update() calls on each game loop
     */
    public AudioSystem(Collection<Class<? extends Component>> requiredComponents, Engine engine) {
        super(requiredComponents, engine);
        myDataManager = new AssetDataManager();
        myEntityPastSound = new HashMap<>();
        myMedia = new HashMap<>();
    }


    /**
     * Loops through all Entities with a SoundComponent, retrieves the corresponding sound file from database and
     * generates/updates the AudioComponent for this Entity to store the MediaPlayer for playing the sound
     * All MediaPlayers are cached in the System to reduce database and I/O accesses
     */
    protected void run(){
        for (Entity entity : this.getEntities()) {
            generateAudio(entity);
        }
    }


    private void generateAudio(Entity entity) {
        String audioName = (String) getComponentValue(SOUND_COMPONENT_CLASS, entity);
        if (!myEntityPastSound.containsKey(entity) || !myEntityPastSound.get(entity).equals(audioName)) {
//            System.out.println("generating Audio for "+audioName);
            myEntityPastSound.put(entity, audioName);
            retrieveAudio(audioName);
            if (myMedia.get(audioName) != null) {
                entity.addComponent(new AudioComponent(myMedia.get(audioName)));
            }
        }
    }


    private void retrieveAudio(String audioName) {
        if (!myMedia.containsKey(audioName)) {
            InputStream audioStream = myDataManager.loadSound(audioName);
            if (audioStream == null) {
                System.out.println("Audio file " + audioName + " not found in database");
                myMedia.put(audioName, null);
                return;
            }
            Media audio;
            try {
                audio = new Media(convertInputStreamToFile(audioStream).toURI().toString());
            } catch (IOException e) {
                System.out.println("AudioSystem couldn't create audio file for " + audioName);
                myMedia.put(audioName, null);
                return;
            }
            myMedia.put(audioName, audio);
        }
    }

    private File convertInputStreamToFile(InputStream inputStream) throws IOException {
        try {
            File audioFile = File.createTempFile("myAudio", ".mp3");
            FileOutputStream outputStream = new FileOutputStream(audioFile);
            int read;
            byte[] bytes = new byte[REAE_BYTE_NUM];
            while ((read = inputStream.read(bytes)) != -1) {
                outputStream.write(bytes, 0, read);
            }
            return audioFile;
        } catch (IOException e) {
            throw e;
        }
    }

}
