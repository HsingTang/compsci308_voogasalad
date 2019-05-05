package factory;

import engine.external.Entity;
import engine.external.IEventEngine;
import engine.external.Level;
import engine.external.component.AssociatedEntityComponent;
import engine.external.component.CameraComponent;
import engine.external.component.CollisionComponent;
import engine.external.component.Component;
import engine.external.component.LivesComponent;
import engine.external.component.NameComponent;
import engine.external.component.OpacityComponent;
import engine.external.component.ScoreComponent;
import engine.external.conditions.Condition;
import engine.external.conditions.StringEqualToCondition;
import engine.external.events.CollisionEvent;
import engine.external.events.Event;
import javafx.beans.property.ObjectProperty;
import runner.external.Game;
import ui.AuthoringEntity;
import ui.AuthoringLevel;
import ui.EntityField;
import ui.LevelField;
import ui.Propertable;
import ui.UIException;
import ui.manager.ObjectManager;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Class that translates Authoring versions of objects into classes usable by the rest of the project
 * @author Harry Ross
 */
public class GameTranslator {

    private ObjectManager myObjectManager;

    /**
     * Creates a new GameTranslator with the given ObjectManager
     * @param objectManager ObjectManager for authoring environment user is saving/loading to
     */
    public GameTranslator(ObjectManager objectManager) {
        myObjectManager = objectManager;
    }

    /**
     * Translates current environment state into Game object
     * @return Game object representing translated state from authoring environment
     * @throws UIException if errors occur while saving
     */
    public Game saveGame() throws UIException {
        Game translatedGame = new Game();
        Map<Entity, String> typeMap = new HashMap<>();

        for (AuthoringLevel authLevel : myObjectManager.getLevels()) { // Translate Levels
            translatedGame.addLevel(saveLevel(authLevel));
        }
        for (AuthoringEntity key : myObjectManager.getTypeMap().keySet()) { // Translate Entity type map
            Entity translatedType = saveEntity(key);
            typeMap.put(translatedType, myObjectManager.getTypeMap().get(key));
        }
        translatedGame.addUserCreatedTypes(typeMap);
        return translatedGame;
    }

    private Level saveLevel(AuthoringLevel authLevel) throws UIException {
        Level newLevel = new Level();
        if (authLevel.getEntities().isEmpty()) {
            throw new UIException("All levels must have at least one Entity");
        }
        newLevel.setBackground(authLevel.getPropertyMap().get(LevelField.BACKGROUND)); // Level properties!!!
        newLevel.setLabel(authLevel.getPropertyMap().get(LevelField.LABEL));
        newLevel.setMusic(authLevel.getPropertyMap().get(LevelField.MUSIC));
        newLevel.setWidth(Double.parseDouble(authLevel.getPropertyMap().get(LevelField.WIDTH)));
        newLevel.setHeight(Double.parseDouble(authLevel.getPropertyMap().get(LevelField.HEIGHT)));

        saveLevelEntities(authLevel, newLevel);
        checkRequiredComponents(newLevel);
        return newLevel;
    }

    private void saveLevelEntities(AuthoringLevel authLevel, Level newLevel) throws UIException {
        for (AuthoringEntity authEntity : authLevel.getEntities()) { // Level entities!!!
            newLevel.addEntity(saveEntity(authEntity));
            for (Event event : myObjectManager.getEvents(authEntity.getPropertyMap().get(EntityField.LABEL))) // Events!!!
                newLevel.addEvent(event);
        }
        for (Entity entity : newLevel.getEntities()) { // Additional Components based on Events!!!
            saveAdditionalComponents(entity, newLevel);
        }
    }

    private void checkRequiredComponents(Level newLevel) throws UIException {
        boolean mainCharExists = false;
        for (Entity entity : newLevel.getEntities()) { // Make sure a main character exists in translated level's Entity set
            if (entity.hasComponents(CameraComponent.class)) {
                mainCharExists = true;
                break;
            }
        }
        if (!mainCharExists) {
            throw new UIException("One Entity per Level must be selected as Focus");
        }
    }

    private Entity saveEntity(AuthoringEntity authEntity) throws UIException {
        Entity basisEntity = new Entity();

        for (EntityField field : EntityField.values()) {
            if (authEntity.getPropertyMap().containsKey(field) && !field.equals(EntityField.VISIBLE) && !field.equals(EntityField.FOCUS) && !field.equals(EntityField.EVENTS)) {
                saveComponent(field, basisEntity, authEntity);
            }
            else if (field.equals(EntityField.FOCUS) && Boolean.parseBoolean(authEntity.getPropertyMap().get(EntityField.FOCUS))) { // Main character found
                basisEntity.addComponent(new CameraComponent(true));
                basisEntity.addComponent(new LivesComponent(3.0));
                basisEntity.addComponent(new ScoreComponent(0.0));
                basisEntity.addComponent(new AssociatedEntityComponent(basisEntity));
            }
            else if (field.equals(EntityField.VISIBLE)) {
                if (Boolean.parseBoolean(authEntity.getPropertyMap().get(EntityField.VISIBLE)))
                    basisEntity.addComponent(new OpacityComponent(1.0));
                else
                    basisEntity.addComponent(new OpacityComponent(0.0));
            }
        }
        return basisEntity;
    }

