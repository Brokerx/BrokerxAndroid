package com.firstidea.android.brokerx;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.Toast;

import com.firstidea.android.brokerx.adapter.ChatListAdapter;
import com.firstidea.android.brokerx.enums.LeadType;
import com.firstidea.android.brokerx.http.ObjectFactory;
import com.firstidea.android.brokerx.http.model.ChatListDTO;
import com.firstidea.android.brokerx.http.model.ChatSummary;
import com.firstidea.android.brokerx.http.model.Lead;
import com.firstidea.android.brokerx.http.model.MessageDTO;
import com.firstidea.android.brokerx.http.model.User;
import com.firstidea.android.brokerx.model.ChatItem;
import com.firstidea.android.brokerx.widget.AppProgressDialog;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

public class ChatListActivity extends AppCompatActivity implements ChatListAdapter.ChatCardListener, SearchView.OnQueryTextListener {
    private RecyclerView.LayoutManager mLayoutManager;
    private RecyclerView mRecyclerView;
    private ArrayList<ChatListDTO> mList;
    private SearchView mSearchView;
    private User me;
    private final int BROKER_SELECTION_REQUEST = 100;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_list);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Chats");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);

        me = User.getSavedUser(this);
        mRecyclerView = (RecyclerView) findViewById(R.id.chatList_recycler_view);
        mRecyclerView.setHasFixedSize(true);

        mLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ChatListActivity.this, MycircleActivity.class);
                intent.putExtra(Constants.KEY_IS_FOR_SELECTION, true);
                startActivityForResult(intent, BROKER_SELECTION_REQUEST);
            }
        });

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
                        Integer leadID = chatSummary.getLeadID() < 0? chatSummary.getLeadID()*chatSummary.getToUser().getUserID():chatSummary.getLeadID();
                        leadItemMap.put(leadID, chatSummary.getItemName());
                        List<ChatSummary> prevChatSummaryList = new ArrayList<ChatSummary>();
                        if (leadChatMap.containsKey(leadID)) {
                            prevChatSummaryList = leadChatMap.get(leadID);
                        }
                        prevChatSummaryList.add(chatSummary);
                        leadChatMap.put(leadID, prevChatSummaryList);
                    }
                    for (Integer leadID : leadChatMap.keySet()) {
                        ChatListDTO chatListDTO = new ChatListDTO();
                        chatListDTO.setItemName(leadItemMap.get(leadID));
                        chatListDTO.setLeadID(leadID);
                        chatListDTO.setChatSummaryList(leadChatMap.get(leadID));
                        mList.add(chatListDTO);
                    }
                    ChatListAdapter mAdapter = new ChatListAdapter(ChatListActivity.this, mList, ChatListActivity.this);
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
//        User me = User.getSavedUser(this);
        Intent intent = new Intent(ChatListActivity.this, ChatActivity.class);
        intent.putExtra(Constants.OTHER_USER_NAME, item.getToUser().getFullName());
        intent.putExtra(Constants.ITEM_NAME, item.getItemName());
        String touserType, fromUserType;
        if (item.getFromUserID() == me.getUserID()) {
            fromUserType = item.getFromUserType();
            touserType = item.getToUserType();
        } else {
            touserType = item.getFromUserType();
            fromUserType = item.getToUserType();
        }
        intent.putExtra(Constants.KEY_USER_TYPE, touserType);
        intent.putExtra(Constants.KEY_USER_TYPE + "1", fromUserType);

        intent.putExtra(Constants.OTHER_USER_ID, item.getToUser().getUserID());
        intent.putExtra(Constants.LEAD_ID, item.getLeadID());
        startActivityForResult(intent, 1007);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);

        MenuInflater inflater = getMenuInflater();

        inflater.inflate(R.menu.menu_chat_list, menu);

        MenuItem searchItem = menu.findItem(R.id.action_search);
        mSearchView = (SearchView) searchItem.getActionView();
        /*int searchImgId = getResources().getIdentifier("android:id/search_button", null, null);
        ImageView v = (ImageView) mSearchView.findViewById(searchImgId);
        v.setImageResource(R.drawable.ic_search_white);*/
        mSearchView.setOnQueryTextListener(this);
        mSearchView.setQueryHint(Html.fromHtml("<font color = #DEDEDE>Search by User/Item name</font>"));
        return true;
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
        if (requestCode == 1007 && resultCode == RESULT_OK) {
            getChatSummary();
        }
        if (requestCode == BROKER_SELECTION_REQUEST && resultCode == RESULT_OK) {
            User user = data.getExtras().getParcelable(Constants.KEY_SELECTED_USER);
            for(ChatListDTO chat: mList) {
                boolean isHandled = false;
                for(ChatSummary chatSummary: chat.getChatSummaryList()) {
                    if(user.getUserID().equals(chatSummary.getToUser().getUserID()) && chatSummary.getLeadID().equals(-1)) {
                        isHandled = true;
                        OnCardClick(chatSummary);
                        break;
                    }
                }
                if(isHandled) {
                    return;
                }
            }
            Intent intent = new Intent(ChatListActivity.this, ChatActivity.class);
            intent.putExtra(Constants.OTHER_USER_NAME, user.getFullName());
            intent.putExtra(Constants.ITEM_NAME, "General Chat");
            String touserType, fromUserType;
            if (me.isBroker()) {
                fromUserType = "R";
                touserType = "B";
            } else {
                fromUserType = "B";
                touserType = "R";
            }
            intent.putExtra(Constants.KEY_USER_TYPE, touserType);
            intent.putExtra(Constants.KEY_USER_TYPE + "1", fromUserType);

            intent.putExtra(Constants.OTHER_USER_ID, user.getUserID());
            intent.putExtra(Constants.LEAD_ID, -1);
            startActivityForResult(intent, 1007);
        }
    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        return false;
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        if (newText.trim().length() == 0) {
            ChatListAdapter mAdapter = new ChatListAdapter(ChatListActivity.this, mList, ChatListActivity.this);
            mRecyclerView.setAdapter(mAdapter);
        } else {
            ArrayList<ChatListDTO> tempList = new ArrayList<>();
            for (ChatListDTO chatListDTO : mList) {
                if (chatListDTO.getItemName().contains(newText)) {
                    tempList.add(chatListDTO);
                    continue;
                }
                for (ChatSummary chatSummary : chatListDTO.getChatSummaryList()) {
                    if (chatSummary.getToUser().getFullName().contains(newText)) {
                        tempList.add(chatListDTO);
                        break;
                    }
                }
            }
            ChatListAdapter mAdapter = new ChatListAdapter(ChatListActivity.this, tempList, ChatListActivity.this);
            mRecyclerView.setAdapter(mAdapter);
        }
        return false;
    }
}
