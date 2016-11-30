package com.firstidea.android.brokerx.http.model;

import com.firstidea.android.brokerx.constants.MsgConstants;
import com.google.gson.Gson;

/**
 *
 * @author Govind
 */
public class MessageDTO {
    private String messageID;
    private String messageText;
    private Object data;
    public String getMessageID() {
        return messageID;
    }

    public void setMessageID(String messageID) {
        this.messageID = messageID;
    }

    public String getMessageText() {
        return messageText;
    }

    public void setMessageText(String messageText) {
        this.messageText = messageText;
    }

    public Object getData() {
        return data;
    }

    public User getUser() {
        Gson gson = new Gson();
        String jsonUser = gson.toJson(data);
        User user = gson.fromJson(jsonUser, User.class);
        return user;
    }

    public void setData(Object data) {
        this.data = data;
    }
    
    public boolean isSuccess() {
        return messageID.equals(MsgConstants.SUCCESS_ID);
    }
    
}
