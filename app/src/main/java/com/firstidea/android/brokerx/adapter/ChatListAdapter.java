package com.firstidea.android.brokerx.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.firstidea.android.brokerx.R;
import com.firstidea.android.brokerx.model.ChatItem;

import java.util.ArrayList;

/**
 * Created by user on 10/20/2016.
 */

public class ChatListAdapter extends RecyclerView.Adapter<ChatListAdapter.ChatViewHolder> {
    private ArrayList<ChatItem> mHList;
    private Context mContext;
    private ChatListAdapter.ChatCardListener mHListner;

    /**
     * <b>public interface OnCardListener</b>
     * <br>Interface to implement the OnClick on Card</br>
     */
    public interface ChatCardListener {
        void OnCardClick(ChatItem item);
    }

    public class ChatViewHolder extends RecyclerView.ViewHolder {
        public LinearLayout mParentlayout;
        private TextView mEnquiryNo;
        private TextView mSellerName;
        private TextView mbuyerName;
        private TextView mSellertime;
        private TextView mSellerDeal;
        private TextView mBuyerDeal;
        private TextView mBuyertime;
        private TextView mNotificationNumber;

        public ChatViewHolder(View itemView) {
            super(itemView);

            mParentlayout = (LinearLayout) itemView.findViewById(R.id.chatParentLinear);
            mEnquiryNo = (TextView) itemView.findViewById(R.id.enquieryno);
            mSellerName = (TextView) itemView.findViewById(R.id.sellerName);
            mbuyerName = (TextView) itemView.findViewById(R.id.buyername);
            mSellertime = (TextView) itemView.findViewById(R.id.sellerTime);

            mSellerDeal = (TextView) itemView.findViewById(R.id.sellerdealNotification);
            mBuyerDeal = (TextView) itemView.findViewById(R.id.buyerNotification);
            mBuyertime = (TextView) itemView.findViewById(R.id.buyertime);
            mNotificationNumber = (TextView) itemView.findViewById(R.id.notificationNo);
        }
    }
    public ChatListAdapter(Context mContext, ArrayList<ChatItem> mHList, ChatCardListener mHListner) {
        this.mContext = mContext;
        this.mHList = mHList;
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

        final ChatItem Item = mHList.get(position);
        holder.mEnquiryNo.setText(Item.getEnquiry());
        holder.mSellerName.setText(Item.getSellername());
        holder.mbuyerName.setText(Item.getBuyername());
        holder.mSellertime.setText(Item.getSellertime());

        holder.mSellerDeal.setText(Item.getSellerdeal());
        holder.mBuyerDeal.setText(Item.getBuyerDeal());
        holder.mBuyertime.setText(Item.getBuyertime());
        holder.mNotificationNumber.setText(Item.getNotify1());

        holder.mParentlayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mHListner.OnCardClick(Item);
            }
        });

    }

    @Override
    public int getItemCount() {
        return mHList.size();
    }


}
