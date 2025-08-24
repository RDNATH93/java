package com.example;

public class ItemShelf {
    private Item item;
    private int code;
    private boolean soldOut;

    public Item getItem() {
        return item;
    }

    public void addItem(Item item) {
        this.item = item;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public boolean isSoldOut() {
        return soldOut;
    }

    public void setSoldOut(boolean isOutOfStock) {
        this.soldOut = isOutOfStock;
    }

}
