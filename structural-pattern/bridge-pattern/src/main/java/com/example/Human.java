package com.example;

public class Human extends LivingThing {

    Human(BreathImplementor breathImplementor){
        super(breathImplementor);
    }

    @Override
    void breathProcess() {
        breathImplementor.breath();
    }
    
}
