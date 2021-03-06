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
import android.support.v7.app.AlertDialog;
import android.text.Html;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;
import android.widget.Toast;

import com.firstidea.android.brokerx.BrokerHomeActivity;
import com.firstidea.android.brokerx.ChatActivity;
import com.firstidea.android.brokerx.Constants;
import com.firstidea.android.brokerx.MyHistoryActivity;
import com.firstidea.android.brokerx.MycircleActivity;
import com.firstidea.android.brokerx.NotificationActivity;
import com.firstidea.android.brokerx.R;
import com.firstidea.android.brokerx.SplashActivity;
import com.firstidea.android.brokerx.adapter.BuyerSellerHomeEnquiriesAdapter;
import com.firstidea.android.brokerx.enums.ChatType;
import com.firstidea.android.brokerx.http.model.Chat;
import com.firstidea.android.brokerx.http.model.Notification;
import com.firstidea.android.brokerx.utils.SharedPreferencesUtil;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import com.google.gson.Gson;

import java.util.Map;

public class MyFirebaseMessagingService extends FirebaseMessagingService {

    private static final String TAG = "MyFirebaseMsgService";
    public static String TYPE_CONNECTION_REQUEST = "ConnectionRequest";
    public static String TYPE_NEW_LEAD_ADDED = "NewLeadAdded";
    public static String TYPE_CONNECTION_REQUEST_ACCEPTED = "ConnectionRequestAccepted";
    public static String TYPE_CONNECTION_REQUEST_REJECTED = "ConnectionRequestRejected";
    public static String TYPE_USER_COMMUNICATION = "UserCommunication";
    public static String TYPE_NEW_NOTIFICATION = "NewNotification";
    public static String TYPE_DEAL_DONE = "DealDone";
    Notification notification = null;
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
                generateNotification(dataContent, "Please add me to your circle", type, null);
            } else if(type != null && type.equals(TYPE_NEW_LEAD_ADDED)) {
                String dataContent =  dataMap.get("data");
                generateNotification("Broker-X", dataContent+" has created a lead for you", type, null);
                SharedPreferencesUtil.putSharedPreferencesBoolean(this, Constants.KEY_LEAD_LIST_UPDATED, true);
                //also send broadcast
            }else if(type != null && (type.equals(TYPE_NEW_NOTIFICATION)
                    || type.equals(TYPE_DEAL_DONE))) {
                String dataContent =  dataMap.get("data");
                String payload =  dataMap.get("payload");

                if(!TextUtils.isEmpty(payload)) {
                    Gson gson = new Gson();
                    notification = gson.fromJson(payload, Notification.class);
                }
                if(!type.equals(TYPE_DEAL_DONE)) {
                    generateNotification("Broker-X", "New Notification", type, null);
                } else {
                    generateNotification(dataContent, "Deal Done", type, null);
                }
                Intent broadcastIntent = new Intent();
                broadcastIntent.setAction(Constants.ACTION_NEW_NOTIFICATION);
                broadcastIntent.putExtra("type","notification");
                broadcastIntent.putExtra("notification", notification);
                this.sendBroadcast(broadcastIntent);
            }else if(type != null && type.equals(TYPE_CONNECTION_REQUEST_ACCEPTED)) {
                String dataContent =  dataMap.get("data");
                generateNotification("Broker-X", dataContent+" Has Accepted Your Request", type, null);
            }else if(type != null && type.equals(TYPE_CONNECTION_REQUEST_REJECTED)) {
                String dataContent =  dataMap.get("data");
                generateNotification("Broker-X", dataContent+" Has Rejected Your Request", type, null);
            }else if(type != null && type.equals(TYPE_USER_COMMUNICATION)) {
                String dataContent =  dataMap.get("data");
                Gson gson = new Gson();
                Chat chat = gson.fromJson(dataContent, Chat.class);
                int ActiveCommUserID = SharedPreferencesUtil.getSharedPreferencesInt(this, Constants.KEY_ACTIVE_COMMUNICATION_USER_ID, -1);
                int ActiveCommAdID = SharedPreferencesUtil.getSharedPreferencesInt(this, Constants.KEY_ACTIVE_COMMUNICATION_LEAD_ID, -1);
                if(ActiveCommUserID == chat.getFromUserID()
                        && ActiveCommAdID == chat.getLeadID()) {
//                LocalBroadcastManager localBroadcastManager = LocalBroadcastManager.getInstance(this);
                    Intent broadcastIntent = new Intent();
                    broadcastIntent.setAction(Constants.ACTION_NEW_MESSAGE);
                    broadcastIntent.putExtra(Constants.KEY_USER_COMMUNICATION, chat);
                    this.sendBroadcast(broadcastIntent);
                } else {
                    String msg = chat.getType().equals(ChatType.TEXT.getType()) ? chat.getMessage(): "Image";
                    generateNotification(chat.getFromUserName(), msg, type, chat);

                    Intent broadcastIntent = new Intent();
                    broadcastIntent.setAction(Constants.ACTION_NEW_NOTIFICATION);
                    broadcastIntent.putExtra("type","chat");
                    this.sendBroadcast(broadcastIntent);
                }

            }
        }

        // Check if message contains a notification payload.
        if (remoteMessage.getNotification() != null) {
            Log.d(TAG, "Message Notification Body: " + remoteMessage.getNotification().getBody());
        }

        // Also if you intend on generating your own notifications as a result of a received FCM
        // message, here is where that should be initiated. See generateNotification method below.
    }

    private void showDealDoneDialog(String dataContent, final Notification notification) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        final AlertDialog alertDialog = builder.create();
        LayoutInflater layoutInflater = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);
        View customView = layoutInflater.inflate(R.layout.deal_done_notification_dialog,null);
        String msg = "Hello, our <b>"+notification.getItemName()+"</b> Deal has been <b>DONE</b>. you can see the deal details in History.";
        ((TextView)customView.findViewById(R.id.message)).setText(Html.fromHtml(msg));
        ((TextView)customView.findViewById(R.id.title)).setText(dataContent);
        customView.findViewById(R.id.close).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog.dismiss();
            }
        });
        customView.findViewById(R.id.close).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //TODO open lead details activity
