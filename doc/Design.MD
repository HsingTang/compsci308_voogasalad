DESIGN.md
===
## High-level design goals
### Authoring
Authoring wanted to create an environment that would be intuitive to the user but versatile enough to allow for as much control as possible. Authoring created its own version of an Entity, a Level, and Events with JavaFX binding/listening features that would not have been serializable had they existed in the same classes other modules used to run games. These, along with some classes to manage them, acted as the backend of our authoring module. This backend enabled us to use binding to update entity properties in multiple areas of the UI as new instances were instantiated and their characteristics were changed. The graphics directly presented to the author were bound to this backend such that they could be edited and updated appropriately. Once the user chooses to save, the front-end version of the entities, levels, and events are translated into the Entity, Level, and Events that are exported from the engine. This translation removes all JavaFX and allows for the serialization of the complete Game Object. Similarly, there are mechanisms to translate a saved game back into a version that is editable by the authoring frontend.

### Center
When creating the Game Center module, we primarily wanted to make the environment as developer-friendly as possible. Some of our primary design goals here were to ensure that it was simple and easy to change the displayed components without much effort. Additionally, we wanted to ensure that it was simple and easy to change the UI, language, and content without needing to change inline code. Lastly, because the game center contains various forms of pop-ups, one of the goals was to create a generalized form of a popup to prevent redundancy in methods and content. 

We also wanted to the Game Center to be user-friendly and not require re-launch to switch between games, change accounts, or add new games. To do this, we decided that the Game Center would call upon the Runner module to handle all of the communication with back-end. This way, the Center can handle multiple Runners at once without having to relaunch. 

Finally, we wanted to the Game Center to be able to function completely on its own, so we ensured to prevent any dependencies between Center and Authoring. This way, we can take the Game Center and use it for any types of information, not simply information from the Authoring module.

### Data
The high level goal of the data module was to provide an api to the other modules for data management whether it is storing images, sounds, user information, or game information. The data module also was designed to encapsulate the details of file storage, and data transformations such as serialization away from the other modules so they do not need to worry about the backend implementation of how data is stored and accessed. Furthermore, the data module was intended to be our utility module so we had the goal of making it as general as possible so other teams could easily plug the module into thier project and be able to use the same data management tools. 

The data module's api is essentially a series of puts and gets (saves and loads) that allow the other modules to save information they want saved and be able to retrieve it later. The authoring module would use the data module to save and load games, sounds and images to our database.  The launcher used the data module to manage users and their passwords. The data module allowed the launcher to create new users and verify their login information. The game center loads in all game information (GameCenterData) objects from the data module, saves and loads all user profile information such as user bios and profile pics using the data modules and creates and displays comments, ratings, and high scores using the data module. The runner depends on the data module for loading in a new game or checkpoint, saving the game or checkpoint, and updating high scores. Finally, the engine uses the data module to load in the images and audio files used in the game.


### Runner
At a high level the goal of the runner is very simple. The intanstiation of a new runner object should create a new window on the screen where the game is played. The idea is that the object that creates the GameRunner will not have to make any calls on it other than its creation. This way, it can be called in several places (center or authoring) and they both have the same functionality. The runner will manage the game as its being played and use the engine to update the state of the game. The runner uses systems that loop through the entities. Specific systems act on entities if those entities have the desired components and this is how the front end of the game is updated.

### Engine
The project design adopts an Entity-Component-System architecture: any game level played by the user is built on a set of entities, and an entity can possess any combination of components. The `Engine` class accepts a `Level` object from `Runner`, and retrieves the entities and events (that were created by user in the authoring environment) from the `Level`. 
Internaly, engine is built on multiple systems, each of which serves certain functionalities under general platformer game rules, such as movement control, collision detection, ImageView display, etc. On every game loop, the `Engine` class receives input Keycodes from `Runner`, invokes the systems to update `Component` values for every `Entity`, executes each `Event` to trigger certain actions when corresponding conditions are met, and returns the updated entities back to `Runner` for front-end display.
#### Entity
An `Entity` is the basic element in a game. components that define its properties (collidablility, visibility, health, etc.). Entities for a game level are created in the Authoring Environment, and packaged into a `Level` object when author saves the game.
#### Component
A `Component` is essentially a container of value. There is an abstract super `Component` class, which is extended by many concrete sub-classes to hold values of certain types (Double, Boolean, String, etc.) for different purposes (position, image, sound, etc.). `Component` values of entities are updated on the game loops.
#### System
General platformer game rules are baked into the systems to update `Component` values for entities when running a game. Under an abstract `VoogaSystem` class, several concrete sub-system classes are designed to apply different rules concerning certain concrete `Component` classes. In order to interact with an entity and modify its components, a system requires the entity to possess a certain set of components, and applies a filtering method to only deal with eligible entities (e.g. `MovementSystem` only deals with entities with `XPositionComponent` and `YPositionComponent`).
#### Event
Events are designed to capture any sophisiticated or custom game logic, that cannot be reasonably pre-built and incorporated as a `System`. Events are composed of `Conditions` and `Actions`
#### Condition
Conditions are the set of necessary pre-cursors for an Event. The conditions may include key inputs, collisions, or any number of state requirements for a game variable (x position, y position, health...)

