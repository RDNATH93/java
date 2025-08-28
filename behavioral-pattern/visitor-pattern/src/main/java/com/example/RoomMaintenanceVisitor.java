package com.example;

public class RoomMaintenanceVisitor implements RoomVisitor {

    @Override
    public void visit(SingleRoomElement singleRoomElement) {
      System.out.println("Room mainatenance for SingleRoom");
    }

    @Override
    public void visit(DoubleRoomElement doubleRoomElement) {
      System.out.println("Room mainatenance for DoubleRoom");
    }

    @Override
    public void visit(DeluxRoomElement deluxRoomElement) {
        System.out.println("Room mainatenance for DeluxRoom");
    }
    
}
