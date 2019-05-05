package engine.external.component;

/**
 * @author Hsingchih Tang
 * @author
 * Stores a String indicating the name of an image
 * Used by Engine to retrieve the image file from database and create ImageViews for Entities
 */
public class SpriteComponent extends Component<String> {

    private final static String DEFAULT = "no_image.png";


    public SpriteComponent(String pathname) {
        super(pathname);
    }

    public SpriteComponent() {
        super(DEFAULT);
    }

}