#### Action
Actions are the executed changes of an Event. They are only executed if the conditions have been met. These changes will often be modifications to components of entities. 


## Adding new features to the project
### Authoring
If a user wanted to add in a new event, action, or condition as an option in the authoring environment, there is a conditions.properties, actions.properties, and an EventType enum that would need to be modified in order for it to be presented as an option to the user. One constraint we do have for actions and conditions that a user can create in the scope of the authoring environment is that the condition or action's associated "trigger-value" would be a string or double value. The exception to this is to add a progression component, which wins the game, and is a separate button option from the other drop-downs in the event-maker panes. Additionally, since we track all groups, we considered implementing a feature that allowed the user to view events from a group perspective, but decided against it since we were close to the deadline. Since events are easy to filter based on their associated conditions, it would not have been difficult to sort out events by group type instead of entity name. Additionally, if a user wished to allow for an additional Entity or Level property to be editable via the UI, there is a properties file that defines how it is displayed and the JavaFX control class by which it can be edited, and they would simply need to add to that file and make sure the relevant field existed in an enum corresponding to either Level or Entity fields.

Adding a new asset type would be very doable. In extending the AssetManager class and writing the abstract methods, a new window would be created that could be added to the general authoring layout. Additionally, saving these assets to the database would require creating a new directory in the assets directory and then calling the saveFolderToDataBase() method would need to be called in the MainGUI class in the saveGame() method. The assets would also need to be loaded whenever a game is loaded back into Authoring. This would be accomplished by adding to the loadAllAssets() method in the MainGui.

Adding a new default type is extremely easy. A new XML file would need to be created in the Data folder in the Authoring module. Each file would need to have a Category tag, this can be anything but those with the same category will be displayed under the same tab. There also needs to be a Components tag and within that, there has to be a Name tag. Beyond that, it's just mixing and matching components. The tags need to be the same name as one of the components in engine and currently the reflection supports doubles, Strings, integers, and booleans for arguments. I believe this encompasses most if not all of the components in engine, allowing for a wide array of possibilities. Once the file is created, it will automatically be added as an option in the Authoring Environment.

### Center
#### Add a new popup 
1. Create a subclass of the Popup class
2. Create a properties file under ``<Class Name>.Properties`` containing information for all of the buttons desired in a popup
3. Implement addHeader(), addBody(), addButtons(), and any methods corresponding to the buttons properties file
4. Wherever the popup is desired to be opened, call the constructor of the specific subclass, and the popup should display as expected. 

#### Add new information to GameCard
1. Ensure that the proper information has a getter/setter in the Data module in the GameCenterData object
2. All of this information can be modified in the GameCard object, simply insert the new information from myGame into the card

#### Changing the buttons displayed on a popup:
1. In the resources folder, change or add in a buttonID corresponding to the button and the display text for the button. If there are multiple languages, add in the buttonID to every language resource file as well. 
2. In the class in which the button is displayed, ensure that a method header corresponding to the buttonID is added with an implementation. This is essential for the reflection that calls on the method of the button. 

#### Changing the color scheme or design scheme of the UI:
1. All of the CSS information is contained in resources/center.css . Go into this file to pick color schemes and other design changes. No other changes to the code are necessary, because each component uses CSS rather inline styling. 

#### Changing the language of the UI: 
1. Under resources/languages, add in the resource bundle with the same IDs as the English resource bundle. Translate the display values into the desired language. 
2. In any classes that have a display component, update the myLanguage field and the myLanguageBundle bundle so that they display properly. 

