package manager;

import center.external.CenterView;
import data.external.GameCenterData;
import javafx.stage.Stage;
import page.CreateNewGamePage;
import page.WelcomeUserPage;
import runner.external.Game;
import ui.main.MainGUI;
    class UserManager{
    private CreateNewGamePage myNewGamePage;
    private String myUserName;
    UserManager(String userName){
        myUserName = userName;
    }
    void render(Stage myStage){
        makePages();

    }

    private void makePages(){

    }
//    private void goToAuthoring(GameCenterData myData){
//        MainGUI myGUI = new MainGUI(new Game(), myData);
//        myGUI.launch();
//    }
//
//    private void goToGameCenter(){
//        CenterView myCenter = new CenterView();
//        myStage.setScene(myCenter.getScene());
//    }
}