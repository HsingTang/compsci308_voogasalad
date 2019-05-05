package ui.panes;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;

/**
 * @author Carrie Hunner
 * This class creates a small pane consisting of an ImageView
 * and then text acting as a title below it.
 * The text can be no longer than a set length and will be cut short
 * before being displayed to prevent formatting problems.
 */
public class AssetImageSubPane extends GridPane {
    private static final int IMAGE_OFFSET = 10;
    private static final int IMAGE_SIZE = 50;
    private int myImageSize;
    private static final int MAX_NUM_CHARS = 6;
    private static final int SPACING_SCALE = 10;
    private static final String STYLE_SHEET = "asset-manager-labels";
    /**
     * Creates a SubPane that consists of an ImageView and
     * then text acting as a title below it
     * @param title String of the name associated with the image
     * @param image ImageView to be displayed
     */
    public AssetImageSubPane(String title, ImageView image){
        myImageSize = IMAGE_SIZE - IMAGE_OFFSET;
        Label text = new Label(cutText(title));
        text.getStyleClass().add(STYLE_SHEET);

        this.add(image, 0, 0);
        this.add(text, 0, 1);
        formatPaneAndImage(image);
    }

    private String cutText(String input){
        String result;
        if(input.length() >= MAX_NUM_CHARS){
            result = input.substring(0, MAX_NUM_CHARS);
        }
        else{
            result = input;
        }
        return result;
    }

    private void formatPaneAndImage(ImageView image) {
        this.setAlignment(Pos.CENTER);
        this.setVgap(SPACING_SCALE);
        this.setHgap(SPACING_SCALE);
        this.setPadding(new Insets(SPACING_SCALE, SPACING_SCALE, SPACING_SCALE, SPACING_SCALE));
        this.setPrefSize(myImageSize, myImageSize);
        image.setFitWidth(myImageSize);
        image.setFitHeight(myImageSize);
    }
}