### Data
New features can be added to the data module in a two step process. If the database already has tables set up to store the information that is being accessed, the first step can be skipped. Otherwise, one must create or append to a table in the database with the kind of data they want to be storing and accessing. Then the user must create a new subclass of Querier that will be used to access this table with prepared sql statements. The user should then write the chain of delegation commands in DataManager, DatabaseEngine, and the newly created Querier to add the new feature. Although this design sounds convoluted, it was designed so the DataManager would be the point of access for the other modules and would be the class that encapsulates away the implementation decisions (such as using a database or file system). The DatabaseEngine then has the role of delegating to the specific querier in charge of that table in the database to process the request.

### Runner
Adding a new feature to the runner is not difficult. A new feature in the application will likely start with the engine create new Components. These components can be anything that hold values, esentially acting as variables for an entity. To add a new feature in Runner all that needs to be done is to create a system and pass it the required components and the access it needs to make modifications to its part of the level. Once the system is added in the SystemManager it will automatically be included in LevelRunner's call to update and it's changes will be displayed on the screen to the user.

### Engine
To add new properties available for Entities, it will be necessary to develop additional concrete `Component` classes that will be utilized by Authoring when creating the Entities. If the new Entity falls under a convenient existing abstract class (such as Numeric) very little additional code will need to be written. Corresponding game rules regarding the new properties (i.e. when and how to modify the new component) might also need to be embedded into associated `Event`/ `Condition`/ `Action` classes as a customized feature, or into a `System` as an extension of the general platformer game rules.

## Justification on major design choices, including trade-offs (i.e., pros and cons)
### Authoring
How to create the default entity types was one of our big decisions. Originally, the entities were hardcoded in a factory class that had static methods that created the defaults whenever called. This system was inflexible, resulted in duplicate code, and made it harder than necessary to add new default types. We started with this system but after consulting Professor Duvall, the possibility of XML was proposed. Creating an XML parser that would create default entites based on XML files would make it far easier to add new types. It also wouldn't require any new code after the initial parser. This design was implemented and through large amounts of reflection, default entites are now able to be created and it works with any components in engine that take in a boolean, string, integer, or double as a parameter. 


### Center
#### Creating a popup superclass
Pros:
* Whenever a new popup is created, a lot of the starter code is already complete in the Popup superclass
* All of the popups have similar style and structure, which creates a more appealing UI
* Reduces duplicated code between classes 

Cons: 
* Because the structure is all very similar, users don't have much choice in customizing the top/bottom of their popup panel
* The abstraction of the superclass is never utilized in the code, so the superclass is mostly just for methods with the same functionality

Alternatives: 
* Create separate classes for every popup and either
    * Have duplicated code in similar methods - this is bad because we want to avoid duplicated code at all costs. If the user wants to change something about a popup, they shouldn't have to do it in every single popup class. 
    * Put similar methods in Utilities class - this is bad because most classes won't need to use these methods. Having them in the Utilities class is essentially giving everything access to them, which is unnecessary. 

#### Using reflection to create and run the buttons
Pros:
* It is extremely easy to add, remove, or change the types of buttons used in the Game Center
* All buttons can be created with one method (in ``Utilities.java``), which reduces duplicated code and button creation complexity

Cons: 
* This causes some un-intuitive dependencies, like needing specific methods with names corresponding to ButtonIDs. This is incredibly important to be added, and forgetting it essentially nullifies the button. 

Alternatives: 
* Do not use reflection on buttons, and instead pass in a lambda to the method call that creates the button
    * This would work, but it requires a little bit more effort to add in new buttons. For example, every time, you would need to write a lambda rather than just pass in the instance of the object. 

#### Relying on Data as the backend
Pros:
* The work in the database is separate from what is displayed, so the front-end really is just a display
* When we switched over from local folders to a database on a server, GameCenter wasn't even affected

Cons: 
* It is difficult to change the back-end of the Center without potentially affecting the back-end of other modules (since Data is a shared module)

Alternatives: 
* Create my own back-end for Center.
    * While this was an option, I decided against it because most of the Center back-end would be calling on Data methods anyways, so it would have just been useless one-line methods that call other methods. 

#### Staying completely separate from Authoring
Pros:
* The GameCenter can be used independently of the Authoring Environment, and can essentially be loaded for any object that is associated with a GameCenterData object

Cons: 
* Sometimes, if Authoring and Center both need a class, it needs to go in a shared imported module such as Data to provide access for both. 

