package com.example;

import java.util.ArrayList;
import java.util.List;

import com.example.state.State;
import com.example.stateImpl.IDLEState;

public class VendingMachine {
    private State state;
    private ItemInventory inventory;
    private List<Coin> coins;


    VendingMachine(){
        state= new IDLEState();
        inventory = new ItemInventory(10);
        coins=new ArrayList<>();
    }

    public State getState() {
        return state;
    }

    public void setState(State state) {
        this.state = state;
    }

    public List<Coin> getCoins() {
        return coins;
    }

    public void setCoins(List<Coin> coins) {
        this.coins = coins;
    }

    public ItemInventory getInventory() {
        return inventory;
    }

    public void setInventory(ItemInventory inventory) {
        this.inventory = inventory;
    }

}