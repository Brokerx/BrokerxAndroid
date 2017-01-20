package com.firstidea.android.brokerx;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

import com.firstidea.android.brokerx.adapter.ChatListAdapter;
import com.firstidea.android.brokerx.model.ChatItem;

import java.util.ArrayList;

public class ChatListActivity extends AppCompatActivity implements ChatListAdapter.ChatCardListener {
    private RecyclerView.LayoutManager mLayoutManager;
    private RecyclerView mRecyclerView;
    private ArrayList<ChatItem> mList;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_list);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Chats");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);

        mRecyclerView = (RecyclerView)findViewById(R.id.chatList_recycler_view);
        mRecyclerView.setHasFixedSize(true);

        mLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());


        mList = new ArrayList<ChatItem>();
        initList();

        ChatListAdapter mAdapter = new ChatListAdapter(this, mList, this);
        mRecyclerView.setAdapter(mAdapter);


    }

    private void initList() {

        ChatItem item1 = new ChatItem();
        item1.setEnquiry("Enquiry Name 1");
        item1.setSellername("Ramesh p (seller)");
        item1.setSellertime("Today 3:49");
        item1.setSellerdeal("done deal then");
        item1.setNotify1("4");
        item1.setBuyername("Ravi (Buyer)");
        item1.setBuyertime("Yesterday 3:49");
        item1.setBuyerDeal("can we meet tomorrow");
        mList.add(item1);

        ChatItem item2 = new ChatItem();
        item2.setEnquiry("Enquiry Name 2");
        item2.setSellername("Ramesh p (seller)");
        item2.setSellertime("Today 3:49");
        item2.setSellerdeal("done deal then");
        item2.setNotify1("4");
        item2.setBuyername("Tushar (Buyer)");
        item2.setBuyertime("Yesterday 3:49");
        item2.setBuyerDeal("can we meet tomorrow");
        mList.add(item2);


        ChatItem item3 = new ChatItem();
        item3.setEnquiry("Enquiry Name 3");
        item3.setSellername("Ramesh p (seller)");
        item3.setSellertime("Today 3:49");
        item3.setSellerdeal("done deal then");
        item3.setNotify1("3");
        item3.setBuyername("Ravi (Buyer)");
        item3.setBuyertime("Yesterday 3:49");
        item3.setBuyerDeal("can we meet tomorrow");
        mList.add(item3);


        ChatItem item4 = new ChatItem();
        item4.setEnquiry("Enquiry Name 4");
        item4.setSellername("Ramesh p (seller)");
        item4.setSellertime("Today 3:49");
        item4.setSellerdeal("done deal then");
        item4.setNotify1("5");
        item4.setBuyername("Ravi (Buyer)");
        item4.setBuyertime("Yesterday 3:49");
        item4.setBuyerDeal("can we meet tomorrow");
        mList.add(item4);
    }

    @Override
    public void OnCardClick(ChatItem item) {

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
