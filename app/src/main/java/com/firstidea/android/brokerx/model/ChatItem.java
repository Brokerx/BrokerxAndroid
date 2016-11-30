package com.firstidea.android.brokerx.model;

/**
 * Created by user on 10/20/2016.
 */

public class ChatItem {
    private int id;
    private String Enquiry,sellername,buyername,sellertime,buyertime,sellerdeal,notify1,buyerDeal;

    public String getBuyerDeal() {
        return buyerDeal;
    }

    public void setBuyerDeal(String buyerDeal) {
        this.buyerDeal = buyerDeal;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getEnquiry() {
        return Enquiry;
    }

    public void setEnquiry(String enquiry) {
        Enquiry = enquiry;
    }

    public String getSellername() {
        return sellername;
    }

    public void setSellername(String sellername) {
        this.sellername = sellername;
    }

    public String getBuyername() {
        return buyername;
    }

    public void setBuyername(String buyername) {
        this.buyername = buyername;
    }

    public String getSellertime() {
        return sellertime;
    }

    public void setSellertime(String sellertime) {
        this.sellertime = sellertime;
    }

    public String getBuyertime() {
        return buyertime;
    }

    public void setBuyertime(String buyertime) {
        this.buyertime = buyertime;
    }

    public String getSellerdeal() {
        return sellerdeal;
    }

    public void setSellerdeal(String sellerdeal) {
        this.sellerdeal = sellerdeal;
    }

    public String getNotify1() {
        return notify1;
    }

    public void setNotify1(String notify1) {
        this.notify1 = notify1;
    }


}
