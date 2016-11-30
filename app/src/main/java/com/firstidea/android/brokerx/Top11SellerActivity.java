package com.firstidea.android.brokerx;

import android.app.DatePickerDialog;
import android.app.Dialog;
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

import com.firstidea.android.brokerx.adapter.TopBrokerAdapter;
import com.firstidea.android.brokerx.adapter.Top_11SellerAdapter;
import com.firstidea.android.brokerx.model.TopBroketItem;

import java.util.ArrayList;
import java.util.Calendar;

public class Top11SellerActivity extends AppCompatActivity implements Top_11SellerAdapter.OnCardListener {
    private RecyclerView.LayoutManager mLayoutManager;
    private RecyclerView mRecyclerView;
    private ArrayList<TopBroketItem> mList;
    private EditText mStartDate,mEndDate;

    private Calendar cal;
    private int day;
    private int month;
    private int year;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_top11_seller);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Top 11 Seller");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
        mStartDate = (EditText) findViewById(R.id.starDate);
        mEndDate = (EditText) findViewById(R.id.endDate);

        mRecyclerView = (RecyclerView)findViewById(R.id.top11seller_recycler_view);
        mRecyclerView.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());


        mList = new ArrayList<TopBroketItem>();
        initList();

        Top_11SellerAdapter mAdapter = new Top_11SellerAdapter(this,mList, this);
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

        TopBroketItem item1 = new TopBroketItem();
        item1.setBroker_name("Ramesh Jha");
        item1.setBasic_charge("3000RS");
        item1.setTotal_charge("5000Rs");
        mList.add(item1);

        TopBroketItem item2 = new TopBroketItem();
        item2.setBroker_name("Ramesh Jha");
        item2.setBasic_charge("3000Rs");
        item2.setTotal_charge("5000Rs");
        mList.add(item2);

        TopBroketItem item3 = new TopBroketItem();
        item3.setBroker_name("Ramesh Jha");
        item3.setBasic_charge("3000Rs");
        item3.setTotal_charge("5000Rs");
        mList.add(item3);

        TopBroketItem item4 = new TopBroketItem();
        item4.setBroker_name("Ramesh Jha");
        item4.setBasic_charge("3000Rs");
        item4.setTotal_charge("5000Rs");
        mList.add(item4);

        TopBroketItem item5 = new TopBroketItem();
        item5.setBroker_name("Ramesh Jha");
        item5.setBasic_charge("3000Rs");
        item5.setTotal_charge("5000Rs");
        mList.add(item5);



    }

    @Override
    public void OnCardClick(TopBroketItem item) {

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
