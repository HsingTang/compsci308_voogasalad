package ui.windows;

import data.external.GameCenterData;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.control.TitledPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import ui.ErrorBox;
import ui.Propertable;
import ui.TreeNode;
import ui.Utility;
import ui.manager.ObjectManager;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.ResourceBundle;
import java.util.Set;

/**
 * @author Carrie Hunner
 * This Class creates an AssetManger Stage
 * It is a superclass designed to enable different assets
 * to be displayed. Currently its sub-classes are ImageManager and
 * Audio Manager. To add another subclass, the methods must be implemented
 * but the corresponding AssetManger properties file must be edited
 * to have a title for the pane and a list of acceptable file extensions
 */
abstract public class AssetManager extends Stage {
    private static final ResourceBundle RESOURCES = ResourceBundle.getBundle("asset_manager");
    protected static final ResourceBundle GENERAL_RESOURCES = ResourceBundle.getBundle("authoring_general");
    private static final ResourceBundle SEPARATOR_RESOURCES = ResourceBundle.getBundle("mainGUI_assets");
    private Set<String> myExtensions;
    private HBox myButtonHBox;
    protected String mySelectedAssetName;
    private TabPane myTabPane;
    private VBox myOuterVBox;
    protected ObjectManager myObjectManager;
    protected Propertable myPropertable;
    private static final String SELECT_BUTTONS = "SelectButtons";
    private static final String UPLOAD_BUTTONS = "UploadButtons";

    private static final String IO_ERROR = "IOError";
    private static final String ERROR_HEADER = "ErrorHeader";
    private static final String EXTENSION_PREFIX = "*.";
    private static final String DEFAULT_STYLE_SHEET = "default.css";
    private static final String ASSET_SPECIFIC_SHEET = "asset-manager";
    private static final String BUTTON_PANE_SHEET = "asset-manager-hbox";
    protected String myAssetFolderPath;
    protected String myTitleKey;
    protected String myExtensionKey;
    protected static final double SPACING = 10;
    protected static final int STAGE_WIDTH = 400;
    private static final int STAGE_HEIGHT = 400;
    private static final int BUTTON_SPACING = 20;
    private String mySavingPrefix;
    protected static final Insets INSETS = new Insets(SPACING, SPACING, SPACING, SPACING);
    private boolean isFirstOpening;

    /**
     * This is a constructor that forces the coder to input info for creating the window
     * @param assetFolderPath String of the path to the assetFolder
     * @param titleKey String of the key to the window title in the asset_manager properties file
     * @param extensionKey String of the key to the list of extensions in the asset_manager properties file
     */
    public AssetManager(String assetFolderPath, String titleKey, String extensionKey){
        myAssetFolderPath = assetFolderPath;
        myTitleKey = titleKey;
        myExtensionKey = extensionKey;
        isFirstOpening = true;
        mySelectedAssetName = "";
        mySavingPrefix = "";
        initializeVariables();
        initializeSubClassVariables();
        initializeStage();
        this.setOnShown(windowEvent -> {
            handleOnShown();
        });
    }

    private void handleOnShown() {
        if(isFirstOpening){
            fillExtensionSet();
            populateTabs();
            createButtonPane();
            isFirstOpening = false;
            setUpOuterPanes();
        }
        else{
            populateTabs();
        }
    }

    private void setUpOuterPanes() {
        myOuterVBox.getChildren().add(myTabPane);
        myOuterVBox.getChildren().add(myButtonHBox);
    }


    private void createButtonPane() {
        String buttonString;
        if(myObjectManager == null){
            buttonString = RESOURCES.getString(SELECT_BUTTONS);
        }
        else{
            buttonString = RESOURCES.getString(UPLOAD_BUTTONS);
        }
        String[] buttonInfo = buttonString.split(",");
        formatButtonHBox();
        for(String s : buttonInfo){
            String[] textAndMethod = s.split(" ");
            Button button = Utility.makeButton(this, textAndMethod[1], textAndMethod[0]);
            myButtonHBox.getChildren().add(button);
        }

    }

    private void formatButtonHBox() {
        myButtonHBox.setFillHeight(true);
        myButtonHBox.setSpacing(BUTTON_SPACING);
        myButtonHBox.setAlignment(Pos.CENTER);
    }

    private void populateTabs() {
        myTabPane.getTabs().clear();
        File assetFolder = new File(myAssetFolderPath);     //going to images directory
        List<File> fileList = Utility.getFilesFromFolder(assetFolder);

        Tab defaultTab = new Tab();
        defaultTab.setText("Default");
        defaultTab.setClosable(false);
        VBox vBox = new VBox();
        myTabPane.getTabs().add(defaultTab);
        ScrollPane defaultScrollPane = new ScrollPane();
        defaultScrollPane.setFitToWidth(true);
        defaultScrollPane.setContent(vBox);
        defaultTab.setContent(defaultScrollPane);
        TreeNode root = new TreeNode("root");

        Tab userUploaded = new Tab();
        userUploaded.setText("Uploaded");
        userUploaded.setClosable(false);
        VBox userVBox = new VBox();
        ScrollPane userScrollPane = new ScrollPane();
        userScrollPane.setFitToWidth(true);
        userScrollPane.setContent(userVBox);
        userUploaded.setContent(userScrollPane);
        myTabPane.getTabs().add(userUploaded);



        for(File file : fileList){
            //TODO check extensions here
            //assuming correct extensions from here on out
            String[] infoArray = file.getName().split("#");  //TODO make this character special somewhere
            if(infoArray.length <= 1){
                //add to default tab
                addAsset(file, userVBox);

            }
            else{
                TreeNode traverseNode = root;
                List<String> infoList = new ArrayList<>(Arrays.asList(infoArray));
                infoList.remove(0); //always "" because name should always start with "/" and splitting around "/"
                addToTree(infoList, traverseNode, file);
            }
        }

        TreeNode traverseNode = root;
        fillVBox(traverseNode, vBox);

    }

