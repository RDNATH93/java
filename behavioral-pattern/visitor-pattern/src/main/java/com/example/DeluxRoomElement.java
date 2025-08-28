package com.example;

public class DeluxRoomElement implements RoomElement {

    private double price;

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    @Override
    public void accept(RoomVisitor visitor) {
        visitor.visit(this);
    }
    
}
