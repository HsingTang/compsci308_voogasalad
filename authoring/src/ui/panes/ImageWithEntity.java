package ui.panes;

import engine.external.component.HeightComponent;
import engine.external.component.WidthComponent;
import javafx.collections.MapChangeListener;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import ui.AuthoringEntity;
import ui.EntityField;
import ui.ErrorBox;

import java.io.FileInputStream;
import java.lang.reflect.Method;
import java.util.ResourceBundle;

public class ImageWithEntity extends ImageView {
    private AuthoringEntity myAuthoringEntity;
    private static final ResourceBundle myResources = ResourceBundle.getBundle("image_with_entity");
    private FileInputStream myInputStream;

    public ImageWithEntity(FileInputStream s, AuthoringEntity authoringEntity) {
        super(new Image(s, (Double) authoringEntity.getBackingEntity().getComponent(WidthComponent.class).getValue(), (Double) authoringEntity.getBackingEntity().getComponent(HeightComponent.class).getValue(), false, false));
        myInputStream = s;
        myAuthoringEntity = authoringEntity;
        myAuthoringEntity.getPropertyMap().addListener((MapChangeListener<Enum, String>) change -> {handleChange(change);
            System.out.println("Change observed");});
    }

    private void handleChange(MapChangeListener.Change<? extends Enum,? extends String> change) {
        if(change.wasAdded() && myResources.containsKey(change.getKey().toString())){
            System.out.println("Change was added");
            String methodName = myResources.getString(change.getKey().toString());
            try {
                Method method = this.getClass().getDeclaredMethod(methodName, String.class);
                method.invoke(this, change.getValueAdded());
            } catch (Exception e) {
                //TODO: get rid of the stack trace once confirmed working
                e.printStackTrace();
                String header = myResources.getString("ErrorHeader");
                String content = myResources.getString("ErrorContent");
                ErrorBox errorBox = new ErrorBox(header, content);
                errorBox.display();
            }
        }
    }

    private void updateX(String x){
        this.setTranslateX(Double.parseDouble(x));
    }

    private void updateY(String y){
        this.setTranslateY(Double.parseDouble(y));
    }

    private void updateWidth(String width){
        Double widthDouble = Double.parseDouble(width);
        Double heightDouble = Double.parseDouble(myAuthoringEntity.getPropertyMap().get(EntityField.YSCALE));
        System.out.println("Width: " + widthDouble);
        System.out.println("Height: " + heightDouble);
        replaceImage(myInputStream, widthDouble, heightDouble);
        this.setFitWidth(widthDouble);
        System.out.println("updateWidth called");
    }

    private void replaceImage(FileInputStream inputStream, Double width, Double height){
        System.out.println("Replacing image");
        Image image = new Image(myInputStream, width, height, false, false);
        myInputStream = inputStream;
        this.setImage(image);
    }

    private void updateHeight(String height){
        Double heightDouble = Double.parseDouble(height);
        Double widthDouble = Double.parseDouble(myAuthoringEntity.getPropertyMap().get(EntityField.XSCALE));
        replaceImage(myInputStream, widthDouble, heightDouble);
        this.setFitWidth(heightDouble);
    }

    private void updateImage(String image){
        System.out.println("updateImage called");
    }
    /**
     * Returns AuthoringEntity associated with this object
     * @return AuthoringEntity
     */
    public AuthoringEntity getAuthoringEntity(){
        return myAuthoringEntity;
    }

}
