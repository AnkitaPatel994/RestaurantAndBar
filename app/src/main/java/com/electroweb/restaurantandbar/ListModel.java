package com.electroweb.restaurantandbar;

class ListModel {

    String ItemId,ItemName,ItemImage,ItemCode,HSNCode,MenuRate,GSTRate,CatId,ItemIdName;

    public ListModel(String itemId, String itemName, String itemImage, String itemCode, String hsnCode, String menuRate, String gstRate, String catId, String itemIdName) {
        this.ItemId = itemId;
        this.ItemName = itemName;
        this.ItemImage = itemImage;
        this.ItemCode = itemCode;
        this.HSNCode = hsnCode;
        this.MenuRate = menuRate;
        this.GSTRate = gstRate;
        this.CatId = catId;
        this.ItemIdName = itemIdName;
    }

    public String getItemId() {
        return ItemId;
    }

    public void setItemId(String itemId) {
        ItemId = itemId;
    }

    public String getItemName() {
        return ItemName;
    }

    public void setItemName(String itemName) {
        ItemName = itemName;
    }

    public String getItemImage() {
        return ItemImage;
    }

    public void setItemImage(String itemImage) {
        ItemImage = itemImage;
    }

    public String getItemCode() {
        return ItemCode;
    }

    public void setItemCode(String itemCode) {
        ItemCode = itemCode;
    }

    public String getHSNCode() {
        return HSNCode;
    }

    public void setHSNCode(String HSNCode) {
        this.HSNCode = HSNCode;
    }

    public String getMenuRate() {
        return MenuRate;
    }

    public void setMenuRate(String menuRate) {
        MenuRate = menuRate;
    }

    public String getGSTRate() {
        return GSTRate;
    }

    public void setGSTRate(String GSTRate) {
        this.GSTRate = GSTRate;
    }

    public String getCatId() {
        return CatId;
    }

    public void setCatId(String CatId) {
        this.CatId = CatId;
    }

    public String getItemIdName() {
        return ItemIdName;
    }

    public void setItemIdName(String itemIdName) {
        ItemIdName = itemIdName;
    }
}
