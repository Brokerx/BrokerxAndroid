package com.firstidea.android.brokerx;

import android.app.Dialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.firstidea.android.brokerx.adapter.BuyerSellerHomeEnquiriesAdapter;
import com.firstidea.android.brokerx.constants.AppConstants;
import com.firstidea.android.brokerx.enums.LeadCurrentStatus;
import com.firstidea.android.brokerx.enums.LeadType;
import com.firstidea.android.brokerx.enums.NotificationType;
import com.firstidea.android.brokerx.fragment.broker.BrokerHomeFragment;
import com.firstidea.android.brokerx.http.ObjectFactory;
import com.firstidea.android.brokerx.http.model.Lead;
import com.firstidea.android.brokerx.http.model.MessageDTO;
import com.firstidea.android.brokerx.http.model.Notification;
import com.firstidea.android.brokerx.http.model.User;
import com.firstidea.android.brokerx.model.HomeHeader;
import com.firstidea.android.brokerx.utils.AppUtils;
import com.firstidea.android.brokerx.utils.ApptDateUtils;
import com.firstidea.android.brokerx.utils.SharedPreferencesUtil;
import com.firstidea.android.brokerx.widget.AppProgressDialog;
import com.firstidea.android.brokerx.widget.HeaderSpanSizeLookup;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

public class BuyerSellerHomeActivity extends AppCompatActivity  {

