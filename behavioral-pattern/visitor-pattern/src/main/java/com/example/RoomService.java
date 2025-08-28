package com.example;

/**
 * It's a Behavioral Pattern
 * This allows you to add new Operation to exising classes without changing their structure
 * ex - we can add new Room Operation like  without changing Room structure
 * It is achieved by seperating operations from objects on which it operates
 * It does "Double Dispatch" to achieve this 
 * Double Dispatch means , method which need to be invoked decided by the caller object and 
 * the object passed in the argument 
 */

public class RoomService {
    public static void main( String[] args ){
        RoomElement singleRoomElement = new SingleRoomElement();
        RoomElement doubRoomElement = new DoubleRoomElement();
        RoomElement deluxRoomElement = new DeluxRoomElement();

        RoomPricingVisitor pricingVisitor = new RoomPricingVisitor();
        RoomMaintenanceVisitor maintenanceVisitor = new RoomMaintenanceVisitor();

        singleRoomElement.accept(pricingVisitor);

        doubRoomElement.accept(maintenanceVisitor);

        deluxRoomElement.accept(pricingVisitor);
        deluxRoomElement.accept(maintenanceVisitor);
    }
}
