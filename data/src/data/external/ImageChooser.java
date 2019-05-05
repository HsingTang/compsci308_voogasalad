package data.external;

import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;
import java.sql.SQLException;
import java.util.List;

/**
 * Allows game center to easily upload new profile pictures for users and have them save to the database
 */
public class ImageChooser {

    private static final String PNG = "png";
    private static final String JPG = "jpg";
    private static final String GIF = "gif";
    private static final List<String> IMAGE_EXTENSIONS = List.of(PNG, JPG, GIF);
    private static final String WILDCARD = "*.";

    private String myUserName;
    private DataManager myDataManager;

    /**
     * Image chooser constructor
     * @param userName name of the user, so image gets saved to the correct user
     */
    public ImageChooser(String userName){
        myDataManager = new DataManager();
        myUserName = userName;
    }

    /**
     * Creates pop up that allows user to upload profile picture and then saves the profile pic to the database
     */
    public void uploadImage() {
        Stage stage = new Stage();
        FileChooser fileChooser = new FileChooser();
        addExtensionsFilter(fileChooser);
        File selectedFile = fileChooser.showOpenDialog(stage);
        if(selectedFile != null){
            String savedName = myUserName + selectedFile.getName();
            try {
                System.out.println(selectedFile);
                myDataManager.setProfilePic(myUserName, selectedFile);
            } catch (SQLException e) {
                // Do nothing if the upload fails, agreed upon handling with game center, let user try again later
            }
        }
    }

    private void addExtensionsFilter(FileChooser chooser) {
        for(String extension : IMAGE_EXTENSIONS){
            FileChooser.ExtensionFilter extensionFilter = new FileChooser.ExtensionFilter(extension, WILDCARD + extension);
            chooser.getExtensionFilters().add(extensionFilter);
        }
    }
}
