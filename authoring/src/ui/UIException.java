package ui;

/**
 * Exception type to be thrown throughout the ByteMe Authoring Environment UI
 * @author Harry Ross
 * @author Anna Darwish
 */
public class UIException extends Exception {

    private String errorMessage;
    private static final String EVENT_ERROR = "Event";

    /**
     * Instantiates a new UIException with specified message
     * @param message Error message to be provided alongside exception
     */
    public UIException(String message) {
        super(message);
        errorMessage = message;
    }

    public void displayUIException(){
        ErrorBox myError = new ErrorBox(EVENT_ERROR, errorMessage);
        myError.display();
    }

}
