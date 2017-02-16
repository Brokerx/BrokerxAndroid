package com.firstidea.android.brokerx;

import android.app.Dialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.firstidea.android.brokerx.adapter.ChatAdapter;
import com.firstidea.android.brokerx.enums.ChatType;
import com.firstidea.android.brokerx.http.ObjectFactory;
import com.firstidea.android.brokerx.http.model.Chat;
import com.firstidea.android.brokerx.http.model.MessageDTO;
import com.firstidea.android.brokerx.http.model.User;
import com.firstidea.android.brokerx.utils.AudioPlayer;
import com.firstidea.android.brokerx.utils.SharedPreferencesUtil;
import com.firstidea.android.brokerx.widget.AppProgressDialog;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

public class ChatActivity extends AppCompatActivity {

    private String mOtherUserName;
    private String mItemName;
    private String mOtherUserType;
    private String mUserType;
    private Integer mUserID;
    private Context mContext;
    private User me;
    private Integer mLealID;
    @BindView(R.id.chat_recycler_view)
    RecyclerView mRecyclerView;
    @BindView(R.id.edit_chat)
    EditText editChat;
    @BindView(R.id.btn_send)
    RelativeLayout btnSend;
    private ArrayList<Chat> mChats;
    private ChatAdapter mChatAdapter;
    private SwipeRefreshLayout mSwipeRefreshLayout;
    private boolean iSetResult = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);
        ButterKnife.bind(this);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
        mContext = this;
        me = User.getSavedUser(mContext);
        mOtherUserName = getIntent().getExtras().getString(Constants.OTHER_USER_NAME);
        mItemName = getIntent().getExtras().getString(Constants.ITEM_NAME);
        mOtherUserType = getIntent().getExtras().getString(Constants.KEY_USER_TYPE);
        if(getIntent().hasExtra(Constants.KEY_USER_TYPE+"1")) {
            mUserType = getIntent().getExtras().getString(Constants.KEY_USER_TYPE + "1");
        } else {
            mUserType = "";
        }
        mUserID = getIntent().getExtras().getInt(Constants.OTHER_USER_ID);
        mLealID = getIntent().getExtras().getInt(Constants.LEAD_ID);
        mLealID = mLealID < 0? -1:mLealID;
        if(mOtherUserType.length() > 0) {
            getSupportActionBar().setTitle(mOtherUserName + " (" + mOtherUserType + ")");
        } else {
            getSupportActionBar().setTitle(mOtherUserName);
        }
        initScreen();
    }

    private void initScreen() {
        if(mItemName.length() > 0) {
            ((TextView) findViewById(R.id.item_name)).setText(mItemName);
        } else {
            findViewById(R.id.item_name).setVisibility(View.GONE);
        }
        getChats();
        LinearLayoutManager chatLayoutManager = new LinearLayoutManager(mContext, LinearLayoutManager.VERTICAL, true);
        chatLayoutManager.setStackFromEnd(true);
        chatLayoutManager.setReverseLayout(true);
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setLayoutManager(chatLayoutManager);
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());

        btnSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(editChat.getText().toString().trim().length() > 0) {
                    sendMsg(ChatType.TEXT.getType(), editChat.getText().toString().trim());
                }
            }
        });
        mSwipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swipeRefreshLayout);
        mSwipeRefreshLayout.setColorSchemeResources(R.color.colorPrimaryDark,
                R.color.colorPrimaryLight,
                R.color.teal,
                R.color.teal);
        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                // Refresh items
                getChats();
            }
        });
    }

    private void sendMsg(String type, String message) {
        final Dialog dialog = AppProgressDialog.show(mContext);
        ObjectFactory.getInstance().getChatServiceInstance().sendMsg(me.getUserID(), me.getFullName(),mUserID, mLealID,type,message,mUserType,mItemName,new Callback<MessageDTO>() {
            @Override
            public void success(MessageDTO messageDTO, Response response) {
                if(messageDTO.isSuccess()) {
                    editChat.setText("");
                    Chat chat = Chat.createFromJSON(messageDTO.getData());
                    ArrayList<Chat> chats = new ArrayList<>();
                    chats.add(chat);
                    chats.addAll(mChats);
                    mChats = chats;
                    mChatAdapter = new ChatAdapter(mContext, mChats);
                    mRecyclerView.setAdapter(mChatAdapter);
                    mChatAdapter.notifyDataSetChanged();
                    mRecyclerView.scrollToPosition(0);
                    playSound(R.raw.send_message);
                    iSetResult = true;
                }
                dialog.dismiss();
            }

            @Override
            public void failure(RetrofitError error) {
                Toast.makeText(mContext, "Some Error occured", Toast.LENGTH_SHORT).show();
                dialog.dismiss();

            }
        });
    }

    private void getChats() {
        final Dialog dialog = AppProgressDialog.show(mContext);
        ObjectFactory.getInstance().getChatServiceInstance().getChats(mUserID, me.getUserID(), mLealID, new Callback<MessageDTO>() {
            @Override
            public void success(MessageDTO messageDTO, Response response) {
                if(messageDTO.isSuccess()) {
                    mChats = Chat.createListFromJson(messageDTO.getData());
                    mChatAdapter = new ChatAdapter(mContext, mChats);
                    mRecyclerView.setAdapter(mChatAdapter);
                    mChatAdapter.notifyDataSetChanged();
                    mRecyclerView.scrollToPosition(0);
                }
                dialog.dismiss();
                mSwipeRefreshLayout.setRefreshing(false);
            }

            @Override
            public void failure(RetrofitError error) {
                Toast.makeText(mContext, "Some Error occured", Toast.LENGTH_SHORT).show();
                dialog.dismiss();
                mSwipeRefreshLayout.setRefreshing(false);
            }
        });
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    BroadcastReceiver messageReceiver = new BroadcastReceiver() {

        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent.getAction().equals(Constants.ACTION_NEW_MESSAGE)) {
                Bundle extra = intent.getExtras();
                Chat chat = extra.getParcelable(Constants.KEY_USER_COMMUNICATION);

                if (chat != null) {
                    ArrayList<Chat> chats = new ArrayList<>();
                    chats.add(chat);
                    chats.addAll(mChats);
                    mChats = chats;
                    mChatAdapter = new ChatAdapter(mContext, mChats);
                    mRecyclerView.setAdapter(mChatAdapter);
                    mChatAdapter.notifyDataSetChanged();
                    mRecyclerView.scrollToPosition(0);
                    playSound(R.raw.incoming);
                }

            }
        }
    };
    @Override
    protected void onResume() {
        super.onResume();
        SharedPreferencesUtil.putSharedPreferencesInt(this, Constants.KEY_ACTIVE_COMMUNICATION_USER_ID, mUserID);
        SharedPreferencesUtil.putSharedPreferencesInt(this, Constants.KEY_ACTIVE_COMMUNICATION_LEAD_ID, mLealID);
        IntentFilter filter = new IntentFilter();
        filter.addAction(Constants.ACTION_NEW_MESSAGE);
        registerReceiver(messageReceiver, filter);
    }

    @Override
    protected void onPause() {
        super.onPause();
        SharedPreferencesUtil.putSharedPreferencesInt(this, Constants.KEY_ACTIVE_COMMUNICATION_USER_ID, 0);
        SharedPreferencesUtil.putSharedPreferencesInt(this, Constants.KEY_ACTIVE_COMMUNICATION_LEAD_ID, 0);
        unregisterReceiver(messageReceiver);
    }

    @Override
    public void finish() {
        if(iSetResult) {
            setResult(RESULT_OK);
        }
        super.finish();
        overridePendingTransition(0, android.R.anim.slide_out_right);
    }

    private void playSound(int typeID) {
        AudioPlayer audioPlayer = new AudioPlayer(this);
        audioPlayer.play(typeID);
    }
}
