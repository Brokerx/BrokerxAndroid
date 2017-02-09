package com.firstidea.android.brokerx.http.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;

/**
 * Created by Govind on 06-Feb-17.
 */

public class ChatSummary implements Parcelable {
    private Integer chatSummaryID;
    private Integer leadID;
    private String itemName;
    private Integer fromUserID;
    private String fromUserType;
    private Integer toUserID;
    private String toUserType;
    private String lastMsg;
    private String lastMsgType;
    private String lastMsgDateTime;
    private User toUser;

    public Integer getChatSummaryID() {
        return chatSummaryID;
    }

    public void setChatSummaryID(Integer chatSummaryID) {
        this.chatSummaryID = chatSummaryID;
    }

    public Integer getLeadID() {
        return leadID;
    }

    public void setLeadID(Integer leadID) {
        this.leadID = leadID;
    }

    public String getItemName() {
        return itemName;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }

    public Integer getFromUserID() {
        return fromUserID;
    }

    public void setFromUserID(Integer fromUserID) {
        this.fromUserID = fromUserID;
    }

    public String getFromUserType() {
        return fromUserType;
    }

    public void setFromUserType(String fromUserType) {
        this.fromUserType = fromUserType;
    }

    public Integer getToUserID() {
        return toUserID;
    }

    public void setToUserID(Integer toUserID) {
        this.toUserID = toUserID;
    }

    public String getToUserType() {
        return toUserType;
    }

    public void setToUserType(String toUserType) {
        this.toUserType = toUserType;
    }

    public String getLastMsg() {
        return lastMsg;
    }

    public void setLastMsg(String lastMsg) {
        this.lastMsg = lastMsg;
    }

    public String getLastMsgType() {
        return lastMsgType;
    }

    public void setLastMsgType(String lastMsgType) {
        this.lastMsgType = lastMsgType;
    }

    public String getLastMsgDateTime() {
        return lastMsgDateTime;
    }

    public void setLastMsgDateTime(String lastMsgDateTime) {
        this.lastMsgDateTime = lastMsgDateTime;
    }

    public User getToUser() {
        return toUser;
    }

    public void setToUser(User toUser) {
        this.toUser = toUser;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeValue(this.chatSummaryID);
        dest.writeValue(this.leadID);
        dest.writeString(this.itemName);
        dest.writeValue(this.fromUserID);
        dest.writeString(this.fromUserType);
        dest.writeValue(this.toUserID);
        dest.writeString(this.toUserType);
        dest.writeString(this.lastMsg);
        dest.writeString(this.lastMsgType);
        dest.writeString(this.lastMsgDateTime);
        dest.writeParcelable(this.toUser, flags);
    }

    public ChatSummary() {
    }

    protected ChatSummary(Parcel in) {
        this.chatSummaryID = (Integer) in.readValue(Integer.class.getClassLoader());
        this.leadID = (Integer) in.readValue(Integer.class.getClassLoader());
        this.itemName = in.readString();
        this.fromUserID = (Integer) in.readValue(Integer.class.getClassLoader());
        this.fromUserType = in.readString();
        this.toUserID = (Integer) in.readValue(Integer.class.getClassLoader());
        this.toUserType = in.readString();
        this.lastMsg = in.readString();
        this.lastMsgType = in.readString();
        this.lastMsgDateTime = in.readString();
        this.toUser = in.readParcelable(User.class.getClassLoader());
    }

    public static final Parcelable.Creator<ChatSummary> CREATOR = new Parcelable.Creator<ChatSummary>() {
        @Override
        public ChatSummary createFromParcel(Parcel source) {
            return new ChatSummary(source);
        }

        @Override
        public ChatSummary[] newArray(int size) {
            return new ChatSummary[size];
        }
    };


    public static ChatSummary createFromJSON(Object object) {
        Gson gson = new Gson();
        String jsonString = gson.toJson(object);
        ChatSummary chatSummary = gson.fromJson(jsonString, ChatSummary.class);
        return chatSummary;
    }

    public static ArrayList<ChatSummary> createListFromJson(Object data) {
        if(data == null) return new  ArrayList<ChatSummary> ();

        Gson gson = new Gson();
        String jsonString = gson.toJson(data);
        ArrayList<ChatSummary> chatSummaries = gson.fromJson(jsonString,  new TypeToken<ArrayList<ChatSummary>>() {}.getType());

        return chatSummaries;
    }

}