    BuyerSellerHomeEnquiriesAdapter adapter;
    RecyclerView recyclerView;
    private Spinner spinner_nav;
    private Context mContext = this;
    private String type;
    private ArrayList<Lead> mLeads;
    private boolean isUserTypeSpinnerInitilized = false;
    private final int NEXT_ACTIVITY_REQ_CODE = 500;
    private final int ACTION_ACTIVITY = 700;
    private SwipeRefreshLayout mSwipeRefreshLayout;
    public TextView unreadNotifCount;
    public TextView unreadChatCount;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_buyer_seller_home);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("");
        spinner_nav = (Spinner) findViewById(R.id.spinner_nav);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(BuyerSellerHomeActivity.this, AddEnquiryFirstActivity.class);
                intent.putExtra("type", type);
                startActivityForResult(intent, NEXT_ACTIVITY_REQ_CODE);
            }
        });
        AppUtils.loadFCMid(this);
        mSwipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swipeRefreshLayout);
        mSwipeRefreshLayout.setColorSchemeResources(R.color.colorPrimaryDark,
                R.color.colorPrimaryLight,
                R.color.teal,
                R.color.teal);
        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                // Refresh items
                getLeads();
            }
        });
        initActionbarSpinner();
        getUnreadNotificationCount();
    }

    private void getUnreadNotificationCount() {
        User me = User.getSavedUser(this);
        ObjectFactory.getInstance().getChatServiceInstance().getUnreadNotificationCount(me.getUserID(), new Callback<MessageDTO>() {
            @Override
            public void success(MessageDTO messageDTO, Response response) {
                if(messageDTO.isSuccess()) {
                    Integer count = ((int) Float.parseFloat(messageDTO.getData().toString()));
                    if(unreadNotifCount == null) {
                        return;
                    }
                    if(count <= 0) {
                        unreadNotifCount.setVisibility(View.GONE);
                        return;
                    }
                    unreadNotifCount.setVisibility(View.VISIBLE);
                    unreadNotifCount.setText(count.toString());
                }
            }

            @Override
            public void failure(RetrofitError error) {
            }
        });
    }

    private void getLeads() {
        final Dialog dialog = AppProgressDialog.show(mContext);
        final User user = User.getSavedUser(mContext);
        String type = isUserTypeSpinnerInitilized ? spinner_nav.getSelectedItem().toString().toUpperCase().charAt(0)+"":LeadType.BUYER.getType();
        String status = ""+ LeadCurrentStatus.Accepted;
        ObjectFactory.getInstance().getLeadServiceInstance().getActiveLeads(user.getUserID(), type, new Callback<MessageDTO>() {
            @Override
            public void success(MessageDTO messageDTO, Response response) {
                if(messageDTO.isSuccess()) {
                    ArrayList<Lead> leads= Lead.createListFromJson(messageDTO.getData());
                    mLeads= new ArrayList<Lead>();
                    mLeads.add(new Lead());
                    Collections.sort(leads, new Comparator<Lead>() {
                        @Override
                        public int compare(Lead lhs, Lead rhs) {
                            Date lhsDate = ApptDateUtils.getServerFormatedDateAndTime(lhs.getLastUpdDateTime());
                            Date rhsDate = ApptDateUtils.getServerFormatedDateAndTime(rhs.getLastUpdDateTime());
                            return (lhsDate.getTime() > rhsDate.getTime() ? -1 : 1);
                        }
                    });

                    for (Lead lead: leads) {
                        if(!TextUtils.isEmpty(lead.getDeletedbyUserIDs()) && lead.getDeletedbyUserIDs().contains(","+user.getSavedUser(mContext).getUserID()+",")){
                            continue;
                        }
                        mLeads.add(lead);
                    }
                    /*if(!isUserTypeSpinnerInitilized){
                        initActionbarSpinner();
                    }*/
                    initializeRecyclerView();
                    fillRecyclerView();
                } else {
                    Toast.makeText(mContext, "Server Error", Toast.LENGTH_SHORT).show();
                }
                dialog.dismiss();
                mSwipeRefreshLayout.setRefreshing(false);
            }

            @Override
            public void failure(RetrofitError error) {
                Toast.makeText(mContext, "Please Check your Internet Connection", Toast.LENGTH_SHORT).show();
                dialog.dismiss();
                mSwipeRefreshLayout.setRefreshing(false);
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_buyer_seller_home, menu);
        View count = menu.findItem(R.id.action_notification).getActionView();
        unreadNotifCount = (TextView) count.findViewById(R.id.counter);
        count.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                unreadNotifCount.setVisibility(View.GONE);
                Intent intent = new Intent(BuyerSellerHomeActivity.this, NewNotificationActivity.class);
                startActivity(intent);
            }
        });


        View chatCount = menu.findItem(R.id.action_chat).getActionView();
        unreadChatCount = (TextView) chatCount.findViewById(R.id.counter);
        chatCount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                unreadChatCount.setVisibility(View.GONE);
                Intent intent = new Intent(BuyerSellerHomeActivity.this, ChatListActivity.class);
                startActivity(intent);
            }
        });

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if(id == R.id.action_notification) {
            Intent intent = new Intent(BuyerSellerHomeActivity.this, NewNotificationActivity.class);
            startActivity(intent);
        } else if(id == R.id.action_chat) {
            Intent intent = new Intent(BuyerSellerHomeActivity.this, ChatListActivity.class);
            startActivity(intent);
        }
        else if(id == R.id.action_logout) {
            SharedPreferencesUtil.clearAll(this);
            Intent intent = new Intent(BuyerSellerHomeActivity.this, SplashActivity.class);
            startActivity(intent);
            finish();
        }
        /*else if(id == R.id.action_profile) {
            Intent intent = new Intent(BuyerSellerHomeActivity.this, SignupActivity.class);
            intent.putExtra(AppConstants.KEY_IS_PROFILE_EDIT, true);
            startActivity(intent);
        }*/
        return super.onOptionsItemSelected(item);
    }

    private void initActionbarSpinner() {
        final String[] spinnerItems = {"Buyer","Seller"};
        ArrayAdapter<String> spinnerAdapert = new ArrayAdapter<String>(mContext, R.layout.buyer_seller_actiobar_spinner_item,spinnerItems);
        spinner_nav.setAdapter(spinnerAdapert);
        spinner_nav.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if(!isUserTypeSpinnerInitilized) return;
                Toast.makeText(BuyerSellerHomeActivity.this, "Showing "+spinnerItems[position]+" Enquiries", Toast.LENGTH_SHORT).show();
                type = (spinnerItems[position].charAt(0)+"").toUpperCase();
                SharedPreferencesUtil.putSharedPreferencesInt(mContext, Constants.KEY_BUYER_SELLER_PREFERANCE, position);
                getLeads();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        int prevSelection = SharedPreferencesUtil.getSharedPreferencesInt(mContext, Constants.KEY_BUYER_SELLER_PREFERANCE, 0);
        if(!isUserTypeSpinnerInitilized) isUserTypeSpinnerInitilized = true;
        spinner_nav.setSelection(prevSelection);
    }

    private void initializeRecyclerView() {
        String type = isUserTypeSpinnerInitilized ? spinner_nav.getSelectedItem().toString().toUpperCase().charAt(0)+"":LeadType.BUYER.getType();
        adapter = new BuyerSellerHomeEnquiriesAdapter(this, mLeads, type, new BuyerSellerHomeEnquiriesAdapter.OnCardClickListener() {
            @Override
            public void onCardClick(Lead lead) {
                Intent enquiry = new Intent(mContext, EnquiryDetailsActivity.class);
                enquiry.putExtra(Lead.KEY_LEAD, lead);
                startActivityForResult(enquiry, ACTION_ACTIVITY);
            }
        });
        recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        recyclerView.setHasFixedSize(true);
        GridLayoutManager layoutManager = new GridLayoutManager(this, 1);
        HeaderSpanSizeLookup headerSpanSizeLookup = new HeaderSpanSizeLookup(adapter, layoutManager);
        layoutManager.setSpanSizeLookup(headerSpanSizeLookup);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapter);
    }

    private void fillRecyclerView() {
        HomeHeader header = new HomeHeader();
//        DragonBallFooter footer = getFooter();
        adapter.setHeader(header);
        adapter.setItems(mLeads);
//        adapter.setFooter(footer);
        adapter.notifyDataSetChanged();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(requestCode == NEXT_ACTIVITY_REQ_CODE  && resultCode == RESULT_OK) {
            getLeads();
        } else if(requestCode == ACTION_ACTIVITY && resultCode == RESULT_OK) {
            getLeads();
        }

    }



    BroadcastReceiver notificationReceiver = new BroadcastReceiver() {

        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent.getAction().equals(Constants.ACTION_NEW_NOTIFICATION)) {
                String type = intent.getStringExtra("type");
                if(type.equals("notification")) {
                    Integer count = 1;
                    if (unreadNotifCount.getVisibility() == View.VISIBLE) {
                        String countString = unreadNotifCount.getText().toString();
                        count = Integer.parseInt(countString) + 1;
                    } else {
                        unreadNotifCount.setVisibility(View.VISIBLE);
                    }
                    unreadNotifCount.setText(count + "");
                    unreadNotifCount.setText(count + "");
                    Notification notification = intent.getParcelableExtra("notification");
                    if(notification != null && (notification.getType().equals(NotificationType.LEAD_CREATED.getNotificationType())
                            || notification.getType().equals(NotificationType.LEAD_REVERTED.getNotificationType())
                            || notification.getType().equals(NotificationType.DEAL_DONE.getNotificationType())
                            || notification.getType().equals(NotificationType.LEAD_STATUS_CHANGED.getNotificationType())
                            || notification.getType().equals(NotificationType.MOVED_TO_PENDING_LEAD.getNotificationType()))) {
                        getLeads();
                    }
                } else {
                    Integer count = 1;
                    if (unreadChatCount.getVisibility() == View.VISIBLE) {
                        String countString = unreadChatCount.getText().toString();
                        count = Integer.parseInt(countString) + 1;
                    } else {
                        unreadChatCount.setVisibility(View.VISIBLE);
                    }
                    unreadChatCount.setText(count + "");
                }
            }
        }
    };
    @Override
    protected void onResume() {
        super.onResume();
        IntentFilter filter = new IntentFilter();
        filter.addAction(Constants.ACTION_NEW_NOTIFICATION);
        registerReceiver(notificationReceiver, filter);
    }

    @Override
    protected void onPause() {
        super.onPause();
        unregisterReceiver(notificationReceiver);
    }
}
