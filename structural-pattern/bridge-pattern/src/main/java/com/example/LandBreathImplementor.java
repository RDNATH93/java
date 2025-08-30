package com.example;

public class LandBreathImplementor implements BreathImplementor {

    @Override
    public void breath() {
        System.out.println("Breath via nose");
        System.out.println("Inhale oxygen");
        System.out.println("Exhale carbon dioxide");
    }
    
}
