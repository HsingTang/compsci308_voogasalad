package controls;

import javafx.scene.control.ToggleButton;
/**
 * This control may be used more than once by the user within the same cycle, and since it may be helpful for the particular
 * instances of these buttons to know what their most recent state is, I created a separate control that extends the toggle
 * button, as it offers more defaults for this situation
 * @author Anna Darwish
 */
class SceneSwitchButton extends ToggleButton {
    private static final String STYLE = "default_launcher.css";
    SceneSwitchButton(String label){
        this.setText(label);
        this.getStyleClass().add(STYLE);

    }

}
