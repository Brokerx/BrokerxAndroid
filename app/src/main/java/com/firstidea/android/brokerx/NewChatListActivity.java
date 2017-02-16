package com.firstidea.android.brokerx;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

import com.firstidea.android.brokerx.adapter.ChatListAdapter;
import com.firstidea.android.brokerx.http.ObjectFactory;
import com.firstidea.android.brokerx.http.model.ChatListDTO;
import com.firstidea.android.brokerx.http.model.ChatSummary;
import com.firstidea.android.brokerx.http.model.MessageDTO;
import com.firstidea.android.brokerx.http.model.User;
import com.firstidea.android.brokerx.widget.AppProgressDialog;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

public class NewChatListActivity extends AppCompatActivity implements ChatListAdapter.ChatCardListener {
    private RecyclerView.LayoutManager mLayoutManager;
    private RecyclerView mRecyclerView;
    private ArrayList<ChatListDTO> mList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_chat_list);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Chats");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);

        mRecyclerView = (RecyclerView) findViewById(R.id.chatList_recycler_view);
        mRecyclerView.setHasFixedSize(true);

        mLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());


        mList = new ArrayList<ChatListDTO>();
        getChatSummary();

    }

    private void getChatSummary() {
        User me = User.getSavedUser(this);
        final Dialog dialog = AppProgressDialog.show(this);
        ObjectFactory.getInstance().getChatServiceInstance().getChatSummary(me.getUserID(), new Callback<MessageDTO>() {
            @Override
            public void success(MessageDTO messageDTO, Response response) {
                if (messageDTO.isSuccess()) {
                    List<ChatSummary> chatSummaries = ChatSummary.createListFromJson(messageDTO.getData());
                    mList = new ArrayList<ChatListDTO>();
                    Map<Integer, String> leadItemMap = new HashMap<Integer, String>();
                    Map<Integer, List<ChatSummary>> leadChatMap = new HashMap<Integer, List<ChatSummary>>();
                    for (ChatSummary chatSummary : chatSummaries) {
                        leadItemMap.put(chatSummary.getLeadID(), chatSummary.getItemName());
                        List<ChatSummary> prevChatSummaryList = new ArrayList<ChatSummary>();
                        if (leadChatMap.containsKey(chatSummary.getLeadID())) {
                            prevChatSummaryList = leadChatMap.get(chatSummary.getLeadID());
                        }
                        prevChatSummaryList.add(chatSummary);
                        leadChatMap.put(chatSummary.getLeadID(), prevChatSummaryList);
                    }
                    for (Integer leadID : leadChatMap.keySet()) {
                        ChatListDTO chatListDTO = new ChatListDTO();
                        chatListDTO.setItemName(leadItemMap.get(leadID));
                        chatListDTO.setLeadID(leadID);
                        chatListDTO.setChatSummaryList(leadChatMap.get(leadID));
                        mList.add(chatListDTO);
                    }
                    ChatListAdapter mAdapter = new ChatListAdapter(NewChatListActivity.this, mList, NewChatListActivity.this);
                    mRecyclerView.setAdapter(mAdapter);
                }
                dialog.dismiss();
            }

            @Override
            public void failure(RetrofitError error) {
                dialog.dismiss();
            }
        });

    }

    @Override
    public void OnCardClick(ChatSummary item) {
        User me  = User.getSavedUser(this);
        Intent intent = new Intent(NewChatListActivity.this, ChatActivity.class);
        intent.putExtra(Constants.OTHER_USER_NAME, item.getToUser().getFullName());
        intent.putExtra(Constants.ITEM_NAME, item.getItemName());
        String touserType, fromUserType;
        if(item.getFromUserID() == me.getUserID()) {
            fromUserType = item.getFromUserType();
            touserType = item.getToUserType();
        } else {
            touserType = item.getFromUserType();
            fromUserType = item.getToUserType();
        }
        intent.putExtra(Constants.KEY_USER_TYPE, touserType);
        intent.putExtra(Constants.KEY_USER_TYPE+"1", fromUserType);

        intent.putExtra(Constants.OTHER_USER_ID, item.getToUser().getUserID());
        intent.putExtra(Constants.LEAD_ID, item.getLeadID());
        startActivityForResult(intent,1007);
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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(requestCode == 1007 && resultCode == RESULT_OK) {
            getChatSummary();
        }
    }
}
