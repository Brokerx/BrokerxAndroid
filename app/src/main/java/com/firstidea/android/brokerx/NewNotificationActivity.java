package com.firstidea.android.brokerx;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Toast;

import com.firstidea.android.brokerx.adapter.BrokerHomeViewPagerAdapter;
import com.firstidea.android.brokerx.adapter.NewNotificationADapter;
import com.firstidea.android.brokerx.adapter.NotificationViewPagerAdapter;
import com.firstidea.android.brokerx.enums.LeadType;
import com.firstidea.android.brokerx.http.ObjectFactory;
import com.firstidea.android.brokerx.http.model.MessageDTO;
import com.firstidea.android.brokerx.http.model.Notification;
import com.firstidea.android.brokerx.http.model.NotificationListDTO;
import com.firstidea.android.brokerx.http.model.User;
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

public class NewNotificationActivity extends AppCompatActivity {

    private ArrayList<Notification> mList;
    private TabLayout tabLayout;
    private ViewPager viewPager;
    private int mLastSelection= 0;
    private NotificationViewPagerAdapter viewPagerAdapter;
    private Activity mContext;
    private SwipeRefreshLayout mSwipeRefreshLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_notification);
        mContext = this;
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Notification");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);

        mSwipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swipeRefreshLayout);
        tabLayout = (TabLayout) findViewById(R.id.tabs);
        viewPager = (ViewPager) findViewById(R.id.viewpager);
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
        viewPager.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {

                switch (event.getAction()) {
                    case MotionEvent.ACTION_MOVE:
                        mSwipeRefreshLayout.setEnabled(false);
                        break;
                    case MotionEvent.ACTION_UP:
                    case MotionEvent.ACTION_CANCEL:
                        mSwipeRefreshLayout.setEnabled(true);
                        break;
                }
                return false;
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
                    Map<String, List<Notification>> buyerNotificationMap = new HashMap<String, List<Notification>>();
                    Map<String, Long> buyerLastNotificationTimeStampMap = new HashMap<String, Long>();
                    Map<String, Integer> buyerUnReadCountMap = new HashMap<String, Integer>();

                    Map<String, List<Notification>> sellerNotificationMap = new HashMap<String, List<Notification>>();
                    Map<String, Long> sellerLastNotificationTimeStampMap = new HashMap<String, Long>();
                    Map<String, Integer> sellerUnReadCountMap = new HashMap<String, Integer>();

                    Map<String, List<Notification>> historyNotificationMap = new HashMap<String, List<Notification>>();
                    Map<String, Long> historyLastNotificationTimeStampMap = new HashMap<String, Long>();
                    Map<String, Integer> historyUnReadCountMap = new HashMap<String, Integer>();

                    for (Notification notification : mList) {
                        if (TextUtils.isEmpty(notification.getCategory())) continue;
                        if (notification.getCategory().equals(LeadType.BUYER.getType())) {
                            String key = notification.getLeadID().toString() + ":" + notification.getFromUserID();
                            List<Notification> notifications = new ArrayList<Notification>();
                            int unreadCount = 0;
                            if (buyerNotificationMap.containsKey(key)) {
                                notifications = buyerNotificationMap.get(key);
                                unreadCount = buyerUnReadCountMap.get(key);
                            }
                            if (!notification.getRead()) {
                                unreadCount++;
                            }
                            long timestamp = ApptDateUtils.getServerFormatedDateAndTime(notification.getCreatedDttm()).getTime();
                            if (buyerLastNotificationTimeStampMap.containsKey(key)) {
                                long prevTimeStamp = buyerLastNotificationTimeStampMap.get(key);
                                if (prevTimeStamp > timestamp) {
                                    timestamp = prevTimeStamp;
                                }
                            }
                            buyerLastNotificationTimeStampMap.put(key, timestamp);
                            notifications.add(notification);
                            buyerNotificationMap.put(key, notifications);
                            buyerUnReadCountMap.put(key, unreadCount);
                        } else if (notification.getCategory().equals(LeadType.SELLER.getType())) {
                            String key = notification.getLeadID().toString() + ":" + notification.getFromUserID();
                            List<Notification> notifications = new ArrayList<Notification>();
                            int unreadCount = 0;
                            if (sellerNotificationMap.containsKey(key)) {
                                notifications = sellerNotificationMap.get(key);
                                unreadCount = sellerUnReadCountMap.get(key);
                            }
                            if(!notification.getRead()) {
                                unreadCount++;
                            }
                            long timestamp = ApptDateUtils.getServerFormatedDateAndTime(notification.getCreatedDttm()).getTime();
                            if(sellerLastNotificationTimeStampMap.containsKey(key)) {
                                long prevTimeStamp = sellerLastNotificationTimeStampMap.get(key);
                                if(prevTimeStamp > timestamp) {
                                    timestamp = prevTimeStamp;
                                }
                            }
                            sellerLastNotificationTimeStampMap.put(key, timestamp);
                            notifications.add(notification);
                            sellerNotificationMap.put(key, notifications);
                            sellerUnReadCountMap.put(key, unreadCount);
                        } else {
                            String key = notification.getLeadID().toString() + ":" + notification.getFromUserID();
                            List<Notification> notifications = new ArrayList<Notification>();
                            int unreadCount = 0;
                            if (historyNotificationMap.containsKey(key)) {
                                notifications = historyNotificationMap.get(key);
                                unreadCount = historyUnReadCountMap.get(key);
                            }
                            if(!notification.getRead()) {
                                unreadCount++;
                            }
                            long timestamp = ApptDateUtils.getServerFormatedDateAndTime(notification.getCreatedDttm()).getTime();
                            if(historyLastNotificationTimeStampMap.containsKey(key)) {
                                long prevTimeStamp = historyLastNotificationTimeStampMap.get(key);
                                if(prevTimeStamp > timestamp) {
                                    timestamp = prevTimeStamp;
                                }
                            }
                            historyLastNotificationTimeStampMap.put(key, timestamp);
                            notifications.add(notification);
                            historyNotificationMap.put(key, notifications);
                            historyUnReadCountMap.put(key, unreadCount);
                        }
                    }
                    ArrayList<NotificationListDTO> buyerNotificationListDTOs = new ArrayList<NotificationListDTO>();
                    for (String key : buyerNotificationMap.keySet()) {
                        String[] keySplit = key.split(":");
                        Integer leadID = Integer.parseInt(keySplit[0]);
                        Integer fromUserID = Integer.parseInt(keySplit[1]);
                        List<Notification> notifications = buyerNotificationMap.get(key);
                        int unreadCount = buyerUnReadCountMap.get(key);
                        NotificationListDTO notificationListDTO = new NotificationListDTO();
                        notificationListDTO.setLeadID(leadID);
                        notificationListDTO.setUnReadCount(unreadCount);
                        notificationListDTO.setFromUserID(fromUserID);
                        notificationListDTO.setLastNotificationTimeStamp(buyerLastNotificationTimeStampMap.get(key));
                        notificationListDTO.setNotifications(notifications);
                        buyerNotificationListDTOs.add(notificationListDTO);
                    }
//                    NotificationADapter mAdapter = new NotificationADapter(mContext, mList);
                    Collections.sort(buyerNotificationListDTOs, new Comparator<NotificationListDTO>() {
                        @Override
                        public int compare(NotificationListDTO lhs, NotificationListDTO rhs) {
                            return rhs.getLastNotificationTimeStamp().compareTo(lhs.getLastNotificationTimeStamp());
                        }
                    });
                    ArrayList<NotificationListDTO> sellerNotificationListDTOs = new ArrayList<NotificationListDTO>();
                    for (String key : sellerNotificationMap.keySet()) {
                        String[] keySplit = key.split(":");
                        Integer leadID = Integer.parseInt(keySplit[0]);
                        Integer fromUserID = Integer.parseInt(keySplit[1]);
                        List<Notification> notifications = sellerNotificationMap.get(key);
                        int unreadCount = sellerUnReadCountMap.get(key);
                        NotificationListDTO notificationListDTO = new NotificationListDTO();
                        notificationListDTO.setLeadID(leadID);
                        notificationListDTO.setUnReadCount(unreadCount);
                        notificationListDTO.setFromUserID(fromUserID);
                        notificationListDTO.setLastNotificationTimeStamp(sellerLastNotificationTimeStampMap.get(key));
                        notificationListDTO.setNotifications(notifications);
                        sellerNotificationListDTOs.add(notificationListDTO);
                    }
//                    NotificationADapter mAdapter = new NotificationADapter(mContext, mList);
                    Collections.sort(sellerNotificationListDTOs, new Comparator<NotificationListDTO>() {
                        @Override
                        public int compare(NotificationListDTO lhs, NotificationListDTO rhs) {
                            return rhs.getLastNotificationTimeStamp().compareTo(lhs.getLastNotificationTimeStamp());
                        }
                    });
                    ArrayList<NotificationListDTO> historyNotificationListDTOs = new ArrayList<NotificationListDTO>();
                    for (String key : historyNotificationMap.keySet()) {
                        String[] keySplit = key.split(":");
                        Integer leadID = Integer.parseInt(keySplit[0]);
                        Integer fromUserID = Integer.parseInt(keySplit[1]);
                        List<Notification> notifications = historyNotificationMap.get(key);
                        int unreadCount = historyUnReadCountMap.get(key);
                        NotificationListDTO notificationListDTO = new NotificationListDTO();
                        notificationListDTO.setLeadID(leadID);
                        notificationListDTO.setUnReadCount(unreadCount);
                        notificationListDTO.setFromUserID(fromUserID);
                        notificationListDTO.setLastNotificationTimeStamp(historyLastNotificationTimeStampMap.get(key));
                        notificationListDTO.setNotifications(notifications);
                        historyNotificationListDTOs.add(notificationListDTO);
                    }
//                    NotificationADapter mAdapter = new NotificationADapter(mContext, mList);
                    Collections.sort(historyNotificationListDTOs, new Comparator<NotificationListDTO>() {
                        @Override
                        public int compare(NotificationListDTO lhs, NotificationListDTO rhs) {
                            return rhs.getLastNotificationTimeStamp().compareTo(lhs.getLastNotificationTimeStamp());
                        }
                    });
                   initViewPager(buyerNotificationListDTOs, sellerNotificationListDTOs, historyNotificationListDTOs);
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

    private void initViewPager(ArrayList<NotificationListDTO> buyerNotificationListDTOs, ArrayList<NotificationListDTO> sellerNotificationListDTOs, ArrayList<NotificationListDTO> historyNotificationListDTOs) {
        viewPagerAdapter = new NotificationViewPagerAdapter(((NewNotificationActivity)mContext).getSupportFragmentManager(), buyerNotificationListDTOs,
                sellerNotificationListDTOs, historyNotificationListDTOs);
        viewPager.setAdapter(viewPagerAdapter);
        final TabLayout.Tab buyerTab = tabLayout.newTab();
        final TabLayout.Tab sellerTab = tabLayout.newTab();
        final TabLayout.Tab historyTab = tabLayout.newTab();
        tabLayout.addTab(buyerTab, 0);
        tabLayout.addTab(sellerTab, 1);
        tabLayout.addTab(historyTab, 2);
//        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                mLastSelection = position;
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
        tabLayout.setupWithViewPager(viewPager);
        viewPager.setCurrentItem(mLastSelection);
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
