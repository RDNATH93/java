package com.example;

public interface RoomElement {
    void accept(RoomVisitor visitor);
    
}
