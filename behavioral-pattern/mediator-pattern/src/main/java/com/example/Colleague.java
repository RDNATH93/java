package com.example;

public interface Colleague {
    String getName();
    void placeBid(int amount);
    void receivedBidNotification(String name, int amount);
}
