package com.firstidea.android.brokerx.model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Govind on 02-Sep-16.
 */
public class Enquiry {
    /*implements
} Parcelable{
    String leadID;
    private String itemName;
    private String userName;
    private String location;
    private String brokerageAmt;
    private String qty;


    public String getItemName() {
        return itemName;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getBrokerageAmt() {
        return brokerageAmt;
    }

    public void setBrokerageAmt(String brokerageAmt) {
        this.brokerageAmt = brokerageAmt;
    }

    public String getQty() {
        return qty;
    }

    public void setQty(String qty) {
        this.qty = qty;
    }

    public String getLeadID() {
        return leadID;
    }

    public void setLeadID(String leadID) {
        this.leadID = leadID;
    }

    public Enquiry(String id, String title, String userName, String address, String brokerage, String qty) {
        this.leadID = id;
        this.itemName = title;
        this.userName = userName;
        this.location = address;
        this.brokerageAmt = brokerage;
        this.qty = qty;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.leadID);
        dest.writeString(this.itemName);
        dest.writeString(this.userName);
        dest.writeString(this.location);
        dest.writeString(this.brokerageAmt);
        dest.writeString(this.qty);
    }

    public Enquiry() {
    }

    private Enquiry(Parcel in) {
        this.leadID = in.readString();
        this.itemName = in.readString();
        this.userName = in.readString();
        this.location = in.readString();
        this.brokerageAmt = in.readString();
        this.qty = in.readString();
    }

    public static final Creator<Enquiry> CREATOR = new Creator<Enquiry>() {
        public Enquiry createFromParcel(Parcel source) {
            return new Enquiry(source);
        }

        public Enquiry[] newArray(int size) {
            return new Enquiry[size];
        }
    };*/
}
