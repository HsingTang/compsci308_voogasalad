package controls;
import javafx.scene.control.PasswordField;
/**
 * This was a textfield that used the display properties of a password field in order to hide what the user was
 * inputting when entering their information for either creating a new account or logging in
 * @author Anna Darwish
 */
public class HiddenField extends PasswordField {
    private String informationRequested;
    private static final String DEFAULT = "";
    private CredentialValidator myField = this::getTextEntered;
    public HiddenField(String information){

        informationRequested = information;
        this.setText(informationRequested);

        this.setOnMouseClicked(mouseEvent -> handleDisplay());
    }
    /**
     * Provides access to values of HiddenField, which hides the display of the String value that the user is typing,
     * in order to validate the user's credentials
     * @author Anna Darwish
     */
    public CredentialValidator accessValue(){
        return myField;
    }
    private void handleDisplay(){
        if (this.getText().equals(informationRequested)){
            this.setText(DEFAULT);
        }
    }
    /**
     * This was used to return the value of the text that the user had input to query the database in regards to whether
     * it matched up with a username in our system
     */
    public String getTextEntered(){
        return (this.getText());
    }

}
