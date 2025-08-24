package com.example.stateImpl;

import java.util.List;

import com.example.Coin;
import com.example.ItemShelf;
import com.example.VendingMachine;
import com.example.state.SelectionStateInf;

public class SelectionState implements SelectionStateInf {

    SelectionState() {
        System.out.println("Currently Vending Machine is on SelectionState");
    }

    @Override
    public void chooseProduct(VendingMachine vendingMachine, int codeNumber) {
        ItemShelf[] slots = vendingMachine.getInventory().getInventory();
        for (ItemShelf slot : slots) {
            if (slot.getCode() == codeNumber) {
                if (slot.isSoldOut()) {
                    System.out.println("Item is currently unavailable");
                    refundFullMoney(vendingMachine);
                } else {
                    int itemPrice = slot.getItem().getPrice();
                    int insertedAmount = 0;
                    for (Coin coin : vendingMachine.getCoins()) {
                        insertedAmount += coin.value;
                    }
                    if (insertedAmount < itemPrice) {
                        System.out.println("Inserted amount is insufficient for selected item");
                        refundFullMoney(vendingMachine);
                        return;
                    } else if (insertedAmount > itemPrice) {
                        getChange(insertedAmount - itemPrice);
                    }
                    vendingMachine.setState(new DispenseState(vendingMachine,codeNumber));
                }
            }
        }

    }

    @Override
    public int getChange(int returnChangeMoney) {
        System.out.println("Returned the change back in the coin dispense tray "+returnChangeMoney);
        return returnChangeMoney;
    }

    @Override
    public List<Coin> refundFullMoney(VendingMachine vendingMachine) {
        System.out.println("Returned the full amount back in the coin dispense tray");
        vendingMachine.setState(new IDLEState(vendingMachine));
        return vendingMachine.getCoins();
    }

}
