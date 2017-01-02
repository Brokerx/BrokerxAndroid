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

public interface LeadService {

    @POST("/lead/saveLead")
    void saveLead(@Body Lead lead,
                           Callback<MessageDTO> callback);

    @POST("/lead/saveLeadStatusHistory")
    void saveLeadStatusHistory(@Body LeadStatusHistory lead,
                           Callback<MessageDTO> callback);

    @FormUrlEncoded
    @POST("/lead/getLeadStatusHistory")
    void getLeadStatusHistory(@Field("leadID") Integer leadID,
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
    @POST("/lead/getLeadDocuments")
    void getLeadDocuments(@Field("leadID") Integer leadID,
                   Callback<MessageDTO> callback);

    @FormUrlEncoded
    @POST("/lead/getHistory")
    void getHistory(@Field("userID") Integer userID,
                   @Field("startDate")String startDate,
                   @Field("endDate")String endDate,
                   Callback<MessageDTO> callback);

    @FormUrlEncoded
    @POST("/lead/getActiveLeads")
    void getActiveLeads(@Field("userID") Integer loginUserID,
                   @Field("type")String leadType,
                   Callback<MessageDTO> callback);

    @FormUrlEncoded
    @POST("/lead/getLeadHistory")
    void getLeadHistory(@Field("leadID") Integer leadID,
                   Callback<MessageDTO> callback);

    @FormUrlEncoded
    @POST("/lead/dealDone")
    void dealDone(@Field("leadID") Integer leadID,
                   Callback<MessageDTO> callback);

    @FormUrlEncoded
    @POST("/lead/getBrokerLeads")
    void getBrokerLeads(@Field("userID") Integer userID,
                   @Field("type")String type,
                   @Field("status")String status,
                   @Field("startDate")String startDate,
                   @Field("endDate")String endDate,
                   Callback<MessageDTO> callback);


    @Multipart
    @POST("/lead/uploadDocument")
    void uploadDocument(@Part("file") TypedFile file,
                  @Part("DataJSON") String advertisementJSON,
                  Callback<MessageDTO> callback);

}
