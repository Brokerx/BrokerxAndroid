package com.firstidea.android.brokerx;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.Toast;

import com.firstidea.android.brokerx.adapter.BuyerSellerHomeEnquiriesAdapter;
import com.firstidea.android.brokerx.constants.AppConstants;
import com.firstidea.android.brokerx.enums.LeadCurrentStatus;
import com.firstidea.android.brokerx.enums.LeadType;
import com.firstidea.android.brokerx.http.ObjectFactory;
import com.firstidea.android.brokerx.http.model.Lead;
import com.firstidea.android.brokerx.http.model.MessageDTO;
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
    }

    private void getLeads() {
        final Dialog dialog = AppProgressDialog.show(mContext);
        User user = User.getSavedUser(mContext);
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
                            String lhsString = lhs.getLastUpdDateTime();
                            String rhsString = rhs.getLastUpdDateTime();
                            Date lhsDate = ApptDateUtils.getServerFormatedDateAndTime(lhs.getLastUpdDateTime());
                            Date rhsDate = ApptDateUtils.getServerFormatedDateAndTime(rhs.getLastUpdDateTime());
                            return (lhsDate.getTime() > rhsDate.getTime() ? -1 : 1);
                        }
                    });
                    mLeads.addAll(leads);
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
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if(id == R.id.action_notification) {
            Intent intent = new Intent(BuyerSellerHomeActivity.this, NotificationActivity.class);
            startActivity(intent);
        } /*else if(id == R.id.action_chat) {
            Intent intent = new Intent(BuyerSellerHomeActivity.this, ChatListActivity.class);
            startActivity(intent);
        }*/
        else if(id == R.id.action_logout) {
            SharedPreferencesUtil.clearAll(this);
            Intent intent = new Intent(BuyerSellerHomeActivity.this, SplashActivity.class);
            startActivity(intent);
            finish();
        }else if(id == R.id.action_profile) {
            Intent intent = new Intent(BuyerSellerHomeActivity.this, SignupActivity.class);
            intent.putExtra(AppConstants.KEY_IS_PROFILE_EDIT, true);
            startActivity(intent);
        }
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
        adapter = new BuyerSellerHomeEnquiriesAdapter(this, mLeads, new BuyerSellerHomeEnquiriesAdapter.OnCardClickListener() {
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
}
