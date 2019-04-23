package com.kredivation.switchland.model;

public class DrawerItem {
    String ItemName;
    int imgResID;
    int id;
    int selectedItem=1;

    public DrawerItem(String itemName, int imgResID, int id) {
        super();
        ItemName = itemName;
        this.imgResID = imgResID;
        this.id = id;
    }

    public String getItemName() {
        return ItemName;
    }

    public void setItemName(String itemName) {
        ItemName = itemName;
    }

    public int getImgResID() {
        return imgResID;
    }

    public void setImgResID(int imgResID) {
        this.imgResID = imgResID;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getSelectedItem() {
        return selectedItem;
    }

    public void setSelectedItem(int selectedItem) {
        this.selectedItem = selectedItem;
    }
}
