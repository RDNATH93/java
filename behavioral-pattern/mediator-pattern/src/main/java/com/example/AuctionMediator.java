package com.example;

public interface AuctionMediator {
    void placeBid(Colleague colleague, int amount);
    void addBidder(Colleague colleague);
}
