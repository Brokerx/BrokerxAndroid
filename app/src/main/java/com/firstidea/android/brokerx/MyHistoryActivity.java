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

import java.util.ArrayList;
import java.util.Calendar;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

public class MyHistoryActivity extends AppCompatActivity implements MyHistoryAdapter.OnHostoryCardListener {
    private RecyclerView.LayoutManager mLayoutManager;
    private RecyclerView mRecyclerView;
    private ArrayList<MyHistroyItem>mList;
    private EditText mStartDate,mEndDate;

    private Calendar cal;
    private int day;
    private int month;
    private int year;
    private Context mContext;
    private ArrayList<Lead> mLeads;
    private User me;
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

        mStartDate = (EditText) findViewById(R.id.starDate);
        mEndDate = (EditText) findViewById(R.id.endDate);



        mRecyclerView = (RecyclerView)findViewById(R.id.history_recycler_view);
        mRecyclerView.setHasFixedSize(true);

        mLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());


        mList = new ArrayList<MyHistroyItem>();
        initList();

        getHistory();



        cal = Calendar.getInstance();
        day = cal.get(Calendar.DAY_OF_MONTH);
        month = cal.get(Calendar.MONTH);
        year = cal.get(Calendar.YEAR);

        showDate(year, month + 1, day);

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
                    MyHistoryAdapter mAdapter = new MyHistoryAdapter(mContext, mLeads,me.isBroker(), MyHistoryActivity.this);
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

        MyHistroyItem item1 = new MyHistroyItem();
        item1.setChemical_Name("Benzine ethyl alcohol with vinyle phosphate");
        item1.setInvoiceNo("ECT4433TSD");
        item1.setBrokerage("5000 Rs");
        item1.setDate("29th July,2016");
        item1.setBasic_charge("3000 Rs");
        item1.setBuyer_brokerage("2000 Rs");
        item1.setSeller_brokerage("3000 Rs");
        item1.setTotal_brokerage("5000 Rs");
        mList.add(item1);

        MyHistroyItem item2 = new MyHistroyItem();
        item2.setChemical_Name("Benzine ethyl alcohol with vinyle phosphate");
        item2.setInvoiceNo("GCT4433TSD");
        item2.setBrokerage("6000 Rs");
        item2.setDate("25th June,2016");
        item2.setBasic_charge("2000 Rs");
        item2.setBuyer_brokerage("4000 Rs");
        item2.setSeller_brokerage("2000 Rs");
        item2.setTotal_brokerage("6000 Rs");
        mList.add(item2);

        MyHistroyItem item3 = new MyHistroyItem();
        item3.setChemical_Name("Benzine ethyl alcohol with vinyle phosphate");
        item3.setInvoiceNo("DCT4433TSD");
        item3.setBrokerage("2000 Rs");
        item3.setDate("12th July,2016");
        item3.setBasic_charge("1000 Rs");
        item3.setBuyer_brokerage("2000 Rs");
        item3.setSeller_brokerage("2000 Rs");
        item3.setTotal_brokerage("5000 Rs");
        mList.add(item3);

        MyHistroyItem item4 = new MyHistroyItem();
        item4.setChemical_Name("Benzine ethyl alcohol with vinyle phosphate");
        item4.setInvoiceNo("FCT4433TSD");
        item4.setBrokerage("3000 Rs");
        item4.setDate("29th Augest,2016");
        item4.setBasic_charge("3500 Rs");
        item4.setBuyer_brokerage("2000 Rs");
        item4.setSeller_brokerage("3000 Rs");
        item4.setTotal_brokerage("5500 Rs");
        mList.add(item4);
    }

    @Override
    public void OnCardClick(MyHistroyItem item) {
        Intent callIntent1 = new Intent(MyHistoryActivity.this,MyHistoryDetailsActivity.class );
        callIntent1.putExtra("mbrObject",item);
        startActivity(callIntent1);

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
