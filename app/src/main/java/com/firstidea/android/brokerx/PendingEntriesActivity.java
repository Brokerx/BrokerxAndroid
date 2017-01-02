package com.firstidea.android.brokerx;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.Toast;

import com.firstidea.android.brokerx.adapter.MyHistoryAdapter;
import com.firstidea.android.brokerx.adapter.PendingEntriesAdapter;
import com.firstidea.android.brokerx.enums.LeadType;
import com.firstidea.android.brokerx.http.ObjectFactory;
import com.firstidea.android.brokerx.http.model.Lead;
import com.firstidea.android.brokerx.http.model.MessageDTO;
import com.firstidea.android.brokerx.http.model.User;
import com.firstidea.android.brokerx.model.PendingEntries;
import com.firstidea.android.brokerx.widget.AppProgressDialog;

import java.util.ArrayList;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

public class PendingEntriesActivity extends AppCompatActivity {
    private RecyclerView.LayoutManager mLayoutManager;
    private RecyclerView mRecyclerView;
    private Context mContext;
    private ArrayList<Lead> mLeads;
    private Spinner spinner_nav;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pending_entries);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Pending Entries");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
        mContext = this;

        spinner_nav = (Spinner) findViewById(R.id.spinner_nav);

        mRecyclerView = (RecyclerView) findViewById(R.id.pending_recycler_view);
        mRecyclerView.setHasFixedSize(true);

        mLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());


        initActionbarSpinner();

    }

    private void initActionbarSpinner() {

        final String[] spinnerItems = {"As Buyer", " As Seller"};
        ArrayAdapter<String> spinnerAdapert = new ArrayAdapter<String>(mContext, R.layout.buyer_seller_actiobar_spinner_item, spinnerItems);
        spinner_nav.setAdapter(spinnerAdapert);
        spinner_nav.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                //Toast.makeText(PendingEntriesActivity.this, "Showing "+spinnerItems[position]+" Enquiries", Toast.LENGTH_SHORT).show();
                getLeads();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


    }

    private void getLeads() {
        User me = User.getSavedUser(mContext);
        final Dialog dialog = AppProgressDialog.show(mContext);
        String type = spinner_nav.getSelectedItemPosition() == 0 ? LeadType.BUYER.getType() : LeadType.SELLER.getType();
        ObjectFactory.getInstance().getLeadServiceInstance().getLeads(me.getUserID(), type, "P", null, null, new Callback<MessageDTO>() {
            @Override
            public void success(MessageDTO messageDTO, Response response) {
                if (messageDTO.isSuccess()) {
                    ArrayList<Lead> leads = Lead.createListFromJson(messageDTO.getData());
                    if (leads.isEmpty()) {
                        findViewById(R.id.empty_list).setVisibility(View.VISIBLE);
                        mRecyclerView.setVisibility(View.GONE);
                    } else {
                        findViewById(R.id.empty_list).setVisibility(View.GONE);
                        mRecyclerView.setVisibility(View.VISIBLE);
                        mLeads = new ArrayList<Lead>();
                        mLeads.addAll(leads);
                        PendingEntriesAdapter mAdapter = new PendingEntriesAdapter(mContext, mLeads);
                        mRecyclerView.setAdapter(mAdapter);
                    }

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
