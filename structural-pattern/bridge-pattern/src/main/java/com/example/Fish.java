package com.example;

public class Fish extends LivingThing {

    Fish(BreathImplementor breathImplementor) {
        super(breathImplementor);
    }

    @Override
    void breathProcess() {
        breathImplementor.breath();
    }
}
