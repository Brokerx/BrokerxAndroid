package com.firstidea.android.brokerx.http.service;

import com.firstidea.android.brokerx.http.model.MessageDTO;

import retrofit.Callback;
import retrofit.http.Field;
import retrofit.http.FormUrlEncoded;
import retrofit.http.POST;

/**
 * Created by Govind on 19-Jan-17.
 */

public interface ChatService {
    @FormUrlEncoded
    @POST("/chat/getChats")
    void getChats(@Field("fromUserID") Integer fromUserID,
                  @Field("toUserID") Integer toUserID,
                  @Field("leadID") Integer leadID,
                  Callback<MessageDTO> callback);

    @FormUrlEncoded
    @POST("/chat/sendMsg")
    void sendMsg(@Field("fromUserID") Integer fromUserID,
                 @Field("fromUserName") String fromUserName,
                 @Field("toUserID") Integer toUserID,
                 @Field("advertisementID") Integer advertisementID,
                 @Field("type") String type,
                 @Field("message") String message,
                 @Field("fromUserType") String fromUserType,
                 @Field("itemName") String itemName,
                 Callback<MessageDTO> callback);
}
