package com.firstidea.android.brokerx;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
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

import com.firstidea.android.brokerx.adapter.Analysis11HighestBrokerAdapter;
import com.firstidea.android.brokerx.adapter.Top11BuyerSellerAdapter;
import com.firstidea.android.brokerx.enums.LeadType;
import com.firstidea.android.brokerx.http.ObjectFactory;
import com.firstidea.android.brokerx.http.model.Lead;
import com.firstidea.android.brokerx.http.model.MessageDTO;
import com.firstidea.android.brokerx.http.model.User;
import com.firstidea.android.brokerx.model.TopBroketItem;
import com.firstidea.android.brokerx.widget.AppProgressDialog;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

public class Top11BuyerSellerActivity extends AppCompatActivity {
    private RecyclerView.LayoutManager mLayoutManager;
    private RecyclerView mRecyclerView;
    private ArrayList<Lead> mList;
    private Context mContext;


    private TextView mStartDateView,mEndDateView;
    private int startDay, endDay;
    private int startMonth, endMonth;
    private int startYear, endYear;
    private Date mStartDate,mEndDate;
    SimpleDateFormat SDF = new SimpleDateFormat("dd MMM yyyy");


    private String mLeadType;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_top11_seller);
        mLeadType = getIntent().getExtras().getString(LeadType.KEY_LEAD_TYPE);
        String title = "Top 11 Highest Paying Sellers";
        if (mLeadType.equals(LeadType.BUYER.getType())) {
            title = "Top 11 Highest Paying Buyers";
        }
        mContext = this;
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(title);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);

        mStartDateView = (TextView) findViewById(R.id.starDate);
        mEndDateView = (TextView) findViewById(R.id.endDate);

        mRecyclerView = (RecyclerView) findViewById(R.id.top11seller_recycler_view);
        mRecyclerView.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());

        getLeads();
    }

    private void getLeads() {
        final Dialog dialog = AppProgressDialog.show(this);
        User me = User.getSavedUser(this);
        ObjectFactory.getInstance().getAnalysisServiceInstance().getBrokerTopHighestPayingUsers(me.getUserID(),mLeadType, null, null, new Callback<MessageDTO>() {
            @Override
            public void success(MessageDTO messageDTO, Response response) {
                if (messageDTO.isSuccess()) {
                    mList = Lead.createListFromJson(messageDTO.getData());
                    Top11BuyerSellerAdapter mAdapter = new Top11BuyerSellerAdapter(mContext, mList, mLeadType);
                    mRecyclerView.setAdapter(mAdapter);
                    mAdapter.notifyDataSetChanged();
                }
                {
                    Toast.makeText(mContext, "Server Error", Toast.LENGTH_SHORT).show();
                }
                dialog.dismiss();
            }

            @Override
            public void failure(RetrofitError error) {
                Toast.makeText(mContext, "Pleaaswe check internet connection", Toast.LENGTH_SHORT).show();
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
