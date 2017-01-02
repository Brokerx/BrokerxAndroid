package com.firstidea.android.brokerx;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
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
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.firstidea.android.brokerx.adapter.MyHistoryAdapter;
import com.firstidea.android.brokerx.enums.LeadCurrentStatus;
import com.firstidea.android.brokerx.enums.LeadType;
import com.firstidea.android.brokerx.http.ObjectFactory;
import com.firstidea.android.brokerx.http.model.Lead;
import com.firstidea.android.brokerx.http.model.MessageDTO;
import com.firstidea.android.brokerx.http.model.User;
import com.firstidea.android.brokerx.model.MyHistroyItem;
import com.firstidea.android.brokerx.widget.AppProgressDialog;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

public class MyHistoryActivity extends AppCompatActivity {
    private RecyclerView.LayoutManager mLayoutManager;
    private RecyclerView mRecyclerView;

    private Context mContext;
    private ArrayList<Lead> mLeads;
    private User me;
    private boolean mIsFromAnalysis = false;
    private Integer brokerID=null;
    private String itemName = null;
    private String leadType=null;

    private TextView mStartDateView,mEndDateView;
    private int startDay, endDay;
    private int startMonth, endMonth;
    private int startYear, endYear;
    private Date mStartDate,mEndDate;
    SimpleDateFormat SDF = new SimpleDateFormat("dd MMM yyyy");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_history);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("My History");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
        mContext = this;
        me = User.getSavedUser(this);

        if(getIntent().hasExtra("IS_FROM_ANALYSYS")) {
            mIsFromAnalysis = getIntent().getExtras().getBoolean("IS_FROM_ANALYSYS");
            itemName = getIntent().getExtras().getString("ItemName");
            getSupportActionBar().setTitle("Analysis: "+itemName);
            if(getIntent().hasExtra("BrokerID")) {
                brokerID = getIntent().getExtras().getInt("BrokerID");
            }
            if(getIntent().hasExtra(LeadType.KEY_LEAD_TYPE)) {
                leadType = getIntent().getExtras().getString(LeadType.KEY_LEAD_TYPE);
            }

        }

        mStartDateView = (TextView) findViewById(R.id.starDate);
        mEndDateView = (TextView) findViewById(R.id.endDate);
        mRecyclerView = (RecyclerView)findViewById(R.id.history_recycler_view);
        mRecyclerView.setHasFixedSize(true);

        mLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());

        if(mIsFromAnalysis) {
            getLeads();
        } else {
            getHistory();
        }


    }

    private void getLeads() {
        final Dialog dialog = AppProgressDialog.show(mContext);
        ObjectFactory.getInstance().getLeadServiceInstance().getLeads(me.getUserID(), leadType, "D",itemName,brokerID, null, null, new Callback<MessageDTO>() {
            @Override
            public void success(MessageDTO messageDTO, Response response) {
                if(messageDTO.isSuccess()) {
                    ArrayList<Lead> leads= Lead.createListFromJson(messageDTO.getData());
                    mLeads= new ArrayList<Lead>();
                    mLeads.addAll(leads);
                    MyHistoryAdapter mAdapter = new MyHistoryAdapter(mContext, mLeads,me.isBroker());
                    mRecyclerView.setAdapter(mAdapter);
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

    private void getHistory() {
        final Dialog dialog = AppProgressDialog.show(mContext);
        ObjectFactory.getInstance().getLeadServiceInstance().getHistory(me.getUserID(), null,null, new Callback<MessageDTO>() {
            @Override
            public void success(MessageDTO messageDTO, Response response) {
                if(messageDTO.isSuccess()) {
                    ArrayList<Lead> leads= Lead.createListFromJson(messageDTO.getData());
                    mLeads= new ArrayList<Lead>();
                    mLeads.addAll(leads);
                    MyHistoryAdapter mAdapter = new MyHistoryAdapter(mContext, mLeads,me.isBroker());
                    mRecyclerView.setAdapter(mAdapter);
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

    public void setStartDate(View view) {
        new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                Calendar calendar = Calendar.getInstance();
                calendar.set(Calendar.YEAR, year);
                calendar.set(Calendar.MONTH, monthOfYear);
                calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                startYear = year;
                startMonth = monthOfYear;
                startDay = dayOfMonth;
                mStartDate= calendar.getTime();
                mStartDateView.setText(SDF.format(mStartDate));
            }
        }, startYear, startMonth, startDay).show();
    }


    public void setEndDate(View view) {
        new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                Calendar calendar = Calendar.getInstance();
                calendar.set(Calendar.YEAR, year);
                calendar.set(Calendar.MONTH, monthOfYear);
                calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                endYear = year;
                endMonth = monthOfYear;
                endDay = dayOfMonth;
                mEndDate = calendar.getTime();
                mEndDateView.setText(SDF.format(mEndDate));
            }
        }, endYear, endMonth, endDay).show();
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
