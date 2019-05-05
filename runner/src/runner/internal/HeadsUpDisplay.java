package runner.internal;

import javafx.scene.Node;
import javafx.scene.text.Text;

/**
 * Heads up Display dynamically displays and updates stats as level progresses
 * @author Louis Jensen
 */
public class HeadsUpDisplay extends Node {
    private Double myLives;
    private Double myScore;
    private Double myLevel;
    private Text myLabel;
    private double xPosition;
    private final double DEFAULT_Y = 30.0;
    private final double DEFAULT_LIVES = 3.0;
    private final double DEFAULT_LEVEL = 1.0;
    private final double DEFAULT_SCORE = 0.0;
    private final double DEFAULT_X_POSITION = 20.0;
    private final String LOGO = "ByteMe   ";
    private final String LEVEL_DISPLAY = "   Level: ";
    private final String SCORE_DISPLAY = "   Score: ";
    private final String LIVES_DISPLAY = "   Lives: ";
    private final String HUD_ID = "HeadsUpDisplay";

    /**
     * Constructor for HeadsUpDisplay
     * @param width - width of screen
     */
    public HeadsUpDisplay(int width){
        myLives = DEFAULT_LIVES;
        myLevel = DEFAULT_LEVEL;
        myScore = DEFAULT_SCORE;
        xPosition = DEFAULT_X_POSITION;
        Text text = new Text (LOGO +
                LEVEL_DISPLAY + myLevel.intValue() +
                SCORE_DISPLAY + myScore.intValue() +
                LIVES_DISPLAY +myLives.intValue());
        text.setId(HUD_ID);
        myLabel = text;
        myLabel.setLayoutX(xPosition);
        myLabel.setLayoutY(DEFAULT_Y);
    }

    /**
     * Updates the HUD to new values
     * Called in game loop
     */
    public void updateLabel(){
        myLabel.setText(LOGO +
                LEVEL_DISPLAY + myLevel.intValue() +
                SCORE_DISPLAY + myScore.intValue() +
                LIVES_DISPLAY + myLives.intValue());
    }

    /**
     * Gets the X position of the HUD so it can stay constant on the screen
     * @return Double x position
     */
    public Double getX(){
        return xPosition;
    }

    /**
     * Gets the info to be added to screen
     * @return Text object with correct information
     */
    public Text getLabel(){
        return myLabel;
    }

    /**
     * Updates the value of Lives
     * @param lives - correct number of lives
     */
    public void updateLives(Double lives){
        myLives = lives;
    }

    /**
     * Updates the value of level
     * @param level - correct level number
     */
    public void updateLevel(Double level){
        myLevel = level;
    }

    /**
     * Updates the value of score
     * @param score - correct score
     */
    public void updateScore(Double score){
        myScore = score;
    }

    public Double getLevel() {
        return myLevel;
    }
}