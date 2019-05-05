package controls;
import data.external.UserDataManager;
import manager.SwitchToUserPage;
import popup.ErrorPopUp;
public class EnterGameButton extends SceneSwitchButton {
    private static final String STYLE = "default_launcher.css";
    private CredentialValidator userNameAccessor;
    private CredentialValidator passWordAccessor;
    private static final String ERROR = "wrong_login";

    /**
     * Handles Validating the user input into the username and password fields in the main login page
     * @author Anna Darwish
     */
    public EnterGameButton(String label, CredentialValidator userName, CredentialValidator passWord, SwitchToUserPage loggedInPage){
        super(label);
        userNameAccessor = userName;
        passWordAccessor = passWord;
        this.getStylesheets().add(STYLE);
        this.setOnMouseReleased(mouseEvent -> {
            if (validateUserCredentials()){
                loggedInPage.switchUserPage(userNameAccessor.currentFieldValue());
            }
            else{
                displayInvalidLogin();
            }

        });
    }
    private boolean validateUserCredentials(){
        String currentUserName = userNameAccessor.currentFieldValue();
        String currentPassWord = passWordAccessor.currentFieldValue();
        UserDataManager dataManager = new UserDataManager();
        return dataManager.validateUser(currentUserName,currentPassWord);
    }

    private void displayInvalidLogin(){
        ErrorPopUp invalidLogin = new ErrorPopUp(ERROR);
        invalidLogin.display();
    }

}
