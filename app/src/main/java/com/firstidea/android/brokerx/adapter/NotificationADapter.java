package com.firstidea.android.brokerx.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.firstidea.android.brokerx.EnquiryDetailsActivity;
import com.firstidea.android.brokerx.MyHistoryDetailsActivity;
import com.firstidea.android.brokerx.PendingEnqBrokerActivity;
import com.firstidea.android.brokerx.PendingEntriesActivity;
import com.firstidea.android.brokerx.R;
import com.firstidea.android.brokerx.enums.NotificationType;
import com.firstidea.android.brokerx.http.model.Lead;
import com.firstidea.android.brokerx.http.model.Notification;
import com.firstidea.android.brokerx.model.NotificationItem;
import com.firstidea.android.brokerx.model.PendingEntries;

import java.util.ArrayList;

/**
 * Created by user on 10/10/2016.
 */

public class NotificationADapter extends RecyclerView.Adapter<NotificationADapter.NotificationViewHolder> {

    private ArrayList<Notification> mHList;
    private Context mContext;

    public class NotificationViewHolder extends RecyclerView.ViewHolder {
        public RelativeLayout mParentlayout;
        private TextView mCircletext2;
        private TextView userName;
        private TextView mNotificationCard;
        private TextView mTimeCard;
        public NotificationViewHolder(View itemView) {
            super(itemView);

            mParentlayout = (RelativeLayout) itemView.findViewById(R.id.notificationLinear);
            mCircletext2 = (TextView) itemView.findViewById(R.id.textCircle2);
            userName = (TextView) itemView.findViewById(R.id.userName);
            mNotificationCard = (TextView) itemView.findViewById(R.id.cardnotification1);
            mTimeCard = (TextView) itemView.findViewById(R.id.timecard2);

        }
    }

    public NotificationADapter(Context mContext, ArrayList<Notification> mList) {
        this.mContext = mContext;
        this.mHList = mList;
    }
    @Override
    public NotificationADapter.NotificationViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(mContext).inflate(R.layout.card_notification_layout, parent, false);
        NotificationViewHolder ViewHolder = new NotificationViewHolder(v);
        return ViewHolder;
    }

    @Override
    public void onBindViewHolder(NotificationADapter.NotificationViewHolder holder, int position) {
        final Notification notification = mHList.get(position);
        holder.mCircletext2.setText(notification.getFromUser().getFullName().charAt(0)+"");
        holder.userName.setText(notification.getItemName()+": "+notification.getFromUser().getFullName());
        holder.mTimeCard.setText(notification.getCreatedDttm());
        holder.mNotificationCard.setText(notification.getMessage());

        holder.mParentlayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(notification.getType().equals(NotificationType.LEAD_CREATED.getNotificationType())
                        || notification.getType().equals(NotificationType.LEAD_REVERTED.getNotificationType())
                        || notification.getType().equals(NotificationType.LEAD_STATUS_CHANGED.getNotificationType())) {
                    Intent enquiry = new Intent(mContext, EnquiryDetailsActivity.class);
                    enquiry.putExtra(Lead.KEY_LEAD_ID, notification.getLeadID());
                    mContext.startActivity(enquiry);
                }else if(notification.getType().equals(NotificationType.DEAL_DONE.getNotificationType())
                            || notification.getType().equals(NotificationType.ANALYSIS_STATUS_CHANGED.getNotificationType())
                            || notification.getType().equals(NotificationType.ANALYSIS_DOCUMENT_UPLOADED.getNotificationType())
                        ) {
                    Intent enquiry = new Intent(mContext, MyHistoryDetailsActivity.class);
                    enquiry.putExtra(Lead.KEY_LEAD_ID, notification.getLeadID());
                    if(notification.getType().equals(NotificationType.ANALYSIS_DOCUMENT_UPLOADED.getNotificationType())) {
                        enquiry.putExtra(MyHistoryDetailsActivity.OPEN_DETAILS_SCREEN, true);
                    }
                    mContext.startActivity(enquiry);
                }else if(notification.getType().equals(NotificationType.MOVED_TO_PENDING_LEAD.getNotificationType())) {
                    Intent enquiry = new Intent(mContext, PendingEntriesActivity.class);
                    mContext.startActivity(enquiry);
                }
            }
        });
    }




    @Override
    public int getItemCount() {
        return mHList.size();
    }


}
