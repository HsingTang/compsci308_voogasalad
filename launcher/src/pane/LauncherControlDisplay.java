package pane;

import controls.LauncherSymbol;
import controls.PaneLabel;
import javafx.geometry.Pos;
import javafx.scene.layout.VBox;
/**
 * Many of the controls in this launcher environment combine an @LauncherSymbol and @PaneLabel to navigate between
 * scenes, so this control helps to standardize this display between pages
 * @author Anna Darwish
 */
class LauncherControlDisplay extends VBox {
    private static final String STYLE = "default_launcher.css";
    private static final double SPACING = 50;
    LauncherControlDisplay(String displayType){
        this.getStylesheets().add(STYLE);
        this.setSpacing(SPACING);
        this.setAlignment(Pos.CENTER);
        this.getChildren().add(0, new PaneLabel(displayType));
        this.getChildren().add(1, new LauncherSymbol(displayType));
    }

}
