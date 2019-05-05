package data.internal;

import data.external.DataManager;
import data.external.DatabaseEngine;
import data.external.GameRating;

import java.io.File;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;

/**
 * Utility class for quickly loading information into the database
 */
public class DatabaseLoader {

    private static final List<String> USER_NAMES = List.of("Megan", "ICouldAlwaysEat", "gamez_n_gainz", "DimaFayyad",
            "MeganPHibbones", "fzero", "carrie", "harry", "hsing", "louis", "michaelzhang", "ryryculhane", "Megan");
    private static final List<String> GAMES = List.of("2F2R", "game1", "holdQlol", "platformtest", "yeetee");
    private static final List<String> COMMENTS = List.of("Great!", "Awesome", "Mediocre", "This game sucks", "I would" +
            " rather take 308 again than play this game");
    private static final List<String> AUTHORS = List.of("ICouldAlwaysEat", "DimaFayyad", "fzero", "harry");
    private static final Map<String, String> gameToAuthor = Map.of(
            "ICouldAlwaysEat", "2F2R",
            "DimaFayyad", "game1",
            "fzero", "holdQlol",
            "harry", "platformtest"
    );

    public static void main(String[] args) {
        DatabaseEngine.getInstance().open();
        setBio("ICouldAlwaysEat", "You can just press control+alt+any random letter and IntelliJ will make something " +
                "good happen to your code");
        setBio("gamez_n_gainz", "Mind yo modules");
        setBio("DimaFayyad", "Relatable content");
        setBio("MeganPHibbones", "Hey! My name is Megan, follow me on LinkedIn!");
        setBio("fzero", "I'm just trying to be a Prime Citizen");
        setBio("carrie", "Hey Megan *dramatic pause* want to play smash?");
        setBio("harry", "If you need help with CSS, my office hours are 4-6...am");
        setBio("hsing", "If you want to get an A take 270 but if you are actually trying to learn something take a different class");
        setBio("louis", "Hey! My name is Megan, follow me on LinkedIn!");
        setBio("michaelzhang", "It's time");
        setBio("ryryculhane", "Hey! My name is Megan, follow me on LinkedIn!");
        setBio("Megan", "Hey! My name is Megan, follow me on LinkedIn!");

        DatabaseEngine.getInstance().close();
    }

    private static void createUser(String userName, String password) {
        DataManager dm = new DataManager();
        System.out.println(dm.createUser(userName, password));
    }

    private static void setBio(String userName, String bio) {
        DataManager dm = new DataManager();
        try {
            dm.setBio(userName, bio);
        } catch (SQLException e) {
            // do nothing, let user try again later, decided by team
        }
    }

    private static void setProfPic(String userName, String path) {
        DataManager dm = new DataManager();
        try {
            dm.setProfilePic(userName, new File(path));
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    private static void createUsers(){
        createUser("ICouldAlwaysEat", "OrNotEat");
        createUser("gamez_n_gainz", "bruh");
        createUser("DimaFayyad", "cs725)@5wt3");
        createUser("MeganPHibbones", "welcome2thaCenter");
        createUser("fzero", "coachesdontplay");
        createUser("carrie", "morelikecarrietheteam");
        createUser("harry", "morelikeharrytheteam");
        createUser("hsing", "letmejustmakeapasswordsystem");
        createUser("louis", "rowingandgrowing");
        createUser("michaelzhang", "RIP");
        createUser("ryryculhane", "noJDBCdriver");
        createUser("Megan", "password");
    }

    private static void validateUsers(){
        DataManager dm = new DataManager();
        System.out.println(dm.validateUser("Ryan", "testPassword"));
        System.out.println(dm.validateUser("Ryan", "wrongPassword"));
        System.out.println(dm.validateUser("FakePerson", "testPassword"));
        System.out.println(dm.validateUser("FakePerson", "wrongPassword"));
    }

    private static void loadImage() {
        DataManager dm = new DataManager();
        dm.saveImage("byteme_default_runnerBackground", new File("Images/byteme_default_runnerBackground.png"));
    }

    private static void loadGameCenterImages() {
        DataManager dm = new DataManager();
        List<String> imagesToLoad = List.of("celeste.jpg", "default_game.png", "downwell.jpg", "fez.jpg", "inside" +
                ".jpg", "limbo.jpg", "mario.jpg", "ori.jpg", "smb.jpg", "spelunky.png", "yooka.jpg");
        for (String image : imagesToLoad) {
            File imageFile = new File("center/data/game_information/images/" + image);
            dm.saveImage("center/data/game_information/images/" + image, imageFile);
        }
    }

    private static void loadGameCenterDataFromCreatedGames() {
        DataManager dm = new DataManager();
        List<String> gamesToLoad = List.of("celeste", "downwell", "fez", "inside", "limbo", "mario", "ori", "spelunky",
                "supermeatboy", "yooka");
        for (String game : gamesToLoad) {
            dm.saveGameDataFromFolder(game);
        }
    }

    private static void loadScores() {
        DataManager dm = new DataManager();
        for (int i = 0; i < 100; i++) {
            int author = (int) (Math.random() * AUTHORS.size());
            int comment = (int) (Math.random() * COMMENTS.size());
            int user = (int) (Math.random() * USER_NAMES.size());
            String authorName = AUTHORS.get(author);
            dm.saveScore(USER_NAMES.get(user), gameToAuthor.get(authorName), authorName, Math.random() * 1000);
        }

    }

    private static void loadRatings() throws SQLException {
        DataManager dm = new DataManager();
        for (int i = 0; i<100; i++) {
            int author = (int) (Math.random() * AUTHORS.size());
            int comment = (int) (Math.random() * COMMENTS.size());
            int user = (int) (Math.random() * USER_NAMES.size());
            String authorName = AUTHORS.get(author);
            dm.addRating(new GameRating(USER_NAMES.get(user), gameToAuthor.get(authorName), authorName,
                    1 + (int)  (Math.random() * 5), COMMENTS.get(comment)));
        }
    }
}
