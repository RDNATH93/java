package com.example;

import java.util.HashMap;
import java.util.Map;

public class Context {
    private Map<String,Integer> context = new HashMap<>();

    public void addValue(String key,Integer value){
        context.put(key, value);
    }

    public int getValue(String key){
        return context.get(key);
    }
}
