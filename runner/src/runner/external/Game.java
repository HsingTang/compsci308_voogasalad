package runner.external;

import engine.external.Level;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class Game {
    private List<Level> myLevels;
    private String myIconName;
    private String myGameName;
    private String myGameDescription;
    private int myWidth;
    private int myHeight;

    public Game(){
        myLevels = new ArrayList<Level>();

        //Temporary defaults
        myHeight = 500;
        myWidth = 500;
    }

    public void addLevel(Level level){
        myLevels.add(level);
    }

    public List<Level> getLevels(){
        return myLevels;
    }

    public int getWidth(){
        return myWidth;
    }

    public int getHeight(){
        return myHeight;
    }
}
