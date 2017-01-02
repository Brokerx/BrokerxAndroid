package com.firstidea.android.brokerx.enums;

/**
 * Created by Govind on 14-Dec-16.
 */

public enum LeadCurrentStatus {

    Accepted("A"), Rejected("X"), Reverted("R"), Pending("P"),Done("D"), Waiting("W"), Deleted("L");

    final String status;

    private LeadCurrentStatus(String status) {
        this.status = status;
    }

    public String getStatus() {
        return status;
    }
}
