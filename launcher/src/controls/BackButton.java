package controls;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import manager.SwitchToUserOptions;
/**
 * This control helps to manage switching between different pages, as many different pages in a login environment
 * have different back pages that they may need to navigate to if they wish to enter a different part of our game and
 * authoring environment
 * @author Anna Darwish
 */
public class BackButton extends ImageView {
    private static final String IMAGE = "back_button.png";
    private static final String STYLE = "back-button";
    public BackButton(SwitchToUserOptions backPage){
        this.setImage(new Image(IMAGE));
        this.getStyleClass().add(STYLE);
        this.setOnMouseClicked(mouseEvent -> backPage.switchPage());
    }

}
