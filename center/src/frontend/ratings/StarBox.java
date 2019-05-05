/**
 * @Author Megan Phibbons
 * @Date April 2019
 * @Purpose Display all of the stars to allow the user to pick and choose their rating in a virtual way
 * @Dependencies javafx, Star
 * @Uses: Displayed in the RatingScreen
 */

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
    private int myCurrentNumberOfStars;

    /**
     * @purpose constructor that initializes the display to be shown
     */
    public StarBox() {
        myDisplay = new BorderPane();
        initializeDisplay();
    }

    /**
     * @purpose give the display of the starbox so that it can be shown in other places
     * @return the current display that the StarBox has
     */
    public Pane getDisplay() {
        return myDisplay;
    }

    /**
     * @purpose allow the RatingScreen to see how many stars are selected.
     * @return the number of selected stars
     */
    public int getCurrentNumberOfStars() {
        return myCurrentNumberOfStars;
    }

    /**
     * @purpose set the number of stars based on a number and change their displays accordingly
     * @param index the index of the star to set true (all previous stars also get set to true)
     */
    public void setStars(int index) {
        HBox newStars = new HBox();
        for(Star star : myStars) {
            if(star.getIndex() < index) {
                star.setSelected(true);
            } else {
                star.setSelected(false);
            }
            setUpStar(star, newStars);
        }
        myCurrentNumberOfStars = index;
        newStars.setAlignment(Pos.CENTER);
        myDisplay.setCenter(newStars);
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
        currentStar.getImageDisplay().setOnMouseClicked(e-> setStars(currentStar.getIndex() + 1));
        stars.getChildren().add(currentStar.getImageDisplay());
    }

}
