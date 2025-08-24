package com.example;

public class ItemInventory {
    ItemShelf[] inventory;

   ItemInventory(int itemCount){
    inventory = new ItemShelf[itemCount];
    initialEmptyInventory();
   }


    public ItemShelf[] getInventory() {
        return inventory;
    }

    public void setInventory(ItemShelf[] inventory) {
        this.inventory = inventory;
    }


    void initialEmptyInventory() {
      int startCode=101;
      for(int i=0;i<inventory.length;i++){
        ItemShelf shelf = new ItemShelf();
        shelf.addItem(new Item());
        shelf.setSoldOut(true);
        shelf.setCode(startCode);
        inventory[i]=shelf;
        startCode++;
      }
   }

   void addItem(Item item,int codeNumber){
       for(int i=0;i<inventory.length;i++){
         ItemShelf shelf = inventory[i];
         if(shelf.getCode()==codeNumber){
            if(shelf.isSoldOut()){
                shelf.addItem(item);
                shelf.setSoldOut(false);
            }else{
                throw new RuntimeException("Item already present, you can not add item here"); 
            }
         }
       }
   }
}
