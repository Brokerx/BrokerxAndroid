package com.firstidea.android.brokerx.http.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.Gson;

import java.util.Date;

/**
 * Created by Govind on 26-Dec-16.
 */

public class LeadStatusHistory implements Parcelable {
    private Integer leadID;
    private Integer currentStatus;
    private String dealDoneDateTime;
    private String goodsLiftedDateTime;
    private String invoiceNumber;
    private String documentsAttachedDateTime;
    private String paymentReceivedDateTime;
    private String documentsreceivedDateTime;
    private String dealClearedDateTime;

    public static final String KEY_STATUS_HISTORY = "LeadStatusHistory";

    public Integer getLeadID() {
        return leadID;
    }

    public void setLeadID(Integer leadID) {
        this.leadID = leadID;
    }

    public Integer getCurrentStatus() {
        return currentStatus;
    }

    public void setCurrentStatus(Integer currentStatus) {
        this.currentStatus = currentStatus;
    }

    public String getDealDoneDateTime() {
        return dealDoneDateTime;
    }

    public void setDealDoneDateTime(String dealDoneDateTime) {
        this.dealDoneDateTime = dealDoneDateTime;
    }

    public String getGoodsLiftedDateTime() {
        return goodsLiftedDateTime;
    }

    public void setGoodsLiftedDateTime(String goodsLiftedDateTime) {
        this.goodsLiftedDateTime = goodsLiftedDateTime;
    }

    public String getInvoiceNumber() {
        return invoiceNumber;
    }

    public void setInvoiceNumber(String invoiceNumber) {
        this.invoiceNumber = invoiceNumber;
    }

    public String getDocumentsAttachedDateTime() {
        return documentsAttachedDateTime;
    }

    public void setDocumentsAttachedDateTime(String documentsAttachedDateTime) {
        this.documentsAttachedDateTime = documentsAttachedDateTime;
    }

    public String getPaymentReceivedDateTime() {
        return paymentReceivedDateTime;
    }

    public void setPaymentReceivedDateTime(String paymentReceivedDateTime) {
        this.paymentReceivedDateTime = paymentReceivedDateTime;
    }

    public String getDocumentsreceivedDateTime() {
        return documentsreceivedDateTime;
    }

    public void setDocumentsreceivedDateTime(String documentsreceivedDateTime) {
        this.documentsreceivedDateTime = documentsreceivedDateTime;
    }

    public String getDealClearedDateTime() {
        return dealClearedDateTime;
    }

    public void setDealClearedDateTime(String dealClearedDateTime) {
        this.dealClearedDateTime = dealClearedDateTime;
    }


    public static LeadStatusHistory createFromJSON(Object object) {
        Gson gson = new Gson();
        String jsonString = gson.toJson(object);
        LeadStatusHistory lead = gson.fromJson(jsonString, LeadStatusHistory.class);
        return lead;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeValue(this.leadID);
        dest.writeValue(this.currentStatus);
        dest.writeString(this.dealDoneDateTime);
        dest.writeString(this.goodsLiftedDateTime);
        dest.writeString(this.invoiceNumber);
        dest.writeString(this.documentsAttachedDateTime);
        dest.writeString(this.paymentReceivedDateTime);
        dest.writeString(this.documentsreceivedDateTime);
        dest.writeString(this.dealClearedDateTime);
    }

    public LeadStatusHistory() {
    }

    protected LeadStatusHistory(Parcel in) {
        this.leadID = (Integer) in.readValue(Integer.class.getClassLoader());
        this.currentStatus = (Integer) in.readValue(Integer.class.getClassLoader());
        this.dealDoneDateTime = in.readString();
        this.goodsLiftedDateTime = in.readString();
        this.invoiceNumber = in.readString();
        this.documentsAttachedDateTime = in.readString();
        this.paymentReceivedDateTime = in.readString();
        this.documentsreceivedDateTime = in.readString();
        this.dealClearedDateTime = in.readString();
    }

    public static final Creator<LeadStatusHistory> CREATOR = new Creator<LeadStatusHistory>() {
        @Override
        public LeadStatusHistory createFromParcel(Parcel source) {
            return new LeadStatusHistory(source);
        }

        @Override
        public LeadStatusHistory[] newArray(int size) {
            return new LeadStatusHistory[size];
        }
    };
}
