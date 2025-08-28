package com.example;

public class SingleRoomElement implements RoomElement {
    
    private double price;

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public void accept(RoomVisitor visitor) {
        visitor.visit(this);
    }
    
}
