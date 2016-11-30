package com.firstidea.android.brokerx.model;

import java.io.Serializable;

/**
 * Created by user on 9/20/2016.
 */
public class PendingEntries implements Serializable {

    public int id;
    public String ChemicalName,PostedName,Address,GetPercentage, Availability;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getChemicalName() {
        return ChemicalName;
    }

    public void setChemicalName(String chemicalName) {
        ChemicalName = chemicalName;
    }

    public String getPostedName() {
        return PostedName;
    }

    public void setPostedName(String postedName) {
        PostedName = postedName;
    }

    public String getAddress() {
        return Address;
    }

    public void setAddress(String address) {
        Address = address;
    }

    public String getGetPercentage() {
        return GetPercentage;
    }

    public void setGetPercentage(String getPercentage) {
        GetPercentage = getPercentage;
    }

    public String getAvailability() {
        return Availability;
    }

    public void setAvailability(String availability) {
        Availability = availability;
    }
}
