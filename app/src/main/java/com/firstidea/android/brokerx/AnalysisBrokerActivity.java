package com.firstidea.android.brokerx;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.Toast;

import com.firstidea.android.brokerx.adapter.AnalysisBrokerAdapter;
import com.firstidea.android.brokerx.model.AnalysisItem;

import java.util.ArrayList;
import java.util.Calendar;

public class AnalysisBrokerActivity extends AppCompatActivity implements AnalysisBrokerAdapter.OnAnalysisBCardListener {
    private RecyclerView.LayoutManager mLayoutManager;
    private RecyclerView mRecyclerView;
    private ArrayList<AnalysisItem> mList;
    private EditText mStartDate, mEndDate;
    private Spinner mSpinner1;

    private LinearLayout mTop11HighestBroker,mTop11HighestSeller;
    private Context mContext = this;
    private Spinner spinner_nav;

    private Calendar cal;
    private int day;
    private int month;
    private int year;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_analysis_broker);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Analysis");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);

        spinner_nav = (Spinner) findViewById(R.id.spinner_nav);
        mStartDate = (EditText) findViewById(R.id.starDate);
        mEndDate = (EditText) findViewById(R.id.endDate);
        // Array of Months acting as a data pump
        String[] spinner1 = {"Select Item", "Item2", "Item3", "Item4", "Item5", "Item6"};
        //Spinner adapter
        mSpinner1 = (Spinner) findViewById(R.id.spinner1);
        ArrayAdapter<String> adapter1 = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, spinner1);
        adapter1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mSpinner1.setAdapter(adapter1);
        mSpinner1.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                Toast.makeText(AnalysisBrokerActivity.this, "select item", Toast.LENGTH_SHORT).show();

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        mRecyclerView = (RecyclerView) findViewById(R.id.anlysis_recycler_view);
        mRecyclerView.setHasFixedSize(true);

        mLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());


        mList = new ArrayList<AnalysisItem>();
        initList();
        initActionbarSpinner();
        top11Broker();

        AnalysisBrokerAdapter mAdapter = new AnalysisBrokerAdapter(this, mList, this);
        mRecyclerView.setAdapter(mAdapter);
        cal = Calendar.getInstance();
        day = cal.get(Calendar.DAY_OF_MONTH);
        month = cal.get(Calendar.MONTH);
        year = cal.get(Calendar.YEAR);

        showDate(year, month + 1, day);

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
                Intent topBroker = new Intent(AnalysisBrokerActivity.this, Top11SellerActivity.class);
                startActivity(topBroker);
            }
        });
    }

    //action Bar Spinner
    private void initActionbarSpinner() {

        final String[] spinnerItems = {"Buyer", "Seller"};
        ArrayAdapter<String> spinnerAdapert = new ArrayAdapter<String>(mContext, R.layout.buyer_seller_actiobar_spinner_item, spinnerItems);
        spinner_nav.setAdapter(spinnerAdapert);
        spinner_nav.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                Toast.makeText(AnalysisBrokerActivity.this, "Showing " + spinnerItems[position] + " Enquiries", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


    }

    @SuppressWarnings("deprecation")
    public void SetDate(View view) {
        showDialog(0);
//        Toast.makeText(getApplicationContext(), "ca", Toast.LENGTH_SHORT)
//                .show();
    }

    @Override
    @SuppressWarnings("deprecation")
    protected Dialog onCreateDialog(int id) {
        // TODO Auto-generated method stub
        if (id == 0) {
            return new DatePickerDialog(this, myDateListener, year, month, day);
        }
        return null;
    }

    private DatePickerDialog.OnDateSetListener myDateListener = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker arg0, int arg1, int arg2, int arg3) {
            // TODO Auto-generated method stub
            // arg1 = year
            // arg2 = month
            // arg3 = day
            showDate(arg1, arg2 + 1, arg3);
        }
    };

    private void showDate(int year, int month, int day) {
        mStartDate.setText(new StringBuilder().append(day).append("/")
                .append(month).append("/").append(year));
        mEndDate.setText(new StringBuilder().append(day).append("/")
                .append(month).append("/").append(year));
    }


    private void initList() {

        AnalysisItem item1 = new AnalysisItem();
        item1.setChemical_Name("Benzine ethyl alcohol with vinyle phosphate");
        item1.setDate("29th July,2016");
        item1.setTotal_Charge("3000 Rs");
        mList.add(item1);

        AnalysisItem item2 = new AnalysisItem();
        item2.setChemical_Name("Benzine ethyl alcohol with vinyle phosphate");
        item2.setDate("29th July,2016");
        item2.setTotal_Charge("3000 Rs");
        mList.add(item2);

        AnalysisItem item3 = new AnalysisItem();
        item3.setChemical_Name("Benzine ethyl alcohol with vinyle phosphate");
        item3.setDate("28th July,2016");
        item3.setTotal_Charge("6000 Rs");
        mList.add(item3);

        AnalysisItem item4 = new AnalysisItem();
        item4.setChemical_Name("Benzine ethyl alcohol with vinyle phosphate");
        item4.setDate("27th July,2016");
        item4.setTotal_Charge("2000 Rs");
        mList.add(item4);

        AnalysisItem item5 = new AnalysisItem();
        item5.setChemical_Name("Benzine ethyl alcohol with vinyle phosphate");
        item5.setDate("29th July,2016");
        item5.setTotal_Charge("5000 Rs");
        mList.add(item5);
    }

    @Override
    public void OnCardClick(AnalysisItem item) {

    }
}