Alternatives: 
* Have some dependencies between Authoring and Center
    * While this would have simplified our design some, ultimately it was not worth it because it would have created unnecessary links between the two. This also would have gone against our primary design goal of keeping the two separate. 

#### Calling on GameRunner from GameCenter
Pros:
* Multiple games can be run at one time
* The GameCenter can still be used as just front-end and has no dependencies on Engine
* The GameCenter has all of the information GameRunner needs, so there is no unnecessary data fetching in the Runner side of things

Cons: 
* The UI of the Runner and Center are slightly different
* Nothing in the Runner can be changed in Center code, even though they are in the same location

Alternatives: 
* Have GameRunner and GameCenter be in the same module
    * This was a viable solution, however GameRunner requires some modules that GameCenter does not require and vice-versa. For example, Runner needs Engine while Center does not. Center, on the other hand, needs voogasalad_util, which Runner does not. Therefore, to reduce unnecessary imports and dependencies and to keep Center primarily visual, we decided to separate Runner and Center. 

### Data
There were three main design decisions made in terms of the data module. 

The first was the implementation decision to store our data in a database hosted on a remote server. This decision was made to increase the functionality of our project, allowing users to play games authored by other people. The main trade-offs in this decision were between time (because it took a very long time to set up the database and get it working on everyone's computers) and functionality. Additionally, there were concerns with data storage and best practices in terms of what information is appropriate to be saved in a database. For example, we realized that it is not best practice to store very large files such as images or sounds in a database and it would be better to use some other tool such as Amazon S3, but we ultimately decided it would be better to have all our data in one centralized location to avoid the headache of setting up another storage tool.

The second main design decision was to have the DataManager class be the main point of entry for other modules to access the Data module and to have the DataManager implement the entire API. This decision was made to ease communication with the other modules by only having to familiarize teammates with one class name to use with data and it made a lot of sense when we were originally just saving and loading games. However, when we began to expand what we stored to include images, sounds, user information and ratings, the role of the DataManager got too large and would have been better split up into separate kinds of data managers for each of the different assets. One key advantage of this design, however, is that DataManager entirely encapsulates away the implementation decisions of how we stored our data. Between the first and second sprint we switched from using only the file system to using a database and it did not affect anyone else's code.

Finally, the data module uses a delegator pattern in the DatabaseEngine to delegate responsibility to the various Querier for querying the database. This decision was made to break up the responsibility of the DatabaseEngine and to also limit the amount of code in the class to just be database resource management (opening and closing the connection).

### Runner
The fundamental concept behind the runner is that it runs the game loop and in the game loop it only needs to make two calls. These calls are engine.updateState() and updateGUI(). The former updates the entities in the back end and the latter updates those entities in the front end. In the beginning of the project in the updateGUI() method I was making a lot of calls that were of the format: if (entity.hasComponent(someComponent)){Do something}. As this method got longer and longer I realized that this was not the best design or most efficient way to organize code. So I decided to change the design to use systems. Inspired from my teammate's code in Engine I created runner Systems that maintained a list of required components and actions to do on entities if they had those components. These systems were then created and stored in a System manager and passed to the level runner so that to add a new system the only change that needed to be made was to the system manager. This design was much more flexible and proved to be easier to understand and much more effective. 

### Engine
#### System
As each system serves a distinct purpose and requires a distinct set of components from entities, when instantiating the systems, the `Engine` class retrieves information about the system class names, the ordering of systems, and the required components for each system from properties files, and uses reflection to instantiate the systems.
Such a design allows for flexible adjustment on the invoking order of systems on each game loop, and also makes it very convenient to add new systems into the engine or to change the component requirements for systems without modifying any already existing `System` or the `Engine` class.

#### Event
Intially events were pre-filtered on the name component of an entity as part of any event's set of conditions. However, we realized this limited the user to only create events between one Entity and one other Entity. For example, collisions between two entity types (such as mario and a goomba) instead of an entity type and a group (such as mario and an enemy, which may be a turtle, bowser, or a goomba). The downside was a less restrictive contract for events (no longer mandating a name pre-filter). The added benefit to this was flexibility to the user to make it easier to author a game that had many repeated behaviors between different entity types, and to leverage groups.

## Assumptions or decisions made to simplify or resolve ambiguities in the project's functionality
### Authoring
We made a few assumptions in order to simplify saving and managing the different types of entities
1) If a user made an event either involving an entity, two entities, or an entity and a group, the actions associated with the that event's conditions should be strictly related to one (of the) entity(ies) or to the group
2) A user would only want to associated one key input with particular set of actions and not require for a case where, for example, an up arrow key corresponds to one set of actions, a down arrow key corresponds to a different set of actions, and an up and down arrow key together ought to correspond to a third set of actions
3) A user would only need to define collisions between two entities or an entity and a group.
4) A user must begin by making a collision event and can not arbitrarily add a collision condition to any given event
5) If a user wanted to involve an entity's sprite or sound component in a condition or action, they would only be able to use the images and sound files they had separately uploaded by going into the top toolbar and adding those files in
6) We assumed that in any given level, there would be one entity in focus for the entirety of the level, and the user would only be able to switch which entity was in focus in a different level
7) A user must explicitly set the level index they wish to go to if they add an action relative to a level. This is different than most other double actions, as we allow the user to set the value to a scaling or offset of the current value.
8) There must be at least one Entity added to a given level, and exactly one Entity per level must be marked as the "Focus," which gives it special functionality in the runner (camera focus, score and lives component ownership).
9) The user is not allowed to create two Entity types or Levels with the same name.
10) The user is not allowed to place an object outside of the room dimensions given, and there are syntax parameters that prevent certain TextField-editable properties from containing invalid values.
11) Not every Entity needs to have every property field by default, so certain fields are deemed optional until the user specifies they are desired.
12) The game title is only permitted to be alphanumeric with whitespace and underscores.
13) Entity names, level names, and group names are only permitted to be alphanumeric with underscores and must begin with a letter.
14) If the user adds an Entity to a Level and proceeds to decrease the Level dimensions such that the Entity is no longer within its bounds, the Entity will remain in that position in case the user made a mistake and would like to revert to reinclude that Entity in the visible level. Having Entities outside the defined room size does not create issues in the runner, so this logic is unproblematic.
15) A new game is given default title, description, and first level with default name and dimensions. Any of these values are editable by the user if they so desire.
16) New levels will be given the name "New_Level_" plus an integer starting from 1 and increasing until the name isn't already being used in another level.

