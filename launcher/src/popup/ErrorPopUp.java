package popup;

import javafx.scene.control.Alert;
import java.util.ResourceBundle;
/**
 * To have a uniform way for informing the user of an error, any page that may require displaying an error box passes its
 * key to this class, which finds the associated message with that error in a resources file
 * @author Anna Darwish
 */
public class ErrorPopUp extends Alert {
    private static final String ERROR = "Error";
    private static final String STYLE = "default_launcher.css";
    private static final ResourceBundle ERROR_RESOURCE = ResourceBundle.getBundle("launcher_error");
    private static final String DELIMITER = "::";

    public ErrorPopUp(String key){
        super(AlertType.ERROR);
        String[] errorInformation = ERROR_RESOURCE.getString(key).split(DELIMITER);
        this.setTitle(ERROR);
        this.setHeaderText(errorInformation[0]);
        this.setContentText(errorInformation[1]);
        this.getDialogPane().getStylesheets().add(STYLE);
    }

    /**
     * Displays ErrorBox until closed by user
     */
    public void display() {
        this.showAndWait();
    }

}
