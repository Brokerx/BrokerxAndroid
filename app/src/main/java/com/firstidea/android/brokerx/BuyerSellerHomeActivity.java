package com.firstidea.android.brokerx;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
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

import com.firstidea.android.brokerx.Chat.ChatListActivity;
import com.firstidea.android.brokerx.adapter.BuyerSellerHomeEnquiriesAdapter;
import com.firstidea.android.brokerx.constants.AppConstants;
import com.firstidea.android.brokerx.enums.LeadType;
import com.firstidea.android.brokerx.fragment.dummy.DummyContent;
import com.firstidea.android.brokerx.http.ObjectFactory;
import com.firstidea.android.brokerx.http.model.Lead;
import com.firstidea.android.brokerx.http.model.MessageDTO;
import com.firstidea.android.brokerx.http.model.User;
import com.firstidea.android.brokerx.model.Enquiry;
import com.firstidea.android.brokerx.model.HomeHeader;
import com.firstidea.android.brokerx.utils.AppUtils;
import com.firstidea.android.brokerx.utils.SharedPreferencesUtil;
import com.firstidea.android.brokerx.widget.AppProgressDialog;
import com.firstidea.android.brokerx.widget.HeaderSpanSizeLookup;

import java.util.ArrayList;

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
                startActivity(intent);
            }
        });
        AppUtils.loadFCMid(this);

        getLeads();
    }

    private void getLeads() {
        final Dialog dialog = AppProgressDialog.show(mContext);
        User user = User.getSavedUser(mContext);
        String type = isUserTypeSpinnerInitilized ? spinner_nav.getSelectedItem().toString().toUpperCase().charAt(0)+"":LeadType.BUYER.getType();
        ObjectFactory.getInstance().getLeadServiceInstance().getLeads(user.getUserID(), type, null, null, null, new Callback<MessageDTO>() {
            @Override
            public void success(MessageDTO messageDTO, Response response) {
                if(messageDTO.isSuccess()) {
                    ArrayList<Lead> leads= Lead.createListFromJson(messageDTO.getData());
                    mLeads= new ArrayList<Lead>();
                    mLeads.add(new Lead());
                    mLeads.addAll(leads);
                    if(!isUserTypeSpinnerInitilized){
                        initActionbarSpinner();
                    }
                    initializeRecyclerView();
                    fillRecyclerView();
                } else {
                    Toast.makeText(mContext, "Server Error", Toast.LENGTH_SHORT).show();
                }
                dialog.dismiss();
            }

            @Override
            public void failure(RetrofitError error) {
                Toast.makeText(mContext, "Please Check your Internet Connection", Toast.LENGTH_SHORT).show();
                dialog.dismiss();
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
        } else if(id == R.id.action_chat) {
            Intent intent = new Intent(BuyerSellerHomeActivity.this, ChatListActivity.class);
            startActivity(intent);
        }else if(id == R.id.action_logout) {
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
                if(!isUserTypeSpinnerInitilized) isUserTypeSpinnerInitilized = true;
                Toast.makeText(BuyerSellerHomeActivity.this, "Showing "+spinnerItems[position]+" Enquiries", Toast.LENGTH_SHORT).show();
                type = (spinnerItems[position].charAt(0)+"").toUpperCase();
                getLeads();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    private void initializeRecyclerView() {
        adapter = new BuyerSellerHomeEnquiriesAdapter(this,mLeads);
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

//    @Override
//    public void OnCardClick(Enquiry item) {
//
//    }
}
