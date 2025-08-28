package com.example;

/**
 * It's a Behavioral Pattern
 * Provides an ability to revert an object to a previous state i.e. UNDO capability
 * and
 * it doesn't expose object internal implementation
 * 
 * also known as Snapshot design pattern
 */

public class Client {
    public static void main( String[] args ){
        
        ConfigurationCareTaker careTaker = new ConfigurationCareTaker();
        //initial state of originator
        ConfigurationOriginator originator = new ConfigurationOriginator(5,7);
  
        //save it
        ConfigurationMemento snapshot1 = originator.createMemento();

        //add it to history
        careTaker.saveMemento(snapshot1);

        originator.setHeight(8);
        originator.setWidth(10);

        //save it
        ConfigurationMemento snapshot2 = originator.createMemento();

        //add it to history
        careTaker.saveMemento(snapshot2);
        

        //originator changing to new state
        originator.setHeight(10);
        originator.setWidth(12);

        System.out.println("Before restore: height "+ 
        originator.getHeight()+" width "+originator.getWidth());

        //UNDO
        ConfigurationMemento savedMemento = careTaker.undo();
        originator.restoreMemento(savedMemento);

        System.out.println("After restore: height "+ 
        originator.getHeight()+" width "+originator.getWidth());
    }
}
