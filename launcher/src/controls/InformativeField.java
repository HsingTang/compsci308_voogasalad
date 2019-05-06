package controls;
import javafx.scene.control.TextField;

public class InformativeField extends TextField {
    private String informationRequested;
    private static final String DEFAULT = "";
    private CredentialValidator myField = this::getTextEntered;
    /**
     * This control was used to provide users with information on what input our gui was expecting to receive from them,
     * and this prompt would disappear when the user enter information
     * @author Anna Darwish
     */
    public InformativeField(String message){
        informationRequested = message;
        this.setText(informationRequested);
        this.setOnMouseClicked(mouseEvent -> handleDisplay());
        this.textProperty().addListener((observableValue, s, t1) -> updateText(t1));
    }
    /**
     * Provides access to values of InformativeField, which displays information concerning what the user should input
     * and displays whatever they have input so far, in order to validate their credentials
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

    private void updateText(String text){
        this.setText(text);
        this.setAccessibleText(text);
    }
    /**
     * This was used to return the value of the text that the user had input
     */
    public String getTextEntered(){
        return this.getText();
    }


}
