package ui.panes;

import javafx.collections.MapChangeListener;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import ui.AuthoringEntity;
import ui.EntityField;
import ui.Utility;

import java.io.FileInputStream;
import java.util.ResourceBundle;

/**
 * @author Carrie Hunner
 * This class extends ImageView and holds an AuthoringEntity associated with it
 * This is the object that is displayed on the Viewer and allows for the properties
 * necessary to travel with the object
 */
public class ImageWithEntity extends ImageView {
    private AuthoringEntity myAuthoringEntity;
    private static final ResourceBundle myResources = ResourceBundle.getBundle("image_with_entity");
    private static final ResourceBundle myGeneralResources = ResourceBundle.getBundle("authoring_general");

    /**
     * Creates an instance of ImageWithEntity with the FileInputStream and the AuthoringEntity provided
     * @param s takes in a fileInputStream to create and set an image
     * @param authoringEntity AuthoringEntity that is associated with this image
     */
    public ImageWithEntity(FileInputStream s, AuthoringEntity authoringEntity) {
        super(new Image(s, Double.parseDouble(authoringEntity.getPropertyMap().get(EntityField.XSCALE)), Double.parseDouble(authoringEntity.getPropertyMap().get(EntityField.YSCALE)), false, false));
        myAuthoringEntity = authoringEntity;
        updateX(authoringEntity.getPropertyMap().get(EntityField.X));
        updateY(authoringEntity.getPropertyMap().get(EntityField.Y));
        updateVisibility(authoringEntity.getPropertyMap().get(EntityField.VISIBLE));
        myAuthoringEntity.getPropertyMap().addListener((MapChangeListener<Enum, String>) change -> {handleChange(change);
        });
        Utility.closeInputStream(s);
    }

    //takes in a change and determines which method to call
    private void handleChange(MapChangeListener.Change<? extends Enum,? extends String> change) {
        if(change.wasAdded() && myResources.containsKey(change.getKey().toString())){
            Utility.makeAndCallMethod(myResources, change, this);
        }
    }

    private void updateX(String x){
        this.setTranslateX(Double.parseDouble(x));
    }

    private void updateY(String y){
        this.setTranslateY(Double.parseDouble(y));
    }

    @SuppressWarnings("unused")
    private void updateWidth(String width){
        updateImage(myAuthoringEntity.getPropertyMap().get(EntityField.IMAGE));
        Double widthDouble = Double.parseDouble(width);
        Double heightDouble = Double.parseDouble(myAuthoringEntity.getPropertyMap().get(EntityField.YSCALE));
        this.setFitWidth(widthDouble);
        this.setFitHeight(heightDouble);
    }

    private void updateImage(String imageName){
        FileInputStream inputStream = Utility.makeImageAssetInputStream(imageName); //closed 2
        Double width = Double.parseDouble(myAuthoringEntity.getPropertyMap().get(EntityField.XSCALE));
        Double height = Double.parseDouble(myAuthoringEntity.getPropertyMap().get(EntityField.YSCALE));
        Image image = new Image(inputStream, width, height, false, false);
        Utility.closeInputStream(inputStream);  //closed 2
        this.setImage(image);
        this.setFitHeight(height);
        this.setFitWidth(width);
    }

    @SuppressWarnings("unused")
    private void updateHeight(String height){
        updateImage(myAuthoringEntity.getPropertyMap().get(EntityField.IMAGE));
        Double heightDouble = Double.parseDouble(height);
        Double widthDouble = Double.parseDouble(myAuthoringEntity.getPropertyMap().get(EntityField.XSCALE));
        this.setFitHeight(heightDouble);
        this.setFitWidth(widthDouble);
    }

    private void updateVisibility(String visible){
        boolean isVisible = Boolean.parseBoolean(visible);
        if(isVisible){
            this.opacityProperty().setValue(1);
        }
        else{
            this.opacityProperty().setValue(.5);
        }
    }

    /**
     * Returns AuthoringEntity associated with this object
     * @return AuthoringEntity
     */
    public AuthoringEntity getAuthoringEntity(){
        return myAuthoringEntity;
    }

}
