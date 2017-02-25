package com.firstidea.android.brokerx.http.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;

/**
 * Created by Govind on 15-Nov-16.
 */

public class Lead implements Parcelable {

    public static final String KEY_LEAD = "Lead";
    public static final String KEY_LEAD_ID = "LeadID";

    private Integer leadID;
    private int createdUserID;
    private Integer brokerID;
    private Integer assignedToUserID;
    private Integer parentLeadID;
    private Integer itemID;
    private String itemName;
    private String type;
    private String buyerStatus;
    private String sellerStatus;
    private String brokerStatus;
    private String make;
    private float qty;
    private Integer qtyUnit;
    private Integer packing;
    private String packingType;
    private String location;
    private float basicPrice;
    private Integer basicPriceUnit;
    private float exciseDuty;
    private Integer exciseUnit;
    private Integer excisetype;
    private float tax;
    private boolean asPerAvailablity;
    private float transportCharges;
    private float miscCharges;
    private float brokerageAmt;
    private float buyerBrokerage;
    private float sellerBrokerage;
    private String againstForm;
    private String creditPeriod;
    private String freeStoragePeriod;
    private String preferredSellerName;
    private String comments;
    private String createdDttm;
    private Integer LastUpdUserID;
    private String lastUpdDateTime;
    User createdUser;
    User broker;
    User assignedToUser;
    private String fieldsAltered;
    private Boolean isMoveToPending;

    public Integer getLeadID() {
        return leadID;
    }

    public void setLeadID(Integer leadID) {
        this.leadID = leadID;
    }

    public int getCreatedUserID() {
        return createdUserID;
    }

    public void setCreatedUserID(int createdUserID) {
        this.createdUserID = createdUserID;
    }

    public Integer getBrokerID() {
        return brokerID;
    }

    public void setBrokerID(Integer brokerID) {
        this.brokerID = brokerID;
    }

    public Integer getAssignedToUserID() {
        return assignedToUserID;
    }

    public void setAssignedToUserID(Integer assignedToUserID) {
        this.assignedToUserID = assignedToUserID;
    }

    public Integer getParentLeadID() {
        return parentLeadID;
    }

    public void setParentLeadID(Integer parentLeadID) {
        this.parentLeadID = parentLeadID;
    }

    public Integer getItemID() {
        return itemID;
    }

    public void setItemID(Integer itemID) {
        this.itemID = itemID;
    }

