package com.firstidea.android.brokerx.http.model;

import java.util.List;

/**
 * Created by Govind on 06-Feb-17.
 */

public class ChatListDTO {
    private String itemName;
    private Integer leadID;
    private List<ChatSummary> chatSummaryList;

    public String getItemName() {
        return itemName;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }

    public Integer getLeadID() {
        return leadID;
    }

    public void setLeadID(Integer leadID) {
        this.leadID = leadID;
    }

    public List<ChatSummary> getChatSummaryList() {
        return chatSummaryList;
    }

    public void setChatSummaryList(List<ChatSummary> chatSummaryList) {
        this.chatSummaryList = chatSummaryList;
    }
}
