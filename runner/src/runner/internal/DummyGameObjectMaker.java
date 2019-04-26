package runner.internal;

import data.external.DataManager;
import engine.external.Entity;
import engine.external.Level;
import engine.external.actions.*;
import engine.external.component.*;
import engine.external.conditions.*;
import engine.external.events.*;


import engine.external.conditions.CollisionCondition;
import engine.external.events.Event;
import engine.external.events.RightCollisionEvent;
import engine.external.events.TopCollisionEvent;

import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;

import engine.external.conditions.LessThanCondition;
import runner.external.Game;
import javafx.stage.Stage;

import java.util.HashMap;


public class DummyGameObjectMaker {
    private Game myGame;

    public DummyGameObjectMaker(){
        myGame = new Game();
        initializeGame(myGame);
    }

    private void initializeGame(Game dummyGame) {
        Level level1 = new Level();
        addDummyEntities(level1, 1.0);
        addDummyEvents(level1, 2.0);
        dummyGame.addLevel(level1);

        Level level2 = new Level();
        addDummyEntities(level2, 2.0);
        addDummyEvents(level2, 3.0);
        dummyGame.addLevel(level2);

        Level level3 = new Level();
        addDummyEntities(level3, 3.0);
        addDummyEvents(level3, 4.0);
        dummyGame.addLevel(level3);

        Level level4 = new Level();
        addDummyEntities(level4, 4.0);
        addDummyEvents(level4, 5.0);
        dummyGame.addLevel(level4);

    }

    private void addDummyEvents(Level level1, Double next) {


        Event event = new Event("one");
        event.addInputs(KeyCode.RIGHT);
        event.addActions(new XPositionAction(NumericAction.ModifyType.RELATIVE, 5.0));

        Event event2 = new Event("one");
        event2.addInputs(KeyCode.S);
        event2.addActions(new XVelocityAction(NumericAction.ModifyType.ABSOLUTE, 2.0));
        RightCollisionEvent oneByTwo = new RightCollisionEvent("one", "two");
        //oneByTwo.addActions(new XPositionAction(NumericAction.ModifyType.RELATIVE, -10.0));
        oneByTwo.addActions(new XVelocityAction(NumericAction.ModifyType.ABSOLUTE, 0.0));
//        oneByTwo.addActions(new HeightAction(NumericAction.ModifyType.SCALE, 2.0));
        LeftCollisionEvent twoByOne = new LeftCollisionEvent("two", "one");
        twoByOne.addActions(new XVelocityAction(NumericAction.ModifyType.SCALE, -1.0));

        Event flappyMoveLeft = new Event("one");
        flappyMoveLeft.addInputs(KeyCode.LEFT);
        flappyMoveLeft.addActions(new XPositionAction(NumericAction.ModifyType.RELATIVE, -5.0));
        //event.addConditions(new GreaterThanCondition(YPositionComponent.class, -50.0));

        //add up down keycodes
//        Event flappyMoveUp = new Event("one");
//        flappyMoveUp.addInputs(KeyCode.UP);
//        flappyMoveUp.addActions(new YPositionAction(NumericAction.ModifyType.RELATIVE, -1.0));

        // let the Entity jump
        Event mushroomJump = new Event("two");
        mushroomJump.addInputs(KeyCode.J);
        mushroomJump.addActions(new YVelocityAction(NumericAction.ModifyType.ABSOLUTE, -5.0));

        //double jump logic (next 2 events)
        Event flappyJump = new Event("one");
        flappyJump.addInputs(KeyCode.UP);
        flappyJump.addConditions(new LessThanCondition(ValueComponent.class,10.0));
        flappyJump.addActions(new YVelocityAction(NumericAction.ModifyType.ABSOLUTE, -5.0));
        flappyJump.addActions(new YPositionAction(NumericAction.ModifyType.RELATIVE,-2.0));
        //flappyJump.addActions(new YAccelerationAction(NumericAction.ModifyType.ABSOLUTE,0.2));
        flappyJump.addActions(new ValueAction(NumericAction.ModifyType.RELATIVE,1.0));
        flappyJump.addActions(new ChangeScoreAction(NumericAction.ModifyType.RELATIVE, 100.0));


        BottomCollisionEvent flappyOnPlatform = new BottomCollisionEvent("one","four");
        flappyOnPlatform.addActions(new YVelocityAction(NumericAction.ModifyType.ABSOLUTE,0.0));
        flappyOnPlatform.addActions(new ValueAction(NumericAction.ModifyType.ABSOLUTE,0.0));


        TopCollisionEvent platformKnocked = new TopCollisionEvent("four","one");
        //platformKnocked.addActions(new HealthAction(NumericAction.ModifyType.RELATIVE,-1.0));

        /**
         * When flappy falls onto a platform with both nonzero acceleration and velocity,
         * setting its velocity to zero and not modifying acceleration will allow flappy to stand above the platform
         * and trigger CollisionEvent in every game loop until the platform crumbles;
         * setting its acceleration to zero and not modifying velocity will achieve a similar effect, except that
         * flappy's oscillation will be more obvious, so for the purpose of game display I'd recommend the first option;
         * setting both acceleration and velocity to zero will allow flappy to "float" above the platform without
         * triggering any further collision -- this could probably be useful in some cases depending on user's decision
         */



        BottomCollisionEvent mushroomOnPlatform = new BottomCollisionEvent("two","four");
        mushroomOnPlatform.addActions(new YVelocityAction(NumericAction.ModifyType.ABSOLUTE,0.0));
//        mushroomOnPlatform.addActions(new YAccelerationAction(NumericAction.ModifyType.ABSOLUTE,0.0));





        RightCollisionEvent rce = new RightCollisionEvent("one", "three");
        rce.addActions(new ProgressionAction(true));
        rce.addActions(new NextLevelAction(NumericAction.ModifyType.ABSOLUTE, next));


        level1.addEvent(event);
        level1.addEvent(event2);

        level1.addEvent(flappyMoveLeft);
        //level1.addEvent(flappyMoveUp);
        level1.addEvent(mushroomJump);
        level1.addEvent(oneByTwo);
        level1.addEvent(twoByOne);
        level1.addEvent(platformKnocked);
        level1.addEvent(flappyOnPlatform);
        level1.addEvent(mushroomOnPlatform);
        level1.addEvent(flappyJump);
    }

