package com.firstidea.android.brokerx.enums;

/**
 * Created by Govind on 20-Nov-16.
 */

public enum ConnectionStatus {
    PENDING("P"),ACCEPTED("A"),REJECTED("R"),BLOCKED("B");

    private final String status;
    ConnectionStatus(String status){
        this.status = status;
    }

    public String getStatus() {
        return status;
    }
}
