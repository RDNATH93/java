package com.example;

public class RoomPricingVisitor implements RoomVisitor {

    @Override
    public void visit(SingleRoomElement singleRoomElement) {
        System.out.println("Calculating price for SingleRoom"); 
        singleRoomElement.setPrice(1000); 
          
    }

    @Override
    public void visit(DoubleRoomElement doubleRoomElement) {
        System.out.println("Calculating price for DoubleRoom"); 
        doubleRoomElement.setPrice(2000); 
          
    }

    @Override
    public void visit(DeluxRoomElement deluxRoomElement) {
        System.out.println("Calculating price for DeluxRoom"); 
        deluxRoomElement.setPrice(5000); 
    }
    
}
