package ui.windows;

import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import ui.LevelField;
import ui.Propertable;
import ui.manager.ObjectManager;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

/**
 * @author Carrie Hunner
 * This class extends AssetManager and provides a
 * window for users to both upload and select previously
 * uploaded audio
 */
public class AudioManager extends AssetManager {

    private static final String EXTENSION_KEY = "AcceptableAudioExtensions";
    private static final String TITLE_KEY = "AudioTitle";
    private static final String ASSET_AUDIO_FOLDER_PATH = GENERAL_RESOURCES.getString("audio_filepath");
    private Map<Pane, ListView> myMap;

    /**
     * This is a default constructor that can be used whenever there is no level to bind to and
     * you don't want to allow for uploading new assets.
     */
    public AudioManager(){
        super(ASSET_AUDIO_FOLDER_PATH, TITLE_KEY, EXTENSION_KEY);
        myObjectManager = null;
        myPropertable = null;
    }

    /**
     * This is a constructor used when you want the selected audio to be added to the propertable (ie level)
     * @param propertable Level to add the background music to
     */
    public AudioManager(Propertable propertable){
        this();
        myPropertable = propertable;
    }

    /**
     * This constructor is for when you want to allow for uploading new audio. The ObjectManager
     * contains the game and authorname and this allows for uploading and saving new assets
     * @param objectManager
     */
    public AudioManager(ObjectManager objectManager){
        this();
        myObjectManager = objectManager;
    }



    /**
     * Method that adds a file to the manager
     * @param file File to be added
     */
    @Override
    protected void addAsset(File file, Pane pane) {
        myMap.putIfAbsent(pane, createAndFormatNewListView());
        ListView listView = myMap.get(pane);
        Label text = new Label(extractDisplayName(file.getName()));
        VBox vBox = new VBox(text);
        vBox.setFillWidth(true);
        vBox.setOnMouseClicked(mouseEvent -> {
            if(mouseEvent.getButton().equals(MouseButton.PRIMARY)){
                mySelectedAssetName = file.getName();
                if(mouseEvent.getClickCount() == 2){
                    Media sound = new Media(file.toURI().toString());
                    MediaPlayer mediaPlayer = new MediaPlayer(sound);
                    mediaPlayer.play();
                }
            }
        });
        if(listView.getItems().contains(vBox)){
            listView.getItems().remove(vBox);
        }
        listView.getItems().add(vBox);
        if(pane.getChildren().contains(listView)){
            pane.getChildren().remove(listView);
        }
        pane.getChildren().add(listView);
    }

    /**
     * initializes the hashmap for when populating the tabs
     */
    @Override
    protected void initializeSubClassVariables() {
        myMap = new HashMap<>();
    }


    /**
     * This method creates a new listview
     * @return Listview
     */
    protected ListView createAndFormatNewListView() {
        ListView listView = new ListView();
        return listView;
    }

    /**
     * This needs to be here for reflection purposes. In creating the buttons the
     * method must exist in the subclass
     */
    @Override
    protected void handleBrowse(){
        super.handleBrowse();
    }

    /**
     * Makes sure the selected asset is null so
     * other classes cannot access the selected
     * because the user closed and didn't apply it
     */
    @Override
    protected void handleClose(){
        super.handleClose();
    }

    /**
     * Sets the appropriate variables based on the selected Asset
     * before closing the window
     */
    @Override
    protected void handleApply() {
        if(!mySelectedAssetName.equals("")){
            if(myPropertable != null){
                myPropertable.getPropertyMap().put(LevelField.MUSIC, mySelectedAssetName);
            }
            this.close();
        }
        else{
            handleNoAssetSelected();
        }
    }
}
