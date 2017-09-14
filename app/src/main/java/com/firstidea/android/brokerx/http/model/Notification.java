package com.firstidea.android.brokerx.http.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;

/**
 * Created by Govind on 22-Jan-17.
 */

public class Notification implements Parcelable {
    private Integer notificationID;
    private Integer toUserID;
    private Integer fromUserID;
    private Integer leadID;
    private String type;
    private String itemName;
    private String message;
    private Boolean isRead;
    private String createdDttm;
    private String category;
    private User fromUser;

    public Integer getNotificationID() {
        return notificationID;
    }

    public void setNotificationID(Integer notificationID) {
        this.notificationID = notificationID;
    }

    public Integer getToUserID() {
        return toUserID;
    }

    public void setToUserID(Integer toUserID) {
        this.toUserID = toUserID;
    }

    public Integer getFromUserID() {
        return fromUserID;
    }

    public void setFromUserID(Integer fromUserID) {
        this.fromUserID = fromUserID;
    }

    public Integer getLeadID() {
        return leadID;
    }

    public void setLeadID(Integer leadID) {
        this.leadID = leadID;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getItemName() {
        return itemName;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Boolean getRead() {
        return isRead;
    }

    public void setRead(Boolean read) {
        isRead = read;
    }

    public String getCreatedDttm() {
        return createdDttm;
    }

    public void setCreatedDttm(String createdDttm) {
        this.createdDttm = createdDttm;
    }

    public User getFromUser() {
        return fromUser;
    }

    public void setFromUser(User fromUser) {
        this.fromUser = fromUser;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public Notification() {
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeValue(this.notificationID);
        dest.writeValue(this.toUserID);
        dest.writeValue(this.fromUserID);
        dest.writeValue(this.leadID);
        dest.writeString(this.type);
        dest.writeString(this.category);
        dest.writeString(this.itemName);
        dest.writeString(this.message);
        dest.writeValue(this.isRead);
        dest.writeString(this.createdDttm);
        dest.writeParcelable(this.fromUser, flags);
    }

    protected Notification(Parcel in) {
        this.notificationID = (Integer) in.readValue(Integer.class.getClassLoader());
        this.toUserID = (Integer) in.readValue(Integer.class.getClassLoader());
        this.fromUserID = (Integer) in.readValue(Integer.class.getClassLoader());
        this.leadID = (Integer) in.readValue(Integer.class.getClassLoader());
        this.type = in.readString();
        this.category = in.readString();
        this.itemName = in.readString();
        this.message = in.readString();
        this.isRead = (Boolean) in.readValue(Boolean.class.getClassLoader());
        this.createdDttm = in.readString();
        this.fromUser = in.readParcelable(User.class.getClassLoader());
    }

    public static final Creator<Notification> CREATOR = new Creator<Notification>() {
        @Override
        public Notification createFromParcel(Parcel source) {
            return new Notification(source);
        }

        @Override
        public Notification[] newArray(int size) {
            return new Notification[size];
        }
    };


    public static Notification createFromJSON(Object object) {
        Gson gson = new Gson();
        String jsonString = gson.toJson(object);
        Notification notification = gson.fromJson(jsonString, Notification.class);
        return notification;
    }

    public static ArrayList<Notification> createListFromJson(Object data) {
        if(data == null) return new  ArrayList<Notification> ();

        Gson gson = new Gson();
        String jsonString = gson.toJson(data);
        ArrayList<Notification> notifications = gson.fromJson(jsonString,  new TypeToken<ArrayList<Notification>>() {}.getType());

        return notifications;
    }
}
