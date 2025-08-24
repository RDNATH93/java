package com.example.stateImpl;

import java.util.List;

import com.example.Coin;
import com.example.VendingMachine;
import com.example.state.HasMoneyStateInf;

public class HasMoneyState implements HasMoneyStateInf  {


    HasMoneyState(){
        System.out.println("Currently Vending Machine is on HasMoneyState");
    }
    @Override
    public void insertCoin(VendingMachine vendingMachine, Coin coin) {
       System.out.println("Inserted a "+coin); 
       var coins = vendingMachine.getCoins();
       coins.add(coin);
       vendingMachine.setCoins(coins);
    }

    @Override
    public void clickOnStartProductSelectionButton(VendingMachine vendingMachine) {
        vendingMachine.setState(new SelectionState());
    }
    
    @Override
    public List<Coin> refundFullMoney(VendingMachine vendingMachine) {
       System.out.println("Returned the full amount back in the coin dispense tray");
       vendingMachine.setState(new IDLEState(vendingMachine));
       return vendingMachine.getCoins();
    }
}
