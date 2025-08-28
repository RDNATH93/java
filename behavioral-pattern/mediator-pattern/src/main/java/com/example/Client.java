package com.example;

/**
 * It's a Behavioral Pattern
 * It encourages loose coupling by keeping objects from reffering to each other explicitly
 * and allow them to communicate through a mediator object
 * 
 * Example - Online Auction System, Airline Management System
 * 
 */
public class Client {
    public static void main( String[] args ){
        AuctionMediator auction = new Auction();

        Colleague bidderA = new Bidder("A", auction);
        Colleague bidderB = new Bidder("B", auction);

        bidderA.placeBid(1000);
        
    }
}
