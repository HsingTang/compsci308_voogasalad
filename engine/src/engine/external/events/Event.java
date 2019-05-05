package engine.external.events;


import engine.external.actions.Action;
import engine.external.component.GroupComponent;
import engine.external.component.NameComponent;
import engine.external.conditions.Condition;
import engine.external.Entity;
import engine.external.IEventEngine;

import engine.external.conditions.InputCondition;

import javafx.scene.input.KeyCode;

import java.io.Serializable;
import java.util.*;
import java.util.function.Consumer;
import java.util.function.Predicate;

/**
 * Events are intended for creating/handling custom logic that is specific to a game, and cannot be reasonably anticipated by the engine beforehand
 * Events execute their actions if their Conditions are met, and the relevant input keys have been pressed.
 *
 * @author Lucas Liu
 * @author Feroze Mohideen
 */
public class Event implements IEventEngine, IEventAuthoring {
    private List<Action> actions = new ArrayList<>();
    private List<Condition> conditions = new ArrayList<>();
    private List<InputCondition> inputConditions = new ArrayList<>();
    private Set<KeyCode> myInputs = new HashSet<>();

    /**
     * An Event is created using the name of the type of entity that this event will apply to
     * e.g. Event e = new Event("Mario") if a user has created an entity/group called "Mario"
     */
    public Event() {
        //This is an intentionally empty constructor
    }

    //need to make this method take in keycode inputs as well
    @Override
    public void execute(List<Entity> entities, Collection<KeyCode> inputs) {
        if (!inputs.containsAll(myInputs)) {
            return;
        }

        for (Entity e : entities) {
            if (conditionsMet(e, inputs)) {
                executeActions(e);
            }
        }
    }


    private boolean conditionsMet(Entity entity, Collection<KeyCode> inputs) {
        for(Condition c: conditions){
            if(c.getMyComponentClass() == NameComponent.class || c.getMyComponentClass() == GroupComponent.class ){
                if(!c.getPredicate().test(entity)){
                    return false;
                }
            }
        }
        for(Condition c: conditions){
            c.checkComponents(entity);
        }
        try {
            boolean conditiontest =
                    conditions.stream().allMatch((Predicate<Condition> & Serializable) condition -> (condition.getPredicate()).test(entity));
            boolean inputtest =
                    inputConditions.stream().allMatch((Predicate<InputCondition> & Serializable) inputCondition -> inputCondition.getPredicate().test(inputs));
            return conditiontest && inputtest;
        } catch (NullPointerException e) {
            //System.out.println("Condition not met, did not have required component");
            return false;
        }
    }

    private void executeActions(Entity entity) {
        try {
            actions.forEach((Consumer<Action> & Serializable) action -> {
                action.checkComponents(entity);
                action.getAction().accept(entity);
            });

        } catch (NullPointerException e) {
            System.out.println("Entity missing component for action");
            return;
        }
    }

    public void addActions(List<Action> actionsToAdd) {
        actions.addAll(actionsToAdd);
    }

    public void addActions(Action action) {
        addActions(Arrays.asList(action));
    }

    public void addConditions(List<Condition> conditionsToAdd) {
        conditions.addAll(conditionsToAdd);
    }

    public void addConditions(Condition condition) {
        addConditions(Arrays.asList(condition));
    }


    public void setConditions(List<Condition> newSetOfConditions) { conditions = new ArrayList<>(newSetOfConditions); }


    public void removeConditions(List<Condition> conditionsToRemove) {
        conditions.removeAll(conditionsToRemove);
    }

    public void removeConditions(Condition conditionToRemove) {
        conditions.remove(conditionToRemove);
    }

    public void setActions(List<Action> newSetOfActions) {
        actions = newSetOfActions;
    }

    public void removeActions(List<Action> actionsToRemove) {
        actions.removeAll(actionsToRemove);
    }

    public void removeActions(Action actionToRemove) {
        actions.remove(actionToRemove);
    }

    @Override
    public void setInputs(Set<KeyCode> inputs) { myInputs = new HashSet<>(inputs); }

    @Override
    public void addInputs(Set<KeyCode> inputsToAdd) {
        myInputs.addAll(inputsToAdd);
    }

    @Override
    public void addInputs(KeyCode inputToAdd) {
        myInputs.add(inputToAdd);
    }

    @Override
    public void removeInputs(Set<KeyCode> inputsToRemove) {
        myInputs.remove(inputsToRemove);
    }

    public List<KeyCode> getImmutableKeyCodes() {return new ArrayList<>(myInputs);}

    public void clearInputs() {
        myInputs.clear();
    }

    public Map<Class<?>, List<?>> getEventInformation() {
        Map<Class<?>, List<?>> myEventInformation = new HashMap<>();
        myEventInformation.put(Condition.class, conditions);
        myEventInformation.put(Action.class, actions);
        return myEventInformation;
    }

    public void addInputConditions(List<InputCondition> inputConditionsToAdd) {
        inputConditions.addAll(inputConditionsToAdd);
    }

    public void addInputConditions(InputCondition inputCondition) {
        addInputConditions(Arrays.asList(inputCondition));
    }

}
