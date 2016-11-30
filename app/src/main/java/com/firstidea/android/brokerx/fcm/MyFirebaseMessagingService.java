package com.firstidea.android.brokerx.fcm;

/**
 * Created by Govind on 03-Nov-16.
 */

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.firstidea.android.brokerx.BrokerHomeActivity;
import com.firstidea.android.brokerx.Constants;
import com.firstidea.android.brokerx.MycircleActivity;
import com.firstidea.android.brokerx.R;
import com.firstidea.android.brokerx.SplashActivity;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import java.util.Map;

public class MyFirebaseMessagingService extends FirebaseMessagingService {

    private static final String TAG = "MyFirebaseMsgService";
    public static String TYPE_CONNECTION_REQUEST = "ConnectionRequest";
    public static String TYPE_NEW_LEAD_ADDED = "NewLeadAdded";
    public static String TYPE_CONNECTION_REQUEST_ACCEPTED = "ConnectionRequestAccepted";
    public static String TYPE_CONNECTION_REQUEST_REJECTED = "ConnectionRequestRejected";

    /**
     * Called when message is received.
     *
     * @param remoteMessage Object representing the message received from Firebase Cloud Messaging.
     */
    // [START receive_message]
    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        // [START_EXCLUDE]
        // There are two types of messages data messages and notification messages. Data messages are handled
        // here in onMessageReceived whether the app is in the foreground or background. Data messages are the type
        // traditionally used with GCM. Notification messages are only received here in onMessageReceived when the app
        // is in the foreground. When the app is in the background an automatically generated notification is displayed.
        // When the user taps on the notification they are returned to the app. Messages containing both notification
        // and data payloads are treated as notification messages. The Firebase console always sends notification
        // messages. For more see: https://firebase.google.com/docs/cloud-messaging/concept-options
        // [END_EXCLUDE]

        // TODO(developer): Handle FCM messages here.
        // Not getting messages here? See why this may be: https://goo.gl/39bRNJ
        Log.d(TAG, "From: " + remoteMessage.getFrom());

        Map<String, String> dataMap = remoteMessage.getData();
        // Check if message contains a data payload.
        if (dataMap.size() > 0) {
            Log.d(TAG, "Message data payload: " + remoteMessage.getData());
            String type = dataMap.get("type");
            if(type != null && type.equals(TYPE_CONNECTION_REQUEST)) {
                String dataContent =  dataMap.get("data");
                generateNotification(dataContent, "Please add me to your circle", type);
            } else if(type != null && type.equals(TYPE_NEW_LEAD_ADDED)) {
                String dataContent =  dataMap.get("data");
                generateNotification("Broker-X", dataContent+" has created a lead for you", type);
            }else if(type != null && type.equals(TYPE_CONNECTION_REQUEST_ACCEPTED)) {
                String dataContent =  dataMap.get("data");
                generateNotification("Broker-X", dataContent+" Has Accepted Your Request", type);
            }else if(type != null && type.equals(TYPE_CONNECTION_REQUEST_REJECTED)) {
                String dataContent =  dataMap.get("data");
                generateNotification("Broker-X", dataContent+" Has Rejected Your Request", type);
            }
        }

        // Check if message contains a notification payload.
        if (remoteMessage.getNotification() != null) {
            Log.d(TAG, "Message Notification Body: " + remoteMessage.getNotification().getBody());
        }

        // Also if you intend on generating your own notifications as a result of a received FCM
        // message, here is where that should be initiated. See generateNotification method below.
    }
    // [END receive_message]

    /**
     * Create and show a simple notification containing the received FCM message.
     *
     * @param title FCM message body received.
     */
    private void generateNotification(String title, String content, String type) {
        Intent intent;
        if(type.equals(TYPE_NEW_LEAD_ADDED)) {
            intent = new Intent(this, BrokerHomeActivity.class);
        } else if(type.equals(TYPE_CONNECTION_REQUEST)) {
            intent = new Intent(this, MycircleActivity.class);
        } else {
            return;
        }
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intent.putExtra(Constants.IS_FROM_NOTIFICATION, true);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0 /* Request code */, intent,
                PendingIntent.FLAG_ONE_SHOT);

        Uri defaultSoundUri= RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this)
                .setSmallIcon(R.mipmap.ic_launcher)
                .setContentTitle(title)
                .setContentText(content)
                .setAutoCancel(true)
                .setSound(defaultSoundUri)
                .setContentIntent(pendingIntent);

        NotificationManager notificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        notificationManager.notify(0 /* ID of notification */, notificationBuilder.build());
    }
}