package com.firstidea.android.brokerx.constants;

/**
 * Created by Govind on 07-Jan-16.
 */
public interface AppConstants {

    //Keys for SharePreferences and Intent extras
    String KEY_GCM_REG_TOKEN = "GCMRegToken";
    String PREV_VERSION = "PREV_VERSION";
    String KEY_SMS_RECEIVER_INTENT = "SMS_RECEIVER";
    String KEY_OTP = "SMS_OTP";
    String KEY_CATEGORY = "Category" ;
    String KEY_PAID_INFO = "PaidInfo" ;
    String KEY_PAID_INFO_AMOUNT = "PaidInfoAmount" ;
    String KEY_FILE_URL = "File_URL" ;
    String KEY_MAIN_CATEGORY_ID = "MainCategoryID";
    String KEY_MAIN_CATEGORY_NAME = "MainCategoryName";
    String KEY_FROM_USER_ID = "FromuserID";
    String KEY_FAV_USER_ID = "FavUserID";
    String KEY_FAV_USER_NAME = "FavUserName";
    String KEY_ADVERTISEMENT_ID = "AdvertisementID";
    String KEY_ADVERTISEMENTS = "Advertisements";
    String KEY_ALL_CATEGORIES = "AllCategories";
    String KEY_LAST_AD_INDEX = "LastAdIndex";
    String KEY_MAIN_CATEGORY_TILE_NO = "MainCategoryTileNo";
    String KEY_USER_COMMUNICATION = "UserCommunication";
    String KEY_ACTIVE_COMMUNICATION_ADVERTISEMENT_ID = "ActiveCommunicationAdvertisementID";
    String KEY_ACTIVE_COMMUNICATION_USER_ID = "ActiveCommunicationUserID";
    String KEY_IS_PROFILE_EDIT = "IsProfileEdit";

    //User Details Keys
    public static String KEY_USER_ID = "user_id";
    public static String KEY_FULL_NAME = "full_name";
    public static String KEY_COMPANY = "company";
    public static String KEY_ADDRESS = "address";
    public static String KEY_LOCALITY = "locality";
    public static String KEY_CITY = "city";
    public static String KEY_MOBILE = "mobile";
    public static String KEY_EMAIL = "email";
    public static String KEY_PHOTO = "photo";
    public static String KEY_IS_BROKER = "isBroker";
    public static String KEY_ITEMS = "items";
    public static String KEY_ABOUT = "About";
    public static String KEY_EPIN_VERIFICATION = "epin_verification";
    public static String KEY_WALLET_BALANCE = "wallet_balance";
    public static String KEY_SIGNUP_DTTM = "signupDttm";
    public static String KEY_LAST_UPD_DTTM = "lastUpdDttm";
    public static String KEY_FAVOURITE_USERS = "FavouriteUsers";
    public static String KEY_SELECTED_CITIES = "SelectedCities";

    //Constants for Intent Actions
    String ACTION_NEW_MESSAGE = "NewMessageReceived";

    static final int PLAY_SERVICES_RESOLUTION_REQUEST = 9000;

    static final String SENT_TOKEN_TO_SERVER = "sentTokenToServer";
    static final String REGISTRATION_COMPLETE = "registrationComplete";
    String SELECTED_CITY = "SelectedCity";
    String IS_MULTI_SELECT = "IsMultiSelect";
    String KEY_IS_FAVOURITE = "IS_FAVOURITE";
}
