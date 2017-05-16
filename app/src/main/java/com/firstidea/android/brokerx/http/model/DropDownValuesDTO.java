package com.firstidea.android.brokerx.http.model;

import com.google.gson.Gson;

import java.util.List;

/**
 * Created by Govind on 15-May-17.
 */

public class DropDownValuesDTO {

    private List<String> itemsDealWith;
    private List<User> buyers;
    private List<User> sellers;

    public List<String> getItemsDealWith() {
        return itemsDealWith;
    }

    public void setItemsDealWith(List<String> itemsDealWith) {
        this.itemsDealWith = itemsDealWith;
    }

    public List<User> getBuyers() {
        return buyers;
    }

    public void setBuyers(List<User> buyers) {
        this.buyers = buyers;
    }

    public List<User> getSellers() {
        return sellers;
    }

    public void setSellers(List<User> sellers) {
        this.sellers = sellers;
    }


    public static DropDownValuesDTO createFromJSON(Object object) {
        Gson gson = new Gson();
        String jsonString = gson.toJson(object);
        DropDownValuesDTO dropDownValuesDTO = gson.fromJson(jsonString, DropDownValuesDTO.class);
        return dropDownValuesDTO;
    }

}
