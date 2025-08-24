package com.example.stateImpl;

import com.example.Item;
import com.example.ItemShelf;
import com.example.VendingMachine;
import com.example.state.DispenseStateInf;

public class DispenseState implements DispenseStateInf {

    // private VendingMachine vendingMachine;
    // private int codeNumber;

    DispenseState() {
        System.out.println("Currently Vending Machine is on DispenseState");
    }

    DispenseState(VendingMachine vendingMachine, int codeNumber) {
        System.out.println("Currently Vending Machine is on DispenseState");
       // this.vendingMachine = vendingMachine;
        //this.codeNumber = codeNumber;
        dispenseProduct(vendingMachine, codeNumber);
    }

    @Override
    public Item dispenseProduct(VendingMachine vendingMachine, int codeNumber) {
        System.out.println("dispensing item from shelf no "+codeNumber);
        ItemShelf [] slots = vendingMachine.getInventory().getInventory();
        Item item=null;
        for(ItemShelf slot:slots){
            if(slot.getCode()==codeNumber){
                item= slot.getItem();
            }
        }
        updateInventory(vendingMachine, item, codeNumber);
        vendingMachine.setState(new IDLEState(vendingMachine));
        return item;
    }

    @Override
    public void updateInventory(VendingMachine vendingMachine, Item item, int codeNumber) {
         System.out.println("updating inventory for shelf no "+codeNumber);
         ItemShelf[] slots= vendingMachine.getInventory().getInventory();
         for(ItemShelf slot:slots){
            if(slot.getCode()==codeNumber){
                slot.setSoldOut(true);
            }
         }
    }

}
