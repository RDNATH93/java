package com.example;

import com.example.state.HasMoneyStateInf;
import com.example.state.IDLEStateInf;
import com.example.state.SelectionStateInf;
import com.example.stateImpl.HasMoneyState;
import com.example.stateImpl.IDLEState;
import com.example.stateImpl.SelectionState;

// State Design Pattern 
public class Main {
    public static void main(String[] args) {
        VendingMachine vendingMachine = new VendingMachine();

        System.out.println("|");
        System.out.println("filling up inventory");
        System.out.println("|");

        fillupInventory(vendingMachine);
        displayInventory(vendingMachine);

        IDLEStateInf idleState = (IDLEState) vendingMachine.getState();
        System.out.println("|");
        System.out.println("clicking on insert coint button");
        System.out.println("|");
        idleState.clickOnCoinInsertButton(vendingMachine);

        HasMoneyStateInf hasMoney = (HasMoneyState) vendingMachine.getState();
        hasMoney.insertCoin(vendingMachine, Coin.NICKLE);
        hasMoney.insertCoin(vendingMachine, Coin.QUATER);
        System.out.println("|");
        System.out.println("clicking on select product button");
        System.out.println("|");
        hasMoney.clickOnStartProductSelectionButton(vendingMachine);

        SelectionStateInf selectionState = (SelectionState) vendingMachine.getState();
        selectionState.chooseProduct(vendingMachine, 102);

        displayInventory(vendingMachine);

    }

    private static void fillupInventory(VendingMachine vendingMachine) {
        ItemShelf[] slots = vendingMachine.getInventory().getInventory();

        for (int i = 0; i < 10; i++) {
            Item item = new Item();
            if (i >= 0 && i < 3) {
                item.setType(ItemType.PEPSI);
                item.setPrice(9);
            }
            if (i >= 3 && i < 5) {
                item.setType(ItemType.COKE);
                item.setPrice(12);
            }
            if (i >= 5 && i < 7) {
                item.setType(ItemType.JUICE);
                item.setPrice(13);
            }
            if (i >= 7 && i < 10) {
                item.setType(ItemType.SODA);
                item.setPrice(7);
            }
            slots[i].addItem(item);
            slots[i].setSoldOut(false);

        }
    }

    static void displayInventory(VendingMachine vendingMachine) {
        ItemShelf[] slots = vendingMachine.getInventory().getInventory();

        for (int i = 0; i < slots.length; i++) {
            Item item = slots[i].getItem();
            System.out.println("Item: " + item.getType() + " shelf: " + slots[i].getCode()
                    + " isAvailable: " + !slots[i].isSoldOut());
        }
    }
}
