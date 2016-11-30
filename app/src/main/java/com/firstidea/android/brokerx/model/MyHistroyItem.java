package com.firstidea.android.brokerx.model;

import java.io.Serializable;

/**
 * Created by user on 9/22/2016.
 */

public class MyHistroyItem implements Serializable {
    private String id;
    private String Chemical_Name;
    private String InvoiceNo;
    private String Brokerage;
    private String Date;
    private String basic_charge;
    private String seller_brokerage;
    private String buyer_brokerage;

    public String getBasic_charge() {
        return basic_charge;
    }

    public void setBasic_charge(String basic_charge) {
        this.basic_charge = basic_charge;
    }

    public String getSeller_brokerage() {
        return seller_brokerage;
    }

    public void setSeller_brokerage(String seller_brokerage) {
        this.seller_brokerage = seller_brokerage;
    }

    public String getBuyer_brokerage() {
        return buyer_brokerage;
    }

    public void setBuyer_brokerage(String buyer_brokerage) {
        this.buyer_brokerage = buyer_brokerage;
    }

    public String getTotal_brokerage() {
        return total_brokerage;
    }

    public void setTotal_brokerage(String total_brokerage) {
        this.total_brokerage = total_brokerage;
    }

    private String total_brokerage;

    public String getChemical_Name() {
        return Chemical_Name;
    }

    public void setChemical_Name(String chemical_Name) {
        Chemical_Name = chemical_Name;
    }

    public String getDate() {

        return Date;
    }

    public void setDate(String date) {
        Date = date;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }



    public String getInvoiceNo() {
        return InvoiceNo;
    }

    public void setInvoiceNo(String invoiceNo) {
        InvoiceNo = invoiceNo;
    }

    public String getBrokerage() {
        return Brokerage;
    }

    public void setBrokerage(String brokerage) {
        Brokerage = brokerage;
    }
}
