package com.firstidea.android.brokerx;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

import com.firstidea.android.brokerx.adapter.NotificationADapter;
import com.firstidea.android.brokerx.model.NotificationItem;

import java.util.ArrayList;

public class NotificationActivity extends AppCompatActivity implements NotificationADapter.OnNotificationCardListener {
    private RecyclerView.LayoutManager mLayoutManager;
    private RecyclerView mRecyclerView;
    private ArrayList<NotificationItem> mList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notification);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Notification");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);

        mRecyclerView = (RecyclerView) findViewById(R.id.notification_recycler_view);
        mRecyclerView.setHasFixedSize(true);

        mLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());


        mList = new ArrayList<NotificationItem>();
        initList();


        NotificationADapter mAdapter = new NotificationADapter(this, mList, this);
        mRecyclerView.setAdapter(mAdapter);


    }


    private void initList() {

        NotificationItem item1 = new NotificationItem();
//        item1.setCircleText1("R");
//        item1.setNotification1("Ramesh has sent you a proposal of changing unit from 300 to 500");
//        item1.setNotification2("Ramesh has sent you a proposal of changing base prise from 300 to 75/Units");
//        item1.setNotification3("Ramesh has sent you a proposal of changing transportation charges from 2300 to 1500Rs");
//        item1.setNotification4("Ramesh has sent you a proposal of changing brokerage charge from 1000 to 800 rs");
        //item1.setTime1("3 hrs ago");
        item1.setCircleText2("S");
        item1.setNotification_card("Suresh has rejected your proposal");
        item1.setTimeCard2("8 hrs ago");
        mList.add(item1);

        NotificationItem item2 = new NotificationItem();
//        item2.setCircleText1("R");
//        item2.setNotification1("Ramesh has sent you a proposal of changing unit from 300 to 500");
//        item2.setNotification2("Ramesh has sent you a proposal of changing base prise from 300 to 75/Units");
//        item2.setNotification3("Ramesh has sent you a proposal of changing transportation charges from 2300 to 1500Rs");
//        item2.setNotification4("Ramesh has sent you a proposal of changing brokerage charge from 1000 to 800 rs");
        //item2.setTime1("3 hrs ago");
        item2.setCircleText2("S");
        item2.setNotification_card("Suresh has rejected your proposal");
        item2.setTimeCard2("8 hrs ago");
        mList.add(item2);

        NotificationItem item3 = new NotificationItem();
//        item3.setCircleText1("R");
//        item3.setNotification1("Ramesh has sent you a proposal of changing unit from 300 to 500");
//        item3.setNotification2("Ramesh has sent you a proposal of changing base prise from 300 to 75/Units");
//        item3.setNotification3("Ramesh has sent you a proposal of changing transportation charges from 2300 to 1500Rs");
//        item3.setNotification4("Ramesh has sent you a proposal of changing brokerage charge from 1000 to 800 rs");
        //item3.setTime1("3 hrs ago");
        item3.setCircleText2("S");
        item3.setNotification_card("Suresh has rejected your proposal");
        item3.setTimeCard2("8 hrs ago");
        mList.add(item3);

        NotificationItem item4 = new NotificationItem();
//        item4.setCircleText1("R");
//        item4.setNotification1("Ramesh has sent you a proposal of changing unit from 300 to 500");
//        item4.setNotification2("Ramesh has sent you a proposal of changing base prise from 300 to 75/Units");
//        item4.setNotification3("Ramesh has sent you a proposal of changing transportation charges from 2300 to 1500Rs");
//        item4.setNotification4("Ramesh has sent you a proposal of changing brokerage charge from 1000 to 800 rs");
        //item4.setTime1("3 hrs ago");
        item4.setCircleText2("S");
        item4.setNotification_card("Suresh has rejected your proposal");
        item4.setTimeCard2("8 hrs ago");
        mList.add(item4);

        NotificationItem item5 = new NotificationItem();
//        item5.setCircleText1("R");
//        item5.setNotification1("Ramesh has sent you a proposal of changing unit from 300 to 500");
//        item5.setNotification2("Ramesh has sent you a proposal of changing base prise from 300 to 75/Units");
//        item5.setNotification3("Ramesh has sent you a proposal of changing transportation charges from 2300 to 1500Rs");
//        item5.setNotification4("Ramesh has sent you a proposal of changing brokerage charge from 1000 to 800 rs");
        //item5.setTime1("3 hrs ago");
        item5.setCircleText2("S");
        item5.setNotification_card("Suresh has rejected your proposal");
        item5.setTimeCard2("8 hrs ago");
        mList.add(item5);
    }

    @Override
    public void OnCardClick(NotificationItem item) {

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
