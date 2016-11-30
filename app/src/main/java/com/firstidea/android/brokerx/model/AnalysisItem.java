package com.firstidea.android.brokerx.model;

import java.io.Serializable;

/**
 * Created by user on 9/28/2016.
 */

public class AnalysisItem implements Serializable{
    private String id;
    private String Chemical_Name;
    private String Broker_Name;
    private String getunits;
    private String Date;
    private String Avability;
    private  String Total_Charge;
    private String Total_BrokerCharage;
    private String BokerR,BrokerS;

    public String getBokerR() {
        return BokerR;
    }

    public void setBokerR(String bokerR) {
        BokerR = bokerR;
    }

    public String getTotal_BrokerCharage() {
        return Total_BrokerCharage;
    }

    public void setTotal_BrokerCharage(String total_BrokerCharage) {
        Total_BrokerCharage = total_BrokerCharage;
    }

    public String getBrokerS() {
        return BrokerS;
    }

    public void setBrokerS(String brokerS) {
        BrokerS = brokerS;
    }

    public String getTotal_Charge() {
        return Total_Charge;
    }

    public void setTotal_Charge(String total_Charge) {
        Total_Charge = total_Charge;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getChemical_Name() {
        return Chemical_Name;
    }

    public void setChemical_Name(String chemical_Name) {
        Chemical_Name = chemical_Name;
    }

    public String getBroker_Name() {
        return Broker_Name;
    }

    public void setBroker_Name(String broker_Name) {
        Broker_Name = broker_Name;
    }

    public String getGetunits() {
        return getunits;
    }

    public void setGetunits(String getunits) {
        this.getunits = getunits;
    }

    public String getDate() {
        return Date;
    }

    public void setDate(String date) {
        Date = date;
    }

    public String getAvability() {
        return Avability;
    }

    public void setAvability(String avability) {
        Avability = avability;
    }
}
