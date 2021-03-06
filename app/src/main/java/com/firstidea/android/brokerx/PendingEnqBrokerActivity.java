package com.firstidea.android.brokerx;

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

import com.firstidea.android.brokerx.adapter.PendingEnqBrokerAdapter;
import com.firstidea.android.brokerx.model.PendingEntries;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class PendingEnqBrokerActivity extends AppCompatActivity implements PendingEnqBrokerAdapter.OnCardListener {
    private RecyclerView.LayoutManager mLayoutManager;
    private RecyclerView mRecyclerView;
    private ArrayList<PendingEntries> mList;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pending_enq_broker);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Pending Entries");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);

        mRecyclerView = (RecyclerView)findViewById(R.id.pendingBroker_recycler_view);
        mRecyclerView.setHasFixedSize(true);

        mLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());


        mList = new ArrayList<PendingEntries>();
        initList();

        PendingEnqBrokerAdapter mAdapter = new PendingEnqBrokerAdapter(this, mList, this);
        mRecyclerView.setAdapter(mAdapter);


    }

    private void initList() {

        PendingEntries item1 = new PendingEntries();
        item1.setChemicalName("Benzene Toluene Ethyl benzene,Xylenes");
        item1.setPostedName("Posted by Mohan");
        item1.setAddress("JSD Warehouse,Dhisar,Mumbai");
        item1.setGetPercentage("You get 30%");
        item1.setAvailability("440 units Available");
        mList.add(item1);

        PendingEntries item2 = new PendingEntries();
        item2.setChemicalName("Benzene Toluene Ethyl benzene,Xylenes");
        item2.setPostedName("Posted by Mohan");
        item2.setAddress("JSD Warehouse,Dhisar,Mumbai");
        item2.setGetPercentage("You get 30%");
        item2.setAvailability("440 units Available");
        mList.add(item2);

        PendingEntries item3 = new PendingEntries();
        item3.setChemicalName("Benzene Toluene Ethyl benzene,Xylenes");
        item3.setPostedName("Posted by Mohan");
        item3.setAddress("JSD Warehouse,Dhisar,Mumbai");
        item3.setGetPercentage("You get 10%");
        item3.setAvailability("359 units Available");
        mList.add(item3);

        PendingEntries item4 = new PendingEntries();
        item4.setChemicalName("Benzene Toluene Ethyl benzene,Xylenes");
        item4.setPostedName("Posted by Mohan");
        item4.setAddress("JSD Warehouse,Dhisar,Mumbai");
        item4.setGetPercentage("You get 20%");
        item4.setAvailability("460 units Available");
        mList.add(item4);

        PendingEntries item5 = new PendingEntries();
        item5.setChemicalName("Benzene Toluene Ethyl benzene,Xylenes");
        item5.setPostedName("Posted by Mohan");
        item5.setAddress("JSD Warehouse,Dhisar,Mumbai");
        item5.setGetPercentage("You get 40%");
        item5.setAvailability("440 units Available");
        mList.add(item5);
    }


    @Override
    public void OnCardClick(PendingEntries item) {

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
