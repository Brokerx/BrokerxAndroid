package com.firstidea.android.brokerx.http.service;

import com.firstidea.android.brokerx.http.model.Lead;
import com.firstidea.android.brokerx.http.model.MessageDTO;

import retrofit.Callback;
import retrofit.http.Body;
import retrofit.http.Field;
import retrofit.http.FormUrlEncoded;
import retrofit.http.POST;

/**
 * Created by Govind on 16-Nov-16.
 */

public interface LeadService {

    @POST("/lead/saveLead")
    void saveLead(@Body Lead lead,
                           Callback<MessageDTO> callback);

    @FormUrlEncoded
    @POST("/lead/getLeads")
    void getLeads(@Field("userID") Integer userID,
                   @Field("type")String type,
                   @Field("status")String status,
                   @Field("startDate")String startDate,
                   @Field("endDate")String endDate,
                   Callback<MessageDTO> callback);

    @FormUrlEncoded
    @POST("/lead/getBrokerLeads")
    void getBrokerLeads(@Field("userID") Integer userID,
                   @Field("type")String type,
                   @Field("status")String status,
                   @Field("startDate")String startDate,
                   @Field("endDate")String endDate,
                   Callback<MessageDTO> callback);
}
