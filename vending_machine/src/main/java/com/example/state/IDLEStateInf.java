package com.example.state;

import com.example.VendingMachine;

public interface IDLEStateInf extends State {
    void clickOnCoinInsertButton(VendingMachine vendingMachine); 
}