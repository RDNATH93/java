package com.example.state;

import java.util.List;

import com.example.Coin;
import com.example.VendingMachine;

public interface SelectionStateInf extends State {
    void chooseProduct(VendingMachine vendingMachine,int codeNumber);
    int getChange(int returnChangeMoney);
    List<Coin> refundFullMoney(VendingMachine vendingMachine);
}