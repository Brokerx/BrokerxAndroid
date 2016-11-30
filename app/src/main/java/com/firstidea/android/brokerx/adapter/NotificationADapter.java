package com.firstidea.android.brokerx.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.firstidea.android.brokerx.R;
import com.firstidea.android.brokerx.model.NotificationItem;

import java.util.ArrayList;

/**
 * Created by user on 10/10/2016.
 */

public class NotificationADapter extends RecyclerView.Adapter<NotificationADapter.NotificationViewHolder> {

    private ArrayList<NotificationItem> mHList;
    private Context mContext;
    private OnNotificationCardListener mHListner;
    /**
     * <b>public interface OnCardListener</b>
     * <br>Interface to implement the OnClick on Card</br>
     */
    public interface OnNotificationCardListener {
        void OnCardClick(NotificationItem item);
    }

    public class NotificationViewHolder extends RecyclerView.ViewHolder {
        public LinearLayout mParentlayout;
        private TextView mCircletext1;
        private TextView mNotification1;
        private TextView mNotification2;
        private TextView mNotification3;
        private TextView mNotification4;
        private TextView mtime1;
        private TextView mCircletext2;
        private TextView mNotificationCard;
        private TextView mTimeCard;
        public NotificationViewHolder(View itemView) {
            super(itemView);

            mParentlayout = (LinearLayout) itemView.findViewById(R.id.notificationLinear);
            mCircletext1 = (TextView) itemView.findViewById(R.id.circleText1);
            mNotification1 = (TextView) itemView.findViewById(R.id.notification1);
            mNotification2 = (TextView) itemView.findViewById(R.id.notification2);
            mNotification3 = (TextView) itemView.findViewById(R.id.notification3);
            mNotification4 = (TextView)itemView.findViewById(R.id.notification4);

            mtime1 = (TextView) itemView.findViewById(R.id.time1);
            mCircletext2 = (TextView) itemView.findViewById(R.id.textCircle2);
            mNotificationCard = (TextView) itemView.findViewById(R.id.cardnotification1);
            mTimeCard = (TextView) itemView.findViewById(R.id.timecard2);

        }
    }

    public NotificationADapter(Context mContext, ArrayList<NotificationItem> mList, OnNotificationCardListener mListner) {
        this.mContext = mContext;
        this.mHList = mList;
        this.mHListner = mListner;
    }
    @Override
    public NotificationADapter.NotificationViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(mContext).inflate(R.layout.card_notification_layout, parent, false);
        NotificationViewHolder ViewHolder = new NotificationViewHolder(v);
        return ViewHolder;
    }

    @Override
    public void onBindViewHolder(NotificationADapter.NotificationViewHolder holder, int position) {
        final NotificationItem Item = mHList.get(position);
//        holder.mCircletext1.setText(Item.getCircleText1());
//        holder.mNotification1.setText(Item.getNotification1());
//        holder.mNotification2.setText(Item.getNotification2());
//        holder.mNotification3.setText(Item.getNotification3());
//        holder.mNotification4.setText(Item.getNotification4());

       // holder.mtime1.setText(Item.getTime1());
        holder.mCircletext2.setText(Item.getCircleText2());
        holder.mTimeCard.setText(Item.getTimeCard2());
        holder.mNotificationCard.setText(Item.getNotification_card());

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
