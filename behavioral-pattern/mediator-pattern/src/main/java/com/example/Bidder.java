package com.example;

public class Bidder implements Colleague {
    private String name;
    private AuctionMediator auctionMediator;

    Bidder(String name, AuctionMediator auctionMediator) {
        this.name = name;
        this.auctionMediator = auctionMediator;
        auctionMediator.addBidder(this);
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public void placeBid(int amount) {
        auctionMediator.placeBid(this, amount);
    }

    @Override
    public void receivedBidNotification(String name, int amount) {
        System.out.println("Bidder " + name + " has placed a bid of amount " + amount);
    }

}
