package ui;

import javafx.application.Platform;
import javafx.collections.MapChangeListener;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.input.ClipboardContent;
import javafx.scene.input.Dragboard;
import javafx.scene.input.TransferMode;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import ui.panes.ImageWithEntity;


import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;

/**
 * @author Carrie
 * @author Harry Ross
 * @author Anna Darwish
 * This class was created to provide general methods that can be used across the UI.
 * It's a place to hold reflection code that may be needed across multiple classes.
 */
public class Utility {

    private static final String UTILITY_RESOURCES = "utility";
    private static final String GENERAL_RESOURCES = "authoring_general";
    private static final String DEFAULT_STYLESHEET = "default.css";

    /**
     * Creates and returns a Button
     * If the reflection fails for the eventHandler, when the button is pressed, the text
     * on the button will change to the text associated with the utility.properties file under the
     * key "ButtonFail". This prevents the program from ever crashing
     * @param o The class that this method is being called in
     *          Ex. If one were making a button in the AssetManager, you would call this method
     *          Utility.makeButton(this, "closeWindow", "Close");
     * @param methodName Name of the method to be called when the button is pressed
     * @param buttonText Text to be displayed on the button
     * @return A Button with the above parameters
     */
    public static Button makeButton(Object o, String methodName, String buttonText){
        ResourceBundle resources = ResourceBundle.getBundle(UTILITY_RESOURCES);
        Button button = new Button();
        button.setOnMouseClicked(e -> {
            try {
                Method buttonMethod = o.getClass().getDeclaredMethod(methodName);
                buttonMethod.setAccessible(true);
                buttonMethod.invoke(o);
            } catch (Exception e1) {
                button.setText(resources.getString("ButtonFail"));
            }});
        button.setText(buttonText);
        return button;
    }

    /**
     * Creates and return a button whose invoked method takes any nonzero number of parameters
     * @param o The Object whose class the invoked method can be found within
     * @param methodName String name of method in Object O to invoke
     * @param buttonText Label text to display on button
     * @param methodParams Arguments to be passed into invoked method
     * @return Button whose action is set to specified method invocation
     */
    public static Button makeButton(Object o, String methodName, String buttonText, Object... methodParams){
        ResourceBundle resources = ResourceBundle.getBundle(UTILITY_RESOURCES);
        Button button = new Button();
        Method reference = findMethod(o, methodName, methodParams);
        button.setOnAction(e -> {
            try {
                reference.setAccessible(true);
                reference.invoke(o, methodParams);
            } catch (Exception e1) {
                button.setText(resources.getString("ButtonFail"));
            }});
        button.setText(buttonText);
        return button;
    }

    /**
     * Locates method with signature corresponding with given parameters, including superclasses, returns reference
     * to method -- important shortcoming, cannot distinguish between overloaded methods with same superclass as parameter
     * @param o Object to locate found method within class of
     * @param methodName String name of method to locate
     * @param methodParams Parameters to be used in search for correct method signature
     * @return Reference to found method
     */
    private static Method findMethod(Object o, String methodName, Object[] methodParams) {
        Method reference = null;
        for (Method m : o.getClass().getDeclaredMethods()) {
            if (m.getName().equals(methodName) && m.getParameterCount() == methodParams.length) {
                boolean matchesSignature = true;
                for (int i = 0; i < methodParams.length; i++) {
                    if (!m.getParameterTypes()[i].isAssignableFrom(methodParams[i].getClass())) {
                        matchesSignature = false;
                        break;
                    }
                }
                if (matchesSignature) {
                    reference = m;
                    break;
                }
            }
        }
        return reference;
    }

    /**
     * Check validity of new value from based on regex syntax from properties file
     */
    public static boolean isValidValue(Enum key, String newVal, String syntaxResource) {
        ResourceBundle bundle = ResourceBundle.getBundle(syntaxResource);
        if (bundle.containsKey(key.name())) { // Label matches syntax, valid
            if (newVal.matches(bundle.getString(key.name()))) {
                return true;
            } else {
                ErrorBox error = new ErrorBox("Variable Error", "Invalid variable, refer to documentation for syntax");
                error.display();
            }
        }
        return false;
    }

    public static Scene createGeneralPane(Node header, Node content, List<Node> myNodes){
        HBox userControls = new HBox();
        userControls.getChildren().addAll(myNodes);
        BorderPane borderPane = new BorderPane(content, header, null, userControls, null);
        borderPane.getStylesheets().add("default.css");
        borderPane.getStyleClass().add("dialog-window");
        borderPane.getCenter().getStyleClass().add("center-pane");
        borderPane.getTop().getStyleClass().add("top-pane");
        return new Scene(borderPane);

    }

    /**
     * Creates a dialog pane with specified header, content, and buttons, returns Scene containing defined content
     * @param header Node to be displayed at top of dialog
     * @param content Node to be displayed at center of dialog
     * @param buttonsList List of buttons to populate a button bar at the bottom of dialog
     * @return Scene containing newly created dialog pane
     */
    public static Scene createDialogPane(Node header, Node content, List<Button> buttonsList) {
        if (header == null)
            header = new HBox();
        if (content == null)
            content = new HBox();

        BorderPane borderPane = new BorderPane(content, header, null, createButtonBar(buttonsList), null);
        Scene scene = new Scene(borderPane);

        scene.getStylesheets().add(DEFAULT_STYLESHEET);
        borderPane.getStyleClass().add("dialog-window");
        borderPane.getCenter().getStyleClass().add("center-pane");
        borderPane.getTop().getStyleClass().add("top-pane");

        return scene;
    }

