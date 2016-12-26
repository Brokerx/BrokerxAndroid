package com.firstidea.android.brokerx.http.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;

/**
 * Created by Govind on 27-Dec-16.
 */

public class LeadDocument implements Parcelable {
    private Integer leadDocumentID;
    private Integer leadID;
    private String documentURL;
    private Integer uploadedByUserID;
    private String uploadedDttm;
    private boolean isDeleted;
    private String type;

    public Integer getLeadDocumentID() {
        return leadDocumentID;
    }

    public void setLeadDocumentID(Integer leadDocumentID) {
        this.leadDocumentID = leadDocumentID;
    }

    public Integer getLeadID() {
        return leadID;
    }

    public void setLeadID(Integer leadID) {
        this.leadID = leadID;
    }

    public String getDocumentURL() {
        return documentURL;
    }

    public void setDocumentURL(String documentURL) {
        this.documentURL = documentURL;
    }

    public Integer getUploadedByUserID() {
        return uploadedByUserID;
    }

    public void setUploadedByUserID(Integer uploadedByUserID) {
        this.uploadedByUserID = uploadedByUserID;
    }

    public String getUploadedDttm() {
        return uploadedDttm;
    }

    public void setUploadedDttm(String uploadedDttm) {
        this.uploadedDttm = uploadedDttm;
    }

    public boolean isDeleted() {
        return isDeleted;
    }

    public void setDeleted(boolean deleted) {
        isDeleted = deleted;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeValue(this.leadDocumentID);
        dest.writeValue(this.leadID);
        dest.writeString(this.documentURL);
        dest.writeValue(this.uploadedByUserID);
        dest.writeString(this.uploadedDttm);
        dest.writeByte(this.isDeleted ? (byte) 1 : (byte) 0);
        dest.writeString(this.type);
    }

    public LeadDocument() {
    }

    protected LeadDocument(Parcel in) {
        this.leadDocumentID = (Integer) in.readValue(Integer.class.getClassLoader());
        this.leadID = (Integer) in.readValue(Integer.class.getClassLoader());
        this.documentURL = in.readString();
        this.uploadedByUserID = (Integer) in.readValue(Integer.class.getClassLoader());
        this.uploadedDttm = in.readString();
        this.isDeleted = in.readByte() != 0;
        this.type = in.readString();
    }

    public static final Parcelable.Creator<LeadDocument> CREATOR = new Parcelable.Creator<LeadDocument>() {
        @Override
        public LeadDocument createFromParcel(Parcel source) {
            return new LeadDocument(source);
        }

        @Override
        public LeadDocument[] newArray(int size) {
            return new LeadDocument[size];
        }
    };


    public static ArrayList<LeadDocument> createListFromJson(Object data) {
        if(data == null) return new  ArrayList<LeadDocument> ();

        Gson gson = new Gson();
        String jsonString = gson.toJson(data);
        ArrayList<LeadDocument> leads = gson.fromJson(jsonString,  new TypeToken<ArrayList<LeadDocument>>() {}.getType());

        return leads;
    }

}
