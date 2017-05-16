package com.firstidea.android.brokerx.http.service;

import com.firstidea.android.brokerx.http.model.MessageDTO;

import retrofit.Callback;
import retrofit.http.Field;
import retrofit.http.FormUrlEncoded;
import retrofit.http.Multipart;
import retrofit.http.POST;
import retrofit.http.Part;
import retrofit.mime.TypedFile;

/**
 * Created by Govind on 23-Dec-15.
 */
public interface UserService {
    @FormUrlEncoded
    @POST("/user/getOTP")
    MessageDTO getOTP(@Field("mobileNumber") String mobileNumber);

    @FormUrlEncoded
    @POST("/user/appLogin")
    void verifyOTP(@Field("mobileNumber") String mobileNumber,
                   @Field("OTP") String password,
                   Callback<MessageDTO> callback);

    @FormUrlEncoded
    @POST("/user/appLogin")
    void appLogin(@Field("userName") String userName,
                  @Field("password") String password,
                  Callback<MessageDTO> callback);

    @Multipart
    @POST("/user/registerUser")
    void register(@Part("file") TypedFile file,
                  @Part("DataJSON") String advertisementJSON,
                  Callback<MessageDTO> callback);

    @FormUrlEncoded
    @POST("/user/updateGCMKey")
    void updateGCMKey(@Field("userID") Integer userID,
                      @Field("gcmKey") String gcmKey,
                      Callback<MessageDTO> callback);

    @FormUrlEncoded
    @POST("/user/sendConnectionRequest")
    void sendConnectionRequest(@Field("fromUserID") Integer fromUserID,
                               @Field("toUserID") Integer toUserID,
                               Callback<MessageDTO> callback);

    @FormUrlEncoded
    @POST("/user/updateBrokerDealsInItems")
    void addToupdateBrokerDealsInItemsFavourite(@Field("userID") Integer userID,
                                                @Field("brokerDealsInItems") String brokerDealsInItems,
                                                Callback<MessageDTO> callback);

    @FormUrlEncoded
    @POST("/user/changeConnectionStatus")
    void changeConnectionStatus(@Field("connectionID") Integer connectionID,
                                @Field("status") String status,
                                Callback<MessageDTO> callback);

    @FormUrlEncoded
    @POST("/user/getUserConnections")
    void getUserConnections(@Field("userID") Integer userID,
                            Callback<MessageDTO> callback);

    @FormUrlEncoded
    @POST("/user/getUserByMobile")
    void getUserByMobile(@Field("mobile") String mobile,
                         Callback<MessageDTO> callback);

    @FormUrlEncoded
    @POST("/user/getAnalysisDropDownValues")
    void getAnalysisDropDownValues(@Field("userID") String mobile,
                                   Callback<MessageDTO> callback);


}
