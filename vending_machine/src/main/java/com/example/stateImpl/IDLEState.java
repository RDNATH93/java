package com.example.stateImpl;

import java.util.ArrayList;

import com.example.VendingMachine;
import com.example.state.IDLEStateInf;

public class IDLEState implements IDLEStateInf {

  public IDLEState() {
    System.out.println("Currently Vending Machine is on IdleState");
  }

  IDLEState(VendingMachine vendingMachine) {
    System.out.println("Currently Vending Machine is on IdleState");
    vendingMachine.setCoins(new ArrayList<>());
  }

  public void clickOnCoinInsertButton(VendingMachine vendingMachine) {
    vendingMachine.setState(new HasMoneyState());
  }

}