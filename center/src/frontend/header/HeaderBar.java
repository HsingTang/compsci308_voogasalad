package frontend.header;

import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import frontend.Utilities;

import java.util.ResourceBundle;

public class HeaderBar {
    private Pane myHeaderLayout;
    private static final String TITLE_FONT = "Arial";
    private static final int TITLE_FONT_SIZE = 50;
    private static final Paint FONT_COLOR = Color.WHITE;
    private ResourceBundle myLanguageBundle;

    public HeaderBar() {
        myLanguageBundle = ResourceBundle.getBundle("languages/English");
        myHeaderLayout = new BorderPane();
        initializeLayouts();
    }

    public Pane getHeaderLayout() {
        return myHeaderLayout;
    }

    private void initializeLayouts() {
        Text title = new Text(Utilities.getValue(myLanguageBundle, "titleText"));
        title.setFont(Font.font(TITLE_FONT, TITLE_FONT_SIZE));
        title.setFill(FONT_COLOR);
        BorderPane.setAlignment(title, Pos.TOP_CENTER);
        StackPane headerLayout = new StackPane();
        BorderPane titleLayout = new BorderPane();
        titleLayout.setCenter(title);
        headerLayout.getChildren().addAll(titleLayout);
        myHeaderLayout = headerLayout;
    }

}