    /**
     * Creates a bar of buttons that can be placed anywhere in UI as a Node. Buttons are displayed in the order passed.
     * @param buttonList List of Buttons to display in bar
     * @return Node of Buttons as a bar
     */
    public static Node createButtonBar(List<Button> buttonList) {
        HBox rtn = new HBox();
        rtn.getChildren().addAll(buttonList);
        rtn.getStyleClass().add("buttons-bar");
        if (!buttonList.isEmpty())
            Platform.runLater(() -> buttonList.get(0).requestFocus());
        return rtn;
    }

    /**
     * Passed a Map where keys are CSS selectors for label and values are label text
     */
    public static Node createLabelsGroup(Map<String, List<String>> labels) {
        VBox labelBox = new VBox();
        for (String labelType : labels.keySet()) {
            for (String newLabelText : labels.get(labelType)) {
                Label newLabel = new Label(newLabelText);
                newLabel.getStyleClass().add(labelType);
                labelBox.getChildren().add(newLabel);
            }
        }
        return labelBox;
    }

    /**
     * This takes an AuthoringEntity and creates an instance of ImageWithEntity
     * @param entity AuthoringEntity
     * @return ImageWithEntity
     */
    public static ImageWithEntity createImageWithEntity(AuthoringEntity entity){
        ResourceBundle generalResources = ResourceBundle.getBundle("authoring_general");

        String imageName = entity.getPropertyMap().get(EntityField.IMAGE);
        ImageWithEntity imageWithEntity = new ImageWithEntity(makeImageAssetInputStream(imageName), entity); //closed
        return imageWithEntity;

    }

    /**
     * Attempts to create a file input stream with the given string path
     * @param imageName String of the path to the desired fileinputstream file
     * @return FileInputStream
     */
    public static FileInputStream makeImageAssetInputStream(String imageName){  //closed
        ResourceBundle pathResources = ResourceBundle.getBundle(GENERAL_RESOURCES);
        File imageAssetFolder = new File(pathResources.getString("images_filepath"));
        FileInputStream result = null;  //closed
        for(File file : getAllFiles(imageAssetFolder)){
            if(file.getName().equals(imageName)){
                try {
                    result = new FileInputStream(file.getPath());   //closed
                    return result;
                } catch (FileNotFoundException e) {
                    String[] info = pathResources.getString("FileException").split(",");
                    ErrorBox errorBox = new ErrorBox(info[0], info[1]);
                    errorBox.display();
                    return result;
                }
            }
        }
        return result;
    }

    /**
     * This method takes in an input stream and closes it and catches the possible exceptions
     * @param fileInputStream
     */
    public static void closeInputStream(FileInputStream fileInputStream){
        try {
            fileInputStream.close();
        } catch (IOException e) {
            ResourceBundle resourceBundle = ResourceBundle.getBundle(UTILITY_RESOURCES);
            String header = resourceBundle.getString("CloseInputStreamError");
            String content = resourceBundle.getString("CloseInputStreamErrorContent");
            ErrorBox errorBox = new ErrorBox(header, content);
            errorBox.display();
        }
    }

    /**
     * This method takes in an ImageWithEntity and sets up drag and drop.
     * It is used in the Viewer as well as the UserCreatedTypesPane
     * @param imageWithEntity
     */
    public static void setupDragAndDropImage(ImageWithEntity imageWithEntity){
        Dragboard db = imageWithEntity.startDragAndDrop(TransferMode.MOVE);
        ClipboardContent content = new ClipboardContent();
        content.putImage(imageWithEntity.getImage());
        db.setContent(content);
    }

    /**
     * This searches for the change name in the given resource bundle and attempts to call that as a method
     * from the object passed in
     * Used by ImageWithEntity and Viewer
     * @param resourceBundle
     * @param change change from a map
     * @param o Object that contains the method
     */
    public static void makeAndCallMethod(ResourceBundle resourceBundle, MapChangeListener.Change<? extends Enum,? extends String> change, Object o){
        String methodName = resourceBundle.getString(change.getKey().toString());
        try {
            Method method = o.getClass().getDeclaredMethod(methodName, String.class);
            method.setAccessible(true);
            method.invoke(o, change.getValueAdded());
        } catch (Exception e) {
            String header = resourceBundle.getString("MethodReflectionError");
            String content = resourceBundle.getString("MethodReflectionErrorContent");
            ErrorBox errorBox = new ErrorBox(header, content);
            errorBox.display();
        }
    }

    /**
     * Takes in an upper directory and iterates through all sub-files
     * and sub-directories and creates a list of every file within those
     * @param upperDirectory File of the starting directory
     * @return Map of the subdirectories to a list of their files, Note: some of these files may be directories in an dof themselves
     */
    public static Map<File, List<File>> getSubFoldersToFiles(File upperDirectory){
        Map<File, List<File>> map = new HashMap<>();
        for(File file : upperDirectory.listFiles()){
            if(file.isDirectory()){
                map.put(file, Arrays.asList(file.listFiles()));
            }
        }
        return map;
    }

    /**
     * Takes in a directory and returns a list of all the files contained
     * @param directory Folder from which user wants files
     * @return List<File> all files contained in directory, omitting any sub-directories
     */
    public static List<File> getFilesFromFolder(File directory){
        List<File> result = new ArrayList<>();
        for(File file : directory.listFiles()){
            if(!file.isDirectory()){
                result.add(file);
            }
        }
        return result;
    }

    private static List<File> getAllFiles(File upperDirectory){
        List<File> list = new ArrayList<>();
        for(File file : upperDirectory.listFiles()){
            if(file.isDirectory()){
                list.addAll(getAllFiles(file));
            }
            else{
                list.add(file);
            }
        }
        return list;
    }
}
