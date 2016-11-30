package com.firstidea.android.brokerx.model;

import java.io.Serializable;

/**
 * Created by user on 10/3/2016.
 */

public class TopBroketItem implements Serializable {
    private int id;
    private String basic_charge,total_charge,broker_name;

    public String getBroker_name() {
        return broker_name;
    }

    public void setBroker_name(String broker_name) {
        this.broker_name = broker_name;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getBasic_charge() {
        return basic_charge;
    }

    public void setBasic_charge(String basic_charge) {
        this.basic_charge = basic_charge;
    }

    public String getTotal_charge() {
        return total_charge;
    }

    public void setTotal_charge(String total_charge) {
        this.total_charge = total_charge;
    }


}
