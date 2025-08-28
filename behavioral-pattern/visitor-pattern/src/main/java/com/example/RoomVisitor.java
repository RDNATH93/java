package com.example;

public interface RoomVisitor {
    void visit(SingleRoomElement singleRoomElement);
    void visit(DoubleRoomElement doubleRoomElement);
    void visit(DeluxRoomElement  deluxRoomElement);
}
