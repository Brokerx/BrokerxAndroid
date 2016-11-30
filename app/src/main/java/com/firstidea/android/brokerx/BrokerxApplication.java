package com.firstidea.android.brokerx;

import android.app.Application;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.support.v4.content.IntentCompat;
import android.util.LruCache;

/**
 * Created by Govind on 20-Nov-16.
 */

public class BrokerxApplication extends Application {
    private static BrokerxApplication sInstance;

    @Override
    public void onCreate() {
        super.onCreate();
        sInstance = this;
    }

    public synchronized static BrokerxApplication getInstance() {
        return sInstance;
    }

    public synchronized static void gotoLoginActivity() {
        Intent intent = new Intent(getContext(), LoginActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | IntentCompat.FLAG_ACTIVITY_CLEAR_TASK);
        ComponentName cn = intent.getComponent();
        Intent mainIntent = IntentCompat.makeRestartActivityTask(cn);
        getContext().startActivity(mainIntent);
    }

    public static synchronized Context getContext() {
        return getInstance().getBaseContext();
    }

    public static synchronized Context getAppContext() {
        return getInstance().getApplicationContext();
    }

}
