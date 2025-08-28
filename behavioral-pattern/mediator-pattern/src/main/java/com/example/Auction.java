package com.example;

import java.util.ArrayList;
import java.util.List;

public class Auction implements AuctionMediator {

    List<Colleague> colleagues = new ArrayList<>();

    @Override
    public void addBidder(Colleague colleague) {
        colleagues.add(colleague);
    }

    @Override
    public void placeBid(Colleague colleague, int amount) {
        for (Colleague col : colleagues) {
            if (!col.getName().equals(colleague.getName())) {
                col.receivedBidNotification(colleague.getName(), amount);
            }
        }

    }

}
