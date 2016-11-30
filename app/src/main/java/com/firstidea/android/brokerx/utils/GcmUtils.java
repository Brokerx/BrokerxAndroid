package com.firstidea.android.brokerx.utils;

import android.content.Context;
import android.widget.Toast;

import com.firstidea.android.brokerx.constants.AppConstants;
import com.firstidea.android.brokerx.constants.MsgConstants;
import com.firstidea.android.brokerx.http.SingletonRestClient;
import com.firstidea.android.brokerx.http.model.MessageDTO;
import com.firstidea.android.brokerx.http.model.User;
import com.firstidea.android.brokerx.http.service.UserService;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

/**
 * Created by jwyn on 17/1/16.
 */
public class GcmUtils {

    public static void registerGCMOnServer(final Context mContext) {
        try {
            String deviceToken = SharedPreferencesUtil.getSharedPreferencesString(
                    mContext, AppConstants.KEY_GCM_REG_TOKEN, null);
            if (deviceToken != null && deviceToken.length() > 0) {
                User user = User.getSavedUser(mContext);
                UserService userService = SingletonRestClient.createService(UserService.class, mContext);
                userService.updateGCMKey(user.getUserID(), deviceToken, new Callback<MessageDTO>() {
                    @Override
                    public void success(MessageDTO messageDTO, Response response) {
                        if(messageDTO.getMessageID().equals(MsgConstants.SUCCESS_ID)) {

                        } else {
                            Toast.makeText(mContext, "Some Error Occures while registering device", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void failure(RetrofitError error) {
                        Toast.makeText(mContext, "Unable to register device", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}

