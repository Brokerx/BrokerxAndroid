package com.firstidea.android.brokerx.http.model;

import android.content.Context;
import android.os.Parcel;
import android.os.Parcelable;

import com.firstidea.android.brokerx.constants.AppConstants;
import com.firstidea.android.brokerx.utils.SharedPreferencesUtil;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by Govind on 14-Nov-16.
 */

public class User implements Parcelable {
    public static final String KEY = "User";
    private Integer userID;
    private String fullName;
    private String mobile;
    private String email;
    private String password;
    private boolean isBroker;
    private String address;
    private String city;
    private String imageURL;
    private String about;
    private String gcmKey;
    private String rating;
    private String status;
    private String brokerDealsInItems;
    private Integer userConnectionID;
    private boolean isMyRequest;
    private Integer leadCount;
    private Float leadAmount;

    public User() {
    }

    public User(String name, String address, String rating, String mobile, String brokerDealsInItems) {
        this.fullName = name;
        this.address = address;
        this.rating = rating;
        this.mobile = mobile;
        this.brokerDealsInItems = brokerDealsInItems;
    }

    public Integer getUserID() {
        return userID;
    }

    public void setUserID(Integer userID) {
        this.userID = userID;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public boolean getBroker() {
        return isBroker;
    }

    public void setBroker(boolean broker) {
        isBroker = broker;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getImageURL() {
        return imageURL;
    }

    public void setImageURL(String imageURL) {
        this.imageURL = imageURL;
    }

    public String getGcmKey() {
        return gcmKey;
    }

    public void setGcmKey(String gcmKey) {
        this.gcmKey = gcmKey;
    }

    public boolean isBroker() {
        return isBroker;
    }

    public String getRating() {
        return rating;
    }

    public void setRating(String rating) {
        this.rating = rating;
    }

    public String getStatus() {
        return status;
    }

    public Integer getUserConnectionID() {
        return userConnectionID;
    }

    public void setUserConnectionID(Integer userConnectionID) {
        this.userConnectionID = userConnectionID;
    }

    public boolean isMyRequest() {
        return isMyRequest;
    }

    public void setMyRequest(boolean myRequest) {
        isMyRequest = myRequest;
    }

    public Integer getLeadCount() {
        return leadCount;
    }

    public void setLeadCount(Integer leadCount) {
        this.leadCount = leadCount;
    }

    public Float getLeadAmount() {
        return leadAmount;
    }

    public void setLeadAmount(Float leadAmount) {
        this.leadAmount = leadAmount;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getBrokerDealsInItems() {
        return brokerDealsInItems;
    }

    public void setBrokerDealsInItems(String brokerDealsInItems) {
        this.brokerDealsInItems = brokerDealsInItems;
    }

    public String getAbout() {
        return about;
    }

    public void setAbout(String about) {
        this.about = about;
    }

    public void saveUser(Context context) {
        SharedPreferencesUtil.putSharedPreferencesInt(context, AppConstants.KEY_USER_ID, this.userID);
        SharedPreferencesUtil.putSharedPreferencesString(context, AppConstants.KEY_FULL_NAME, this.fullName);
        SharedPreferencesUtil.putSharedPreferencesString(context, AppConstants.KEY_ADDRESS, this.address);
        SharedPreferencesUtil.putSharedPreferencesString(context, AppConstants.KEY_CITY, this.city);
        SharedPreferencesUtil.putSharedPreferencesString(context, AppConstants.KEY_MOBILE, this.mobile);
        SharedPreferencesUtil.putSharedPreferencesString(context, AppConstants.KEY_EMAIL, this.email);
        SharedPreferencesUtil.putSharedPreferencesString(context, AppConstants.KEY_PHOTO, this.imageURL);
        SharedPreferencesUtil.putSharedPreferencesBoolean(context, AppConstants.KEY_IS_BROKER, this.isBroker);
        SharedPreferencesUtil.putSharedPreferencesString(context, AppConstants.KEY_ITEMS, this.brokerDealsInItems);
        SharedPreferencesUtil.putSharedPreferencesString(context, AppConstants.KEY_ABOUT, this.about);

    }

    public static User getSavedUser(Context context) {
        try {
            User user = new User();
            user.userID = SharedPreferencesUtil.getSharedPreferencesInt(context, AppConstants.KEY_USER_ID, 0);
            if (user.userID <= 0) {
                return null;
            }
            user.fullName = SharedPreferencesUtil.getSharedPreferencesString(context, AppConstants.KEY_FULL_NAME, "");
            user.address = SharedPreferencesUtil.getSharedPreferencesString(context, AppConstants.KEY_ADDRESS, "");
            user.city = SharedPreferencesUtil.getSharedPreferencesString(context, AppConstants.KEY_CITY, "");
            user.mobile = SharedPreferencesUtil.getSharedPreferencesString(context, AppConstants.KEY_MOBILE, "");
            user.email = SharedPreferencesUtil.getSharedPreferencesString(context, AppConstants.KEY_EMAIL, "");
            user.imageURL = SharedPreferencesUtil.getSharedPreferencesString(context, AppConstants.KEY_PHOTO, "");
            user.about = SharedPreferencesUtil.getSharedPreferencesString(context, AppConstants.KEY_ABOUT, "");
            user.brokerDealsInItems = SharedPreferencesUtil.getSharedPreferencesString(context, AppConstants.KEY_ITEMS, "");
            user.isBroker = SharedPreferencesUtil.getSharedPreferencesBoolean(context, AppConstants.KEY_IS_BROKER, false);

            return user;

        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeValue(this.userID);
        dest.writeString(this.fullName);
        dest.writeString(this.mobile);
        dest.writeString(this.email);
        dest.writeString(this.password);
        dest.writeByte(this.isBroker ? (byte) 1 : (byte) 0);
        dest.writeByte(this.isMyRequest ? (byte) 1 : (byte) 0);
        dest.writeString(this.address);
        dest.writeString(this.city);
        dest.writeString(this.rating);
        dest.writeString(this.status);
        dest.writeString(this.brokerDealsInItems);
        dest.writeString(this.imageURL);
        dest.writeString(this.about);
    }

    protected User(Parcel in) {
        this.userID = (Integer) in.readValue(Integer.class.getClassLoader());
        this.fullName = in.readString();
        this.mobile = in.readString();
        this.email = in.readString();
        this.password = in.readString();
        this.isBroker = in.readByte() != 0;
        this.isMyRequest = in.readByte() != 0;
        this.address = in.readString();
        this.city = in.readString();
        this.rating = in.readString();
        this.status = in.readString();
        this.brokerDealsInItems = in.readString();
        this.imageURL = in.readString();
        this.about = in.readString();
    }

    public static final Parcelable.Creator<User> CREATOR = new Parcelable.Creator<User>() {
        @Override
        public User createFromParcel(Parcel source) {
            return new User(source);
        }

        @Override
        public User[] newArray(int size) {
            return new User[size];
        }
    };


    public static User createFromJSON(Object object) {
        Gson gson = new Gson();
        String jsonString = gson.toJson(object);
        User user = gson.fromJson(jsonString, User.class);
        return user;
    }

    public static ArrayList<User> createListFromJson(Object data) {
        if(data == null) return new  ArrayList<User> ();

        Gson gson = new Gson();
        String jsonString = gson.toJson(data);
        ArrayList<User> users = gson.fromJson(jsonString,  new TypeToken<ArrayList<User>>() {}.getType());

        return users;
    }
}