### Center
There were a few major assumptions made by GameCenter:
1. There will be a GameCenterData object associated with every game. If there is not, the game will not display. 
2. Every GameCenterData object is linked to a game. For example, one cannot exist without the other. 
3. If a developper adds in new buttons to the properties fils, they will also add the required methods for running those buttons
4. When someone is using Center, they will always be connected to an account
5. Every user will have a bio
6. Center will always have access to the database. Otherwise, nothing will display. 
7. Users will not have titles longer than ~20 characters. If the titles are too long, the text does not wrap to the card.

### Data
There were a couple assumptions made by the data module to simplify the project's functionality:

1) Every game will have a score
2) The higher the score the better. This was used to produce the high scores that get displayed in the game center
3) The game name + author name will be unique. One author cannot make two games of the same name.
4) No two people can have the same user name
5) Authoring environment would have a specific prefix for how they want to save assets
6) The user will be connected to the internet so they can access the database
7) Users can only have one rating per game (their rating gets updated if they rate again)
8) A user will not pass two checkpoints within the same second

### Runner
The runner assumes that any splash screens desired by the game author will be created as 'levels' in the authoring environment. When the user presses play game the runner launches immediately into the first level. This is by design so that if the user had preffered a splash screen they would have to author a custom one. The runner also assumes that all levels displayed will have a score, level number, and number of lives to be displayed in the heads up display. 

### Engine
#### Reverting position for colliding entities
The CollisionSystem detects collisions between entities that possess ImageViews, and utilizes the `.intersects()` method proided by JavaFX ImageView class in combination with the relative positions of two entities to identify collisions in different directions (top, bottom, left, right). Upon a collision, the `CollisionSystem` saves the two colliding entities to each other's `CollidedComponent` in the corresponding direction, which could be later utilized by CollisionEvent's condition checking and action triggering.
The assumption was made that two collidable entities could never overlap with each other, in order to avoid scenarios where two entities might run further into each other and lead to more complicated collision logics. To comply with this assumption, the `PositionComponent` classes in X and Y directions have additional fields and methods to keep track of the current as well as previous position values (i.e. in the last frame) for Entities. `CollisionSystem` is designed to record the old position values for colliding entities, and revert their positions back at the end of each game loop after all the other systems have finished updating, such that the entities will be back to the state where they were about to get in touch, but have not yet collided.