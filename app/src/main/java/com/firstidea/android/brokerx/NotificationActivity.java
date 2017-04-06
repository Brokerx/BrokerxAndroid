package com.firstidea.android.brokerx;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.Toast;

import com.firstidea.android.brokerx.adapter.NewNotificationADapter;
import com.firstidea.android.brokerx.adapter.NotificationADapter;
import com.firstidea.android.brokerx.http.ObjectFactory;
import com.firstidea.android.brokerx.http.model.MessageDTO;
import com.firstidea.android.brokerx.http.model.Notification;
import com.firstidea.android.brokerx.http.model.NotificationListDTO;
import com.firstidea.android.brokerx.http.model.User;
import com.firstidea.android.brokerx.model.NotificationItem;
import com.firstidea.android.brokerx.utils.ApptDateUtils;
import com.firstidea.android.brokerx.widget.AppProgressDialog;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

public class NotificationActivity extends AppCompatActivity {
    private RecyclerView.LayoutManager mLayoutManager;
    private RecyclerView mRecyclerView;
    private ArrayList<Notification> mList;
    private Context mContext;
    private SwipeRefreshLayout mSwipeRefreshLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notification);
        mContext = this;
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Notification");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);

        mRecyclerView = (RecyclerView) findViewById(R.id.notification_recycler_view);
        mRecyclerView.setHasFixedSize(true);

        mLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());
        mSwipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swipeRefreshLayout);
        mSwipeRefreshLayout.setColorSchemeResources(R.color.colorPrimaryDark,
                R.color.colorPrimaryLight,
                R.color.teal,
                R.color.teal);
        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                // Refresh items
                getNotiFications();
            }
        });
        getNotiFications();
    }

    private void getNotiFications() {
        User me = User.getSavedUser(this);
        final Dialog dialog = AppProgressDialog.show(this);
        ObjectFactory.getInstance().getChatServiceInstance().getNotifications(me.getUserID(), new Callback<MessageDTO>() {
            @Override
            public void success(MessageDTO messageDTO, Response response) {
                if (messageDTO.isSuccess()) {
                    mList = Notification.createListFromJson(messageDTO.getData());
                    Map<String, List<Notification>> notificationMap = new HashMap<String, List<Notification>>();
                    Map<String, Long> lastNotificationTimeStampMap = new HashMap<String, Long>();
                    Map<String, Integer> unReadCountMap = new HashMap<String, Integer>();
                    for (Notification notification : mList) {
                        String key = notification.getLeadID().toString() + ":" + notification.getFromUserID();
                        List<Notification> notifications = new ArrayList<Notification>();
                        int unreadCount = 0;
                        if (notificationMap.containsKey(key)) {
                            notifications = notificationMap.get(key);
                            unreadCount = unReadCountMap.get(key);
                        }
                        if(!notification.getRead()) {
                            unreadCount++;
                        }
                        long timestamp = ApptDateUtils.getServerFormatedDateAndTime(notification.getCreatedDttm()).getTime();
                        if(lastNotificationTimeStampMap.containsKey(key)) {
                            long prevTimeStamp = lastNotificationTimeStampMap.get(key);
                            if(prevTimeStamp > timestamp) {
                                timestamp = prevTimeStamp;
                            }
                        }
                        lastNotificationTimeStampMap.put(key, timestamp);
                        notifications.add(notification);
                        notificationMap.put(key, notifications);
                        unReadCountMap.put(key, unreadCount);
                    }
                    ArrayList<NotificationListDTO> notificationListDTOs = new ArrayList<NotificationListDTO>();
                    for (String key : notificationMap.keySet()) {
                        String[] keySplit = key.split(":");
                        Integer leadID = Integer.parseInt(keySplit[0]);
                        Integer fromUserID = Integer.parseInt(keySplit[1]);
                        List<Notification> notifications = notificationMap.get(key);
                        int unreadCount = unReadCountMap.get(key);
                        NotificationListDTO notificationListDTO = new NotificationListDTO();
                        notificationListDTO.setLeadID(leadID);
                        notificationListDTO.setUnReadCount(unreadCount);
                        notificationListDTO.setFromUserID(fromUserID);
                        notificationListDTO.setLastNotificationTimeStamp(lastNotificationTimeStampMap.get(key));
                        notificationListDTO.setNotifications(notifications);
                        notificationListDTOs.add(notificationListDTO);
                    }
//                    NotificationADapter mAdapter = new NotificationADapter(mContext, mList);
                    Collections.sort(notificationListDTOs, new Comparator<NotificationListDTO>() {
                        @Override
                        public int compare(NotificationListDTO lhs, NotificationListDTO rhs) {
                            return rhs.getLastNotificationTimeStamp().compareTo(lhs.getLastNotificationTimeStamp());
                        }
                    });
                    NewNotificationADapter mAdapter = new NewNotificationADapter(mContext, notificationListDTOs);
                    mRecyclerView.setAdapter(mAdapter);
                }
                dialog.dismiss();
                mSwipeRefreshLayout.setRefreshing(false);
            }

            @Override
            public void failure(RetrofitError error) {
                Toast.makeText(mContext, "Unable to get notifications...", Toast.LENGTH_SHORT).show();
                dialog.dismiss();
                finish();
                mSwipeRefreshLayout.setRefreshing(false);
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(0, android.R.anim.slide_out_right);
    }
}
