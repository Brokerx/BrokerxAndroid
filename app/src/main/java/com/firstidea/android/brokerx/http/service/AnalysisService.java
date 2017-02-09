package com.firstidea.android.brokerx.http.service;

import com.firstidea.android.brokerx.http.model.Lead;
import com.firstidea.android.brokerx.http.model.LeadStatusHistory;
import com.firstidea.android.brokerx.http.model.MessageDTO;

import retrofit.Callback;
import retrofit.http.Body;
import retrofit.http.Field;
import retrofit.http.FormUrlEncoded;
import retrofit.http.Multipart;
import retrofit.http.POST;
import retrofit.http.Part;
import retrofit.mime.TypedFile;

/**
 * Created by Govind on 16-Nov-16.
 */

public interface AnalysisService {

    @FormUrlEncoded
    @POST("/analysis/getBrokerTopHighestPayingLeads")
    void getBrokerTopHighestPayingLeads(@Field("brokerID") Integer brokerID,
                                        @Field("startDate") String startDate,
                                        @Field("endDate") String endDate,
                                        Callback<MessageDTO> callback);

    @FormUrlEncoded
    @POST("/analysis/getBrokerTopHighestPayingUsers")
    void getBrokerTopHighestPayingUsers(@Field("brokerID") Integer brokerID,
                                        @Field("type") String type,
                                        @Field("startDate") String startDate,
                                        @Field("endDate") String endDate,
                                        Callback<MessageDTO> callback);

    @FormUrlEncoded
    @POST("/analysis/getBrokerDesparity")
    void getBrokerDesparity(@Field("brokerID") Integer brokerID,
                            @Field("startDate") String startDate,
                            @Field("endDate") String endDate,
                            Callback<MessageDTO> callback);

    @FormUrlEncoded
    @POST("/analysis/getTopBrokers")
    void getTopBrokers(@Field("userID") Integer userID,
                        @Field("type")String type,
                        @Field("startDate") String startDate,
                        @Field("endDate") String endDate,
                        Callback<MessageDTO> callback);

}
