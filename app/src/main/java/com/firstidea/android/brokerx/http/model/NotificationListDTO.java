package com.firstidea.android.brokerx.http.model;

import java.util.List;

/**
 * Created by Govind on 17-Feb-17.
 */

public class NotificationListDTO {
    private Integer leadID;
    private Integer fromUserID;
    private List<Notification> notifications;

    public Integer getLeadID() {
        return leadID;
    }

    public void setLeadID(Integer leadID) {
        this.leadID = leadID;
    }

    public Integer getFromUserID() {
        return fromUserID;
    }

    public void setFromUserID(Integer fromUserID) {
        this.fromUserID = fromUserID;
    }

    public List<Notification> getNotifications() {
        return notifications;
    }

    public void setNotifications(List<Notification> notifications) {
        this.notifications = notifications;
    }
}
