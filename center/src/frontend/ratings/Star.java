/**
 * @Author Megan Phibbons
 * @Date April 2019
 * @Purpose Display a singular star and hold information of whether or not the star is selected
 * @Dependencies javafx
 * @Uses: Five stars are created in the StarBox, this just makes display logic easier to use.
 */

package frontend.ratings;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import java.io.FileInputStream;

public class Star {
    private boolean mySelected;
    private static final String OFF_LOCATION = "center/data/white-star.jpg";
    private static final String ON_LOCATION = "center/data/gold-star.jpg";
    private static final int STAR_WIDTH = 25;

    private int myIndex;
    private ImageView myImageView;

    /**
     * @purpose construct a star given an index in order to be able to use multiple stars in a line
     * @param index where the star falls in line with ohter stars
     */
    public Star(int index) {
        myIndex = index;
        setSelected(false);
    }

    /**
     * @purpose get the display of the star so that it can be shown in the StarBox
     * @return the image currently corresponding to the display.
     */
    public ImageView getImageDisplay() {
        return myImageView;
    }

    /**
     * @purpose set the star to be either selected or unselected to change a rating
     * @param selected the boolean of whether or not the star should be selected
     */
    public void setSelected(boolean selected) {
        mySelected = selected;
        try {
            if(selected) {
                myImageView = new ImageView(new Image(new FileInputStream(ON_LOCATION)));
            } else {
                myImageView = new ImageView(new Image(new FileInputStream(OFF_LOCATION)));
            }
            myImageView.setPreserveRatio(true);
            myImageView.setFitWidth(STAR_WIDTH);
        } catch(Exception e) {
            // do nothing, in this case the user will just not be able to see what they chose.
        }
    }

    public int getIndex() {
        return myIndex;
    }

}
