package com.example.state;

import java.util.List;

import com.example.Coin;
import com.example.VendingMachine;

public interface HasMoneyStateInf extends State {
    void insertCoin(VendingMachine vendingMachine,Coin coin);
    List<Coin> refundFullMoney(VendingMachine vendingMachine);
    void clickOnStartProductSelectionButton(VendingMachine vendingMachine);
}