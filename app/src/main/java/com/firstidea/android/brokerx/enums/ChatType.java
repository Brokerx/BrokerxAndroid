package com.firstidea.android.brokerx.enums;

/**
 * Created by Govind on 19-Jan-17.
 */

public enum ChatType {
    TEXT("txt"), IMAGE("img"), FILE("file");

    String chatType;

    public static String KEY_CHAT_TYPE = "ChatType";

    ChatType(String chatType) {
        this.chatType = chatType;
    }
    public String getType() {
        return chatType;
    }
}
