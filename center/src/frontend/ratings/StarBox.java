package frontend.ratings;

import javafx.geometry.Pos;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;

import java.util.ArrayList;
import java.util.List;

public class StarBox {
    private static final int NUMBER_OF_STARS = 5;

    private List<Star> myStars;
    private BorderPane myDisplay;

    public StarBox() {
        myDisplay = new BorderPane();
        initializeDisplay();
    }

    public Pane getDisplay() {
        return myDisplay;
    }

    private void initializeDisplay() {
        myStars = new ArrayList<>();
        HBox stars = new HBox();
        for(int i = 0; i < NUMBER_OF_STARS; i++) {
            Star currentStar = new Star(i);
            setUpStar(currentStar, stars);
            myStars.add(currentStar);
        }
        stars.setAlignment(Pos.CENTER);
        myDisplay.setCenter(stars);
    }

    private void setUpStar(Star currentStar, HBox stars) {
        currentStar.getImageDisplay().setOnMouseClicked(e-> handleStarClick(currentStar));
        stars.getChildren().add(currentStar.getImageDisplay());
    }

    private void handleStarClick(Star currentStar) {
        HBox newStars = new HBox();
        for(Star star : myStars) {
            if(star.getIndex() <= currentStar.getIndex()) {
                star.setSelected(true);
            } else {
                star.setSelected(false);
            }
            setUpStar(star, newStars);
        }
        newStars.setAlignment(Pos.CENTER);
        myDisplay.setCenter(newStars);
    }

}