    private void addDummyEntities(Level level, Double current) {
        // this is the dummy object that authoring has to make for each game
        Entity gameObject = new Entity();
//        gameObject.addComponent(new VariablesComponent(new HashMap<>(){{
//            put("Score", 0.0);
//            put("Lives", 3.0);
//        }}));
        gameObject.addComponent(new ScoreComponent(0.0));





        Entity dummy1 = new Entity();
        Entity dummy2 = new Entity();
        Entity dummy3 = new Entity();
        Entity dummy4 = new Entity();
        Entity dummy5 = new Entity();
        Entity dummy6 = new Entity();



        //flappy double jump
        Component vc = new ValueComponent(0.0);
        dummy1.addComponent(vc);
        dummy1.addComponent(new XPositionComponent(200.0));
        dummy1.addComponent(new YPositionComponent(50.0));
        dummy1.addComponent(new ZPositionComponent(0.0));
        dummy2.addComponent(new XPositionComponent(400.0));
        dummy2.addComponent(new YPositionComponent(50.0));
        dummy2.addComponent(new ZPositionComponent(0.0));
        dummy3.addComponent(new XPositionComponent(90.0));
        dummy3.addComponent(new YPositionComponent(100.0));
        dummy3.addComponent(new ZPositionComponent(0.0));

        dummy1.addComponent(new WidthComponent(40.0));
        dummy1.addComponent(new HeightComponent(50.0));
        dummy2.addComponent(new WidthComponent(40.0));
        dummy2.addComponent(new HeightComponent(40.0));
        dummy3.addComponent(new WidthComponent(200.0));
        dummy3.addComponent(new HeightComponent(80.0));

//        dummy1.addComponent(new ImageViewComponent(new ImageView("basketball.png")));
//        dummy2.addComponent(new ImageViewComponent(new ImageView("basketball.png")));
//        dummy3.addComponent(new ImageViewComponent(new ImageView("basketball.png")));
        dummy1.addComponent(new SpriteComponent("flappy_bird"));
        dummy2.addComponent(new SpriteComponent("mushroom.png"));
        dummy3.addComponent(new SpriteComponent("basketball"));

        dummy1.addComponent(new CameraComponent(true));

        dummy1.addComponent(new NameComponent("one"));
        dummy2.addComponent(new NameComponent("two"));
        dummy3.addComponent(new NameComponent("three"));

        dummy1.addComponent(new XVelocityComponent(2.0));
        dummy1.addComponent(new YVelocityComponent(0.0));
        dummy1.addComponent(new XAccelerationComponent(0.0));
        dummy1.addComponent(new YAccelerationComponent(0.2));

        dummy2.addComponent(new XVelocityComponent(-2.0));
        dummy2.addComponent(new YVelocityComponent(0.0));
        dummy2.addComponent(new XAccelerationComponent(0.0));
        dummy2.addComponent(new YAccelerationComponent(0.2));

        dummy1.addComponent(new CollisionComponent(true));
        dummy2.addComponent(new CollisionComponent(true));


        dummy4.addComponent(new XPositionComponent(170.0));
        dummy4.addComponent(new YPositionComponent(400.0));
        dummy4.addComponent(new ZPositionComponent(0.0));
        dummy4.addComponent(new WidthComponent(500.0));
        dummy4.addComponent(new HeightComponent(80.0));
        dummy4.addComponent(new SpriteComponent("mario_block.png"));
        dummy4.addComponent(new CollisionComponent(true));
        dummy4.addComponent(new HealthComponent(100.0));
        dummy4.addComponent(new NameComponent("four"));

        dummy5.addComponent(new XPositionComponent(250.0));
        dummy5.addComponent(new YPositionComponent(400.0));
        dummy5.addComponent(new ZPositionComponent(0.0));
        dummy5.addComponent(new WidthComponent(80.0));
        dummy5.addComponent(new HeightComponent(80.0));
        dummy5.addComponent(new SpriteComponent("mario_block.png"));
        dummy5.addComponent(new CollisionComponent(true));
        dummy5.addComponent(new NameComponent("five"));

        dummy6.addComponent(new XPositionComponent(330.0));
        dummy6.addComponent(new YPositionComponent(400.0));
        dummy6.addComponent(new ZPositionComponent(0.0));
        dummy6.addComponent(new WidthComponent(160.0));
        dummy6.addComponent(new HeightComponent(80.0));
        dummy6.addComponent(new SpriteComponent("mario_block.png"));
        dummy6.addComponent(new CollisionComponent(true));
        dummy6.addComponent(new NameComponent("six"));

        dummy1.addComponent(new NextLevelComponent(current));
        dummy1.addComponent(new ProgressionComponent(false));

        dummy1.addComponent(new AssociatedEntityComponent(gameObject));

        level.addEntity(dummy1);
        level.addEntity(dummy2);
        level.addEntity(dummy3);
        level.addEntity(dummy4);
        level.addEntity(gameObject);
//        level.addEntity(dummy5);
//        level.addEntity(dummy6);

    }

    public Game getGame(String dummyString){
        return myGame;
    }

    public void serializeObject(){
        DataManager dm = new DataManager();
        dm.saveGameData("game1", myGame);
    }
}