//                Toast.makeText(MyFirebaseMessagingService.this, "TODO open lead details activity", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(MyFirebaseMessagingService.this, MyHistoryActivity.class);
                intent.putExtra("IS_FROM_NOTIFICATION", true);
                intent.putExtra("NOTIFICATION_LED_ID", notification.getLeadID());
                startActivity(intent);
                alertDialog.dismiss();
            }
        });
        alertDialog.setContentView(customView);
        alertDialog.getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        alertDialog.getWindow().setFlags(WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED,0);
        alertDialog.show();
    }
    // [END receive_message]

    /**
     * Create and show a simple notification containing the received FCM message.
     *
     * @param title FCM message body received.
     */
    private void generateNotification(String title, String content, String type, Chat chat) {
        Intent intent;
        if(type.equals(TYPE_NEW_LEAD_ADDED)) {
            intent = new Intent(this, BrokerHomeActivity.class);
        } else if(type.equals(TYPE_CONNECTION_REQUEST)) {
            intent = new Intent(this, MycircleActivity.class);
        } else if(type.equals(TYPE_NEW_NOTIFICATION)) {
            intent = new Intent(this, NotificationActivity.class);
        } else if(type.equals(TYPE_DEAL_DONE)) {
            intent = new Intent(MyFirebaseMessagingService.this, MyHistoryActivity.class);
            intent.putExtra("IS_FROM_NOTIFICATION", true);
            intent.putExtra("NOTIFICATION_LED_ID", notification.getLeadID());
        } else if(type.equals(TYPE_USER_COMMUNICATION)) {
            intent = new Intent(this, ChatActivity.class);
            intent.putExtra(Constants.OTHER_USER_NAME, chat.getFromUserName());
            intent.putExtra(Constants.ITEM_NAME, chat.getItemName());
            intent.putExtra(Constants.KEY_USER_TYPE, chat.getFromUserType());
            Integer userID = chat.getFromUserID();
            intent.putExtra(Constants.OTHER_USER_ID, userID);
            intent.putExtra(Constants.LEAD_ID, chat.getLeadID());
        } else {
            return;
        }
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intent.putExtra(Constants.IS_FROM_NOTIFICATION, true);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0 /* Request code */, intent,
                PendingIntent.FLAG_ONE_SHOT);

        Uri defaultSoundUri= RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this)
                .setSmallIcon(R.mipmap.app_icon)
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