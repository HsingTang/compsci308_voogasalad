package engine.internal.systems;

import data.external.AssetDataManager;
import engine.external.Entity;
import engine.external.component.Component;
import engine.external.component.ImageViewComponent;
import engine.external.Engine;
import engine.external.component.OpacityComponent;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import voogasalad.util.reflection.ReflectionException;


import java.io.InputStream;
import java.util.Collection;
import java.util.HashMap;

/**
 * @author Hsingchih Tang
 * Updates the ImageView visualization and positions for Entities that have a SpriteComponent and PositionComponents
 */
public class ImageViewSystem extends VoogaSystem {

    AssetDataManager myDataManager;
    HashMap<Entity, String> myEntityPastSprite;
    HashMap<String, Image> myImages;

    /**
     * Accepts a reference to the Engine in charge of all Systems in current game, and a Collection of Component classes
     * that this System would require from an Entity in order to interact with its relevant Components
     *
     * @param requiredComponents collection of Component classes required for an Entity to be processed by this System
     * @param engine             the main Engine which initializes all Systems for a game and makes update() calls on each game loop
     */
    public ImageViewSystem(Collection<Class<? extends Component>> requiredComponents, Engine engine) {
        super(requiredComponents, engine);
        myDataManager = new AssetDataManager();
        myEntityPastSprite = new HashMap<>();
        myImages = new HashMap<>();
    }

    @Override
    /**
     * Assign or update the ImageViewComponents for each eligible Entity.
     * Sizes and positions of each Entity's Imageview are updated on every game loop.
     * The system keeps track of the Sprites of Entities and creates a new ImageView if an Entity's Sprite has changed.
     */
    protected void run() throws ReflectionException {
        for (Entity entity : this.getEntities()) {
            generateImageView(entity);
        }
    }

    /**
     * Generate or adjust the height/width/positions/opacity of an Entity's ImageView
     * Only access database to retrieve new image if the Sprite String has changed since the last game loop
     * Cache all seen images with corresponding Sprite Strings in the System to reduce database accesses
     * Set an Entity as not collidable if its image cannot be found from database
     *
     * @param entity the entity for which to manage ImageView
     */
    private void generateImageView(Entity entity) {
        ImageView imageView;
        String imageName = (String) getComponentValue(SPRITE_COMPONENT_CLASS, entity);
        if (!myEntityPastSprite.containsKey(entity) || !myEntityPastSprite.get(entity).equals(imageName)) {
//            System.out.println("generating ImageView for "+imageName);
            myEntityPastSprite.put(entity, imageName);
            retrieveImage(imageName);
            if (myImages.get(imageName) == null) {
                System.out.println("Image file " + imageName + " not found in database.");
                entity.removeComponent(COLLISION_COMPONENT_CLASS);
                return;
            }
            imageView = new ImageView(myImages.get(imageName));
        } else {
            if (myImages.get(imageName) == null) {
                return;
            }
            imageView = (ImageView) getComponentValue(IMAGEVIEW_COMPONENT_CLASS, entity);
        }
        entity.addComponent(new ImageViewComponent(setImageViewOpacity(setImgViewHeight(setImgViewWidth(setImgViewY(setImgViewX(imageView, entity), entity), entity), entity), entity)));
    }


    private void retrieveImage(String imageName) {
        if (!myImages.containsKey(imageName)) {
            InputStream imageStream = myDataManager.loadImage(imageName);
            if (imageStream == null) {
                myImages.put(imageName, null);
            } else {
                myImages.put(imageName, new Image(imageStream));
            }
            return;
        }
    }

    private ImageView setImgViewX(ImageView m, Entity e) {
        m.setX((Double) getComponentValue(X_POSITION_COMPONENT_CLASS, e));
        return m;
    }

    private ImageView setImgViewY(ImageView m, Entity e) {
        m.setY((Double) getComponentValue(Y_POSITION_COMPONENT_CLASS, e));
        return m;
    }

    private ImageView setImgViewWidth(ImageView m, Entity e) {
        m.setFitWidth((Double) getComponentValue(WIDTH_COMPONENT_CLASS, e));
        return m;
    }

    private ImageView setImgViewHeight(ImageView m, Entity e) {
        m.setFitHeight((Double) getComponentValue(HEIGHT_COMPONENT_CLASS, e));
        return m;
    }

    private ImageView setImageViewOpacity(ImageView m, Entity e) {
        double opacity;
        try {
            opacity = (Double) getComponentValue(OPACITY_COMPONENT_CLASS, e);
        } catch (NullPointerException e1) {
            e.addComponent(new OpacityComponent(1.0));
            opacity = (Double) getComponentValue(OPACITY_COMPONENT_CLASS, e);

        }
        m.setOpacity(opacity);
        return m;
    }

}
