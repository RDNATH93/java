package com.example;

public class WaterBreathImplementor implements BreathImplementor {

    @Override
    public void breath() {
        System.out.println("Breath via gills");
        System.out.println("Inhale oxygen");
        System.out.println("Exhale carbon dioxide");
    }
    
}
