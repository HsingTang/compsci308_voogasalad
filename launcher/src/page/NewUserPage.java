package page;
import controls.BackButton;
import controls.HiddenField;
import controls.InformativeField;
import controls.TitleLabel;
import data.external.DataManager;
import javafx.scene.control.Button;
import javafx.scene.layout.VBox;
import manager.SwitchToUserOptions;
import manager.SwitchToUserPage;
import popup.ErrorPopUp;

public class NewUserPage extends VBox {
    private static final String MY_STYLE = "new-user-vbox";
    private static final String NEW_USER_KEY = "new_user";

    private static final String USERNAME = "Enter New UserName";
    private static final String PASSWORD = "Enter Password";
    private static final String CREATE = "Create Account";

    private static final String BLANK = "";
    private static final String REENTER_PASSWORD = "Re-enter Password";

    private static final String DIFFERENT_PASSWORD = "mismatch_passwords";
    private static final String INADEQUATE_PASSWORD = "inadequate_passwords";
    private static final String USERNAME_IN_USE = "username_in_use";
    private InformativeField userName = new InformativeField(USERNAME);
    private HiddenField passWord = new HiddenField(PASSWORD);
    private HiddenField reenter = new HiddenField(REENTER_PASSWORD);
    private DataManager myDataManager  = new DataManager();
    /**
     * This page will prompt the user to enter in a new username and password when they are creating a new account
     * It validates the account as being in line with certain policies, such as password length, and also verifies that
     * the username is not already in the database before creating the account
     * @author Anna Darwish
     */
    public NewUserPage(SwitchToUserOptions goBack, SwitchToUserPage enterGame){
        this.getChildren().add(new BackButton(goBack));
        setUpCenterVBox(enterGame);

    }
    private void setUpCenterVBox(SwitchToUserPage enterGame){
        VBox create = new VBox();
        create.getChildren().add(new TitleLabel(NEW_USER_KEY));
        create.getStyleClass().add(MY_STYLE);
        create.getChildren().add(userName);
        create.getChildren().add(passWord);
        create.getChildren().add(reenter);
        Button createAccount = new Button(CREATE);
        create.getChildren().add(createAccount);
        setUpCreateAccount(createAccount,enterGame);
        this.getChildren().add(create);
    }

    private void setUpCreateAccount(Button createAccount, SwitchToUserPage enterGame){
        createAccount.setOnMouseClicked(mouseEvent -> {
            if (validCredentials()){
                enterGame.switchUserPage(userName.getTextEntered());
            }
            else{
                displayNewUserError();
            }
            userName.setText(BLANK);
            passWord.setText(BLANK);
            reenter.setText(BLANK);
        });
    }

    private boolean validCredentials() {
        return equalPassWord() && adequatePassword() && validUserName();

    }

    private boolean equalPassWord(){
        return passWord.getTextEntered().equals(reenter.getTextEntered());
    }

    private boolean adequatePassword(){
        return passWord.getTextEntered().length() >= 8;

    }

    private boolean validUserName(){
        return myDataManager.createUser(userName.getTextEntered(),passWord.getTextEntered());
    }


    private void displayNewUserError(){
        ErrorPopUp differentPasswords;
        if (!equalPassWord()){
            differentPasswords = new ErrorPopUp(DIFFERENT_PASSWORD);
        }
        else if (!adequatePassword()){
            System.out.println(passWord.getTextEntered());
            differentPasswords = new ErrorPopUp(INADEQUATE_PASSWORD);
        }
        else {
            differentPasswords = new ErrorPopUp(USERNAME_IN_USE);
        }
        differentPasswords.display();
    }
}
