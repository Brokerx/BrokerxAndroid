package com.firstidea.android.brokerx.http.model;

import java.util.List;

/**
 * Created by Govind on 17-Feb-17.
 */

public class NotificationListDTO {
    private Integer leadID;
    private Integer fromUserID;
    private int unReadCount;
    private Long lastNotificationTimeStamp;
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

    public int getUnReadCount() {
        return unReadCount;
    }

    public void setUnReadCount(int unReadCount) {
        this.unReadCount = unReadCount;
    }

    public List<Notification> getNotifications() {
        return notifications;
    }

    public void setNotifications(List<Notification> notifications) {
        this.notifications = notifications;
    }

    public Long getLastNotificationTimeStamp() {
        return lastNotificationTimeStamp;
    }

    public void setLastNotificationTimeStamp(Long lastNotificationTimeStamp) {
        this.lastNotificationTimeStamp = lastNotificationTimeStamp;
    }
}
