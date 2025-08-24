package com.example.state;

import com.example.Item;
import com.example.VendingMachine;

public interface DispenseStateInf extends State {
    Item dispenseProduct(VendingMachine vendingMachine,int codeNumber);
    void updateInventory(VendingMachine vendingMachine,Item item,int codeNumber);    
}