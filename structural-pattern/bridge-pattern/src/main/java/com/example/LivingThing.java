package com.example;

public abstract class LivingThing {
    protected BreathImplementor breathImplementor;
    
    LivingThing(BreathImplementor breathImplementor){
        this.breathImplementor=breathImplementor;
    }

    abstract void breathProcess();

}
