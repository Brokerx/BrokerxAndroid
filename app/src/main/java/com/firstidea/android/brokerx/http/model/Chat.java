package com.firstidea.android.brokerx.http.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;

/**
 * Created by Govind on 19-Jan-17.
 */

public class Chat implements Parcelable {
    private Integer chatID;
    private Integer fromUserID;
    private Integer toUserID;
    private Integer leadID;
    private String message;
    private String type;
    private String createdDttm;
    private String fromUserName;
    private String fromUserPhoto;
    private String fromUserType;
    private String itemName;

    public Integer getChatID() {
        return chatID;
    }

    public void setChatID(Integer chatID) {
        this.chatID = chatID;
    }

    public Integer getFromUserID() {
        return fromUserID;
    }

    public void setFromUserID(Integer fromUserID) {
        this.fromUserID = fromUserID;
    }

    public Integer getToUserID() {
        return toUserID;
    }

    public void setToUserID(Integer toUserID) {
        this.toUserID = toUserID;
    }

    public Integer getLeadID() {
        return leadID;
    }

    public void setLeadID(Integer leadID) {
        this.leadID = leadID;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getCreatedDttm() {
        return createdDttm;
    }

    public void setCreatedDttm(String createdDttm) {
        this.createdDttm = createdDttm;
    }

    public String getFromUserName() {
        return fromUserName;
    }

    public void setFromUserName(String fromUserName) {
        this.fromUserName = fromUserName;
    }

    public String getFromUserPhoto() {
        return fromUserPhoto;
    }

    public void setFromUserPhoto(String fromUserPhoto) {
        this.fromUserPhoto = fromUserPhoto;
    }

    public String getFromUserType() {
        return fromUserType;
    }

    public void setFromUserType(String fromUserType) {
        this.fromUserType = fromUserType;
    }

    public String getItemName() {
        return itemName;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }

    public static Chat createFromJSON(Object object) {
        Gson gson = new Gson();
        String jsonString = gson.toJson(object);
        Chat chat = gson.fromJson(jsonString, Chat.class);
        return chat;
    }

    public static ArrayList<Chat> createListFromJson(Object data) {
        if(data == null) return new  ArrayList<Chat> ();

        Gson gson = new Gson();
        String jsonString = gson.toJson(data);
        ArrayList<Chat> chats = gson.fromJson(jsonString,  new TypeToken<ArrayList<Chat>>() {}.getType());

        return chats;
    }

    public Chat() {
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeValue(this.chatID);
        dest.writeValue(this.fromUserID);
        dest.writeValue(this.toUserID);
        dest.writeValue(this.leadID);
        dest.writeString(this.message);
        dest.writeString(this.type);
        dest.writeString(this.createdDttm);
        dest.writeString(this.fromUserName);
        dest.writeString(this.fromUserPhoto);
        dest.writeString(this.fromUserType);
        dest.writeString(this.itemName);
    }

    protected Chat(Parcel in) {
        this.chatID = (Integer) in.readValue(Integer.class.getClassLoader());
        this.fromUserID = (Integer) in.readValue(Integer.class.getClassLoader());
        this.toUserID = (Integer) in.readValue(Integer.class.getClassLoader());
        this.leadID = (Integer) in.readValue(Integer.class.getClassLoader());
        this.message = in.readString();
        this.type = in.readString();
        this.createdDttm = in.readString();
        this.fromUserName = in.readString();
        this.fromUserPhoto = in.readString();
        this.fromUserType = in.readString();
        this.itemName = in.readString();
    }

    public static final Creator<Chat> CREATOR = new Creator<Chat>() {
        @Override
        public Chat createFromParcel(Parcel source) {
            return new Chat(source);
        }

        @Override
        public Chat[] newArray(int size) {
            return new Chat[size];
        }
    };
}
