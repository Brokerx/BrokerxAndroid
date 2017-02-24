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
import com.firstidea.android.brokerx.PendingEntriesActivity;
import com.firstidea.android.brokerx.R;
import com.firstidea.android.brokerx.enums.NotificationType;
import com.firstidea.android.brokerx.http.model.Lead;
import com.firstidea.android.brokerx.http.model.Notification;
import com.firstidea.android.brokerx.http.model.NotificationListDTO;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by user on 10/10/2016.
 */

public class NewNotificationADapter extends RecyclerView.Adapter<NewNotificationADapter.NotificationViewHolder> {

    private ArrayList<NotificationListDTO> mHList;
    private Context mContext;
    private LayoutInflater mInflater;

    public class NotificationViewHolder extends RecyclerView.ViewHolder {
        public LinearLayout mDynamicLayout;
        public RelativeLayout mParentlayout;
        private TextView mCircletext2;
        private TextView userName;
        private TextView mNotificationBadge;

        public NotificationViewHolder(View itemView) {
            super(itemView);
            mParentlayout = (RelativeLayout) itemView.findViewById(R.id.notificationLinear);
            mDynamicLayout = (LinearLayout) itemView.findViewById(R.id.dynamic_layout);
            mCircletext2 = (TextView) itemView.findViewById(R.id.textCircle2);
            userName = (TextView) itemView.findViewById(R.id.userName);
            mNotificationBadge = (TextView) itemView.findViewById(R.id.badge_count);

        }
    }

    public NewNotificationADapter(Context mContext, ArrayList<NotificationListDTO> mList) {
        this.mContext = mContext;
        this.mHList = mList;
        mInflater = LayoutInflater.from(mContext);
    }

    @Override
    public NewNotificationADapter.NotificationViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(mContext).inflate(R.layout.card_new_notification_layout, parent, false);
        NotificationViewHolder ViewHolder = new NotificationViewHolder(v);
        return ViewHolder;
    }

    @Override
    public void onBindViewHolder(final NewNotificationADapter.NotificationViewHolder holder, int position) {
        final NotificationListDTO notificationList = mHList.get(position);
        Notification lastNotification = notificationList.getNotifications().get(0);
        holder.mCircletext2.setText(lastNotification.getFromUser().getFullName().charAt(0) + "");
        holder.userName.setText(lastNotification.getItemName() + ": " + lastNotification.getFromUser().getFullName());
        holder.mNotificationBadge.setText(notificationList.getUnReadCount() + "/" + notificationList.getNotifications().size());
        if (notificationList.getUnReadCount() <= 0) {
            holder.mNotificationBadge.setBackgroundResource(R.drawable.gray_circle);
        }

        holder.mParentlayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(holder.mDynamicLayout.getVisibility() == View.VISIBLE) {
                    return;
                }
                List<Notification> notifications = notificationList.getNotifications();
                for (final Notification notification : notifications) {
                    View view = mInflater.inflate(R.layout.row_sub_notification, null);
                    ((TextView) view.findViewById(R.id.notification_msg)).setText(notification.getMessage());
                    ((TextView) view.findViewById(R.id.timecard2)).setText(notification.getCreatedDttm());
                    view.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            if (notification.getType().equals(NotificationType.LEAD_CREATED.getNotificationType())
                                    || notification.getType().equals(NotificationType.LEAD_REVERTED.getNotificationType())
                                    || notification.getType().equals(NotificationType.LEAD_STATUS_CHANGED.getNotificationType())) {
                                Intent enquiry = new Intent(mContext, EnquiryDetailsActivity.class);
                                enquiry.putExtra(Lead.KEY_LEAD_ID, notification.getLeadID());
                                mContext.startActivity(enquiry);
                            } else if (notification.getType().equals(NotificationType.DEAL_DONE.getNotificationType())
                                    || notification.getType().equals(NotificationType.ANALYSIS_STATUS_CHANGED.getNotificationType())
                                    || notification.getType().equals(NotificationType.ANALYSIS_DOCUMENT_UPLOADED.getNotificationType())
                                    ) {
                                Intent enquiry = new Intent(mContext, MyHistoryDetailsActivity.class);
                                enquiry.putExtra(Lead.KEY_LEAD_ID, notification.getLeadID());
                                if (notification.getType().equals(NotificationType.ANALYSIS_DOCUMENT_UPLOADED.getNotificationType())) {
                                    enquiry.putExtra(MyHistoryDetailsActivity.OPEN_DETAILS_SCREEN, true);
                                }
                                mContext.startActivity(enquiry);
                            } else if (notification.getType().equals(NotificationType.MOVED_TO_PENDING_LEAD.getNotificationType())) {
                                Intent enquiry = new Intent(mContext, PendingEntriesActivity.class);
                                mContext.startActivity(enquiry);
                            }

                        }
                    });
                    holder.mDynamicLayout.addView(view);
                    holder.mDynamicLayout.setVisibility(View.VISIBLE);
                }
            }
        });
    }


    @Override
    public int getItemCount() {
        return mHList.size();
    }


}
