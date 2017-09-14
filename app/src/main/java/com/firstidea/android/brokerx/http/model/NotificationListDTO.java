package com.firstidea.android.brokerx.http.model;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.List;

/**
 * Created by Govind on 17-Feb-17.
 */

public class NotificationListDTO implements Parcelable {
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


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeValue(this.leadID);
        dest.writeValue(this.fromUserID);
        dest.writeInt(this.unReadCount);
        dest.writeValue(this.lastNotificationTimeStamp);
        dest.writeTypedList(this.notifications);
    }

    public NotificationListDTO() {
    }

    protected NotificationListDTO(Parcel in) {
        this.leadID = (Integer) in.readValue(Integer.class.getClassLoader());
        this.fromUserID = (Integer) in.readValue(Integer.class.getClassLoader());
        this.unReadCount = in.readInt();
        this.lastNotificationTimeStamp = (Long) in.readValue(Long.class.getClassLoader());
        this.notifications = in.createTypedArrayList(Notification.CREATOR);
    }

    public static final Parcelable.Creator<NotificationListDTO> CREATOR = new Parcelable.Creator<NotificationListDTO>() {
        @Override
        public NotificationListDTO createFromParcel(Parcel source) {
            return new NotificationListDTO(source);
        }

        @Override
        public NotificationListDTO[] newArray(int size) {
            return new NotificationListDTO[size];
        }
    };
}
