package com.firstidea.android.brokerx;

import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.firstidea.android.brokerx.enums.LeadType;
import com.firstidea.android.brokerx.http.model.User;
import com.firstidea.android.brokerx.utils.StringUtils;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class AnalysisBrokerActivity extends AppCompatActivity {
    private TextView mStartDateView, mEndDateView;
    private Spinner mSpinner1;

    private LinearLayout mTop11HighestBroker,mTop11HighestSeller, mTop11HighestBuyer, desparityLayout;
    private Context mContext = this;

    private Calendar cal;
    private int startDay, endDay;
    private int startMonth, endMonth;
    private int startYear, endYear;
    private User me;
    private Date mStartDate,mEndDate;

    SimpleDateFormat SDF = new SimpleDateFormat("dd MMM yyyy");
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_analysis_broker);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Analysis");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);

        mStartDateView = (TextView) findViewById(R.id.starDate);
        mEndDateView = (TextView) findViewById(R.id.endDate);
        me = User.getSavedUser(this);
        List<String> spinner1 = StringUtils.getListOfComaValues(me.getBrokerDealsInItems());
        //Spinner adapter
        mSpinner1 = (Spinner) findViewById(R.id.spinner1);
        ArrayAdapter<String> adapter1 = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, spinner1);
        adapter1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mSpinner1.setAdapter(adapter1);
        mSpinner1.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        top11Broker();

        cal = Calendar.getInstance();
        startDay = endDay = cal.get(Calendar.DAY_OF_MONTH);
        startMonth = endMonth = cal.get(Calendar.MONTH);
        startYear = endYear = cal.get(Calendar.YEAR);
        findViewById(R.id.button_search).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String item = mSpinner1.getSelectedItem().toString();
                Intent intent = new Intent(mContext, MyHistoryActivity.class);
                intent.putExtra("IS_FROM_ANALYSYS", true);
                intent.putExtra("ItemName", item);
                intent.putExtra("StartDate", mStartDate.getTime());
                intent.putExtra("EnndDate", mEndDate.getTime());
                intent.putExtra(LeadType.KEY_LEAD_TYPE, "B");
                intent.putExtra("BrokerID", me.getUserID());
                startActivity(intent);
            }
        });
    }

    //to show next screen of top 11 paying Brokergae
    private void top11Broker() {

        mTop11HighestBroker = (LinearLayout) findViewById(R.id.top11HighesBrokerLinear);
        mTop11HighestBroker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent topBroker = new Intent(AnalysisBrokerActivity.this, Top11HighestBrokerActivity.class);
                startActivity(topBroker);
            }
        });

        mTop11HighestSeller = (LinearLayout) findViewById(R.id.top11sellerLinear);
        mTop11HighestSeller.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent topBroker = new Intent(AnalysisBrokerActivity.this, Top11BuyerSellerActivity.class);
                topBroker.putExtra(LeadType.KEY_LEAD_TYPE, LeadType.SELLER.getType());
                startActivity(topBroker);
            }
        });

        mTop11HighestBuyer = (LinearLayout) findViewById(R.id.top11BuyerLinear);
        mTop11HighestBuyer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent topBroker = new Intent(AnalysisBrokerActivity.this, Top11BuyerSellerActivity.class);
                topBroker.putExtra(LeadType.KEY_LEAD_TYPE, LeadType.BUYER.getType());
                startActivity(topBroker);
            }
        });

        desparityLayout = (LinearLayout) findViewById(R.id.desparity_layout);
        desparityLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent topBroker = new Intent(AnalysisBrokerActivity.this, PieChartActivity.class);
                topBroker.putExtra(LeadType.KEY_LEAD_TYPE, LeadType.BUYER.getType());
                startActivity(topBroker);
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

}