    public String getItemName() {
        return itemName;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getBuyerStatus() {
        return buyerStatus;
    }

    public void setBuyerStatus(String buyerStatus) {
        this.buyerStatus = buyerStatus;
    }

    public String getSellerStatus() {
        return sellerStatus;
    }

    public void setSellerStatus(String sellerStatus) {
        this.sellerStatus = sellerStatus;
    }

    public String getBrokerStatus() {
        return brokerStatus;
    }

    public void setBrokerStatus(String brokerStatus) {
        this.brokerStatus = brokerStatus;
    }

    public String getMake() {
        return make;
    }

    public void setMake(String make) {
        this.make = make;
    }

    public float getQty() {
        return qty;
    }

    public void setQty(float qty) {
        this.qty = qty;
    }

    public Integer getQtyUnit() {
        return qtyUnit;
    }

    public void setQtyUnit(Integer qtyUnit) {
        this.qtyUnit = qtyUnit;
    }

    public Integer getPacking() {
        return packing;
    }

    public void setPacking(Integer packing) {
        this.packing = packing;
    }

    public String getPackingType() {
        return packingType;
    }

    public void setPackingType(String packingType) {
        this.packingType = packingType;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public float getBasicPrice() {
        return basicPrice;
    }

    public void setBasicPrice(float basicPrice) {
        this.basicPrice = basicPrice;
    }

    public float getExciseDuty() {
        return exciseDuty;
    }

    public void setExciseDuty(float exciseDuty) {
        this.exciseDuty = exciseDuty;
    }

    public Integer getBasicPriceUnit() {
        return basicPriceUnit;
    }

    public void setBasicPriceUnit(Integer basicPriceUnit) {
        this.basicPriceUnit = basicPriceUnit;
    }

    public Integer getExciseUnit() {
        return exciseUnit;
    }

    public void setExciseUnit(Integer exciseUnit) {
        this.exciseUnit = exciseUnit;
    }

    public Integer getExcisetype() {
        return excisetype;
    }

    public void setExcisetype(Integer excisetype) {
        this.excisetype = excisetype;
    }

    public float getTax() {
        return tax;
    }

    public void setTax(float tax) {
        this.tax = tax;
    }

    public boolean isAsPerAvailablity() {
        return asPerAvailablity;
    }

    public void setAsPerAvailablity(boolean asPerAvailablity) {
        this.asPerAvailablity = asPerAvailablity;
    }

    public float getTransportCharges() {
        return transportCharges;
    }

    public void setTransportCharges(float transportCharges) {
        this.transportCharges = transportCharges;
    }

    public float getMiscCharges() {
        return miscCharges;
    }

    public void setMiscCharges(float miscCharges) {
        this.miscCharges = miscCharges;
    }

    public float getBrokerageAmt() {
        return brokerageAmt;
    }

    public void setBrokerageAmt(float brokerageAmt) {
        this.brokerageAmt = brokerageAmt;
    }

    public float getBuyerBrokerage() {
        return buyerBrokerage;
    }

    public void setBuyerBrokerage(float buyerBrokerage) {
        this.buyerBrokerage = buyerBrokerage;
    }

    public float getSellerBrokerage() {
        return sellerBrokerage;
    }

    public void setSellerBrokerage(float sellerBrokerage) {
        this.sellerBrokerage = sellerBrokerage;
    }

    public String getAgainstForm() {
        return againstForm;
    }

    public void setAgainstForm(String againstForm) {
        this.againstForm = againstForm;
    }

    public String getCreditPeriod() {
        return creditPeriod;
    }

    public void setCreditPeriod(String creditPeriod) {
        this.creditPeriod = creditPeriod;
    }

    public String getFreeStoragePeriod() {
        return freeStoragePeriod;
    }

    public void setFreeStoragePeriod(String freeStoragePeriod) {
        this.freeStoragePeriod = freeStoragePeriod;
    }

    public String getPreferredSellerName() {
        return preferredSellerName;
    }

    public void setPreferredSellerName(String preferredSellerName) {
        this.preferredSellerName = preferredSellerName;
    }

    public String getComments() {
        return comments;
    }

    public void setComments(String comments) {
        this.comments = comments;
    }

    public String getCreatedDttm() {
        return createdDttm;
    }

    public void setCreatedDttm(String createdDttm) {
        this.createdDttm = createdDttm;
    }

    public Integer getLastUpdUserID() {
        return LastUpdUserID;
    }

    public void setLastUpdUserID(Integer lastUpdUserID) {
        LastUpdUserID = lastUpdUserID;
    }

    public String getLastUpdDateTime() {
        return lastUpdDateTime;
    }

    public void setLastUpdDateTime(String lastUpdDateTime) {
        this.lastUpdDateTime = lastUpdDateTime;
    }

    public User getCreatedUser() {
        return createdUser;
    }

    public void setCreatedUser(User createdUser) {
        this.createdUser = createdUser;
    }

    public User getBroker() {
        return broker;
    }

    public void setBroker(User broker) {
        this.broker = broker;
    }

    public User getAssignedToUser() {
        return assignedToUser;
    }

    public void setAssignedToUser(User assignedToUser) {
        this.assignedToUser = assignedToUser;
    }

    public String getFieldsAltered() {
        return fieldsAltered;
    }

    public void setFieldsAltered(String fieldsAltered) {
        this.fieldsAltered = fieldsAltered;
    }

    public Boolean getMoveToPending() {
        return isMoveToPending;
    }

    public void setMoveToPending(Boolean moveToPending) {
        isMoveToPending = moveToPending;
    }

    public Lead() {
    }


    public static Lead createFromJSON(Object object) {
        Gson gson = new Gson();
        String jsonString = gson.toJson(object);
        Lead lead = gson.fromJson(jsonString, Lead.class);
        return lead;
    }

    public static ArrayList<Lead> createListFromJson(Object data) {
        if(data == null) return new  ArrayList<Lead> ();

        Gson gson = new Gson();
        String jsonString = gson.toJson(data);
        ArrayList<Lead> leads = gson.fromJson(jsonString,  new TypeToken<ArrayList<Lead>>() {}.getType());

        return leads;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeValue(this.leadID);
        dest.writeInt(this.createdUserID);
        dest.writeValue(this.brokerID);
        dest.writeValue(this.assignedToUserID);
        dest.writeValue(this.parentLeadID);
        dest.writeValue(this.itemID);
        dest.writeString(this.itemName);
        dest.writeString(this.type);
        dest.writeString(this.buyerStatus);
        dest.writeString(this.sellerStatus);
        dest.writeString(this.brokerStatus);
        dest.writeString(this.make);
        dest.writeFloat(this.qty);
        dest.writeValue(this.qtyUnit);
        dest.writeValue(this.packing);
        dest.writeString(this.packingType);
        dest.writeString(this.location);
        dest.writeFloat(this.basicPrice);
        dest.writeValue(this.basicPriceUnit);
        dest.writeFloat(this.exciseDuty);
        dest.writeValue(this.exciseUnit);
        dest.writeValue(this.excisetype);
        dest.writeFloat(this.tax);
        dest.writeByte(this.asPerAvailablity ? (byte) 1 : (byte) 0);
        dest.writeFloat(this.transportCharges);
        dest.writeFloat(this.miscCharges);
        dest.writeFloat(this.brokerageAmt);
        dest.writeFloat(this.buyerBrokerage);
        dest.writeFloat(this.sellerBrokerage);
        dest.writeString(this.againstForm);
        dest.writeString(this.creditPeriod);
        dest.writeString(this.freeStoragePeriod);
        dest.writeString(this.preferredSellerName);
        dest.writeString(this.comments);
        dest.writeString(this.createdDttm);
        dest.writeValue(this.LastUpdUserID);
        dest.writeString(this.lastUpdDateTime);
        dest.writeParcelable(this.createdUser, flags);
        dest.writeParcelable(this.broker, flags);
        dest.writeParcelable(this.assignedToUser, flags);
        dest.writeString(this.fieldsAltered);
        dest.writeValue(this.isMoveToPending);
    }

    protected Lead(Parcel in) {
        this.leadID = (Integer) in.readValue(Integer.class.getClassLoader());
        this.createdUserID = in.readInt();
        this.brokerID = (Integer) in.readValue(Integer.class.getClassLoader());
        this.assignedToUserID = (Integer) in.readValue(Integer.class.getClassLoader());
        this.parentLeadID = (Integer) in.readValue(Integer.class.getClassLoader());
        this.itemID = (Integer) in.readValue(Integer.class.getClassLoader());
        this.itemName = in.readString();
        this.type = in.readString();
        this.buyerStatus = in.readString();
        this.sellerStatus = in.readString();
        this.brokerStatus = in.readString();
        this.make = in.readString();
        this.qty = in.readFloat();
        this.qtyUnit = (Integer) in.readValue(Integer.class.getClassLoader());
        this.packing = (Integer) in.readValue(Integer.class.getClassLoader());
        this.packingType = in.readString();
        this.location = in.readString();
        this.basicPrice = in.readFloat();
        this.basicPriceUnit = (Integer) in.readValue(Integer.class.getClassLoader());
        this.exciseDuty = in.readFloat();
        this.exciseUnit = (Integer) in.readValue(Integer.class.getClassLoader());
        this.excisetype = (Integer) in.readValue(Integer.class.getClassLoader());
        this.tax = in.readFloat();
        this.asPerAvailablity = in.readByte() != 0;
        this.transportCharges = in.readFloat();
        this.miscCharges = in.readFloat();
        this.brokerageAmt = in.readFloat();
        this.buyerBrokerage = in.readFloat();
        this.sellerBrokerage = in.readFloat();
        this.againstForm = in.readString();
        this.creditPeriod = in.readString();
        this.freeStoragePeriod = in.readString();
        this.preferredSellerName = in.readString();
        this.comments = in.readString();
        this.createdDttm = in.readString();
        this.LastUpdUserID = (Integer) in.readValue(Integer.class.getClassLoader());
        this.lastUpdDateTime = in.readString();
        this.createdUser = in.readParcelable(User.class.getClassLoader());
        this.broker = in.readParcelable(User.class.getClassLoader());
        this.assignedToUser = in.readParcelable(User.class.getClassLoader());
        this.fieldsAltered = in.readString();
        this.isMoveToPending = (Boolean) in.readValue(Boolean.class.getClassLoader());
    }

    public static final Creator<Lead> CREATOR = new Creator<Lead>() {
        @Override
        public Lead createFromParcel(Parcel source) {
            return new Lead(source);
        }

        @Override
        public Lead[] newArray(int size) {
            return new Lead[size];
        }
    };
}
