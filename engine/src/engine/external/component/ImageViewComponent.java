package engine.external.component;

import javafx.scene.image.ImageView;

/**
 * @author Hsingchih Tang
 * Stores the ImageView for an Entity if it also has a SpriteComponent
 * Not serializable; needs to be created in Engine
 */
public class ImageViewComponent extends Component<ImageView> {

    public ImageViewComponent(ImageView imageView) {
        super(imageView);
    }

}
