package com.firstidea.android.brokerx.enums;

/**
 * Created by Govind on 25-Feb-17.
 */

public enum ExciseType {
    INCLUSIVE(1), EXCLUSIVE(2);

    Integer type;


    ExciseType(Integer type) {
        this.type = type;
    }
    public Integer getType() {
        return type;
    }
}
