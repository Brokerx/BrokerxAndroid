package com.firstidea.android.brokerx.enums;

/**
 * Created by Govind on 22-Nov-16.
 */

public enum LeadType {
    BUYER("B"), SELLER("S");

    String leadType;

    LeadType(String leadType) {
        this.leadType = leadType;
    }
    public String getType() {
        return leadType;
    }
}
