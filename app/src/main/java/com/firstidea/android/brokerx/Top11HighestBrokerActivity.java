package com.firstidea.android.brokerx;

import android.app.DatePickerDialog;
import android.app.Dialog;
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

import com.firstidea.android.brokerx.adapter.Analysis11HighestBrokerAdapter;
import com.firstidea.android.brokerx.model.AnalysisItem;

import java.util.ArrayList;
import java.util.Calendar;

public class Top11HighestBrokerActivity extends AppCompatActivity implements Analysis11HighestBrokerAdapter.OnAnalysisHBCardListener {
    private RecyclerView.LayoutManager mLayoutManager;
    private RecyclerView mRecyclerView;
    private ArrayList<AnalysisItem> mList;
    private EditText mStartDate, mEndDate;

    private Calendar cal;
    private int day;
    private int month;
    private int year;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_top11_highest_broker);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Top 11 highest Brokerage");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);

        mStartDate = (EditText) findViewById(R.id.starDate);
        mEndDate = (EditText) findViewById(R.id.endDate);
        mRecyclerView = (RecyclerView) findViewById(R.id.topHighestBroker_recycler_view);
        mRecyclerView.setHasFixedSize(true);

        mLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());


        mList = new ArrayList<AnalysisItem>();
        initList();

        Analysis11HighestBrokerAdapter mAdapter = new Analysis11HighestBrokerAdapter(this, mList, this);
        mRecyclerView.setAdapter(mAdapter);

        cal = Calendar.getInstance();
        day = cal.get(Calendar.DAY_OF_MONTH);
        month = cal.get(Calendar.MONTH);
        year = cal.get(Calendar.YEAR);

        showDate(year, month + 1, day);


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
        item1.setChemical_Name("Benzene Toluene Ethyl benzene,Xylenes");
        item1.setTotal_BrokerCharage("5000Rs");
        item1.setBokerR("3000Rs");
        item1.setBrokerS("2000Rs");
        mList.add(item1);

        AnalysisItem item2 = new AnalysisItem();
        item2.setChemical_Name("Benzene Toluene Ethyl benzene,Xylenes");
        item2.setTotal_BrokerCharage("5000Rs");
        item2.setBokerR("3000Rs");
        item2.setBrokerS("2000Rs");
        mList.add(item2);


        AnalysisItem item3 = new AnalysisItem();
        item3.setChemical_Name("Benzene Toluene Ethyl benzene,Xylenes");
        item3.setTotal_BrokerCharage("5000Rs");
        item3.setBokerR("3000Rs");
        item3.setBrokerS("2000Rs");
        mList.add(item3);


        AnalysisItem item4 = new AnalysisItem();
        item4.setChemical_Name("Benzene Toluene Ethyl benzene,Xylenes");
        item4.setTotal_BrokerCharage("5000Rs");
        item4.setBokerR("3000Rs");
        item4.setBrokerS("2000Rs");
        mList.add(item4);


        AnalysisItem item5 = new AnalysisItem();
        item5.setChemical_Name("Benzene Toluene Ethyl benzene,Xylenes");
        item5.setTotal_BrokerCharage("5000Rs");
        item5.setBokerR("3000sS");
        item5.setBrokerS("2000Rs");
        mList.add(item5);



    }

    @Override
    public void OnCardClick(AnalysisItem item) {

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
