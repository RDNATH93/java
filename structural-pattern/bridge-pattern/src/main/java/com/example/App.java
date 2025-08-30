package com.example;

/**
 * It's a Structural Pattern
 * It decouples abstraction from its impelmentation so that the two can 
 * vary independently
 *
 */
public class App {
    public static void main( String[] args ){
        Human human = new Human(new LandBreathImplementor());
        human.breathProcess();

        System.out.println();
        Fish fish = new Fish(new WaterBreathImplementor());
        fish.breathProcess();
    }
}
