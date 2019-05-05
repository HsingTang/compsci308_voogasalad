package controls;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import manager.SwitchToUserOptions;

public class LogOutButton extends ImageView {
    private static final String IMAGE = "logout.jpg";
    private static final String STYLE = "log-out";
    private static final int OFFSET = 150;
    /**
     * Although there are other controls that offer a similar feature of moving to a particular page, the styling for
     * the logout button was a bit more difficult to handle since it wasn't often part of the other components, so instead of
     * having to deal with repeating this styling, I placed it into its own class
     * @author Anna Darwish
     */
    public LogOutButton(SwitchToUserOptions logOut){
        this.setImage(new Image(IMAGE));
        this.getStyleClass().add(STYLE);
        this.setLayoutX(0);
        this.setTranslateY(OFFSET);
        this.setOnMouseClicked(mouseEvent -> logOut.switchPage());
    }

}