    private void fillVBox(TreeNode root, VBox vBox) {
        for(TreeNode treeNode : root.getNodeChildren()){
            List<TreeNode> toBePanes = treeNode.getNodeChildren();
            for(TreeNode treeNode1 : toBePanes) {
                TitledPane titledPane = new TitledPane();
                titledPane.setText(treeNode1.getName());
                titledPane.setAlignment(Pos.TOP_LEFT);
                vBox.getChildren().add(titledPane);
                VBox vBox1 = new VBox();
                ScrollPane scrollPane = new ScrollPane();
                scrollPane.setContent(vBox1);
                scrollPane.setFitToWidth(true);
                titledPane.setContent(scrollPane);
                fillVBox(treeNode1, vBox1);
            }
        }
        for(File file : root.getFileChildren()){
            addAsset(file, vBox);
        }
    }

    private void addToTree(List<String> info, TreeNode root, File file){
        if(info.size() == 1){
            root.addChild(file);
            return;
        }
        else{
            root = root.next(info.get(0));
            info.remove(0);
            addToTree(info, root, file);
        }
    }

    /**
     * Method that adds a file to the manager
     * @param file File to be added
     * @param pane to add the asset to
     */
    abstract protected void addAsset(File file, Pane pane);

    /**
     * Some variables in the subclasses need to be initialized before this constructor is finished
     * creating everything
     */
    abstract protected void initializeSubClassVariables();

    private void initializeVariables(){
        myExtensions = new HashSet<>();
        myOuterVBox = new VBox();
        myOuterVBox.setPrefHeight(STAGE_HEIGHT);
        myButtonHBox = new HBox();
        myButtonHBox.setMinHeight(50);
        myTabPane = new TabPane();
    }

    private void initializeStage(){
        this.setResizable(false);
        this.setWidth(STAGE_WIDTH);
        this.setHeight(STAGE_HEIGHT);
        Scene scene = new Scene(myOuterVBox);
        scene.getStylesheets().add(DEFAULT_STYLE_SHEET);
        myOuterVBox.getStyleClass().add(ASSET_SPECIFIC_SHEET);
        myButtonHBox.getStyleClass().add(BUTTON_PANE_SHEET);
        this.setScene(scene);
    }

    private void fillExtensionSet() {
        for(String s: RESOURCES.getString(myExtensionKey).split(",")){
            myExtensions.add(s);
        }
    }

    /**
     * Allows the user to browse and add new assets
     * this is protected because the method needs to be
     * accessible in subclasses for reflection
     */
    protected void handleBrowse(){
        Stage stage = new Stage();
        FileChooser chooser = new FileChooser();
        initializeFileExtensions(chooser);
        File selectedFile = chooser.showOpenDialog(stage);
        if(selectedFile != null){
            saveAsset(selectedFile);
            populateTabs();
        }
    }

    private void saveAsset(File selectedFile){
        try {
            GameCenterData gameCenterData = myObjectManager.getGameCenterData();
            String gameName = gameCenterData.getTitle();
            String authorName = gameCenterData.getAuthorName();
            String prefix = SEPARATOR_RESOURCES.getString("userUploaded") + gameName + authorName;
            File dest = new File(myAssetFolderPath + prefix + selectedFile.getName()); //any location
            Files.copy(selectedFile.toPath(), dest.toPath(), StandardCopyOption.REPLACE_EXISTING);
        } catch (Exception e) {
            String[] text = RESOURCES.getString(IO_ERROR).split(",");
            ErrorBox errorBox = new ErrorBox(text[0], text[1]);
            errorBox.display();
        }
    }

    private void initializeFileExtensions(FileChooser chooser) {
        for(String s : myExtensions){
            FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter(s,EXTENSION_PREFIX + s);
            chooser.getExtensionFilters().add(extFilter);
        }
    }

    protected void createAndDisplayAlert(String contentText) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(RESOURCES.getString(ERROR_HEADER));
        alert.setHeaderText(contentText);
        alert.setContentText(null);
        alert.show();
    }

    /**
     * Makes sure the selected asset is null so
     * other classes cannot access the selected
     * because the user closed and didn't apply it
     */
    protected void handleClose(){
        mySelectedAssetName = null;
        this.close();
    }


    /**
     * This method is called by subclasses when someone hits the "Apply" button but
     * no Asset is selected
     */
    protected void handleNoAssetSelected(){
        String[] text = RESOURCES.getString("NOIMAGE").split(",");
        ErrorBox errorBox = new ErrorBox(text[0], text[1]);
        errorBox.display();
    }

    /**
     * should apply whatever needs to happen after an image has been selected
     */
    @SuppressWarnings("unused")
    abstract protected void handleApply();


    /**
     * Gets the Name of the selected asset
     * @return String of the selected asset name
     */
    public String getAssetName(){
        return mySelectedAssetName;
    }

    /**
     * Used by subclasses (Audio/Image manager) to take in a filename that may have the file separator
     * and find the original name of the image purely for display purposes
     * @param fileName name of the file to extract the name from
     * @return String of the image name to be displayed
     */
    protected String extractDisplayName(String fileName){
        String result = fileName;
        if(fileName.contains(SEPARATOR_RESOURCES.getString("defaults"))){
            String[] splitText = fileName.split(SEPARATOR_RESOURCES.getString("defaults"));
            result = splitText[splitText.length-1];
        }
        else if(fileName.contains(mySavingPrefix)){
            result = fileName.replaceAll(mySavingPrefix, "");
        }
        return result;
    }
}
