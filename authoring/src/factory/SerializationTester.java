package factory;

import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.io.xml.DomDriver;
import data.external.DataManager;
import engine.external.Entity;
import engine.external.component.HealthComponent;
import runner.external.Game;
import data.external.GameCenterData;

import java.io.*;
import java.util.List;


public class SerializationTester {
    private XStream mySerializer;

    public SerializationTester(){
        mySerializer = new XStream(new DomDriver());
    }

    public void saveAndMakeMario(){
        Entity myMario = new Entity();
        String mySavedEnemy = mySerializer.toXML(myMario);
        Entity mySecondMario = (Entity)mySerializer.fromXML(mySavedEnemy);
        mySecondMario.printMyComponents();
    }

    public void saveAndMakeNewGameWithObject() {
        DataManager dm = new DataManager();
        dm.createGameFolder("TestGameName");
        Entity myMario = new Entity();
        dm.createGameFolder("RyanGame");
        dm.saveGameData("RyanGame", myMario);
        //Entity mySecondMario = (Entity)dm.loadGameData("RyanGame");
        //mySecondMario.printMyComponents();
    }

    public void testObjectReferences(){
        DataManager dm = new DataManager();
        Game game = new Game();
        Entity mario = new Entity();
        HealthComponent health = new HealthComponent(20.0D);
        Entity ryan = new Entity();
        dm.createGameFolder("LucasGame");
        dm.saveGameData("LucasGame", game);
    }

    public void testGameCenterInfo() {
        GameCenterData test = new GameCenterData();
        test.setTitle("TESTING GAME!!!!");
        test.setDescription("this is just a simple test");
        DataManager dm = new DataManager();
        dm.createGameFolder("testingGameInfo");
        dm.saveGameInfo("testingGameInfo", test);
    }

    public void testDatabaseConnection(){
//        DatabaseEngine de = new DatabaseEngine();
//        de.open();
//        System.out.println("Before: ==========================================================");
//        de.printGameTable();
//        System.out.println("Create new entry: ==================================================");
//        de.createEntryForNewGame("RyanGame");
//        de.printGameTable();
//        System.out.println("Update Entry: =======================================================");
//        de.updateGameEntryData("RyanGame", "testing");
//        de.printGameTable();
//        de.close();
    }

    public void testSavingImages(){
        DataManager dm = new DataManager();
        dm.saveImage("flappy_bird", new File("runner/resources/flappy_bird.png"));
    }

    public void testLoadingImages() throws IOException {
        DataManager dm = new DataManager();
        InputStream is = dm.loadImage("flower_test1");
        byte[] buffer = new byte[is.available()];
        is.read(buffer);
        File targetFile = new File("C:\\Users\\Owner\\Desktop\\whatever1.png");
        OutputStream outStream = new FileOutputStream(targetFile);
        outStream.write(buffer);
    }

    public void testSavingSounds(){
        DataManager dm = new DataManager();
        dm.saveSound("bach_chaconne", new File("Sounds/Bach Chaconne - Ryan Culhane.mp3"));
    }

    public void testLoadingSounds() throws IOException {
        DataManager dm = new DataManager();
        InputStream is = dm.loadSound("bach_chaconne");
        byte[] buffer = new byte[is.available()];
        is.read(buffer);
        File targetFile = new File("C:\\Users\\Owner\\Desktop\\bc.mp3");
        OutputStream outStream = new FileOutputStream(targetFile);
        outStream.write(buffer);
    }

    public void loadImagesToDatabase() {
        DataManager dm = new DataManager();
        List<String> images = List.of("Images/cat.jpg", "Images/GH OST.png", "Images/mario_block.png", "Images" +
                "/spaceship.png", "Images/wide_spaceship.png", "runner/resources/mushroom.png");
        for (String image: images){
            String[] split = image.split("/");
            dm.saveImage(split[split.length-1], new File(image));
        }
    }
}