    private void saveComponent(EntityField field, Entity basisEntity, AuthoringEntity authEntity) throws UIException {
        Class<?> dataType = field.getComponentDataType();
        Class<? extends Component> componentClass = field.getComponentClass();

        if (authEntity.getPropertyMap().containsKey(field)) {
            String newValue = authEntity.getPropertyMap().get(field);

            if (basisEntity.hasComponents(componentClass)) {
                if (dataType.equals(String.class))
                    ((Component) basisEntity.getComponent(componentClass)).setValue(newValue);
                else if (dataType.equals(Double.class))
                    ((Component) basisEntity.getComponent(componentClass)).setValue(Double.parseDouble(newValue));
                else if (dataType.equals(Boolean.class))
                    ((Component) basisEntity.getComponent(componentClass)).setValue(Boolean.parseBoolean(newValue));
            } else {
                try {
                    if (dataType.equals(String.class))
                        basisEntity.addComponent(componentClass.getConstructor(dataType).newInstance(newValue));
                    else if (dataType.equals(Double.class))
                        basisEntity.addComponent(componentClass.getConstructor(dataType).newInstance(Double.parseDouble(newValue)));
                    else if (dataType.equals(Boolean.class))
                        basisEntity.addComponent(componentClass.getConstructor(dataType).newInstance(Boolean.parseBoolean(newValue)));
                } catch (Exception e) {
                    throw new UIException("Error translating components");
                }
            }
        }
    }

    private void saveAdditionalComponents(Entity basisEntity, Level newLevel) {
        //TODO group events, group collision events?
        for (Event event : myObjectManager.getEvents((String) basisEntity.getComponent(NameComponent.class).getValue())) { // Cycle through each Entity for its Events in OM
            if (CollisionEvent.class.isAssignableFrom(event.getClass())) {
                basisEntity.addComponent(new CollisionComponent(true)); // Add CollisionComponent to the Entities that need it
                String otherEntityLabel = ((CollisionEvent) event).getCollisionWithEntity();
                for (Entity entity : newLevel.getEntities()) {                  // Add CollisionComponent to other actor
                    if (entity.getComponent(NameComponent.class).getValue().equals(otherEntityLabel) && !entity.hasComponents(CollisionComponent.class))
                        entity.addComponent(new CollisionComponent(true));
                }
            }
        }
    }

    /**
     * Loads Game object and translates into data usable by authoring environment
     * @param game Game being loaded
     * @param currentLevel Object property to set level focus properly
     * @throws UIException if error occurs during load
     */
    public void loadGame(Game game, ObjectProperty<Propertable> currentLevel) throws UIException {
        for (Entity type : game.getUserCreatedTypes().keySet()) {
            AuthoringEntity newType = new AuthoringEntity(type, myObjectManager);
            myObjectManager.addEntityType(newType, game.getUserCreatedTypes().get(type));
        }
        for (Level level : game.getLevels()) {
            AuthoringLevel newLevel = new AuthoringLevel(level.getLabel(), myObjectManager);
            newLevel.getPropertyMap().put(LevelField.HEIGHT, String.valueOf(level.getHeight()));
            newLevel.getPropertyMap().put(LevelField.WIDTH, String.valueOf(level.getWidth()));
            newLevel.getPropertyMap().put(LevelField.BACKGROUND, level.getBackground());
            newLevel.getPropertyMap().put(LevelField.MUSIC, level.getMusic());
            myObjectManager.addLevel(newLevel);
            currentLevel.setValue(newLevel);

            loadEntities(level);
            loadEvents(level);
        }
    }

    private void loadEntities(Level level) {
        for (Entity entity : level.getEntities()) {
            AuthoringEntity newAuthEntity = new AuthoringEntity(entity, myObjectManager);
            myObjectManager.addEntityInstance(newAuthEntity);
            if (newAuthEntity.getPropertyMap().get(EntityField.GROUP) != null) { // Add Group label as found in Entities
                myObjectManager.getLabelManager().addLabel(EntityField.GROUP,
                        newAuthEntity.getPropertyMap().get(EntityField.GROUP));
            }
        }
    }

    private void loadEvents(Level level) throws UIException {
        for (IEventEngine event : level.getEvents()) {
            boolean noName = true;
            for(Condition c : (List<Condition>) ((Event) event).getEventInformation().get(Condition.class)) {
                if (c.getClass().equals(StringEqualToCondition.class) && ((StringEqualToCondition) c).getComponentClass().equals(NameComponent.class)) {
                    String entityType = ((StringEqualToCondition) c).getValue(); // Guaranteed to be associated with NameComponent
                    if (!myObjectManager.getEvents(entityType).contains(event))
                        myObjectManager.getEvents(entityType).add((Event) event);
                    noName = false;
                    break;
                }
            }
            if (noName) {
                throw new UIException("No Entity type associated with one or more Event(s)");
            }
        }
    }

}
