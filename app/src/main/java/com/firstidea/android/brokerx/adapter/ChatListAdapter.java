package com.firstidea.android.brokerx.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.firstidea.android.brokerx.R;
import com.firstidea.android.brokerx.http.SingletonRestClient;
import com.firstidea.android.brokerx.http.model.ChatListDTO;
import com.firstidea.android.brokerx.http.model.ChatSummary;
import com.firstidea.android.brokerx.model.ChatItem;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

/**
 * Created by user on 10/20/2016.
 */

public class ChatListAdapter extends RecyclerView.Adapter<ChatListAdapter.ChatViewHolder> {
    private ArrayList<ChatListDTO> mChatList;
    private Context mContext;
    private ChatListAdapter.ChatCardListener mHListner;

    /**
     * <b>public interface OnCardListener</b>
     * <br>Interface to implement the OnClick on Card</br>
     */
    public interface ChatCardListener {
        void OnCardClick(ChatSummary item);
    }

    public class ChatViewHolder extends RecyclerView.ViewHolder {
        public LinearLayout mParentlayout;
        private TextView itemName;
        private TextView userName1;
        private TextView userName2;
        private TextView msgTime1;
        private TextView userMsg1;
        private TextView userMsg2;
        private TextView msgTime2;
        private ImageView user_image1;
        private ImageView user_image2;
        private LinearLayout user_layout1;
        private LinearLayout user_layout2;
        private View divider;

        public ChatViewHolder(View itemView) {
            super(itemView);

            mParentlayout = (LinearLayout) itemView.findViewById(R.id.chatParentLinear);
            itemName = (TextView) itemView.findViewById(R.id.itemName);
            userName1 = (TextView) itemView.findViewById(R.id.userName1);
            userName2 = (TextView) itemView.findViewById(R.id.userName2);
            userMsg1 = (TextView) itemView.findViewById(R.id.userMsg1);
            userMsg2 = (TextView) itemView.findViewById(R.id.userMsg2);
            msgTime1 = (TextView) itemView.findViewById(R.id.msgTime1);
            msgTime2 = (TextView) itemView.findViewById(R.id.msgTime2);
            user_image1 = (ImageView) itemView.findViewById(R.id.user_image1);
            user_image2 = (ImageView) itemView.findViewById(R.id.user_image2);
            user_layout2 = (LinearLayout) itemView.findViewById(R.id.user_layout2);
            user_layout1 = (LinearLayout) itemView.findViewById(R.id.user_layout1);
            divider = itemView.findViewById(R.id.divider);
        }
    }
    public ChatListAdapter(Context mContext, ArrayList<ChatListDTO> mHList, ChatCardListener mHListner) {
        this.mContext = mContext;
        this.mChatList = mHList;
        this.mHListner = mHListner;
    }
    @Override
    public ChatListAdapter.ChatViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
       View v = LayoutInflater.from(mContext).inflate(R.layout.chat_cardlist_layout, parent, false);
        ChatViewHolder ChatViewHolder = new ChatViewHolder(v);
        return ChatViewHolder;
    }

    @Override
    public void onBindViewHolder(ChatListAdapter.ChatViewHolder holder, int position) {

        final ChatListDTO Item = mChatList.get(position);
        if(Item.getLeadID().equals(-1)) {

        } else {

        }
        holder.itemName.setText(Item.getItemName());
        final ChatSummary chatSummary1 = Item.getChatSummaryList().get(0);
        holder.userName1.setText(chatSummary1.getToUser().getFullName());
        holder.userMsg1.setText(chatSummary1.getLastMsg());
        holder.msgTime1.setText(chatSummary1.getLastMsgDateTime());
        if(!TextUtils.isEmpty(chatSummary1.getToUser().getImageURL())) {
            String imgUrl = SingletonRestClient.BASE_PROFILE_IMAGE_URL+"thumb_"+chatSummary1.getToUser().getImageURL();
            Picasso.with(mContext).load(imgUrl)
                    .error(R.drawable.user_profile)
                    .placeholder(R.drawable.user_profile)
                    .into(holder.user_image1);
        }

        holder.user_layout1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mHListner.OnCardClick(chatSummary1);
            }
        });
        if(Item.getChatSummaryList().size() > 1) {
            holder.user_layout2.setVisibility(View.VISIBLE);
            holder.divider.setVisibility(View.VISIBLE);
            final ChatSummary chatSummary2 = Item.getChatSummaryList().get(0);
            holder.userName1.setText(chatSummary2.getToUser().getFullName());
            holder.userMsg1.setText(chatSummary2.getLastMsg());
            holder.msgTime1.setText(chatSummary2.getLastMsgDateTime());
            if(!TextUtils.isEmpty(chatSummary2.getToUser().getImageURL())) {
                String imgUrl = SingletonRestClient.BASE_PROFILE_IMAGE_URL+"thumb_"+chatSummary2.getToUser().getImageURL();
                Picasso.with(mContext).load(imgUrl)
                        .error(R.drawable.user_profile)
                        .placeholder(R.drawable.user_profile)
                        .into(holder.user_image1);
            }
            holder.user_layout2.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mHListner.OnCardClick(chatSummary2);
                }
            });
        } else {
            holder.user_layout2.setVisibility(View.GONE);
            holder.divider.setVisibility(View.GONE);
        }

    }

    @Override
    public int getItemCount() {
        return mChatList.size();
    }


}
