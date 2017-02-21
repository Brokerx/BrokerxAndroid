package com.firstidea.android.brokerx;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.widget.SwipeRefreshLayout;
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
    private Integer brokerID = null;
    private String itemName = null;
    private String leadType = null;

    private TextView mStartDateView, mEndDateView;
    private int startDay, endDay;
    private int startMonth, endMonth;
    private int startYear, endYear;
    private Date mStartDate, mEndDate;
    SimpleDateFormat SDF = new SimpleDateFormat("dd MMM yyyy");
    private SwipeRefreshLayout mSwipeRefreshLayout;

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

        if (getIntent().hasExtra("IS_FROM_ANALYSYS")) {
            mIsFromAnalysis = getIntent().getExtras().getBoolean("IS_FROM_ANALYSYS");
            itemName = getIntent().getExtras().getString("ItemName");
            getSupportActionBar().setTitle("Analysis: " + itemName);
            if (getIntent().hasExtra("BrokerID")) {
                brokerID = getIntent().getExtras().getInt("BrokerID");
            }
            if (getIntent().hasExtra(LeadType.KEY_LEAD_TYPE)) {
                leadType = getIntent().getExtras().getString(LeadType.KEY_LEAD_TYPE);
            }

        }

        Calendar cal = Calendar.getInstance();
        startDay = endDay = cal.get(Calendar.DAY_OF_MONTH);
        startMonth = endMonth = cal.get(Calendar.MONTH);
        startYear = endYear = cal.get(Calendar.YEAR);

        mStartDateView = (TextView) findViewById(R.id.starDate);
        mEndDateView = (TextView) findViewById(R.id.endDate);
        mRecyclerView = (RecyclerView) findViewById(R.id.history_recycler_view);
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
                if (mIsFromAnalysis) {
                    getLeads();
                } else {
                    getHistory();
                }
            }
        });

        if (mIsFromAnalysis) {
            getLeads();
        } else {
            getHistory();
        }

        findViewById(R.id.button_search).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mStartDateView.getText().toString().trim().length() > 0
                        && mEndDateView.getText().toString().trim().length() > 0) {
                    if (mIsFromAnalysis) {
                        getLeads();
                    } else {
                        getHistory();
                    }
                } else {
                    Toast.makeText(mContext, "Please Select Start and End Date", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }

    private void getLeads() {
        final Dialog dialog = AppProgressDialog.show(mContext);
        String startDate = null, enadDate = null;
        if (mStartDate != null && mEndDate != null) {
            SimpleDateFormat SDF = new SimpleDateFormat("yyyy-MM-dd");
            startDate = SDF.format(mStartDate);
            enadDate = SDF.format(mEndDate);
        }
        ObjectFactory.getInstance().getLeadServiceInstance().getLeads(me.getUserID(), leadType, "D", itemName, brokerID, startDate, enadDate, new Callback<MessageDTO>() {
            @Override
            public void success(MessageDTO messageDTO, Response response) {
                if (messageDTO.isSuccess()) {
                    ArrayList<Lead> leads = Lead.createListFromJson(messageDTO.getData());
                    mLeads = new ArrayList<Lead>();
                    mLeads.addAll(leads);
                    MyHistoryAdapter mAdapter = new MyHistoryAdapter(mContext, mLeads, me.isBroker());
                    mRecyclerView.setAdapter(mAdapter);
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

    private void getHistory() {
        final Dialog dialog = AppProgressDialog.show(mContext);
        String startDate = null, enadDate = null;
        if (mStartDate != null && mEndDate != null) {
            SimpleDateFormat SDF = new SimpleDateFormat("yyyy-MM-dd");
            startDate = SDF.format(mStartDate);
            enadDate = SDF.format(mEndDate);
        }
        ObjectFactory.getInstance().getLeadServiceInstance().getHistory(me.getUserID(), startDate, enadDate, new Callback<MessageDTO>() {
            @Override
            public void success(MessageDTO messageDTO, Response response) {
                if (messageDTO.isSuccess()) {
                    ArrayList<Lead> leads = Lead.createListFromJson(messageDTO.getData());
                    mLeads = new ArrayList<Lead>();
                    mLeads.addAll(leads);
                    MyHistoryAdapter mAdapter = new MyHistoryAdapter(mContext, mLeads, me.isBroker());
                    mRecyclerView.setAdapter(mAdapter);
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

    public void setStartDate(View view) {
        DatePickerDialog dpd = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                Calendar calendar = Calendar.getInstance();
                calendar.set(Calendar.YEAR, year);
                calendar.set(Calendar.MONTH, monthOfYear);
                calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                startYear = year;
                startMonth = monthOfYear;
                startDay = dayOfMonth;
                mStartDate = calendar.getTime();
                mStartDateView.setText(SDF.format(mStartDate));
            }
        }, startYear, startMonth, startDay);
        Calendar c1 = Calendar.getInstance();
        c1.set(Calendar.HOUR_OF_DAY, 1);
        c1.set(Calendar.MINUTE, 0);
        c1.set(Calendar.SECOND, 0);
        c1.set(Calendar.MILLISECOND, 0);
        dpd.getDatePicker().setMaxDate(c1.getTimeInMillis());
        dpd.show();
    }

    public void setEndDate(View view) {
        DatePickerDialog dpd = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
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
        }, endYear, endMonth, endDay);
        Calendar c1 = Calendar.getInstance();
        c1.set(Calendar.HOUR_OF_DAY, 1);
        c1.set(Calendar.MINUTE, 0);
        c1.set(Calendar.SECOND, 0);
        c1.set(Calendar.MILLISECOND, 0);
        dpd.getDatePicker().setMaxDate(c1.getTimeInMillis());
        dpd.show();
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
